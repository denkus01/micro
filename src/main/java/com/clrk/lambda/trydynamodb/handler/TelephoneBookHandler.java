package com.clrk.lambda.trydynamodb.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.clrk.lambda.trydynamodb.importer.TelephoneBookImporter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TelephoneBookHandler implements RequestHandler<S3Event, String> {

    private final TelephoneBookImporter telephoneBookImporter;

    @Autowired
    public TelephoneBookHandler(TelephoneBookImporter telephoneBookImporter) {
        this.telephoneBookImporter = telephoneBookImporter;
    }

    @Override
    public String handleRequest(S3Event input, Context context) {
        log.debug("received : {}", input);

        S3EventNotification.S3EventNotificationRecord record = input.getRecords().get(0);
        String srcBucket = record.getS3().getBucket().getName();
        String srcKey = record.getS3().getObject().getKey();

        telephoneBookImporter.importTelephoneBook(srcBucket, srcKey);

        return "Lambda has completed correct";

    }
}