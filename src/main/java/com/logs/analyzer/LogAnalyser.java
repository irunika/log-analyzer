package com.logs.analyzer;

import com.logs.analyzer.creator.EndpointInfo;
import com.logs.analyzer.creator.LogLine;
import com.logs.analyzer.creator.LogLineCreator;
import com.logs.analyzer.creator.LogLineCreatorImpl;
import com.logs.analyzer.exception.LogLineCreatorException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * Analyze the log and provide report.
 */
public class LogAnalyser {
    private final LogLineCreator logLineCreator;

    public LogAnalyser() {
        this.logLineCreator = new LogLineCreatorImpl();
    }

    public LogAnalysisReport analyse(BufferedReader reader) throws IOException, LogLineCreatorException {
        Map<String, Counter<String>> ipCountMap = new HashMap<>();
        Map<EndpointInfo, Counter<EndpointInfo>> endpointCount = new HashMap<>();
        Set<String> ipAddressesSet = new HashSet<>();

        String currentLine;
        while ((currentLine = reader.readLine()) != null) {
            LogLine logLine = logLineCreator.create(currentLine);
            ipCountMap.putIfAbsent(logLine.getIpAddress(), new Counter<>(logLine.getIpAddress()));
            ipCountMap.get(logLine.getIpAddress()).increase();
            endpointCount.putIfAbsent(logLine.getEndpointInfo(), new Counter<>(logLine.getEndpointInfo()));
            endpointCount.get(logLine.getEndpointInfo()).increase();
            ipAddressesSet.add(logLine.getIpAddress());
        }
        return new LogAnalysisReport(ipAddressesSet, new LinkedList<>(ipCountMap.values()), new LinkedList<>(endpointCount.values()));
    }
}
