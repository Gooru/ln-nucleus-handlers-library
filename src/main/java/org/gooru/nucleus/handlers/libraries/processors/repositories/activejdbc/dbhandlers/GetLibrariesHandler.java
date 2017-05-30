package org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.dbhandlers;

import java.util.ArrayList;
import java.util.List;

import org.gooru.nucleus.handlers.libraries.constants.MessageConstants;
import org.gooru.nucleus.handlers.libraries.processors.ProcessorContext;
import org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.entities.AJEntityLibrary;
import org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.entities.AJEntityTenant;
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
public class GetLibrariesHandler implements DBHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetLibrariesHandler.class);

    private final ProcessorContext context;
    boolean isAnonymous = false;
    LazyList<AJEntityLibrary> libraries;

    public GetLibrariesHandler(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public ExecutionResult<MessageResponse> checkSanity() {
        if (this.context.userId() != null
            && this.context.userId().equalsIgnoreCase(MessageConstants.MSG_USER_ANONYMOUS)) {
            isAnonymous = true;
        }

        LOGGER.debug("checkSanity() OK");
        return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
    }

    @Override
    public ExecutionResult<MessageResponse> validateRequest() {

        // Get all global and discoverable tenants
        LazyList<AJEntityTenant> tenants =
            AJEntityTenant.findBySQL(AJEntityTenant.SELECT_GLOBAL_AND_DISCOVERABLE_TENANTS);
        if (tenants.isEmpty()) {
            LOGGER.debug("no global or discoverable tenant found");
            return new ExecutionResult<>(MessageResponseFactory.createOkayResponse(),
                ExecutionResult.ExecutionStatus.SUCCESSFUL);
        }

        List<String> tenantList = new ArrayList<>(tenants.size());
        tenants.forEach(tenant -> {
            tenantList.add(tenant.getString(AJEntityTenant.ID));
        });

        // If logged in user, add current tenant as well 
        if (!isAnonymous) {
            tenantList.add(context.tenant());
        }

        // Get libraries for all tanants from above list
        this.libraries = AJEntityLibrary.findBySQL(AJEntityLibrary.SELECT_LIBRARIES_BY_TENANTS,
            CommonUtils.toPostgresArrayString(tenantList));

        LOGGER.debug("validateRequest() OK");
        return new ExecutionResult<>(null, ExecutionResult.ExecutionStatus.CONTINUE_PROCESSING);
    }

    @Override
    public ExecutionResult<MessageResponse> executeRequest() {
        JsonArray librariesArray = new JsonArray(new JsonFormatterBuilder()
            .buildSimpleJsonFormatter(false, AJEntityLibrary.LIBRARIES_FIELDS).toJson(this.libraries));

        JsonObject response = new JsonObject();
        response.put(AJEntityLibrary.RESP_KEY_LIBRARIES, librariesArray);
        return new ExecutionResult<>(MessageResponseFactory.createOkayResponse(response),
            ExecutionResult.ExecutionStatus.SUCCESSFUL);
    }

    @Override
    public boolean handlerReadOnly() {
        return true;
    }
}
