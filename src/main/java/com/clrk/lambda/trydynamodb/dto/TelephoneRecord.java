package com.clrk.lambda.trydynamodb.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TelephoneRecord {
    private final String name;
    private final String surname;
    private final String telephone;
}
