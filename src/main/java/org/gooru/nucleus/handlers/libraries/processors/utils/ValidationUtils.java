package org.gooru.nucleus.handlers.libraries.processors.utils;

import java.util.UUID;

import org.gooru.nucleus.handlers.libraries.constants.MessageConstants;


/**
 * @author ashish on 29/12/16.
 */
public final class ValidationUtils {

    private ValidationUtils() {
        throw new AssertionError();
    }

    public static boolean validateUser(String userId) {
        return !(userId == null || userId.isEmpty()) && (userId.equalsIgnoreCase(MessageConstants.MSG_USER_ANONYMOUS)
            || validateUuid(userId));
    }

    public static boolean validateNullOrEmpty(String value) {
        return !(value == null || value.isEmpty());
    }

    public static boolean validateInt(String id) {
        if (id == null || id.isEmpty()) {
            return false;
        }
        try {
            Integer.valueOf(id);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static boolean validateId(String id) {
        return !(id == null || id.isEmpty()) && validateUuid(id);
    }

    private static boolean validateUuid(String uuidString) {
        try {
            UUID.fromString(uuidString);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
