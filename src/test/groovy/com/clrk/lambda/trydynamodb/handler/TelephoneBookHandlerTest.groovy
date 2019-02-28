package com.clrk.lambda.trydynamodb.handler

import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.dynamodbv2.document.Table
import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.events.S3Event
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.event.S3EventNotification
import com.amazonaws.services.s3.model.S3Object
import com.clrk.lambda.trydynamodb.ContextConfig
import com.clrk.lambda.trydynamodb.TestContextConfig
import com.clrk.lambda.trydynamodb.dao.TelephoneBookDaoImpl
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.AutoCleanup
import spock.lang.Specification
import spock.lang.Subject

@ContextConfiguration(classes = [ContextConfig, TestContextConfig])

class TelephoneBookHandlerTest extends Specification {
    @Autowired
    AmazonS3Client amazonS3Client
    @Autowired
    DynamoDB dynamoDBMock
    @Subject
    @Autowired
    TelephoneBookHandler sut
    @AutoCleanup
    InputStream telephoneBookInputStream
    @AutoCleanup
    InputStream s3JsonInputStream

    Context contextMock
    Table tableMock

    def setup() {
        contextMock = Mock(Context)
        tableMock = Mock(Table)
    }

    def "test handleRequest"() {
        given:
        def key = 'key'
        def bucket = 'bucket'

        s3JsonInputStream = TelephoneBookHandlerTest.getClassLoader().getResourceAsStream('S3EventExample.json')
        telephoneBookInputStream = new BufferedInputStream(TelephoneBookHandlerTest.getClassLoader()
                .getResourceAsStream('telephone_book.xls'))

        ObjectMapper objectMapper = new ObjectMapper()
        def records = objectMapper.readValue(s3JsonInputStream, new TypeReference<List<S3EventNotification.S3EventNotificationRecord>>() {
        })
        def s3Event = new S3Event(records)
        S3Object s3Object = new S3Object(key: key, bucketName: bucket, objectContent: telephoneBookInputStream)

        when:
        sut.handleRequest(s3Event, contextMock)

        then:
        1 * dynamoDBMock.getTable(TelephoneBookDaoImpl.TELEPHONE_BOOK_TABLE) >> tableMock
        4 * tableMock.putItem({
            it.getFileName != null
            it.surname != null
            it.telephone != null
        })
        1 * amazonS3Client.getObject({
            it.bucketName == bucket
            it.key == key
        }) >> s3Object

    }
}
