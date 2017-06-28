package org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.dbhelpers;

import org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.entities.AJEntityLibrary;
import org.javalite.activejdbc.LazyList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author szgooru Created On: 28-Jun-2017
 */
public final class LibraryHelper {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(LibraryHelper.class);

    private LibraryHelper() {
        throw new AssertionError();
    }

    public static AJEntityLibrary getLibrary(String libraryId) {
        // If library id is convertible in int then lookup for the library by id
        // and name. There is possibility that library name could be the number
        // If library id is not convertible in int then only look for the
        // library by name.

        // TODO: Need to handle the situation where id and name could be same
        // for two different libraries
        boolean isIntConvertible = false;
        int intLibraryId = 0;
        try {
            intLibraryId = Integer.parseInt(libraryId);
            isIntConvertible = true;
        } catch (NumberFormatException nfe) {
            isIntConvertible = false;
        }

        LazyList<AJEntityLibrary> libraries = null;
        if (isIntConvertible) {
            LOGGER.debug("getting library by id or name");
            libraries = AJEntityLibrary.findBySQL(AJEntityLibrary.SELECT_LIBRARIES_BY_ID_NAME, intLibraryId, libraryId);
        } else {
            LOGGER.debug("getting library by name");
            libraries = AJEntityLibrary.findBySQL(AJEntityLibrary.SELECT_LIBRARIES_BY_NAME, libraryId);
        }

        if (libraries.isEmpty()) {
            return null;
        }

        return libraries.get(0);
    }
}
