package org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.dbhandlers;

import org.gooru.nucleus.handlers.libraries.processors.ProcessorContext;

/**
 * @author szgooru Created On: 26-May-2017
 */
public final class DBHandlerBuilder {

    private DBHandlerBuilder() {
        throw new AssertionError();
    }

    public static DBHandler buildGetLibrariesHandler(ProcessorContext context) {
        return new GetLibrariesHandler(context);
    }

    public static DBHandler buildGetLibraryContentsHandler(ProcessorContext context) {
        return new GetLibraryContentsHandler(context);
    }
}
