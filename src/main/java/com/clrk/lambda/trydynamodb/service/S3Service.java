package com.clrk.lambda.trydynamodb.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Slf4j
@Service
public class S3Service {

    private final AmazonS3 amazonS3;

    @Autowired
    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public InputStream getObjectContent(String bucket, String key) {
        log.info("Start downloading {} from bucket {} ",key, bucket);
        S3Object s3Object = amazonS3.getObject(new GetObjectRequest(
                bucket, key));
        final S3ObjectInputStream objectContent = s3Object.getObjectContent();
        log.info("Downloading was successful");
        return objectContent;
    }
}
