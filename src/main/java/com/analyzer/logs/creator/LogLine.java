package com.analyzer.logs.creator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * This is used to represent a created log line.
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class LogLine {
    String ipAddress;
    EndpointInfo endpointInfo;
    int httpStatusCode;
}
