package com.clrk.lambda.trydynamodb.dao

import com.clrk.lambda.trydynamodb.ContextConfig
import com.clrk.lambda.trydynamodb.dto.TelephoneRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration(classes = ContextConfig)
class TelephoneBookDaoDevTest extends Specification {
    @Autowired
    TelephoneBookDao sut

    def 'test'() {
        given:
        final telephoneRecord = TelephoneRecord.builder()
                .name("Ivan3")
                .surname("Ivanov")
                .telephone("9119107754")
                .build()
        when:
        def save = sut.save(telephoneRecord)
        then:
        noExceptionThrown()
    }

}

