package org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.entities;

import java.util.Arrays;
import java.util.List;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

/**
 * @author szgooru Created On: 31-May-2017
 */
@Table("course")
public class AJEntityCourse extends Model {

    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String OWNER_ID = "owner_id";
    public static final String ORIGINAL_CREATOR_ID = "original_creator_id";
    public static final String ORIGINAL_COURSE_ID = "original_course_id";
    public static final String PUBLISH_STATUS = "publish_status";
    public static final String THUMBNAIL = "thumbnail";
    public static final String TAXONOMY = "taxonomy";
    public static final String COLLABORATOR = "collaborator";
    public static final String VISIBLE_ON_PROFILE = "visible_on_profile";
    public static final String SEQUENCE_ID = "sequence_id";
    
    public static final String COURSE_ID = "course_id";
    public static final String UNIT_COUNT = "unit_count";

    public static final String SELECT_COURSES =
        "SELECT id, title, description, publish_status, thumbnail, owner_id, original_creator_id, original_course_id, collaborator, taxonomy,"
            + " sequence_id FROM course WHERE id = ANY(?::uuid[]) AND is_deleted = false ORDER BY created_at DESC";
    
    public static final String SELECT_UNIT_COUNT_FOR_COURSES =
        "SELECT count(unit_id) as unit_count, course_id FROM unit WHERE course_id = ANY"
            + " (?::uuid[]) AND is_deleted = false GROUP BY course_id";
    
    public static final String SELECT_COURSE_FOR_COLLECTION =
        "SELECT id, title, visible_on_profile FROM course WHERE id = ANY (?::uuid[]) AND is_deleted = false";

    public static final List<String> COURSE_LIST = Arrays.asList(ID, TITLE, DESCRIPTION, PUBLISH_STATUS, THUMBNAIL,
        OWNER_ID, ORIGINAL_CREATOR_ID, COLLABORATOR, ORIGINAL_COURSE_ID, TAXONOMY, SEQUENCE_ID, VISIBLE_ON_PROFILE);
    
    public static final List<String> COURSE_FIELDS_FOR_COLLECTION = Arrays.asList(ID, TITLE, VISIBLE_ON_PROFILE);
}
