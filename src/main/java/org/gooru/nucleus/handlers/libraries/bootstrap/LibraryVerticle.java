package org.gooru.nucleus.handlers.libraries.bootstrap;

import org.gooru.nucleus.handlers.libraries.bootstrap.shutdown.Finalizer;
import org.gooru.nucleus.handlers.libraries.bootstrap.shutdown.Finalizers;
import org.gooru.nucleus.handlers.libraries.bootstrap.startup.Initializer;
import org.gooru.nucleus.handlers.libraries.bootstrap.startup.Initializers;
import org.gooru.nucleus.handlers.libraries.constants.MessagebusEndpoints;
import org.gooru.nucleus.handlers.libraries.processors.ProcessorBuilder;
import org.gooru.nucleus.handlers.libraries.processors.responses.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;

/**
 * @author szgooru Created On: 26-May-2017
 */
public class LibraryVerticle extends AbstractVerticle {

    private static final Logger LOGGER = LoggerFactory.getLogger(LibraryVerticle.class);

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        EventBus eb = vertx.eventBus();

        vertx.executeBlocking(blockingFuture -> {
            startApplication();
            blockingFuture.complete();
        }, startApplicationFuture -> {
            if (startApplicationFuture.succeeded()) {
                eb.consumer(MessagebusEndpoints.MBEP_LIBRARY, message -> {
                    LOGGER.debug("Received message: '{}'", message.body());
                    vertx.executeBlocking(future -> {
                        MessageResponse result = ProcessorBuilder.build(message).process();
                        future.complete(result);
                    }, res -> {
                        MessageResponse result = (MessageResponse) res.result();
                        LOGGER.debug("Sending response: '{}'", result.reply());
                        message.reply(result.reply(), result.deliveryOptions());
                    });
                }).completionHandler(result -> {
                    if (result.succeeded()) {
                        LOGGER.info("Library end point ready to listen");
                        startFuture.complete();
                    } else {
                        LOGGER.error("Error registering the library handler. Halting the Library machinery");
                        startFuture.fail(result.cause());
                        Runtime.getRuntime().halt(1);
                    }
                });
            } else {
                startFuture.fail("Not able to initialize the Library machinery properly");
            }
        });
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        shutDownApplication();
        super.stop(stopFuture);
    }

    private void startApplication() {
        Initializers initializers = new Initializers();
        try {
            for (Initializer initializer : initializers) {
                initializer.initializeComponent(vertx, config());
            }
        } catch (IllegalStateException ise) {
            LOGGER.error("Error initializing application", ise);
            Runtime.getRuntime().halt(1);
        }
    }

    private void shutDownApplication() {
        Finalizers finalizers = new Finalizers();
        for (Finalizer finalizer : finalizers) {
            finalizer.finalizeComponent();
        }
    }
}
