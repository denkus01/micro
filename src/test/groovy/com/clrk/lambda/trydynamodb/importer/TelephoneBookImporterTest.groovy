package com.clrk.lambda.trydynamodb.importer

import com.clrk.lambda.trydynamodb.dao.IncomingFileDao
import com.clrk.lambda.trydynamodb.dao.TelephoneBookDao
import com.clrk.lambda.trydynamodb.dto.Status
import com.clrk.lambda.trydynamodb.dto.TelephoneRecord
import com.clrk.lambda.trydynamodb.parser.ParseCellValueException
import com.clrk.lambda.trydynamodb.parser.XlsParser
import com.clrk.lambda.trydynamodb.service.S3Service
import spock.lang.Specification
import spock.lang.Subject

class TelephoneBookImporterTest extends Specification {
    @Subject
    TelephoneBookImporter sut

    S3Service s3ServiceMock
    XlsParser xlsParserMock
    TelephoneBookDao telephoneBookDaoMock
    List<TelephoneRecord> telephoneRecord
    IncomingFileDao incomingFileDaoMock

    void setup() {
        s3ServiceMock = Mock(S3Service)
        xlsParserMock = Mock(XlsParser)
        telephoneBookDaoMock = Mock(TelephoneBookDao)
        incomingFileDaoMock = Mock(IncomingFileDao)
        sut = new TelephoneBookImporter(s3ServiceMock, xlsParserMock, telephoneBookDaoMock, incomingFileDaoMock)
    }

    def 'should import telephone book'() {
        given:
        final bucket = 's3bucket'
        final key = '1key'
        final inputStreamMock = Mock(InputStream)

        when:
        sut.importTelephoneBook(bucket, key)

        then:
        1 * s3ServiceMock.getObjectContent(bucket, key) >> inputStreamMock
        1 * xlsParserMock.parse(inputStreamMock, key) >> telephoneRecord
        1 * telephoneBookDaoMock.save(telephoneRecord)
        1 * incomingFileDaoMock.save({
            it.fileName == key
            it.status == Status.FOUND
            it.createdDate != null
            it.finishDate == null
        }
        )
        1 * incomingFileDaoMock.update({
            it.fileName == key
            it.status == Status.PARSED
            it.createdDate != null
            it.finishDate != null
        })
    }

    def "should not import telephone book"() {
        given:
        final bucket = 's3bucket'
        final key = '1key'
        final inputStreamMock = Mock(InputStream)

        when:
        sut.importTelephoneBook(bucket, key)

        then:

        1 * s3ServiceMock.getObjectContent(bucket, key) >> inputStreamMock
        1 * xlsParserMock.parse(inputStreamMock, key) >> {throw new ParseCellValueException('Error')}
        1 * incomingFileDaoMock.update({
            it.fileName == key
            it.status == Status.ERROR_PARSING
            it.createdDate != null
            it.finishDate != null
        })
        0 * telephoneBookDaoMock.save(_)
    }
}
