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
    
    public static final String RESP_KEY_LIBRARY_CONTENTS = "library_contents";

    public static final String SELECT_LIBRARY_CONETNTS =
        "SELECT id, content_id, content_type, sequence_id FROM library_content WHERE library_id = ? ORDER BY sequence_id";

    public static final List<String> LIBRARY_CONTENTS_FIELDS = Arrays.asList(ID, CONTENT_ID, CONTENT_TYPE, SEQUENCE_ID);
}
