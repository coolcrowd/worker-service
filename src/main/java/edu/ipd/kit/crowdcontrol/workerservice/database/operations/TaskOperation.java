package edu.ipd.kit.crowdcontrol.workerservice.database.operations;

import edu.ipd.kit.crowdcontrol.workerservice.database.model.tables.records.AnswerRecord;
import org.jooq.DSLContext;

import java.util.List;

/**
 * @author LeanderK
 * @version 1.0
 */
public class TaskOperation extends AbstractOperation {

    public TaskOperation(DSLContext create) {
        super(create);
    }

    public List<AnswerRecord> prepareRating(int worker, int experiment, int amount) {
        //TODO
        return null;
    }
}
