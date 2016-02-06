package edu.kit.ipd.crowdcontrol.workerservice.database.operations;

import edu.kit.ipd.crowdcontrol.workerservice.database.model.Tables;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.AnswerRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.RatingRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.TaskRecord;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static edu.kit.ipd.crowdcontrol.workerservice.database.model.Tables.*;

/**
 * TaskOperations contains all queries concerned with the Creative- and Rating-Tasks.
 * @author LeanderK
 * @version 1.0
 */
public class TaskOperations extends AbstractOperation {

    /**
     * creates a new TaskOperation
     * @param create the context used to communicate with the database
     */
    public TaskOperations(DSLContext create) {
        super(create);
    }

    /**
     * returns the TaskRecord
     * @param experiment the experiment the task belongs to
     * @param platform the platform the task belongs to
     * @return the record of the task in the database
     * @throws IllegalArgumentException if the Task is not existing
     */
    public TaskRecord getTask(int experiment, String platform) throws IllegalArgumentException {
        return create.selectFrom(TASK)
                .where(TASK.EXPERIMENT.eq(experiment))
                .and(TASK.CROWD_PLATFORM.eq(platform))
                .fetchOptional()
                .orElseThrow(() -> new IllegalArgumentException("no Task existing for: experiment=" + experiment +
                        " and platform=" + platform));
    }



    /**
     * reserves a number of ratings for the given worker.
     * @param worker the worker to reserve the ratings for
     * @param experiment the experiment to reserve the ratings for
     * @param amount the amount of ratings to reserve
     * @return a map where the values are the answers to rate and the keys the ids of the ratings
     */
    public Map<Integer, AnswerRecord> prepareRating(int worker, int experiment, int amount) {
        Map<Integer, AnswerRecord> answers = create.transactionResult(config -> {
            LocalDateTime limit = LocalDateTime.now().minus(2, ChronoUnit.HOURS);
            Timestamp timestamp = Timestamp.valueOf(limit);
            Field<Integer> count = DSL.count(RATING.ID_RATING).as("count");
            Map<Integer, AnswerRecord> toRate = DSL.using(config).select()
                    .select(ANSWER.fields())
                    .select(count)
                    .from(ANSWER)
                    .leftJoin(RATING).on(RATING.ANSWER_R.eq(ANSWER.ID_ANSWER)
                            .and(RATING.RATING_.isNotNull().or(RATING.TIMESTAMP.greaterOrEqual(timestamp))))
                    .where(ANSWER.EXPERIMENT.eq(experiment))
                    .and(ANSWER.WORKER_ID.notEqual(worker))
                    .and(ANSWER.ID_ANSWER.notIn(
                            DSL.select(RATING.ANSWER_R).where(RATING.WORKER_ID.eq(worker).and(RATING.EXPERIMENT.eq(experiment)))))
                    .and(ANSWER.QUALITY_ASSURED.eq(true).and(ANSWER.QUALITY.notEqual(0)).or(DSL.condition(true)))
                    .groupBy(ANSWER.fields())
                    .having(count.lessThan(
                            DSL.select(EXPERIMENT.RATINGS_PER_ANSWER).from(EXPERIMENT).where(EXPERIMENT.ID_EXPERIMENT.eq(experiment))))
                    .orderBy(ANSWER.QUALITY_ASSURED.desc())
                    .limit(amount)
                    .fetchMap(Tables.ANSWER.ID_ANSWER, record -> record.into(Tables.ANSWER));


            List<RatingRecord> emptyRatings = toRate.values().stream()
                    .map(answer -> {
                        RatingRecord ratingRecord = new RatingRecord();
                        ratingRecord.setAnswerR(answer.getIdAnswer());
                        ratingRecord.setWorkerId(worker);
                        ratingRecord.setExperiment(experiment);
                        return ratingRecord;
                    })
                    .collect(Collectors.toList());

            DSL.using(config).batchInsert(emptyRatings).execute();

            return toRate;
        });

        List<Integer> answerIds = answers.values().stream().map(AnswerRecord::getIdAnswer).collect(Collectors.toList());

        Result<RatingRecord> ratings = create.selectFrom(RATING)
                .where(RATING.ANSWER_R.in(answerIds))
                .and(RATING.WORKER_ID.eq(worker))
                .fetch();

        return ratings.stream()
                .collect(Collectors.toMap(RatingRecord::getIdRating, record -> answers.get(record.getAnswerR())));
    }

    /**
     * returns the amount of answers submitted for the passed experiment
     * @param experimentID the primary key of the experiment
     * @return the number of answers submitted
     */
    public int getAnswersCount(int experimentID) {
        return create.fetchCount(
                DSL.selectFrom(ANSWER)
                        .where(ANSWER.EXPERIMENT.eq(experimentID))
                        .and(ANSWER.QUALITY_ASSURED.eq(true).and(Tables.ANSWER.QUALITY.notEqual(0))
                                .or(DSL.condition(true))
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
        return create.fetchCount(
                DSL.selectFrom(Tables.ANSWER)
                        .where(Tables.ANSWER.EXPERIMENT.eq(experimentID))
                        .and(Tables.ANSWER.WORKER_ID.eq(workerID))
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
}
