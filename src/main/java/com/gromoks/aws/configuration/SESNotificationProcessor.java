package com.gromoks.aws.configuration;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class SESNotificationProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        System.out.println("Exchange Message" + exchange);
    }
}
