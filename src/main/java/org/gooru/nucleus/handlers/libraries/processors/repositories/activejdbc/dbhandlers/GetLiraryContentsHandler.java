package org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.dbhandlers;

import java.util.ResourceBundle;

import org.gooru.nucleus.handlers.libraries.constants.CommonConstants;
import org.gooru.nucleus.handlers.libraries.processors.ProcessorContext;
import org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.entities.AJEntityLibrary;
import org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.entities.AJEntityLibraryContent;
import org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.formatter.JsonFormatterBuilder;
import org.gooru.nucleus.handlers.libraries.processors.responses.ExecutionResult;
import org.gooru.nucleus.handlers.libraries.processors.responses.MessageResponse;
import org.gooru.nucleus.handlers.libraries.processors.responses.MessageResponseFactory;
import org.javalite.activejdbc.LazyList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On: 26-May-2017
 */
public class GetLiraryContentsHandler implements DBHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetLiraryContentsHandler.class);
    private static final ResourceBundle MESSAGES = ResourceBundle.getBundle(CommonConstants.RESOURCE_BUNDLE);

    private final ProcessorContext context;
    private int libraryId;
    private LazyList<AJEntityLibraryContent> libraryContents;

    public GetLiraryContentsHandler(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public ExecutionResult<MessageResponse> checkSanity() {
        // Checking library id
        if (context.libraryId() == null || context.libraryId().isEmpty()) {
            LOGGER.warn("invalid library id provided");
            return new ExecutionResult<>(
                MessageResponseFactory.createInvalidRequestResponse(MESSAGES.getString("invalid.libraryid")),
                ExecutionResult.ExecutionStatus.FAILED);
        }

        try {
            this.libraryId = Integer.parseInt(context.libraryId());
        } catch (NumberFormatException nfe) {
            LOGGER.warn("library id is not integer");
            return new ExecutionResult<>(
                MessageResponseFactory.createInvalidRequestResponse(MESSAGES.getString("invalid.libraryid")),
                ExecutionResult.ExecutionStatus.FAILED);
        }

        LOGGER.debug("checkSanity() OK");
        return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
    }

    @Override
    public ExecutionResult<MessageResponse> validateRequest() {
        // check for library existence
        AJEntityLibrary library = AJEntityLibrary.findById(this.libraryId);
        if (library == null) {
            LOGGER.warn("library not found for id '{}'", this.libraryId);
            return new ExecutionResult<MessageResponse>(
                MessageResponseFactory.createNotFoundResponse(MESSAGES.getString("not.found")),
                ExecutionResult.ExecutionStatus.FAILED);
        }

        this.libraryContents =
            AJEntityLibraryContent.findBySQL(AJEntityLibraryContent.SELECT_LIBRARY_CONETNTS, this.libraryId);

        LOGGER.debug("validateRequest() OK");
        return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
    }

    @Override
    public ExecutionResult<MessageResponse> executeRequest() {
        JsonArray libraryContentArray = new JsonArray(new JsonFormatterBuilder().buildSimpleJsonFormatter(false, AJEntityLibraryContent.LIBRARY_CONTENTS_FIELDS)
            .toJson(this.libraryContents));
        
        JsonObject response = new JsonObject();
        response.put(AJEntityLibraryContent.RESP_KEY_LIBRARY_CONTENTS, libraryContentArray);
        return new ExecutionResult<>(MessageResponseFactory.createOkayResponse(response),
            ExecutionResult.ExecutionStatus.SUCCESSFUL);
    }

    @Override
    public boolean handlerReadOnly() {
        return true;
    }

}
