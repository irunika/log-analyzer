package com.analyzer.logs.creator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class EndpointInfo {
    private String httpMethod;
    private String url;
}
