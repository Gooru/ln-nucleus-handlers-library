package org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.entities;

import java.util.Arrays;
import java.util.List;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

/**
 * @author szgooru
 * Created On: 31-May-2017
 */
@Table("original_resource")
public class AJEntityOriginalResource extends Model {

    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String FORMAT = "format";
    public static final String PUBLISH_STATUS = "publish_status";
    public static final String CONTENT_SUBFORMAT = "content_subformat";
    public static final String THUMBNAIL = "thumbnail";
    public static final String TAXONOMY = "taxonomy";
    public static final String UPDATED_AT = "updated_at";
    public static final String CREATOR_ID = "creator_id";
    public static final String COLLECTION_ID = "collection_id";
    public static final String VISIBLE_ON_PROFILE = "visible_on_profile";
    
    public static final String CONTENT_FORMAT = "content_format";
    public static final String ORIGINAL_CREATOR_ID = "original_creator_id";
    public static final String RESOURCE_CONTENT_FORMAT = "resource";
    
    public static final String SELECT_RESOURCES =
        "SELECT id, title, description, publish_status, content_subformat, taxonomy, creator_id, visible_on_profile FROM original_resource"
        + " WHERE id = ANY(?::uuid[]) AND is_deleted = false ORDER BY updated_at DESC";
    
    public static final List<String> RESOURCE_LIST = Arrays.asList(ID, TITLE, DESCRIPTION, PUBLISH_STATUS,
        CONTENT_SUBFORMAT, TAXONOMY, CREATOR_ID, VISIBLE_ON_PROFILE);
}
