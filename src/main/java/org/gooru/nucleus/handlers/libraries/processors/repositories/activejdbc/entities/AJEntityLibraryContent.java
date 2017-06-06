package org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.entities;

import java.util.Arrays;
import java.util.List;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

/**
 * @author szgooru Created On: 30-May-2017
 */
@Table("library_content")
public class AJEntityLibraryContent extends Model {

    public static final String ID = "id";
    public static final String LIBRARY_ID = "library_id";
    public static final String CONTENT_ID = "content_id";
    public static final String CONTENT_TYPE = "content_type";
    public static final String SEQUENCE_ID = "sequence_id";

    public static final String CONTENT_TYPE_ALL = "all";
    public static final String CONTENT_TYPE_COURSE = "course";
    public static final String CONTENT_TYPE_COLLECTION = "collection";
    public static final String CONTENT_TYPE_ASSESSMENT = "assessment";
    public static final String CONTENT_TYPE_RESOURCE = "resource";
    public static final String CONTENT_TYPE_QUESTION = "question";
    public static final String CONTENT_TYPE_RUBRIC = "rubric";

    public static final List<String> VALID_CONTENT_TYPES = Arrays.asList(CONTENT_TYPE_COURSE, CONTENT_TYPE_COLLECTION,
        CONTENT_TYPE_ASSESSMENT, CONTENT_TYPE_RESOURCE, CONTENT_TYPE_QUESTION, CONTENT_TYPE_RUBRIC, CONTENT_TYPE_ALL);

    public static final String SELECT_LIBRARY_CONTENTS_ALL =
        "SELECT id, content_id, content_type, sequence_id FROM library_content WHERE library_id = ? LIMIT ? OFFSET ?";

    public static final String SELECT_LIBRARY_CONTENTS_BY_CONTENTTYPE =
        "SELECT id, content_id, content_type, sequence_id FROM library_content WHERE library_id = ? AND content_type = ? ORDER BY"
            + " sequence_id LIMIT ? OFFSET ?";

    public static final List<String> LIBRARY_CONTENTS_FIELDS = Arrays.asList(ID, CONTENT_ID, CONTENT_TYPE, SEQUENCE_ID);

    public String getContentType() {
        return this.getString(CONTENT_TYPE);
    }
}
