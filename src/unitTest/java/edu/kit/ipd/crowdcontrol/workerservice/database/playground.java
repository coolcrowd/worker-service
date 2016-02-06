package edu.kit.ipd.crowdcontrol.workerservice.database;

import edu.kit.ipd.crowdcontrol.workerservice.database.operations.OperationsDataHolder;
import org.junit.Test;

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
        boolean b = data.createWorkerOperations()
                .isUnderThreshold(10, 1);
        System.out.println("test");
    }
}
