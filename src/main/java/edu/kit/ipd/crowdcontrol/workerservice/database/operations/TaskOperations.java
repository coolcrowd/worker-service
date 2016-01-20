package edu.kit.ipd.crowdcontrol.workerservice.database.operations;

import edu.kit.ipd.crowdcontrol.workerservice.database.model.Tables;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.AnswerRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.RatingRecord;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record1;
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
     * returns the ID of the Task
     * @param experiment the experiment the task belongs to
     * @param platform the platform the task belongs to
     * @return the id of the task
     * @throws IllegalArgumentException if the Task is not existing
     */
    public int getTaskID(int experiment, String platform) throws IllegalArgumentException {
        return create.select(Tables.TASK.ID_TASK)
                .from(Tables.TASK)
                .where(Tables.TASK.EXPERIMENT.eq(experiment))
                .and(Tables.TASK.CROWD_PLATFORM.eq(platform))
                .fetchOptional()
                .map(Record1::value1)
                .orElseThrow(() -> new IllegalArgumentException("no Task existing for: experiment=" + experiment +
                        " and platform=" + platform));
    }

    /**
     * reserves a number of ratings for the given worker.
     * @param worker the worker to reserve the ratings for
     * @param task the task the worker is working on
     * @param experiment the experiment to reserve the ratings for
     * @param amount the amount of ratings to reserve
     * @return the list of answers to rate
     */
    public List<AnswerRecord> prepareRating(int worker, int task, int experiment, int amount) {
        return create.transactionResult(config -> {
            LocalDateTime limit = LocalDateTime.now().minus(2, ChronoUnit.HOURS);
            Timestamp timestamp = Timestamp.valueOf(limit);
            Field<Integer> count = DSL.count(Tables.RATING.ID_RATING).as("count");
            List<AnswerRecord> toRate = DSL.using(config).select()
                    .select(Tables.ANSWER.fields())
                    .select(count)
                    .from(Tables.ANSWER)
                    .leftJoin(Tables.RATING).on(Tables.RATING.ANSWER_R.eq(Tables.ANSWER.ID_ANSWER).and(Tables.RATING.RATING_.isNotNull().or(Tables.RATING.TIMESTAMP.greaterOrEqual(timestamp))))
                    .where(Tables.ANSWER.TASK.in(DSL.select(Tables.TASK.ID_TASK).from(Tables.TASK).where(Tables.TASK.EXPERIMENT.eq(experiment))))
                    .groupBy(Tables.ANSWER.fields())
                    .having(count.lessThan(
                            DSL.select(Tables.EXPERIMENT.RATINGS_PER_ANSWER).from(Tables.EXPERIMENT).where(Tables.EXPERIMENT.ID_EXPERIMENT.eq(experiment))))
                    .limit(amount)
                    .fetch()
                    .map(record -> record.into(Tables.ANSWER));

            List<RatingRecord> emptyRatings = toRate.stream()
                    .map(answer -> {
                        RatingRecord ratingRecord = new RatingRecord();
                        ratingRecord.setAnswerR(answer.getIdAnswer());
                        ratingRecord.setWorkerId(worker);
                        ratingRecord.setTask(task);
                        return ratingRecord;
                    })
                    .collect(Collectors.toList());

            DSL.using(config).batchInsert(emptyRatings).execute();

            return toRate;
        });
    }
}
