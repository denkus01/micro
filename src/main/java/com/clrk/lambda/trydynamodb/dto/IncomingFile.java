package com.clrk.lambda.trydynamodb.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
public class IncomingFile {
    private String fileName;
    private LocalDateTime createdDate;
    private LocalDateTime finishDate;
    private Status status;
}
