package com.clrk.lambda.trydynamodb.dao;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.clrk.lambda.trydynamodb.dto.IncomingFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import static org.springframework.util.ObjectUtils.*;

@Repository
@Slf4j
public class IncomingFileDaoImpl implements IncomingFileDao {
    private static final String INCOMING_FILE_TABLES = "incoming_file";
    private static final String FIRST_KEY = "name";
    private static final String SECOND_KEY = "createdDate";
    private static final String STATUS_OF_DOCUMENT = "statusOfDocument";
    private static final String FINISH_DATE = "finishDate";
    private final DynamoDB dynamoDb;

    public IncomingFileDaoImpl(DynamoDB dynamoDB) {
        this.dynamoDb = dynamoDB;
    }

    @Override
    public void save(IncomingFile incomingFile) {
        Table table = dynamoDb.getTable(INCOMING_FILE_TABLES);
        log.debug("saving new files record: {}", incomingFile);
        PrimaryKey primaryKey = new PrimaryKey(FIRST_KEY, incomingFile.getFileName(),
                SECOND_KEY, incomingFile.getCreatedDate().toString());

        Item item = new Item()
                .withPrimaryKey(primaryKey)
                .withString(STATUS_OF_DOCUMENT, incomingFile.getStatus().toString())
                .withString(FINISH_DATE, nullSafeToString(incomingFile.getFinishDate()));
        PutItemOutcome putItemOutcome = table.putItem(item);

        log.debug("result is :{}", putItemOutcome);
        log.info(incomingFile.getFileName());
    }

    @Override
    public void update(IncomingFile incomingFile) {
        Table tableForUpdate = dynamoDb.getTable(INCOMING_FILE_TABLES);
        log.debug("update files  in record: {}", incomingFile);
        UpdateItemSpec updateItemSpec = new UpdateItemSpec().withPrimaryKey(FIRST_KEY, incomingFile.getFileName(),
                SECOND_KEY, incomingFile.getCreatedDate().toString())
                .withUpdateExpression("set finishDate = :finishDate, statusOfDocument = :updateStatus")
                .withValueMap(new ValueMap().withString(":finishDate", String.valueOf(incomingFile.getFinishDate()))
                        .withString(":updateStatus", incomingFile.getStatus().toString()))
                .withReturnValues(ReturnValue.UPDATED_NEW);
        UpdateItemOutcome outcome = tableForUpdate.updateItem(updateItemSpec);

        log.debug(" update result is :{}", outcome);
        log.info(incomingFile.getFileName());
    }
}
