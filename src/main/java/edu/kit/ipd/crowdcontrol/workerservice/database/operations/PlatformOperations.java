package edu.kit.ipd.crowdcontrol.workerservice.database.operations;

import com.google.common.cache.Cache;
import com.google.common.cache.LoadingCache;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.Tables;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.PlatformRecord;
import org.jooq.DSLContext;

/**
 * Contains all the queries responsible for the Platforms.
 * @author LeanderK
 * @version 1.0
 */
public class PlatformOperations extends AbstractOperation {
    private LoadingCache<String, PlatformRecord> platformCache = createCache(this::getPlatformFromDB);

    /**
     * creates a new instance of PlatformOperations
     * @param create the context to use
     * @param cacheEnabled whether the caching functionality should be enabled
     */
    public PlatformOperations(DSLContext create, boolean cacheEnabled) {
        super(create, cacheEnabled);
    }

    /**
     * returns the Platform for the passed name or throws an exception.
     * <p>
     * this method is cached
     * @param name the name of the platform
     * @return the platformsRecord corresponding to the name
     * @throws PlatformNotFoundException if the platform is not found
     */
    public PlatformRecord getPlatform(String name) throws PlatformNotFoundException {
        return cacheGetHelper(platformCache, name,
                key -> String.format("unable to get PLatform %s from the db", key));
    }

    /**
     * returns the Platform for the passed name or throws an exception.
     * <p>
     * fetches the platform from the db
     * @param name the name of the platform
     * @return the platformsRecord corresponding to the name
     * @throws PlatformNotFoundException if the platform is not found
     */
    private PlatformRecord getPlatformFromDB(String name) throws PlatformNotFoundException {
        return create.selectFrom(Tables.PLATFORM)
                .where(Tables.PLATFORM.ID_PLATFORM.eq(name))
                .fetchOptional()
                .orElseThrow(() -> new PlatformNotFoundException(name));
    }
}
