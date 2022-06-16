package com.analyzer.logs;

import com.analyzer.logs.creator.EndpointInfo;
import com.analyzer.logs.creator.LogLine;
import com.analyzer.logs.creator.LogLineCreator;
import com.analyzer.logs.creator.LogLineCreatorImpl;
import com.analyzer.logs.exception.LogLineCreationException;

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
public class LogAnalyzer {
    private final LogLineCreator logLineCreator;

    public LogAnalyzer() {
        this.logLineCreator = new LogLineCreatorImpl();
    }

    public LogAnalysisReport Analyze(BufferedReader reader) throws IOException, LogLineCreationException {
        Map<String, ObjectCounter<String>> ipCountMap = new HashMap<>();
        Map<EndpointInfo, ObjectCounter<EndpointInfo>> endpointCount = new HashMap<>();
        Set<String> ipAddressesSet = new HashSet<>();

        String currentLine;
        while ((currentLine = reader.readLine()) != null) {
            LogLine logLine = logLineCreator.create(currentLine);
            ipCountMap.putIfAbsent(logLine.getIpAddress(), new ObjectCounter<>(logLine.getIpAddress()));
            ipCountMap.get(logLine.getIpAddress()).increase();
            endpointCount.putIfAbsent(logLine.getEndpointInfo(), new ObjectCounter<>(logLine.getEndpointInfo()));
            endpointCount.get(logLine.getEndpointInfo()).increase();
            ipAddressesSet.add(logLine.getIpAddress());
        }
        return new LogAnalysisReport(ipAddressesSet, new LinkedList<>(ipCountMap.values()), new LinkedList<>(endpointCount.values()));
    }
}
