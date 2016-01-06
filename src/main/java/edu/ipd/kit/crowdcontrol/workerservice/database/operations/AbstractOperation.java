package edu.ipd.kit.crowdcontrol.workerservice.database.operations;

import org.jooq.DSLContext;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * superclass of all Database-Operations. Contains abstractions and helper-methods.
 * @author LeanderK
 * @version 1.0
 */
public abstract class AbstractOperation {
    protected final DSLContext create;

    /**
     * creates a new AbstractOperation
     * @param create the Context to use
     */
    public AbstractOperation(DSLContext create) {
        this.create = create;
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
}
