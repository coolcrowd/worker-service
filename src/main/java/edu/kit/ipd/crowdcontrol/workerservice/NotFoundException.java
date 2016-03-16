package edu.kit.ipd.crowdcontrol.workerservice;

/**
 * this exception represents an 404 NOT FOUND status
 * @author LeanderK
 * @version 1.0
 */
public class NotFoundException extends RuntimeException {
    /**
     * Constructs a new NotFoundException exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public NotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new NotFoundException exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public NotFoundException() {
    }
}
