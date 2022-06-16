package com.analyzer.logs.creator;

import com.analyzer.logs.exception.LogLineCreationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class LogLineCreatorImplTest {

    private LogLineCreatorImpl logLineCreator;

    @BeforeEach
    public void setup() {
        logLineCreator = new LogLineCreatorImpl();
    }

    @Test
    public void findIpAddress_shouldReturnIpAddress() throws LogLineCreationException {
        String logLine = "177.71.128.21 - - [10/Jul/2018:22:21:28 +0200] \"GET /intranet-analytics/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (X11; U; Linux x86_64; fr-FR) AppleWebKit/534.7 (KHTML, like Gecko) Epiphany/2.30.6 Safari/534.7";
        String ipAddress = logLineCreator.findIpAddress(logLine);
        assertEquals("177.71.128.21", ipAddress);
    }

    @Test
    public void findIpAddress_shouldThrowException() throws LogLineCreationException {
        String logLineWithoutIp = "- - [10/Jul/2018:22:21:28 +0200] \"GET /intranet-analytics/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (X11; U; Linux x86_64; fr-FR) AppleWebKit/534.7 (KHTML, like Gecko) Epiphany/2.30.6 Safari/534.7";
        Exception exception = assertThrows(LogLineCreationException.class, () -> logLineCreator.findIpAddress(logLineWithoutIp));
        assertEquals("Cannot find a IP address in log: " + logLineWithoutIp, exception.getMessage());
    }

    @Test
    public void findEndpointInfo_shouldReturnEndpointInfo() throws LogLineCreationException {
        String logLine = "177.71.128.21 - - [10/Jul/2018:22:21:28 +0200] \"GET /intranet-analytics/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (X11; U; Linux x86_64; fr-FR) AppleWebKit/534.7 (KHTML, like Gecko) Epiphany/2.30.6 Safari/534.7";
        EndpointInfo endpointInfo = logLineCreator.findEndpointInfo(logLine);
        assertEquals(new EndpointInfo("GET", "/intranet-analytics/"), endpointInfo);
    }

    @Test
    public void findEndpointInfo_shouldThrowEndpointNotFoundException() {
        String logLineWithoutEndpointInfo = "177.71.128.21 - - [10/Jul/2018:22:21:28 +0200] 200 3574 \"-\" \"Mozilla/5.0 (X11; U; Linux x86_64; fr-FR) AppleWebKit/534.7 (KHTML, like Gecko) Epiphany/2.30.6 Safari/534.7";
        Exception exception = assertThrows(LogLineCreationException.class, () -> logLineCreator.findEndpointInfo(logLineWithoutEndpointInfo));
        assertEquals("Cannot find endpoint details in log: " + logLineWithoutEndpointInfo, exception.getMessage());
    }

    @Test
    public void findEndpointInfo_shouldThrowInfoMissingException() {
        String logLineWithoutSomeInfo = "177.71.128.21 - - [10/Jul/2018:22:21:28 +0200] \"GET HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (X11; U; Linux x86_64; fr-FR) AppleWebKit/534.7 (KHTML, like Gecko) Epiphany/2.30.6 Safari/534.7";
        Exception exception = assertThrows(LogLineCreationException.class, () -> logLineCreator.findEndpointInfo(logLineWithoutSomeInfo));
        assertEquals("HTTP method or URL is missing in the endpoint info in the log: " + logLineWithoutSomeInfo, exception.getMessage());
    }

    @Test
    public void httpStatusCode_shouldReturnHttpStatusCode() throws LogLineCreationException {
        String logLine = "177.71.128.21 - - [10/Jul/2018:22:21:28 +0200] \"GET /intranet-analytics/ HTTP/1.1\" 200 3574 \"-\" \"Mozilla/5.0 (X11; U; Linux x86_64; fr-FR) AppleWebKit/534.7 (KHTML, like Gecko) Epiphany/2.30.6 Safari/534.7";
        int statusCode = logLineCreator.httpStatusCode(logLine);
        assertEquals(200, statusCode);
    }

    @Test
    public void httpStatusCode_shouldThrowStatusCodeNotFoundException() {
        String logLineWithInvalidStatusCode = "177.71.128.21 - - [10/Jul/2018:22:21:28 +0200] \"GET /intranet-analytics/ HTTP/1.1\" STS 3574 \"-\" \"Mozilla/5.0 (X11; U; Linux x86_64; fr-FR) AppleWebKit/534.7 (KHTML, like Gecko) Epiphany/2.30.6 Safari/534.7";
        Exception exception = assertThrows(LogLineCreationException.class, () -> logLineCreator.httpStatusCode(logLineWithInvalidStatusCode));
        assertEquals("Cannot find the HTTP status code in the log: " + logLineWithInvalidStatusCode, exception.getMessage());
    }
}
