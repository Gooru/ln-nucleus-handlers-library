package org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc;

import org.gooru.nucleus.handlers.libraries.processors.ProcessorContext;
import org.gooru.nucleus.handlers.libraries.processors.repositories.LibraryContentsRepo;
import org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.dbhandlers.DBHandlerBuilder;
import org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.transactions.TransactionExecutor;
import org.gooru.nucleus.handlers.libraries.processors.responses.MessageResponse;

/**
 * @author szgooru Created On: 26-May-2017
 */
public class AJLibraryContentsRepo implements LibraryContentsRepo {

    private final ProcessorContext context;

    public AJLibraryContentsRepo(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public MessageResponse getLibraryContents() {
        return TransactionExecutor.executeTransaction(DBHandlerBuilder.buildGetLibraryContentsHandler(context));
    }

}
