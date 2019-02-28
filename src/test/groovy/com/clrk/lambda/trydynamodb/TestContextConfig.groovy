package com.clrk.lambda.trydynamodb

import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.s3.AmazonS3Client
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import spock.mock.DetachedMockFactory

@Configuration
class TestContextConfig {
    def factory = new DetachedMockFactory()

    @Bean
    DynamoDB dynamoDB() {
       return factory.Mock(DynamoDB)
    }

    @Bean
    AmazonS3Client amazonS3Client() {
        return factory.Mock(AmazonS3Client)
    }

}
