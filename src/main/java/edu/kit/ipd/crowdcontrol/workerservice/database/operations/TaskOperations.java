package edu.kit.ipd.crowdcontrol.workerservice.database.operations;

import edu.kit.ipd.crowdcontrol.workerservice.database.model.Tables;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.AnswerRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.RatingRecord;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

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
     * reserves a number of ratings for the given worker.
     * @param worker the worker to reserve the ratings for
     * @param experiment the experiment to reserve the ratings for
     * @param amount the amount of ratings to reserve
     * @return the list of answers to rate
     */
    public List<AnswerRecord> prepareRating(int worker, int experiment, int amount) {
        return create.transactionResult(config -> {
            Field<Integer> count = DSL.selectCount().from(Tables.RATING).asField();

            LocalDateTime limit = LocalDateTime.now().minus(2, ChronoUnit.HOURS);
            Timestamp timestamp = Timestamp.valueOf(limit);
            List<AnswerRecord> toRate = DSL.using(config).select()
                    .select(Tables.ANSWER.fields())
                    .select(count)
                    .from(Tables.ANSWER)
                    .leftJoin(Tables.RATING).onKey()
                    .where(Tables.ANSWER.TASK.eq(experiment))
                    .and(Tables.RATING.RATING_.isNotNull().or(Tables.RATING.TIMESTAMP.greaterOrEqual(timestamp)))
                    .having(count.lessThan(
                            DSL.select(Tables.EXPERIMENT.RATINGS_PER_ANSWER).where(Tables.EXPERIMENT.IDEXPERIMENT.eq(worker))))
                    .limit(amount)
                    .fetch()
                    .map(record -> record.into(Tables.ANSWER));

            List<RatingRecord> emptyRatings = toRate.stream()
                    .map(answer -> {
                        RatingRecord ratingRecord = new RatingRecord();
                        ratingRecord.setAnswerR(answer.getIdanswer());
                        ratingRecord.setWorkerId(worker);
                        ratingRecord.setTask(experiment);
                        return ratingRecord;
                    })
                    .collect(Collectors.toList());

            DSL.using(config).batchInsert(emptyRatings);

            return toRate;
        });
    }
}
