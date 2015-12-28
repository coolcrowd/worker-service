package edu.ipd.kit.crowdcontrol.workerservice.database.operations;

import edu.ipd.kit.crowdcontrol.workerservice.database.model.Tables;
import edu.ipd.kit.crowdcontrol.workerservice.database.model.tables.records.PlatformsRecord;
import org.jooq.DSLContext;

import java.util.Optional;

/**
 * Contains all the queries responsible for the Platforms.
 * @author LeanderK
 * @version 1.0
 */
public class PlatformOperations extends AbstractOperation {

    protected PlatformOperations(DSLContext create) {
        super(create);
    }

    /**
     * returns the Platform for the name
     * @param name the Name
     * @return the platformsRecord or empty if not found
     */
    public Optional<PlatformsRecord> getPlatform(String name) {
        return create.selectFrom(Tables.PLATFORMS)
                .where(Tables.PLATFORMS.NAME.eq(name))
                .fetchOptional();
    }
}
