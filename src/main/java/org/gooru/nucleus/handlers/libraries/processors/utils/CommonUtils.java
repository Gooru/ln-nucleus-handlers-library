package org.gooru.nucleus.handlers.libraries.processors.utils;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author szgooru Created On: 29-May-2017
 */
public final class CommonUtils {

    private CommonUtils() {
        throw new AssertionError();
    }

    public static String toPostgresArrayString(Collection<String> input) {
        int approxSize = ((input.size() + 1) * 36); // Length of UUID is around
                                                    // 36
                                                    // chars
        Iterator<String> it = input.iterator();
        if (!it.hasNext()) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder(approxSize);
        sb.append('{');
        for (;;) {
            String s = it.next();
            sb.append('"').append(s).append('"');
            if (!it.hasNext()) {
                return sb.append('}').toString();
            }
            sb.append(',');
        }
    }
}
