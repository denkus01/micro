package com.clrk.lambda.trydynamodb.parser;

import lombok.Getter;

public enum ColumnOrder {
    NAME(0),
    SURNAME(1),
    TELEPHONE(2);

    @Getter
    private final int column;

    ColumnOrder(int column) {
        this.column = column;
    }

}
