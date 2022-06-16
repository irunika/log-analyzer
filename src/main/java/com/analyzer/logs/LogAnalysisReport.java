package com.analyzer.logs;

import com.analyzer.logs.creator.EndpointInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Set;

@Getter
@AllArgsConstructor
public class LogAnalysisReport {
    private final Set<String> ipAddresses;
    private final List<ObjectCounter<String>> ipCount;
    private final List<ObjectCounter<EndpointInfo>> endpointCount;
}
