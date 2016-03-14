package edu.kit.ipd.crowdcontrol.workerservice.database.operations;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.jooq.DSLContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * superclass of all Database-Operations. Contains abstractions and helper-methods.
 * @author LeanderK
 * @version 1.0
 */
public abstract class AbstractOperation {
    protected final DSLContext create;
    private final boolean cacheEnabled;

    /**
     * creates a new AbstractOperation
     * @param create the context to use to communicate with the database
     * @param cacheEnabled whether the caching functionality should be enabled
     */
    public AbstractOperation(DSLContext create, boolean cacheEnabled) {
        this.create = create;
        this.cacheEnabled = cacheEnabled;
    }

    /**
     * creates a cache that has a maximum capacity of 1000 values and expires after 5 minutes when caching is enabled,
     * otherwise just loads every time.
     * @param cacheLoader the cacheLoader
     * @param <K> the type of the key
     * @param <V> the type of the value
     * @return a LoadingCache
     */
    protected <K, V> LoadingCache<K, V> createCache(CacheLoaderInterface<K, V> cacheLoader) {
        if (cacheEnabled) {
            return CacheBuilder.newBuilder()
                    .maximumSize(20)
                    .refreshAfterWrite(5, TimeUnit.MINUTES)
                    .expireAfterAccess(30, TimeUnit.MINUTES)
                    .build(new CacheLoader<K, V>() {
                        @Override
                        public V load(K key) throws Exception {
                            return cacheLoader.load(key);
                        }
                    });
        } else {
            return CacheBuilder.newBuilder()
                    .maximumSize(0)
                    .build(new CacheLoader<K, V>() {
                        @Override
                        public V load(K key) throws Exception {
                            return cacheLoader.load(key);
                        }
                    });
        }
    }

    /**
     * little helper method to prevent duplicate code
     * @param cache the cache to check
     * @param key the key for the cache
     * @param errorMessage the (eventual) error message
     * @param <K> the type of the key
     * @param <V> the type of the value
     * @return the value or runtimeException
     */
    protected <K, V> V cacheGetHelper(LoadingCache<K, V> cache, K key, Function<K, String> errorMessage) {
        try {
            return cache.get(key);
        } catch (ExecutionException e) {
            throw new RuntimeException(errorMessage.apply(key), e.getCause());
        }
    }

    /**
     * maps the values of the map
     * @param source the source map
     * @param mapping the mapping of the values of the map
     * @param <T> the type of the keY
     * @param <X> the type of the source-map values
     * @param <Y> the type of the target-map values
     * @return the resulting mapped Map
     */
    protected <T, X, Y> Map<T, Y> mapMap(Map<T, X> source, Function<X, Y> mapping) {
        Map<T, Y> result = new HashMap<>();
        for (Map.Entry<T, X> entry : source.entrySet()) {
            result.put(entry.getKey(), mapping.apply(entry.getValue()));
        }
        return result;
    }

    // functionalInterface leads to cleaner code with createCache
    @FunctionalInterface
    protected interface CacheLoaderInterface<K, V> {
        V load(K key) throws Exception;
    }
}
