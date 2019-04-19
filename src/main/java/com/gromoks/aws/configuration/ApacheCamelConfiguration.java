package com.gromoks.aws.configuration;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApacheCamelConfiguration extends CamelConfiguration {
    @Value("${aws-ses.credentials.sqsArn}")
    private String sqsArn;

    @Bean
    public RouteBuilder emailConfirmationRouter() {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("aws-sqs://" + sqsArn + "?amazonSQSClient=#sqsClient&maxMessagesPerPoll=" + 10)
                        .process(new SESNotificationProcessor())
                        .log("Message")
                        .to("log:Message");
            }
        };
    }
}
