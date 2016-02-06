package edu.kit.ipd.crowdcontrol.workerservice.database.operations;

import java.sql.SQLException;
import java.sql.Savepoint;

/**
 * @author LeanderK
 * @version 1.0
 */
public class MockSavepoint implements Savepoint {
    private final String name;
    private final int savepointId = OperationsDataHolder.nextRandomInt();

    public MockSavepoint(String name) {
        this.name = name;
    }

    /**
     * Retrieves the generated ID for the savepoint that this
     * <code>Savepoint</code> object represents.
     *
     * @return the numeric ID of this savepoint
     * @throws SQLException if this is a named savepoint
     * @since 1.4
     */
    @Override
    public int getSavepointId() throws SQLException {
        return 0;
    }

    /**
     * Retrieves the name of the savepoint that this <code>Savepoint</code>
     * object represents.
     *
     * @return the name of this savepoint
     * @throws SQLException if this is an un-named savepoint
     * @since 1.4
     */
    @Override
    public String getSavepointName() throws SQLException {
        return name;
    }
}
