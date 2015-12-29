package edu.ipd.kit.crowdcontrol.workerservice.database.operations;

import edu.ipd.kit.crowdcontrol.workerservice.database.model.Tables;
import edu.ipd.kit.crowdcontrol.workerservice.database.model.tables.records.PlatformsRecord;
import org.jooq.DSLContext;

/**
 * Contains all the queries responsible for the Platforms.
 * @author LeanderK
 * @version 1.0
 */
public class PlatformOperations extends AbstractOperation {

    public PlatformOperations(DSLContext create) {
        super(create);
    }

    /**
     * returns the Platform for the name
     * @param name the Name
     * @return the platformsRecord
     * @throws PlatformNotFoundException if the platform is not found
     */
    public PlatformsRecord getPlatform(String name) throws PlatformNotFoundException {
        return create.selectFrom(Tables.PLATFORMS)
                .where(Tables.PLATFORMS.NAME.eq(name))
                .fetchOptional()
                .orElseThrow(() -> new PlatformNotFoundException(name));
    }
}
