package com.clrk.lambda.trydynamodb.dao

import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.dynamodbv2.document.Item
import com.amazonaws.services.dynamodbv2.document.PrimaryKey
import com.amazonaws.services.dynamodbv2.document.Table
import com.clrk.lambda.trydynamodb.TestContextConfig
import com.clrk.lambda.trydynamodb.dto.TelephoneRecord
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import spock.lang.Subject

@ContextConfiguration(classes = TestContextConfig)
class TelephoneBookDaoTest extends Specification {
    @Subject
    TelephoneBookDao sut

    DynamoDB dynamoDBMock
    Table tableMock

    def setup() {
        dynamoDBMock = Mock(DynamoDB)
        sut = new TelephoneBookDaoImpl(dynamoDBMock)
        tableMock = Mock(Table)
    }

    def 'integration test for dynamo bd'() {
        given:
        def name = 'Ivan'
        def surName = 'Ivanov'
        def telephone = '9119107772'

        final telephoneRecord = TelephoneRecord.builder()
                .name(name)
                .surname(surName)
                .telephone(telephone)
                .build()

        and:
        final primaryKey = new PrimaryKey('fileName', name, 'surname', surName)
        Item expectedItem = new Item()
                .withPrimaryKey(primaryKey)
                .withString('telephone', telephone)

        when:
        sut.save(telephoneRecord, table)

        then:
        1 * dynamoDBMock.getTable(_) >> tableMock

        1 * tableMock.putItem(expectedItem)

    }
}

