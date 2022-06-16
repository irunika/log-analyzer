package com.logs.analyzer;

import com.logs.analyzer.creator.EndpointInfo;
import com.logs.analyzer.exception.LogAnalyzerException;
import com.logs.analyzer.exception.LogLineCreatorException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws LogAnalyzerException {
        if (args == null || args.length == 0) {
            throw new LogAnalyzerException("Cannot find the file path to the log file.");
        }
        String path = args[0];
        File file = new File(path);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            LogAnalyser logAnalyzer = new LogAnalyser();
            LogAnalysisReport report = logAnalyzer.analyse(br);

            System.out.println("-------- IP Addresses --------");
            String ips = String.join(", ", report.getIpAddresses());
            System.out.println(ips);

            System.out.println("\n-------- 3 Most Visited URLS --------");
            report.getEndpointCount().sort((a, b) -> Integer.compare(b.getCount(), a.getCount()));
            for (int i = 0; i < 3; i++) {
                Counter<EndpointInfo> endpointInfoObjectCounter = report.getEndpointCount().get(i);
                System.out.println("URL: " + endpointInfoObjectCounter.getValue().getHttpMethod() + " " + endpointInfoObjectCounter.getValue().getUrl() + ", count: " + endpointInfoObjectCounter.getCount());
            }

            System.out.println("\n-------- 3 Most Active IP addresses --------");
            report.getIpCount().sort((a, b) -> Integer.compare(b.getCount(), a.getCount()));
            for (int i = 0; i < 3; i++) {
                Counter<String> ipCounter = report.getIpCount().get(i);
                System.out.println("IP: " + ipCounter.getValue() + ", count: " + ipCounter.getCount());
            }

        } catch (IOException | LogLineCreatorException e) {
            throw new RuntimeException(e);
        }
    }
}
