package com.logs.analyzer;

import com.logs.analyzer.creator.EndpointInfo;
import com.logs.analyzer.exception.LogLineCreatorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LogAnalyzerTest {

    private LogAnalyser logAnalyzer;

    @BeforeEach
    public void setup() {
        this.logAnalyzer = new LogAnalyser();
    }

    @Test
    public void analyze_shouldProvideAnalyzedReport() {
        String path = "src/test/resources/test-sample.log";
        File file = new File(path);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            // Generate expected report
            Map<String, Counter<String>> ipCounter = new HashMap<>();
            ipCounter.put("168.41.191.40", new Counter<>("168.41.191.40", 4));
            ipCounter.put("168.41.191.34", new Counter<>("168.41.191.34", 5));
            ipCounter.put("177.71.128.21", new Counter<>("177.71.128.21", 2));
            ipCounter.put("50.112.00.11", new Counter<>("50.112.00.11", 1));

            List<Counter<EndpointInfo>> endpointCounter = new ArrayList<>();
            endpointCounter.add(new Counter<>(new EndpointInfo("GET", "/blog/category/community/"), 5));
            endpointCounter.add(new Counter<>(new EndpointInfo("GET", "/faq/how-to/"), 3));
            endpointCounter.add(new Counter<>(new EndpointInfo("GET", "/faq/"), 2));
            endpointCounter.add(new Counter<>(new EndpointInfo("GET", "/asset.js"), 1));
            endpointCounter.add(new Counter<>(new EndpointInfo("GET", "/docs/manage-websites/"), 1));
            Map<EndpointInfo, Counter<EndpointInfo>> endpointInfoMap = endpointCounter.stream()
                    .collect(Collectors.toMap(Counter::getValue, endpointObj -> endpointObj));

            Set<String> ipSet = new HashSet<>();
            ipSet.add("168.41.191.40");
            ipSet.add("168.41.191.34");
            ipSet.add("177.71.128.21");
            ipSet.add("50.112.00.11");

            // Actual report
            LogAnalysisReport report = logAnalyzer.analyse(br);

            // Asserting ip address set.
            assertEquals(ipSet, report.getIpAddresses());

            // asserting endpoint info list
            report.getEndpointCount().forEach(epc -> {
                assertEquals(endpointInfoMap.get(epc.getValue()), epc);
            });

            // Asserting ip count list
            report.getIpCount().forEach(ipc -> {
                assertEquals(ipCounter.get(ipc.getValue()), ipc);
            });
        } catch (IOException | LogLineCreatorException e) {
            throw new RuntimeException(e);
        }
    }
}
