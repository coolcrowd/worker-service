package edu.kit.ipd.crowdcontrol.workerservice.database;

import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.AnswerRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.TaskRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.OperationsDataHolder;
import org.junit.Test;

import java.util.List;

/**
 * @author LeanderK
 * @version 1.0
 */
public class playground {

    @Test
    //@Ignore
    public void test() {
        // Initialise your data provider (implementation further down):
        OperationsDataHolder data = new OperationsDataHolder();
        boolean b = data.createExperimentOperations()
                .insertTaskChooserParamOrIgnore("test1", "test2", "aaf", "aafaf");
        System.out.println("test");
    }
}
