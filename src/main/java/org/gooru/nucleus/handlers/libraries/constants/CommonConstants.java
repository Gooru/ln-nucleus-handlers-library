package org.gooru.nucleus.handlers.libraries.constants;

/**
 * @author szgooru Created On: 26-May-2017
 */
public final class CommonConstants {

    public static final String RESOURCE_BUNDLE = "messages";

    public static final String REQ_PARAM_CONTENT_TYPE = "contentType";
    public static final String REQ_PARAM_LIMIT = "limit";
    public static final String REQ_PARAM_OFFSET = "offset";
    
    public static final String RESP_JSON_KEY_LIBRARY_CONTENTS = "library_contents";
    public static final String RESP_JSON_KEY_LIBRARY = "library";
    public static final String RESP_JSON_KEY_COURSES = "courses";
    public static final String RESP_JSON_KEY_COURSE = "course";
    public static final String RESP_JSON_KEY_COLLECTIONS = "collections";
    public static final String RESP_JSON_KEY_ASSESSMENTS = "assessments";
    public static final String RESP_JSON_KEY_ASSESSMENT = "assessment";
    public static final String RESP_JSON_KEY_RESOURCES = "resources";
    public static final String RESP_JSON_KEY_QUESTIONS = "questions";
    public static final String RESP_JSON_KEY_RUBRICS = "rubrics";
    public static final String RESP_JSON_KEY_OWNER_DETAILS = "owner_details";
    public static final String RESP_JSON_KEY_FILTERS = "filters";
    
    public static final int DEFAULT_OFFSET = 0;
    
    private CommonConstants() {
        throw new AssertionError();
    }
}
