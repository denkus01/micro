package com.clrk.lambda.trydynamodb;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.clrk.lambda.trydynamodb.handler.TelephoneBookHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class GeneralHandler implements RequestHandler<S3Event, String> {

    private static final ApplicationContext CONTEXT = new AnnotationConfigApplicationContext(ContextConfig.class);

    @Override
    public String handleRequest(S3Event input, Context context) {
        final TelephoneBookHandler telephoneBookHandler = CONTEXT.getBean(TelephoneBookHandler.class);
    return telephoneBookHandler.handleRequest(input,context);
    }
}
