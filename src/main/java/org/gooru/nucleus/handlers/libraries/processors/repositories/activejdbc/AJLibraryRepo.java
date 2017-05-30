package org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc;

import org.gooru.nucleus.handlers.libraries.processors.ProcessorContext;
import org.gooru.nucleus.handlers.libraries.processors.repositories.LibraryRepo;
import org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.dbhandlers.DBHandlerBuilder;
import org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.transactions.TransactionExecutor;
import org.gooru.nucleus.handlers.libraries.processors.responses.MessageResponse;

/**
 * @author szgooru Created On: 26-May-2017
 */
public class AJLibraryRepo implements LibraryRepo {

    private final ProcessorContext context;

    public AJLibraryRepo(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public MessageResponse getLibraries() {
        return TransactionExecutor.executeTransaction(DBHandlerBuilder.buildGetLibrariesHandler(context));
    }

}
