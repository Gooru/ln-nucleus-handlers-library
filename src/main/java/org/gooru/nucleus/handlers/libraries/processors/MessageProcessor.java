package org.gooru.nucleus.handlers.libraries.processors;

import java.util.ResourceBundle;

import org.gooru.nucleus.handlers.libraries.constants.CommonConstants;
import org.gooru.nucleus.handlers.libraries.constants.MessageConstants;
import org.gooru.nucleus.handlers.libraries.processors.commands.CommandProcessorBuilder;
import org.gooru.nucleus.handlers.libraries.processors.exceptions.VersionDeprecatedException;
import org.gooru.nucleus.handlers.libraries.processors.responses.ExecutionResult;
import org.gooru.nucleus.handlers.libraries.processors.responses.MessageResponse;
import org.gooru.nucleus.handlers.libraries.processors.responses.MessageResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.MultiMap;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On: 26-May-2017
 */
public class MessageProcessor implements Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProcessor.class);
    private static final ResourceBundle MESSAGES = ResourceBundle.getBundle(CommonConstants.RESOURCE_BUNDLE);

    private final Message<Object> message;
    private String userId;
    private JsonObject session;
    private JsonObject request;

    public MessageProcessor(Message<Object> message) {
        this.message = message;
    }

    @Override
    public MessageResponse process() {
        try {
            ExecutionResult<MessageResponse> validateResult = validateAndInitialize();
            if (validateResult.isCompleted()) {
                return validateResult.result();
            }

            final String msgOp = message.headers().get(MessageConstants.MSG_HEADER_OP);
            return CommandProcessorBuilder.lookupBuilder(msgOp).build(createContext()).process();
        } catch (VersionDeprecatedException e) {
            LOGGER.error("Version is deprecated");
            return MessageResponseFactory.createVersionDeprecatedResponse();
        } catch (Throwable e) {
            LOGGER.error("Unhandled exception in processing", e);
            return MessageResponseFactory.createInternalErrorResponse(MESSAGES.getString("unexpected.error"));
        }
    }

    private ProcessorContext createContext() {
        MultiMap headers = message.headers();
        String libraryId = headers.get(MessageConstants.LIBRARY_ID);
        return new ProcessorContext.ProcessorContextBuilder(userId, session, request, libraryId, headers).build();
    }

    private ExecutionResult<MessageResponse> validateAndInitialize() {
        if (message == null || !(message.body() instanceof JsonObject)) {
            LOGGER.error("Invalid message received, either null or body of message is not JsonObject ");
            return new ExecutionResult<>(
                MessageResponseFactory.createInvalidRequestResponse(MESSAGES.getString("invalid.payload")),
                ExecutionResult.ExecutionStatus.FAILED);
        }

        userId = ((JsonObject) message.body()).getString(MessageConstants.MSG_USER_ID);
        if (!ProcessorContextHelper.validateUser(userId)) {
            LOGGER.error("Invalid user id passed. Not authorized.");
            return new ExecutionResult<>(
                MessageResponseFactory.createForbiddenResponse(MESSAGES.getString("invalid.user")),
                ExecutionResult.ExecutionStatus.FAILED);
        }
        session = ((JsonObject) message.body()).getJsonObject(MessageConstants.MSG_KEY_SESSION);
        request = ((JsonObject) message.body()).getJsonObject(MessageConstants.MSG_HTTP_BODY);

        if (session == null || session.isEmpty()) {
            LOGGER.error("Invalid session obtained, probably not authorized properly");
            return new ExecutionResult<>(
                MessageResponseFactory.createForbiddenResponse(MESSAGES.getString("invalid.session")),
                ExecutionResult.ExecutionStatus.FAILED);
        }

        if (request == null) {
            LOGGER.error("Invalid JSON payload on Message Bus");
            return new ExecutionResult<>(
                MessageResponseFactory.createInvalidRequestResponse(MESSAGES.getString("invalid.payload")),
                ExecutionResult.ExecutionStatus.FAILED);
        }

        // All is well, continue processing
        return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
    }

}
