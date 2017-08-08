package org.gooru.nucleus.handlers.libraries.processors.repositories;

import org.gooru.nucleus.handlers.libraries.processors.ProcessorContext;
import org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.AJRepoBuilder;

/**
 * @author szgooru
 * Created On: 26-May-2017
 */
public final class RepoBuilder {

    private RepoBuilder() {
        throw new AssertionError();
    }
    
    public static LibraryRepo buildLibraryRepo(ProcessorContext context) {
        return AJRepoBuilder.buildLibraryRepo(context);
    }
    
    public static LibraryContentsRepo buildLibraryContentsRepo(ProcessorContext context) {
        return AJRepoBuilder.buildLibraryContentsRepo(context);
    }
}
