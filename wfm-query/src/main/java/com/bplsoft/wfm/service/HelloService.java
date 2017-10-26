package com.bplsoft.wfm.service;


import com.bplsoft.common.property.Property;
import com.bplsoft.wfm.repository.HelloQueryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Random;

public class HelloService {

    private static final Logger logger = LoggerFactory.getLogger(HelloService.class);

    @Inject
    HelloQueryRepository queryRepository;

    @Inject
    @Property("app.name")
    private String applicationName;

    private double random = new Random().nextDouble();

    public String sayHi(String name) {

        logger.debug("queryRepository findAll: " + queryRepository.findAll());
        logger.debug("repository findAll: " + queryRepository.findAll());
        logger.debug("queryRepository count: " + queryRepository.count());
        logger.debug("repository count: " + queryRepository.count());

        return String.format("App name '%1s' Hi %2s SERVICE: %3s", applicationName, name, random);
    }

}
