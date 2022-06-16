package com.analyzer.logs;

import com.analyzer.logs.creator.EndpointInfo;
import com.analyzer.logs.exception.LogLineCreationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LogAnalyzerTest {

    private LogAnalyzer logAnalyzer;

    @BeforeEach
    public void setup() {
        this.logAnalyzer = new LogAnalyzer();
    }

    @Test
    public void analyze_shouldProvideAnalyzedReport() {
        String path = "src/test/resources/logfile";
        File file = new File(path);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            // Generate expected report
            Map<String, ObjectCounter<String>> ipCounter = new HashMap<>();
            ipCounter.put("168.41.191.40", new ObjectCounter<>("168.41.191.40", 4));
            ipCounter.put("168.41.191.34", new ObjectCounter<>("168.41.191.34", 5));
            ipCounter.put("177.71.128.21", new ObjectCounter<>("177.71.128.21", 2));
            ipCounter.put("50.112.00.11", new ObjectCounter<>("50.112.00.11", 1));

            List<ObjectCounter<EndpointInfo>> endpointCounter = new ArrayList<>();
            endpointCounter.add(new ObjectCounter<>(new EndpointInfo("GET", "/blog/category/community/"), 5));
            endpointCounter.add(new ObjectCounter<>(new EndpointInfo("GET", "/faq/how-to/"), 3));
            endpointCounter.add(new ObjectCounter<>(new EndpointInfo("GET", "/faq/"), 2));
            endpointCounter.add(new ObjectCounter<>(new EndpointInfo("GET", "/asset.js"), 1));
            endpointCounter.add(new ObjectCounter<>(new EndpointInfo("GET", "/docs/manage-websites/"), 1));
            Map<EndpointInfo, ObjectCounter<EndpointInfo>> endpointInfoMap = endpointCounter.stream()
                    .collect(Collectors.toMap(ObjectCounter::getObject, endpointObj -> endpointObj));

            Set<String> ipSet = new HashSet<>();
            ipSet.add("168.41.191.40");
            ipSet.add("168.41.191.34");
            ipSet.add("177.71.128.21");
            ipSet.add("50.112.00.11");

            // Actual report
            LogAnalysisReport report = logAnalyzer.Analyze(br);

            // Asserting ip address set.
            assertEquals(ipSet, report.getIpAddresses());

            // asserting endpoint info list
            report.getEndpointCount().forEach(epc -> {
                assertEquals(endpointInfoMap.get(epc.getObject()), epc);
            });

            // Asserting ip count list
            report.getIpCount().forEach(ipc -> {
                assertEquals(ipCounter.get(ipc.getObject()), ipc);
            });
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (LogLineCreationException e) {
            throw new RuntimeException(e);
        }
    }
}
