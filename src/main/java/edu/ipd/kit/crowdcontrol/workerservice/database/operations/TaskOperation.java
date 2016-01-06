package edu.ipd.kit.crowdcontrol.workerservice.database.operations;

import edu.ipd.kit.crowdcontrol.workerservice.database.model.tables.records.AnswerRecord;
import org.jooq.DSLContext;

import java.util.List;

/**
 * TaskOperations contains all queries concerned with the Creative- and Rating-Tasks.
 * @author LeanderK
 * @version 1.0
 */
public class TaskOperation extends AbstractOperation {

    /**
     * creates a new TaskOperation
     * @param create the context used to communicate with the database
     */
    public TaskOperation(DSLContext create) {
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
        //TODO
        return null;
    }
}
