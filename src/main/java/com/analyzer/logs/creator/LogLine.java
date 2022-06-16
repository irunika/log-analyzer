package com.analyzer.logs.creator;

import lombok.Data;

/**
 * This is used to represent a created log line.
 */
@Data
public class LogLine {
    String ipAddress;
    EndpointInfo endpointInfo;
    String httpResponseCode;
}
