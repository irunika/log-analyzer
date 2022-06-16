package com.analyzer.logs.creator;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EndpointInfo {
    private String httpMethod;
    private String url;
}
