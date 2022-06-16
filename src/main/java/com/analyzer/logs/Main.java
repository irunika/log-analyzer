package com.analyzer.logs;

import com.analyzer.logs.creator.EndpointInfo;
import com.analyzer.logs.exception.LogAnalyzerException;
import com.analyzer.logs.exception.LogLineCreationException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws LogAnalyzerException {
        if (args == null || args.length == 0) {
            throw new LogAnalyzerException("Cannot find the file path to the log file.");
        }
        String path = args[0];
        File file = new File(path);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            LogAnalyzer logAnalyzer = new LogAnalyzer();
            LogAnalysisReport report = logAnalyzer.Analyze(br);

            System.out.println("-------- IP Addresses --------");
            String ips = String.join(", ", report.getIpAddresses());
            System.out.println(ips);

            System.out.println("\n-------- 3 Most Visited URLS --------");
            report.getEndpointCount().sort((a, b) -> Integer.compare(b.getCount(), a.getCount()));
            for (int i = 0; i < 3; i++) {
                ObjectCounter<EndpointInfo> endpointInfoObjectCounter = report.getEndpointCount().get(i);
                System.out.println("URL: " + endpointInfoObjectCounter.getObject().getHttpMethod() + " " + endpointInfoObjectCounter.getObject().getUrl() + ", count: " + endpointInfoObjectCounter.getCount());
            }

            System.out.println("\n-------- 3 Most Active IP addresses --------");
            report.getIpCount().sort((a, b) -> Integer.compare(b.getCount(), a.getCount()));
            for (int i = 0; i < 3; i++) {
                ObjectCounter<String> ipCounter = report.getIpCount().get(i);
                System.out.println("IP: " + ipCounter.getObject() + ", count: " + ipCounter.getCount());
            }


        } catch (IOException | LogLineCreationException e) {
            throw new RuntimeException(e);
        }
    }
}
