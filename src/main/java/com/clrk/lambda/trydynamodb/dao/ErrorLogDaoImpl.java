package com.clrk.lambda.trydynamodb.dao;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.clrk.lambda.trydynamodb.dto.ErrorLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class ErrorLogDaoImpl implements ErrorLogDao {

    private static final String ERROR_LOG = "error_log";
    private static final String STATUS_OF_DOCUMENT = "statusOfDocument";
    private static final String ERROR_DESCRIPTION = "errorDescription";
    public static final String KEY_NAME = "name";
    private final DynamoDB dynamoDb;

    public ErrorLogDaoImpl(DynamoDB dynamoDB) {
        this.dynamoDb = dynamoDB;
    }

    @Override
    public void save(ErrorLog errorLog) {
        Table table = dynamoDb.getTable(ERROR_LOG);
        log.debug("saving new error in log: {}", errorLog);
        PrimaryKey primaryKey = new PrimaryKey(STATUS_OF_DOCUMENT, errorLog.getStatusOfDocument().toString());

        Item item = new Item()
                .withPrimaryKey(primaryKey)
                .withString(KEY_NAME, errorLog.getName())
                .withString(ERROR_DESCRIPTION, errorLog.getErrorDescription());
        PutItemOutcome putItemOutcome = table.putItem(item);

        log.debug("result is :{}", putItemOutcome);
        log.info(errorLog.getErrorDescription());
    }
}
