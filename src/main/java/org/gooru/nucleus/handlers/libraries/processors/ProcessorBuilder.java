package org.gooru.nucleus.handlers.libraries.processors;

import io.vertx.core.eventbus.Message;

/**
 * @author szgooru Created On: 26-May-2017
 */
public class ProcessorBuilder {
    private ProcessorBuilder() {
        throw new AssertionError();
    }

    public static Processor build(Message<Object> message) {
        return new MessageProcessor(message);
    }
}
