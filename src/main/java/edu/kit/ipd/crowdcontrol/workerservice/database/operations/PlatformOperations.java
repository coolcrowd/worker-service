package edu.kit.ipd.crowdcontrol.workerservice.database.operations;

import edu.kit.ipd.crowdcontrol.workerservice.database.model.Tables;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.PlatformRecord;
import org.jooq.DSLContext;

/**
 * Contains all the queries responsible for the Platforms.
 * @author LeanderK
 * @version 1.0
 */
public class PlatformOperations extends AbstractOperation {

    /**
     * creates a new instance of PlatformOperations
     * @param create the context to use
     */
    public PlatformOperations(DSLContext create) {
        super(create);
    }

    /**
     * returns the Platform for the passed name or throws an exception.
     * @param name the name of the platform
     * @return the platformsRecord corresponding to the name
     * @throws PlatformNotFoundException if the platform is not found
     */
    public PlatformRecord getPlatform(String name) throws PlatformNotFoundException {
        return create.selectFrom(Tables.PLATFORM)
                .where(Tables.PLATFORM.ID_PLATFORM.eq(name))
                .fetchOptional()
                .orElseThrow(() -> new PlatformNotFoundException(name));
    }
}
