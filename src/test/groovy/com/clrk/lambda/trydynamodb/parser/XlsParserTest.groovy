package com.clrk.lambda.trydynamodb.parser

import com.clrk.lambda.trydynamodb.dto.TelephoneRecord
import spock.lang.AutoCleanup
import spock.lang.Specification
import spock.lang.Subject

class XlsParserTest extends Specification {
    @Subject
    XlsParser sut
    @AutoCleanup
    InputStream inputStream

    void setup() {
        sut = new XlsParser()
        inputStream = new BufferedInputStream(XlsParserTest.getClassLoader()
                .getResourceAsStream('telephone_book.xls'))
    }

    def 'should parse source file'() {
        given:
        def expectedRecord = TelephoneRecord.builder()
                .name('Vasa')
                .surname('Pupkin')
                .telephone('111-111-111')
                .build()

        when:
        List<TelephoneRecord> result = sut.parse(inputStream, key)
        // TODO implement when
        then:
        assert result.size() == 2
        assert result.contains(expectedRecord)
        // TODO implement then
    }
}
