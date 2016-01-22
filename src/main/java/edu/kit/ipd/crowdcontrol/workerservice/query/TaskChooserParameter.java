package edu.kit.ipd.crowdcontrol.workerservice.query;

import java.util.Objects;

/**
 * represents a parameter for the TaskChooser
 * @author LeanderK
 * @version 1.0
 */
public class TaskChooserParameter {
    private final String description;
    private final String regex;
    private final String data;

    /**
     * constructs a new instance of TaskChooserParameter
     * @param description the description of the parameter, not null
     * @param regex the regex to check the input with, not null
     * @param data the data, may be null
     */
    public TaskChooserParameter(String description, String regex, String data) {
        Objects.requireNonNull(description);
        Objects.requireNonNull(regex);
        this.description = description;
        this.regex = regex;
        this.data = data;
    }

    /**
     * constructs a new instance of TaskChooserParameter
     * @param description the description of the parameter, not null
     * @param regex the regex to check the input with, not null
     */
    public TaskChooserParameter(String description, String regex) {
        this(description, regex, null);
    }

    /**
     * returns the description of the TaskChooserParameter
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * the regex to check the input with
     * @return the regex
     */
    public String getRegex() {
        return regex;
    }

    /**
     * the data or null of no data was set
     * @return data or null
     */
    public String getData() {
        return data;
    }
}
