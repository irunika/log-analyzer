package com.logs.analyzer.creator;

import com.logs.analyzer.exception.LogLineCreatorException;

/**
 * Create {@link LogLine} for the given Log Line Pattern.
 * Extend this class to support different types of access log patterns.
 */
public interface LogLineCreator {

    /**
     * Create a log line from the given log line string pattern.
     *
     * @param logLineStr Log line string.
     * @return constructed {@link LogLine}.
     */
    LogLine create(String logLineStr) throws LogLineCreatorException;
}
