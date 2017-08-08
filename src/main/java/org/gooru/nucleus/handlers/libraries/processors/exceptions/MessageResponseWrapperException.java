package org.gooru.nucleus.handlers.libraries.processors.exceptions;

import org.gooru.nucleus.handlers.libraries.processors.responses.MessageResponse;

/**
 * @author szgooru Created On: 26-May-2017
 */
public class MessageResponseWrapperException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final MessageResponse messageResponse;

    public MessageResponseWrapperException(MessageResponse messageResponse) {
        this.messageResponse = messageResponse;
    }

    public MessageResponse getMessageResponse() {
        return messageResponse;
    }
}
