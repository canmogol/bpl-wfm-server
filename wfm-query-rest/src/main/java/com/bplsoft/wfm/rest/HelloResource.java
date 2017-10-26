package com.bplsoft.wfm.rest;

import com.bplsoft.common.interceptor.MethodLoggingInterceptor;
import com.bplsoft.wfm.rest.dto.HelloData;
import com.bplsoft.wfm.rest.dto.HelloAnotherDTO;
import com.bplsoft.wfm.rest.dto.HelloDTO;
import com.bplsoft.wfm.rest.dto.HelloValue;
import com.bplsoft.wfm.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.Random;


@Slf4j
@Path("/hi")
@Interceptors({MethodLoggingInterceptor.class})
public class HelloResource {

    @Inject
    HelloService service;

    double random = new Random().nextDouble();

    @GET
    @Produces("text/plain")
    public String sayHi(@QueryParam("name") String name) {

        // all args constructor
        val helloDTO1 = new HelloDTO(1L, 1, "first");

        // no args constructor
        val helloDTO2 = new HelloDTO();

        // setters
        helloDTO2.setId(1L);
        helloDTO2.setName("second");
        helloDTO2.setAge(22);

        // getters
        // this logger is the SLF4J interface, should be using logback implementation
        log.info("first name " + helloDTO1.getName());
        log.info("second name " + helloDTO1.getName());

        // equals and hashcode generated, according to annotation
        log.info("are equal? " + (helloDTO1.equals(helloDTO2)));
        log.info("first to string " + helloDTO1.toString());
        log.info("second to string " + helloDTO2.toString());

        val helloAnotherDTO = HelloAnotherDTO.builder()
            .id(3L)
            .age(33)
            .name("third")
            .phoneNumber("5554443322")
            .phoneNumber("5054043020")
            .build();
        log.info("third to string " + helloAnotherDTO.toString());

        val helloValue = new HelloValue(4L, 44, "fourth");
        log.info("fourth (only all args constructor and getter methods available) name " + helloValue.getName());
        log.info("fourth to string " + helloValue.toString());

        val helloData = new HelloData();
        helloData.setId(5L);
        helloData.setAge(55);
        helloData.setName("fifth");
        log.info("fifth (setters, getters, toString, equals and hash available) to string " + helloData.toString());

        return service.sayHi(name) + " REST:" + random;
    }

}
