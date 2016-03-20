package edu.kit.ipd.crowdcontrol.workerservice.database.operations;

import com.google.common.cache.LoadingCache;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.Tables;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.enums.ExperimentsPlatformModeMode;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.enums.ExperimentsPlatformStatusPlatformStatus;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.*;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static edu.kit.ipd.crowdcontrol.workerservice.database.model.Tables.*;
import static org.jooq.impl.DSL.or;

/**
 * ExperimentsPlatformOperations contains all queries concerned with the platforms of the experiment and the associated
 * information.
 * @author LeanderK
 * @version 1.0
 */
public class ExperimentsPlatformOperations extends AbstractOperation {
    private static final Logger logger = LoggerFactory.getLogger(ExperimentsPlatformOperations.class);
    private final ExperimentOperations experimentOperations;
    private LoadingCache<Tuple2<Integer, String>, ExperimentsPlatformModeMode> platformModeCache = createCache(
            tuple -> getExperimentsPlatformModeFromDB(tuple.v1, tuple.v2));

    /**
     * creates a new ExperimentsPlatformOperations
     * @param create the context used to communicate with the database
     * @param cacheEnabled whether the caching functionality should be enabled
     * @param experimentOperations
     */
    public ExperimentsPlatformOperations(DSLContext create, boolean cacheEnabled, ExperimentOperations experimentOperations) {
        super(create, cacheEnabled);
        this.experimentOperations = experimentOperations;
    }

    /**
     * returns the mode for the platform of the experiment
     * <p>
     * this method is cached.
     * @param experiment the primary key of the experiment
     * @param platform the platform to search for
     * @return the mode of the platform
     * @throws IllegalArgumentException if the experimentsPlatform is not existing
     */
    public ExperimentsPlatformModeMode getExperimentsPlatformMode(int experiment, String platform) throws IllegalArgumentException {
        return cacheGetHelper(platformModeCache, Tuple.tuple(experiment, platform),
                tuple -> String.format("unable to load the PlatformMode from the db for experiment %d and platform %s", tuple.v1, tuple.v2));
    }

    /**
     * returns the mode for the platform of the experiment
     * <p>
     * this method uses the db.
     * @param experiment the primary key of the experiment
     * @param platform the platform to search for
     * @return the mode of the platform
     * @throws IllegalArgumentException if the experimentsPlatform is not existing
     */
    private ExperimentsPlatformModeMode getExperimentsPlatformModeFromDB(int experiment, String platform) throws IllegalArgumentException {
        return create.select(EXPERIMENTS_PLATFORM_MODE.MODE)
                .from(EXPERIMENTS_PLATFORM_MODE)
                .where(EXPERIMENTS_PLATFORM_MODE.EXPERIMENTS_PLATFORM.in(
                        DSL.select(EXPERIMENTS_PLATFORM.IDEXPERIMENTS_PLATFORMS)
                                .from(EXPERIMENTS_PLATFORM)
                                .where(EXPERIMENTS_PLATFORM.EXPERIMENT.eq(experiment))
                                .and(EXPERIMENTS_PLATFORM.PLATFORM.eq(platform))
                ))
                .orderBy(EXPERIMENTS_PLATFORM_MODE.TIMESTAMP.desc())
                .limit(1)
                .fetchOptional()
                .map(Record1::value1)
                .orElse(ExperimentsPlatformModeMode.normal);
    }

    /**
     * this method checks whether the platform is in creative-stopping
     * @param experiment the experiment to check for
     * @param platform the platform to check for
     * @return true if in creative-stopping, false if not
     */
    public boolean isInCreativeStopping(int experiment, String platform) {
        return create.fetchExists(
                DSL.select(EXPERIMENTS_PLATFORM_STATUS.PLATFORM_STATUS)
                    .from(EXPERIMENTS_PLATFORM_STATUS)
                    .innerJoin(EXPERIMENTS_PLATFORM).on(
                        EXPERIMENTS_PLATFORM_STATUS.PLATFORM.eq(EXPERIMENTS_PLATFORM.IDEXPERIMENTS_PLATFORMS)
                        .and(EXPERIMENTS_PLATFORM.EXPERIMENT.eq(experiment))
                        .and(EXPERIMENTS_PLATFORM.PLATFORM.eq(platform))
                    )
                    .where(EXPERIMENTS_PLATFORM_STATUS.PLATFORM_STATUS.eq(ExperimentsPlatformStatusPlatformStatus.creative_stopping))
        );
    }

    /**
     * Reserves the number of answers for the given worker.
     * <p>
     * The method first searches for all the open answers, updates them and reserves the difference.
     * @param worker the worker to reserve for
     * @param experiment the primary key of the experiment
     * @return a list of primary keys for the reserverations
     */
    public List<Integer> prepareAnswers(int worker, int experiment) {
        ExperimentRecord experimentRecord = experimentOperations.getExperiment(experiment);
        Integer neededAnswers = experimentRecord.getNeededAnswers();
        create.transaction(config -> {
            int totalAnswers = getAnswersCount(experiment, config);
            int givenAnswers = getAnswersCount(experiment, worker, config);
            int toWorkOn = Math.min(Math.max(neededAnswers - totalAnswers, 0), (experimentRecord.getAnwersPerWorker() - givenAnswers));
            int openReservations = DSL.using(config).select(DSL.count())
                        .from(ANSWER_RESERVATION)
                        .where(ANSWER_RESERVATION.EXPERIMENT.eq(experiment))
                        .and(ANSWER_RESERVATION.WORKER.eq(worker))
                        .and(ANSWER_RESERVATION.USED.eq(false))
                        .fetchOne()
                        .value1();

            logger.trace("Worker {} has {} reserved open answers {}.", worker, openReservations);

            Timestamp now = Timestamp.valueOf(LocalDateTime.now());

            DSL.using(config).update(ANSWER_RESERVATION)
                    .set(ANSWER_RESERVATION.TIMESTAMP, now)
                    .where(ANSWER_RESERVATION.ID_ANSWER_RESERVATION.in(openReservations))
                    .execute();

            int reserveNew = Math.max(toWorkOn - openReservations, 0);
            logger.trace("Reserving {} new Answers for worker {}.", reserveNew, worker);

            List<AnswerReservationRecord> reservationRecords = new ArrayList<>(reserveNew);
            for (int i = 0; i < reserveNew; i++) {
                reservationRecords.add(new AnswerReservationRecord(null, worker, experiment, now, false));
            }

            DSL.using(config).batchInsert(reservationRecords).execute();
        });
        return create.select(ANSWER_RESERVATION.ID_ANSWER_RESERVATION)
                .from(ANSWER_RESERVATION)
                .where(ANSWER_RESERVATION.WORKER.eq(worker))
                .and(ANSWER_RESERVATION.EXPERIMENT.eq(experiment))
                .and(ANSWER_RESERVATION.USED.eq(false))
                .fetch()
                .map(Record1::value1);
    }

    /**
     * Reserves a number of ratings for the given worker.
     * <p>
     * Returns the reserved ratings if the worker has them.
     *
     * @param worker the worker to reserve the ratings for
     * @param experiment the experiment to reserve the ratings for
     * @param amount the amount of ratings to reserve
     * @return a map where the values are the answers to rate and the keys the ids of the ratings
     */
    public Map<Integer, AnswerRecord> prepareRating(int worker, int experiment, int amount) {
        Map<Integer, AnswerRecord> answers = create.transactionResult(config -> {
            Map<Integer, AnswerRecord> reservedRatings = DSL.using(config).select(ANSWER.fields())
                    .select(RATING_RESERVATION.ID_RESERVERD_RATING)
                    .from(ANSWER)
                    .innerJoin(RATING_RESERVATION).on(RATING_RESERVATION.WORKER.eq(worker)
                            .and(RATING_RESERVATION.EXPERIMENT.eq(experiment))
                            .and(RATING_RESERVATION.ANSWER.eq(ANSWER.ID_ANSWER))
                            .and(RATING_RESERVATION.USED.eq(false))
                    )
                    .fetchMap(RATING_RESERVATION.ID_RESERVERD_RATING, record -> record.into(Tables.ANSWER));
            logger.trace("Worker {} has reserved open ratings {}.", worker, reservedRatings);

            Timestamp now = Timestamp.valueOf(LocalDateTime.now());

            DSL.using(config).update(RATING_RESERVATION)
                    .set(RATING_RESERVATION.TIMESTAMP, now)
                    .where(RATING_RESERVATION.ID_RESERVERD_RATING.in(reservedRatings.keySet()))
                    .execute();

            int reserveNew = Math.max(amount - reservedRatings.size(), 0);
            logger.trace("Reserving {} new Ratings for worker {}.", reserveNew, worker);

            LocalDateTime limit = LocalDateTime.now().minus(2, ChronoUnit.HOURS);
            Timestamp timestamp = Timestamp.valueOf(limit);
            Field<Integer> count = DSL.count(RATING_RESERVATION.ID_RESERVERD_RATING).as("count");
            Map<Integer, AnswerRecord> toRate = DSL.using(config).select()
                    .select(ANSWER.fields())
                    .select(count)
                    .from(ANSWER)
                    .leftJoin(RATING_RESERVATION).on(RATING_RESERVATION.ANSWER.eq(ANSWER.ID_ANSWER)
                            .and(RATING_RESERVATION.USED.eq(true).or(RATING_RESERVATION.TIMESTAMP.greaterOrEqual(timestamp))))
                    .where(ANSWER.EXPERIMENT.eq(experiment))
                    .and(ANSWER.WORKER_ID.notEqual(worker))
                    .and(ANSWER.ID_ANSWER.notIn(
                            DSL.select(RATING_RESERVATION.ANSWER).from(RATING_RESERVATION).where(RATING_RESERVATION.WORKER.eq(worker).and(RATING_RESERVATION.EXPERIMENT.eq(experiment)))))
                    .and(
                            ANSWER.QUALITY_ASSURED.eq(true).and(ANSWER.QUALITY.greaterOrEqual(
                                    DSL.select(EXPERIMENT.RESULT_QUALITY_THRESHOLD)
                                            .from(EXPERIMENT)
                                            .where(EXPERIMENT.ID_EXPERIMENT.eq(experiment))
                            )).or(DSL.condition(true))
                    )
                    .groupBy(ANSWER.fields())
                    .having(count.lessThan(
                            DSL.select(EXPERIMENT.RATINGS_PER_ANSWER).from(EXPERIMENT).where(EXPERIMENT.ID_EXPERIMENT.eq(experiment))))
                    .orderBy(ANSWER.QUALITY_ASSURED.desc())
                    .limit(reserveNew)
                    .fetchMap(Tables.ANSWER.ID_ANSWER, record -> record.into(Tables.ANSWER));

            List<RatingReservationRecord> reservations = toRate.values().stream()
                    .map(answer -> new RatingReservationRecord(null, worker, experiment, answer.getIdAnswer(), timestamp, false))
                    .collect(Collectors.toList());

            DSL.using(config).batchInsert(reservations).execute();

            Map<Integer, AnswerRecord> reserved = reservedRatings.entrySet().stream()
                    .collect(Collectors.toMap(entry -> entry.getValue().getIdAnswer(), Map.Entry::getValue));

            toRate.putAll(reserved);

            return toRate;
        });

        logger.trace("Worker {} can rate {}", worker, answers.values());

        List<Integer> answerIds = answers.values().stream()
                .map(AnswerRecord::getIdAnswer)
                .collect(Collectors.toList());

        Result<RatingReservationRecord> ratings = create.selectFrom(RATING_RESERVATION)
                .where(RATING_RESERVATION.ANSWER.in(answerIds))
                .and(RATING_RESERVATION.WORKER.eq(worker))
                .fetch();

        return ratings.stream()
                .collect(Collectors.toMap(RatingReservationRecord::getIdReserverdRating, record -> answers.get(record.getAnswer())));
    }

    /**
     * returns the amount of answers submitted for the passed experiment
     * @param experimentID the primary key of the experiment
     * @param worker the worker to exclude
     * @return the number of answers submitted
     */
    public int getAnswersCountWithoutWorker(int experimentID, int worker) {
        return getAnswersCountWithoutWorker(experimentID, worker, create.configuration());
    }

    /**
     * returns the amount of answers submitted for the passed experiment
     * @param experiment the experiment
     * @param config the config to use
     * @return the answerCount
     */
    private int getAnswersCount(int experiment, Configuration config) {
        return getAnswersCountWithoutWorker(experiment, -1, config);
    }

    /**
     * returns the amount of answers submitted for the passed experiment
     * @param experiment the experiment
     * @param config the config to use
     * @return the answerCount
     */
    private int getAnswersCountWithoutWorker(int experiment, int worker, Configuration config) {
        LocalDateTime limit = LocalDateTime.now().minus(2, ChronoUnit.HOURS);
        Timestamp timestamp = Timestamp.valueOf(limit);
        return DSL.using(config).fetchCount(
                DSL.select(ANSWER_RESERVATION.ID_ANSWER_RESERVATION)
                        .from(ANSWER_RESERVATION)
                        .leftJoin(ANSWER).onKey()
                        .where(ANSWER_RESERVATION.USED.eq(true).or(
                                ANSWER_RESERVATION.TIMESTAMP.greaterOrEqual(timestamp)
                                .and(ANSWER_RESERVATION.WORKER.notEqual(worker))
                        ))
                        .and(ANSWER_RESERVATION.EXPERIMENT.eq(experiment))
                        .and(ANSWER.QUALITY_ASSURED.eq(true).and(Tables.ANSWER.QUALITY.greaterOrEqual(
                                DSL.select(EXPERIMENT.RESULT_QUALITY_THRESHOLD)
                                        .from(EXPERIMENT)
                                        .where(EXPERIMENT.ID_EXPERIMENT.eq(experiment)))
                                ).or(DSL.condition(true))
                        )
        );
    }

    /**
     * returns the amount of answers submitted for the passed experiment by the worker
     * @param experimentID the primary key of the experiment
     * @param workerID the worker to check for
     * @return the number of answers submitted
     */
    public int getAnswersCount(int experimentID, int workerID) {
        return getAnswersCount(experimentID, workerID, create.configuration());
    }

    /**
     * returns the amount of answers submitted for the passed experiment by the worker
     * @param experimentID the primary key of the experiment
     * @param workerID the worker to check for
     * @param config the configuration to use
     * @return the number of answers submitted
     */
    private int getAnswersCount(int experimentID, int workerID, Configuration config) {
        return DSL.using(config).fetchCount(
                DSL.selectFrom(Tables.ANSWER_RESERVATION)
                        .where(Tables.ANSWER_RESERVATION.EXPERIMENT.eq(experimentID))
                        .and(Tables.ANSWER_RESERVATION.WORKER.eq(workerID))
                        .and(Tables.ANSWER_RESERVATION.USED.eq(true))
        );
    }

    /**
     * returns the amount of answers submitted for the passed experiment by the worker
     * @param experimentID the primary key of the experiment
     * @return the number of answers submitted
     */
    public int getGoodAnswersCount(int experimentID) {
        return create.fetchCount(
                DSL.select(ANSWER.ID_ANSWER)
                        .from(ANSWER)
                        .where(ANSWER.EXPERIMENT.eq(experimentID))
                        .and(ANSWER.QUALITY.greaterOrEqual(
                                DSL.select(EXPERIMENT.RESULT_QUALITY_THRESHOLD)
                                        .from(EXPERIMENT)
                                        .where(EXPERIMENT.ID_EXPERIMENT.eq(experimentID))
                        ))
        );
    }

    /**
     * returns the amount of ratings submitted for the passed experiment by the worker
     * @param experimentID the primary key of the experiment
     * @param workerID the worker to check for
     * @return the number of ratings submitted
     */
    public int getRatingsCount(int experimentID, int workerID) {
        return create.fetchCount(
                DSL.selectFrom(Tables.RATING)
                        .where(Tables.RATING.EXPERIMENT.eq(experimentID))
                        .and(Tables.RATING.WORKER_ID.eq(workerID))
        );
    }

    /**
     * Lists all the answers and the count of their ratings were the answers were not from the passed worker and
     * do belong to the experiment.
     * @param experiment the experiment worked on
     * @param worker the primary key of the worker
     * @return the result
     */
    public List<Record> getOtherAnswersWithCount(int experiment, int worker) {
        LocalDateTime limit = LocalDateTime.now().minus(2, ChronoUnit.HOURS);
        Timestamp timestamp = Timestamp.valueOf(limit);
        return create.select(ANSWER.fields())
                .select(DSL.count(RATING.ID_RATING).as("count"))
                .from(ANSWER)
                .leftJoin(RATING).on(RATING.ANSWER_R.eq(ANSWER.ID_ANSWER)
                        .and(RATING.RATING_.isNotNull().or(RATING.TIMESTAMP.greaterOrEqual(timestamp))))
                .where(ANSWER.WORKER_ID.notEqual(worker))
                .and(ANSWER.EXPERIMENT.eq(experiment))
                .groupBy(ANSWER.fields())
                .fetch();
    }

    /**
     * returns true if the worker submitted work to our system
     * @param experiment the active experiment
     * @param worker the worker
     * @return true of the worker has submitted work, false if not
     */
    public boolean hasWork(int experiment, int worker) {
        boolean hasAnswers = create.fetchExists(
                DSL.select(ANSWER_RESERVATION.ID_ANSWER_RESERVATION)
                        .from(ANSWER_RESERVATION)
                        .where(ANSWER_RESERVATION.WORKER.eq(worker))
                        .and(ANSWER_RESERVATION.EXPERIMENT.eq(experiment))
                        .and(ANSWER_RESERVATION.USED.eq(true))
        );
        if (hasAnswers) {
            return hasAnswers;
        }

        boolean hasRatings = create.fetchExists(
                DSL.select(RATING_RESERVATION.ID_RESERVERD_RATING)
                        .from(RATING_RESERVATION)
                        .where(RATING_RESERVATION.WORKER.eq(worker))
                        .and(RATING_RESERVATION.EXPERIMENT.eq(experiment))
                        .and(RATING_RESERVATION.USED.eq(true))
        );
        return hasRatings;
    }

    /**
     * releases all the unused reservations
     * @param worker the worker associated with the reservations
     * @param experiment the experiment worked on
     */
    public void releaseReservations(int worker, int experiment) {
        create.deleteFrom(ANSWER_RESERVATION)
                .where(ANSWER_RESERVATION.WORKER.eq(worker))
                .and(ANSWER_RESERVATION.USED.eq(false))
                .and(ANSWER_RESERVATION.EXPERIMENT.eq(experiment))
                .execute();
        create.deleteFrom(RATING_RESERVATION)
                .where(RATING_RESERVATION.WORKER.eq(worker))
                .and(RATING_RESERVATION.USED.eq(false))
                .and(RATING_RESERVATION.EXPERIMENT.eq(experiment))
                .execute();
    }
}
