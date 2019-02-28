package com.clrk.lambda.trydynamodb.dao;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.clrk.lambda.trydynamodb.dto.TelephoneRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class TelephoneBookDaoImpl implements TelephoneBookDao {
    private static final String TELEPHONE_BOOK_TABLE = "telephone_book";
    private final DynamoDB dynamoDb;

    @Autowired
    public TelephoneBookDaoImpl(DynamoDB dynamoDb) {
        this.dynamoDb = dynamoDb;
    }

    @Override
    public void save(List<TelephoneRecord> telephoneRecords) {
        Table table = dynamoDb.getTable(TELEPHONE_BOOK_TABLE);
        for (TelephoneRecord telephoneRecord : telephoneRecords) {
            save(telephoneRecord, table);
        }
    }

    @Override
    public void save(TelephoneRecord telephoneRecord) {
        save(telephoneRecord, null);
    }

    private void save(TelephoneRecord telephoneRecord, Table table) {
        log.debug("saving new telephone record: {}", telephoneRecord);
        if (table == null) {
            table = dynamoDb.getTable(TELEPHONE_BOOK_TABLE);
        }
        PrimaryKey primaryKey = new PrimaryKey("name", telephoneRecord.getName(), "surname", telephoneRecord.getSurname());

        Item item = new Item()
                .withPrimaryKey(primaryKey)
                .withString("telephone", telephoneRecord.getTelephone());
        PutItemOutcome putItemOutcome = table.putItem(item);
        //TODO add checking status of response

        log.debug("result is :{}", putItemOutcome);
        log.info(telephoneRecord.getName());

    }


}