package org.gooru.nucleus.handlers.libraries.processors.utils;

import java.util.Collection;
import java.util.Iterator;

import org.gooru.nucleus.handlers.libraries.app.components.AppConfiguration;
import org.gooru.nucleus.handlers.libraries.constants.CommonConstants;
import org.gooru.nucleus.handlers.libraries.processors.ProcessorContext;

import io.vertx.core.json.JsonArray;

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
    
    public static Integer getLimitFromRequest(ProcessorContext context) {
        AppConfiguration appConfig = AppConfiguration.getInstance();
        try {
            String strLimit = readRequestParam(CommonConstants.REQ_PARAM_LIMIT, context);
            int limitFromRequest = strLimit != null ? Integer.valueOf(strLimit) : appConfig.getDefaultPagesize();
            return limitFromRequest > appConfig.getMaxPagesize() ? appConfig.getDefaultPagesize(): limitFromRequest;
        } catch (NumberFormatException nfe) {
            return appConfig.getDefaultPagesize();
        }
    }
    
    public static Integer getOffsetFromRequest(ProcessorContext context) {
        try {
            String offsetFromRequest = readRequestParam(CommonConstants.REQ_PARAM_OFFSET, context);
            return offsetFromRequest != null ? Integer.valueOf(offsetFromRequest) : CommonConstants.DEFAULT_OFFSET;
        } catch (NumberFormatException nfe) {
            return CommonConstants.DEFAULT_OFFSET;
        }
    }
    
    public static String readRequestParam(String param, ProcessorContext context) {
        JsonArray requestParams = context.request().getJsonArray(param);
        if (requestParams == null || requestParams.isEmpty()) {
            return null;
        }

        String value = requestParams.getString(0);
        return (value != null && !value.isEmpty()) ? value : null;
    }
    
    public static String toPostgresIntegerArray(Collection<?> input) {
        int approxSize = ((input.size() + 1) * 36); // Length of UUID is around 36 chars
        
        Iterator<?> it = input.iterator();
        if (!it.hasNext()) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder(approxSize);
        sb.append('{');
        for (;;) {
            Integer s = Integer.parseInt(it.next().toString());
            sb.append(s);
            if (!it.hasNext()) {
                return sb.append('}').toString();
            }
            sb.append(',');
        }
    }
}
