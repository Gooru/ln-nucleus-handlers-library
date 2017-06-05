package org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.dbhandlers;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.gooru.nucleus.handlers.libraries.constants.CommonConstants;
import org.gooru.nucleus.handlers.libraries.constants.MessageConstants;
import org.gooru.nucleus.handlers.libraries.processors.ProcessorContext;
import org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.dbhelpers.FetchContentDetailsHelper;
import org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.entities.AJEntityLibrary;
import org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.entities.AJEntityLibraryContent;
import org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.formatter.JsonFormatterBuilder;
import org.gooru.nucleus.handlers.libraries.processors.responses.ExecutionResult;
import org.gooru.nucleus.handlers.libraries.processors.responses.MessageResponse;
import org.gooru.nucleus.handlers.libraries.processors.responses.MessageResponseFactory;
import org.gooru.nucleus.handlers.libraries.processors.utils.CommonUtils;
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
    private String contentType;
    private int limit;
    private int offset;
    private LazyList<AJEntityLibraryContent> libraryContents;
    private AJEntityLibrary library;

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

        this.contentType = CommonUtils.readRequestParam(CommonConstants.REQ_PARAM_CONTENT_TYPE, context);
        if (this.contentType == null || !AJEntityLibraryContent.VALID_CONTENT_TYPES.contains(contentType)) {
            LOGGER.warn("invalid content type provided, setting default");
            this.contentType = AJEntityLibraryContent.CONTENT_TYPE_COURSE;
        }

        this.limit = CommonUtils.getLimitFromRequest(context);
        this.offset = CommonUtils.getOffsetFromRequest(context);

        LOGGER.debug("checkSanity() OK");
        return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
    }

    @Override
    public ExecutionResult<MessageResponse> validateRequest() {

        // If library id is convertible in int then lookup for the library by id
        // and name. There is possibility that library name could be the number
        // If library id is not convertible in int then only look for the
        // library by name.

        // TODO: Need to handle the situation where id and name could be same
        // for two different libraries
        boolean isIntConvertible = false;
        int intLibraryId = 0;
        try {
            intLibraryId = Integer.parseInt(context.libraryId());
            isIntConvertible = true;
        } catch (NumberFormatException nfe) {
            isIntConvertible = false;
        }

        LazyList<AJEntityLibrary> libraries = null;
        if (isIntConvertible) {
            libraries = AJEntityLibrary.findBySQL(AJEntityLibrary.SELECT_LIBRARIES_BY_ID_NAME, intLibraryId,
                context.libraryId());
        } else {
            libraries = AJEntityLibrary.findBySQL(AJEntityLibrary.SELECT_LIBRARIES_BY_NAME, context.libraryId());
        }

        if (libraries.isEmpty()) {
            LOGGER.warn("library not found for id '{}'", context.libraryId());
            return new ExecutionResult<MessageResponse>(
                MessageResponseFactory.createNotFoundResponse(MESSAGES.getString("not.found")),
                ExecutionResult.ExecutionStatus.FAILED);
        }

        this.library = libraries.get(0);

        LOGGER.debug("validateRequest() OK");
        return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
    }

    @Override
    public ExecutionResult<MessageResponse> executeRequest() {
        JsonObject response = new JsonObject();
        JsonObject libraryJson = new JsonObject(new JsonFormatterBuilder()
            .buildSimpleJsonFormatter(false, AJEntityLibrary.LIBRARY_SUMMARY_FIELDS).toJson(this.library));
        response.put(CommonConstants.RESP_JSON_KEY_LIBRARY, libraryJson);
        
        response.mergeIn(FetchContentDetailsHelper.fetchContentDetails(this.contentType,
            this.library.getInteger(AJEntityLibrary.ID), this.limit, this.offset));
        //response.put(CommonConstants.RESP_JSON_KEY_LIBRARY_CONTENTS, libraryContentsJson);
        response.put(CommonConstants.RESP_JSON_KEY_FILTERS, getFiltersJson());
        return new ExecutionResult<>(MessageResponseFactory.createOkayResponse(response),
            ExecutionResult.ExecutionStatus.SUCCESSFUL);
    }

    @Override
    public boolean handlerReadOnly() {
        return true;
    }

    private JsonObject getFiltersJson() {
        JsonObject filters = new JsonObject();
        filters.put(CommonConstants.REQ_PARAM_CONTENT_TYPE, this.contentType)
            .put(CommonConstants.REQ_PARAM_LIMIT, this.limit).put(CommonConstants.REQ_PARAM_OFFSET, this.offset);
        return filters;
    }
}
