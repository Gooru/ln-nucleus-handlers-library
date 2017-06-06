package org.gooru.nucleus.handlers.libraries.processors.commands;

import org.gooru.nucleus.handlers.libraries.processors.ProcessorContext;
import org.gooru.nucleus.handlers.libraries.processors.repositories.RepoBuilder;
import org.gooru.nucleus.handlers.libraries.processors.responses.MessageResponse;

/**
 * @author szgooru Created On: 26-May-2017
 */
class LibrariesGetProcessor extends AbstractCommandProcessor {

    LibrariesGetProcessor(ProcessorContext context) {
        super(context);
    }

    @Override
    protected void setDeprecatedVersions() {
        // NOOP
    }

    @Override
    protected MessageResponse processCommand() {
        return RepoBuilder.buildLibraryRepo(context).getLibraries();
    }

}
