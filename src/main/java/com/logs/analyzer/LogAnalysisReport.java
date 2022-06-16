package com.logs.analyzer;

import com.logs.analyzer.creator.EndpointInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Set;

/**
 * Holds the analysed values from the analyser.
 */
@Getter
@AllArgsConstructor
public class LogAnalysisReport {
    private final Set<String> ipAddresses;
    private final List<Counter<String>> ipCount;
    private final List<Counter<EndpointInfo>> endpointCount;
}
