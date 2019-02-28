package com.clrk.lambda.trydynamodb.parser;

public class ParseCellValueException extends RuntimeException {
    public ParseCellValueException(String message, Exception e) {
        super(message,e);
    }
    public ParseCellValueException(String message){
        super(message);
    }
}
