package edu.kit.ipd.crowdcontrol.workerservice;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Thrown on requests with invalid accept header.
 *
 * @author Niklas Keller
 */
public class NotAcceptableException extends BadRequestException {
    /**
     * @param providedType
     *         Provided accept header.
     * @param supportedTypes
     *         Supported content-type values.
     */
    public NotAcceptableException(String providedType, String... supportedTypes) {
        super("The requested resource is not available as '%s'. Available: %s.", providedType,
                Arrays.stream(supportedTypes).collect(Collectors.joining("', '", "'", "'")));
    }
}
