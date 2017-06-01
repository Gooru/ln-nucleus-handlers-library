package org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.entities;

import java.util.Arrays;
import java.util.List;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

/**
 * @author szgooru Created On: 31-May-2017
 */
@Table("rubric")
public class AJEntityRubric extends Model {

    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String CREATOR_ID = "creator_id";
    public static final String ORIGINAL_CREATOR_ID = "original_creator_id";
    public static final String PUBLISH_STATUS = "publish_status";
    public static final String TAXONOMY = "taxonomy";
    public static final String THUMBNAIL = "thumbnail";
    public static final String VISIBLE_ON_PROFILE = "visible_on_profile";

    public static final String SELECT_RUBRICS =
        "SELECT id, title, description, publish_status, thumbnail, taxonomy, creator_id, original_creator_id,"
            + " visible_on_profile FROM rubric WHERE id = ANY(?::uuid[]) AND is_deleted = false ORDER BY updated_at DESC";

    public static final List<String> RUBRIC_LIST = Arrays.asList(ID, TITLE, DESCRIPTION, PUBLISH_STATUS, THUMBNAIL,
        TAXONOMY, CREATOR_ID, ORIGINAL_CREATOR_ID, VISIBLE_ON_PROFILE);

}
