package com.clrk.lambda.trydynamodb.dao

import com.clrk.lambda.trydynamodb.ContextConfig
import com.clrk.lambda.trydynamodb.dto.IncomingFile
import com.clrk.lambda.trydynamodb.dto.Status
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

import java.time.LocalDateTime

@Stepwise
@ContextConfiguration(classes = ContextConfig)
class IncomingFileDaoDevTest extends Specification {

    @Autowired
    IncomingFileDao sut
    @Shared
    IncomingFile incomingFile

    final static localDate = LocalDateTime.now()

    void setupSpec() {

        incomingFile = IncomingFile.builder()
                .fileName('rerer.txt')
                .createdDate(localDate)
                .finishDate(LocalDateTime.now().plusDays(1))
                .status(Status.FOUND)
                .build()
    }

    def 'should save'() {
        when:
        def save = sut.save(incomingFile)
        then:
        noExceptionThrown()
    }

    def ' should update'() {
        given:
        def updateIncomingFile = incomingFile.toBuilder().status(Status.PARSED).createdDate(localDate).build()

        when:
        def save = sut.update(updateIncomingFile)
        then:
        noExceptionThrown()
    }
}
