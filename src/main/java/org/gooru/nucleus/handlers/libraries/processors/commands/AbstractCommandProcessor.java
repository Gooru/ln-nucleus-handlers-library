package org.gooru.nucleus.handlers.libraries.processors.commands;

import java.util.ArrayList;
import java.util.List;

import org.gooru.nucleus.handlers.libraries.processors.Processor;
import org.gooru.nucleus.handlers.libraries.processors.ProcessorContext;
import org.gooru.nucleus.handlers.libraries.processors.responses.MessageResponse;
import org.gooru.nucleus.handlers.libraries.processors.utils.VersionValidationUtils;

/**
 * @author szgooru Created On: 26-May-2017
 */
public abstract class AbstractCommandProcessor implements Processor {

    protected final List<String> deprecatedVersions = new ArrayList<>();
    protected final ProcessorContext context;
    protected String version;

    protected AbstractCommandProcessor(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public MessageResponse process() {
        setDeprecatedVersions();
        version = VersionValidationUtils.validateVersion(deprecatedVersions, context.requestHeaders());
        return processCommand();
    }

    protected abstract void setDeprecatedVersions();

    protected abstract MessageResponse processCommand();
}
