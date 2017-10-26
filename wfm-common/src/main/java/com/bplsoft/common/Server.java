package com.bplsoft.common;

import com.bplsoft.common.property.PropertyProducer;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.resource.PathResourceManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;


public class Server {

    public static final String STATIC_CONTENT_PATH = "STATIC_CONTENT_PATH";
    public static final Logger logger = LoggerFactory.getLogger(Server.class);
    public static final String DEFAULT_PORT = "9091";
    public static final String DEFAULT_HOSTNAME = "localhost";

    public static void main(final String[] args) {

        try {
            // initialize property producer, should read properties
            PropertyProducer.init();


            // current Application
            final String applicationClass = PropertyProducer.getValue("app.class.name");

            // stop if there isn't any application class available, cannot run without it
            if (applicationClass == null) {
                throw new Exception("There is no application class defined in properties file.");
            }

            // create undertow JaxRS server
            UndertowJaxrsServer server = new UndertowJaxrsServer();

            // create Resteasy Deployment
            ResteasyDeployment resteasyDeployment = new ResteasyDeployment();
            resteasyDeployment.setInjectorFactoryClass("org.jboss.resteasy.cdi.CdiInjectorFactory");
            resteasyDeployment.setApplicationClass(applicationClass);

            // get deployment parameters
            final String apiPath = Optional.ofNullable(PropertyProducer.getValue("app.rest.mapping.prefix")).orElse("/api");
            final String contextPath = Optional.ofNullable(PropertyProducer.getValue("app.context.path")).orElse("/");
            final String deploymentName = Optional.ofNullable(PropertyProducer.getValue("app.deployment.name")).orElse("");

            logger.debug(String.format("will start rest context path: '%1s', api path: '%2s', deployment name: '%3s'", contextPath, apiPath, deploymentName));

            // create deployment
            DeploymentInfo deploymentInfo = server.undertowDeployment(resteasyDeployment, apiPath);
            deploymentInfo.setClassLoader(Server.class.getClassLoader());
            deploymentInfo.setContextPath(contextPath);
            deploymentInfo.setDeploymentName(deploymentName);
            deploymentInfo.addListeners(Servlets.listener(org.jboss.weld.environment.servlet.Listener.class));

            // create WebSocket deployment
            final String websocketResourcesProperty = PropertyProducer.getValue("app.websocket.resource");
            if (websocketResourcesProperty != null) {
                WebSocketDeploymentInfo webSocketDeploymentInfo = new WebSocketDeploymentInfo();
                Stream.of(websocketResourcesProperty.split(","))
                    .map(String::trim)
                    .map(websocketResource -> {
                        try {
                            return Class.forName(websocketResource);
                        } catch (ClassNotFoundException e) {
                            logger.error(String.format("could not found class '%1s'", websocketResource));
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .peek(webSocketResourceClass -> logger.debug(String.format("will start web socket '%1s'", webSocketResourceClass.getName())))
                    .forEach(webSocketDeploymentInfo::addEndpoint);
                deploymentInfo.addServletContextAttribute(WebSocketDeploymentInfo.ATTRIBUTE_NAME, webSocketDeploymentInfo);
            } else {
                logger.debug("there are no websocket resources available, will not register any web socket resource");
            }


            // deploy to server
            server.deploy(deploymentInfo);

            // add static content to server
            // STATIC_CONTENT_PATH might be a VM parameter or environment variable
            // ex: -DSTATIC_CONTENT_PATH=/home/can/projects/canmogol/bpl-wfm/web-application/backoffice/web/
            // C:\canm\garb\bpl-wfm\web-application\backoffice\web
            final String contentPathSystemProperty = System.getProperty(STATIC_CONTENT_PATH);
            if (contentPathSystemProperty != null) {
                final Path contentPath = Paths.get(contentPathSystemProperty);
                logger.debug(String.format("will serve static/web content from %1s", contentPathSystemProperty));
                if (Files.isDirectory(contentPath)) {
                    server.addResourcePrefixPath(
                        "/",
                        Handlers.resource(
                            new PathResourceManager(
                                contentPath,
                                100
                            )
                        ).setDirectoryListingEnabled(true)
                    );
                }
            } else {
                logger.debug("STATIC_CONTENT_PATH is not defined, will not serve static/web content");
            }

            // server port and host
            final Integer port = Integer.valueOf(Optional.ofNullable(PropertyProducer.getValue("app.port")).orElse(DEFAULT_PORT));
            final String hostname = Optional.ofNullable(PropertyProducer.getValue("app.host")).orElse(DEFAULT_HOSTNAME);

            logger.debug(String.format("will start server %1s : %2s", hostname, port));

            // start server
            server.start(Undertow.builder().addHttpListener(port, hostname));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }


}
