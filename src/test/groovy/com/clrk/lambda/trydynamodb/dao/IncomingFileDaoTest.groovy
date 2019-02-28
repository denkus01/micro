package com.clrk.lambda.trydynamodb.dao

import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.dynamodbv2.document.Item
import com.amazonaws.services.dynamodbv2.document.PrimaryKey
import com.amazonaws.services.dynamodbv2.document.Table
import com.clrk.lambda.trydynamodb.dto.IncomingFile
import com.clrk.lambda.trydynamodb.dto.Status
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDateTime


class IncomingFileDaoTest extends Specification {
    @Subject
    IncomingFileDao sut

    DynamoDB dynamoDBMock
    Table tableMock

    def setup() {
        dynamoDBMock = Mock(DynamoDB)
        sut = new IncomingFileDaoImpl(dynamoDBMock)
        tableMock = Mock(Table)
    }

    def 'should save in dynamo database'() {
        given:
        def fileName = 'first'
        def date = LocalDateTime.now()
        def finishDate = LocalDateTime.now().plusDays(1)
        def status = Status.PARSED

        final incomingFile = IncomingFile.builder()
                .fileName(fileName)
                .createdDate(date)
                .finishDate(finishDate)
                .status(status)
                .build()

        and:
        final primaryKey = new PrimaryKey('fileName', fileName, 'createdDate', date)
        Item expectedItem = new Item()
                .withPrimaryKey(primaryKey)
                .withString("finish_date", finishDate.toString())
                .withString('status', status.toString())

        when:
        sut.save(incomingFile)
        sut.update(incomingFile)

        then:
        1 * dynamoDBMock.getTable(_) >> tableMock

        1 * tableMock.putItem(expectedItem)
    }

    def 'should update in dynamo database'() {
        given:
        def fileName = 'first'
        def date = LocalDateTime.now()
        def finishDate = LocalDateTime.now()
        def status = Status.PARSED

        final incomingFile = IncomingFile.builder()
                .fileName(fileName)
                .createdDate(date)
                .finishDate(finishDate)
                .status(status)
                .build()

        and:
        final primaryKey = new PrimaryKey('fileName', fileName, 'createdDate', date)
        Item expectedItem = new Item()
                .withPrimaryKey(primaryKey)
                .withString("finish_date", finishDate.toString())
                .withString('status', status.toString())

        when:

        sut.update(incomingFile)

        then:
        1 * dynamoDBMock.getTable(_) >> tableMock

        1 * tableMock.updateItem(expectedItem)
    }

}
