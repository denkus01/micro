package com.clrk.lambda.trydynamodb.service

import com.clrk.lambda.trydynamodb.TestContextConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification


@ContextConfiguration(classes = TestContextConfig)
class S3ServiceDevTest extends Specification {
    @Autowired
    S3Service s3Service

    def 'test'() {
        given:
        String bucket = 'lambda-test111'
        String key = 'telephone_book.xls'

        when:
        InputStream inputStream = s3Service.getObjectContent(bucket, key)
        then:

        assert inputStream != null
    }
}
