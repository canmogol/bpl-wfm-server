package com.bplsoft.common.interceptor;

import org.jboss.weld.interceptor.util.proxy.TargetInstanceProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class MethodLoggingInterceptor {

    @AroundInvoke
    public Object executeContracts(InvocationContext ctx) throws Exception {
        // create a logger for target class
        final Logger logger;
        final String className;
        if (ctx.getTarget() instanceof TargetInstanceProxy) {
            TargetInstanceProxy targetInstanceProxy = (TargetInstanceProxy) ctx.getTarget();
            logger = LoggerFactory.getLogger(targetInstanceProxy.getTargetClass());
            className = targetInstanceProxy.getTargetClass().getName();
        } else {
            logger = LoggerFactory.getLogger(ctx.getTarget().getClass());
            className = ctx.getTarget().getClass().getName();
        }

        // log method begin
        String log = String.format("%1s %2s begin", className, ctx.getMethod().getName());
        logger.debug(log);

        // execute method
        Object result = ctx.proceed();

        // log method end
        log = String.format("%1s %2s end", className, ctx.getMethod().getName());
        logger.debug(log);

        // return result
        return result;
    }
}
