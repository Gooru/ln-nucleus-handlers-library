package org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc;

import org.gooru.nucleus.handlers.libraries.processors.ProcessorContext;
import org.gooru.nucleus.handlers.libraries.processors.repositories.LibraryContentsRepo;
import org.gooru.nucleus.handlers.libraries.processors.repositories.LibraryRepo;

/**
 * @author szgooru Created On: 26-May-2017
 */
public final class AJRepoBuilder {

    private AJRepoBuilder() {
    }

    public static LibraryRepo buildLibraryRepo(ProcessorContext context) {
        return new AJLibraryRepo(context);
    }

    public static LibraryContentsRepo buildLibraryContentsRepo(ProcessorContext context) {
        return new AJLibraryContentsRepo(context);
    }

}
