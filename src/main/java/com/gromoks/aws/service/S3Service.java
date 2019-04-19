package com.gromoks.aws.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class S3Service {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final AmazonS3 s3client;

    @Value("${aws-ses.credentials.awsS3BucketName}")
    private String awsS3BucketName;

    public S3Service(AmazonS3 s3client) {
        this.s3client = s3client;
    }

    public InputStream downloadFile(String filename) {
        try {
            S3Object s3object = s3client.getObject(new GetObjectRequest(awsS3BucketName, filename));
            return s3object.getObjectContent();
        } catch (Exception e) {
            logger.info("Requested file '{}' is not found in bucket: {}", filename, awsS3BucketName);
            throw new RuntimeException(String.format("Requested file '%s' is not found in bucket: %s",
                    filename, awsS3BucketName));
        }
    }
}