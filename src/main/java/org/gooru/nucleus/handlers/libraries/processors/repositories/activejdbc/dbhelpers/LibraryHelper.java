package org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.dbhelpers;

import org.gooru.nucleus.handlers.libraries.app.components.AppConfiguration;
import org.gooru.nucleus.handlers.libraries.constants.CommonConstants;
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
        // and short name. There is possibility that library short name could be
        // the number. If library id is not convertible in int then only look
        // for the library by short name.

        // TODO: Need to handle the situation where id and short name could be
        // same for two different libraries
        boolean isIntConvertible = false;
        int intLibraryId = 0;
        try {
            intLibraryId = Integer.parseInt(libraryId);
            isIntConvertible = true;
        } catch (NumberFormatException nfe) {
            isIntConvertible = false;
        }

        if (!isIntConvertible) {
            LOGGER.debug("library id is not convertible to int so getting library by short name");
            return getLibraryByShortname(libraryId);
        }

        return getLibraryByShortNameOrId(libraryId, intLibraryId);
    }

    private static AJEntityLibrary getLibraryByShortname(String shortName) {
        LazyList<AJEntityLibrary> libraries =
            AJEntityLibrary.findBySQL(AJEntityLibrary.SELECT_LIBRARIES_BY_SHORTNAME, shortName);
        return libraries.isEmpty() ? null : libraries.get(0);
    }

    private static AJEntityLibrary getLibraryByShortNameOrId(String shortName, int intLibraryId) {
        LOGGER.debug("getting library by id or short name");
        LazyList<AJEntityLibrary> libraries =
            AJEntityLibrary.findBySQL(AJEntityLibrary.SELECT_LIBRARIES_BY_ID_SHORTNAME, intLibraryId, shortName);
        if (libraries.isEmpty()) {
            return null;
        }

        if (libraries.size() == 1) {
            return libraries.get(0);
        }

        LOGGER.warn("library id and shortname are clashing for:{}", shortName);
        AppConfiguration appConfig = AppConfiguration.getInstance();

        AJEntityLibrary library = libraries.get(0);
        if (appConfig.getDefaultLibraryLookup().equalsIgnoreCase(CommonConstants.DEFAULT_LIBRARY_LOOKUP_SHORTNAME)) {
            String libShortName = library.getString(AJEntityLibrary.SHORT_NAME);
            if (libShortName.equalsIgnoreCase(shortName)) {
                LOGGER.debug("short name matched, returning");
                return library;
            }
        } else {
            if (library.getInteger(AJEntityLibrary.ID) == intLibraryId) {
                LOGGER.debug("id matched, returning");
                return library;
            }
        }

        // This is not a default return. This return is in case above if
        // conditions (comparing shortName or id)
        // inside if-else block does not match. Above check is on get(0) item
        // and if it's not matched then return get(1)
        return libraries.get(1);
    }
}
