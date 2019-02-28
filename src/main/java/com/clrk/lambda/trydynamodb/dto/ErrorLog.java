package com.clrk.lambda.trydynamodb.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ErrorLog {
    private String name;
    private Status statusOfDocument;
    private String errorDescription;
}
