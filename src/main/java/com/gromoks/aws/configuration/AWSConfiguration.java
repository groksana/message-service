package com.gromoks.aws.configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSConfiguration {
    @Value("${aws-ses.credentials.awsAccessKey}")
    private String awsAccessKey;

    @Value("${aws-ses.credentials.awsAccessSecret}")
    private String awsSecretKey;

    @Bean
    public AmazonSimpleEmailService amazonSimpleEmailService() {
        return AmazonSimpleEmailServiceClient.builder()
                .withRegion(Regions.US_EAST_1)
                .withCredentials(getCredentialsProvider())
                .build();
    }

    @Bean
    public AWSStaticCredentialsProvider getCredentialsProvider() {
        return new AWSStaticCredentialsProvider(
                new BasicAWSCredentials(awsAccessKey, awsSecretKey));
    }

    @Bean(name = "sqsClient")
    public AmazonSQS sqsClient() {
        return AmazonSQSClient.builder()
                .withRegion(Regions.US_EAST_1)
                .withCredentials(getCredentialsProvider())
                .build();
    }

    @Bean
    public AmazonS3 s3client() {
        return AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(Regions.US_EAST_1.getName()))
                .withCredentials(getCredentialsProvider())
                .build();
    }
}
