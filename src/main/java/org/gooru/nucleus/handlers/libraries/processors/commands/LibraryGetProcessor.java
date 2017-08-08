package org.gooru.nucleus.handlers.libraries.processors.commands;

import java.util.ResourceBundle;

import org.gooru.nucleus.handlers.libraries.constants.CommonConstants;
import org.gooru.nucleus.handlers.libraries.processors.ProcessorContext;
import org.gooru.nucleus.handlers.libraries.processors.repositories.RepoBuilder;
import org.gooru.nucleus.handlers.libraries.processors.responses.MessageResponse;
import org.gooru.nucleus.handlers.libraries.processors.responses.MessageResponseFactory;
import org.gooru.nucleus.handlers.libraries.processors.utils.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author szgooru Created On: 28-Jun-2017
 */
public class LibraryGetProcessor extends AbstractCommandProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LibraryGetProcessor.class);
    private static final ResourceBundle MESSAGES = ResourceBundle.getBundle(CommonConstants.RESOURCE_BUNDLE);

    protected LibraryGetProcessor(ProcessorContext context) {
        super(context);
    }

    @Override
    protected void setDeprecatedVersions() {
        // NOOP
    }

    @Override
    protected MessageResponse processCommand() {
        if (!ValidationUtils.validateNullOrEmpty(context.libraryId())) {
            LOGGER.warn("invalid library id passed");
            return MessageResponseFactory.createInternalErrorResponse(MESSAGES.getString("invalid.libraryid"));
        }

        return RepoBuilder.buildLibraryRepo(context).getLibrary();
    }

}
