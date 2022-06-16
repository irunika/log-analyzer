package com.analyzer.logs.creator;

import com.analyzer.logs.exception.LogLineCreationException;

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
    LogLine create(String logLineStr) throws LogLineCreationException;
}
