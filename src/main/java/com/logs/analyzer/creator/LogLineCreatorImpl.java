package com.logs.analyzer.creator;

import com.logs.analyzer.exception.LogLineCreatorException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of the {@link LogLineCreator}.
 */
public class LogLineCreatorImpl implements LogLineCreator{
    private static final Pattern ipAddressPattern = Pattern.compile("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
    private static final Pattern urlLinePattern = Pattern.compile("(?<=]\\s\").+HTTP/\\d\\.\\d");
    private static final Pattern statusCodePattern = Pattern.compile("(?<=HTTP/\\d.\\d\"\\s)\\d{3}");

    @Override
    public LogLine create(String logLineStr) throws LogLineCreatorException {
        String ipAddress = findIpAddress(logLineStr);
        EndpointInfo endpointInfo = findEndpointInfo(logLineStr);
        int httpStatusCode = findHttpStatusCode(logLineStr);
        return new LogLine(ipAddress, endpointInfo, httpStatusCode);
    }

    public String findIpAddress(String logLineStr) throws LogLineCreatorException {
        Matcher m = ipAddressPattern.matcher(logLineStr);
        if (!m.find()) {
            throw new LogLineCreatorException("Cannot find a IP address in log: " + logLineStr);
        }
        return m.group(0);
    }

    public EndpointInfo findEndpointInfo(String logLineStr) throws LogLineCreatorException {
        Matcher m = urlLinePattern.matcher(logLineStr);
        if (!m.find()) {
            throw new LogLineCreatorException("Cannot find endpoint details in log: " + logLineStr);
        }
        String endpointStr = m.group(0);
        String[] endpointSplit = endpointStr.split("\\s");

        if (endpointSplit.length != 3) {
            throw new LogLineCreatorException("HTTP method or URL is missing in the endpoint info in the log: " + logLineStr);
        }
        return new EndpointInfo(endpointSplit[0], endpointSplit[1]);
    }

    public int findHttpStatusCode(String logLineStr) throws LogLineCreatorException {
        Matcher m = statusCodePattern.matcher(logLineStr);
        if (!m.find()) {
            throw new LogLineCreatorException("Cannot find the HTTP status code in the log: " + logLineStr);
        }
        String httpStatusCode = m.group(0);
        return Integer.parseInt(httpStatusCode);
    }
}
