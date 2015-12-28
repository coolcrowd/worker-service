package edu.ipd.kit.crowdcontrol.workerservice.database.operations;

import org.jooq.DSLContext;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author LeanderK
 * @version 1.0
 */
public abstract class AbstractOperation {
    protected final DSLContext create;

    protected AbstractOperation(DSLContext create) {
        this.create = create;
    }

    protected <T, X, Y> Map<T, Y> mapMap(Map<T, X> source, Function<X, Y> mapping) {
        Map<T, Y> result = new HashMap<>();
        for (Map.Entry<T, X> entry : source.entrySet()) {
            result.put(entry.getKey(), mapping.apply(entry.getValue()));
        }
        return result;
    }
}
