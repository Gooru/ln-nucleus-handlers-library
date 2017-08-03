package org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.entities;

import java.util.Arrays;
import java.util.List;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

/**
 * @author szgooru Created On: 29-May-2017
 */
@Table("library")
public class AJEntityLibrary extends Model {

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String SHORT_NAME = "short_name";
    public static final String DESCRIPTION = "description";
    public static final String THUMBNAIL = "thumbnail";
    public static final String TENANT = "tenant";
    public static final String TENANT_ROOT = "tenant_root";
    public static final String COURSE_COUNT = "course_count";
    public static final String ASSESSMENT_COUNT = "assessment_count";
    public static final String COLLECTION_COUNT = "collection_count";
    public static final String RESOURCE_COUNT = "resource_count";
    public static final String QUESTION_COUNT = "question_count";
    public static final String RUBRIC_COUNT = "rubric_count";
    public static final String SEQUENCE_ID = "sequence_id";
    public static final String TAXONOMY = "taxonomy";

    public static final String RESP_KEY_LIBRARIES = "libraries";

    public static final String SELECT_LIBRARIES_BY_TENANTS =
        "SELECT id, name, short_name, description, thumbnail, tenant, tenant_root, course_count, assessment_count, collection_count, resource_count,"
            + " question_count, rubric_count, sequence_id, taxonomy FROM library where tenant = ANY(?::uuid[]) order by tenant, sequence_id";

    public static final String SELECT_LIBRARIES_BY_ID_SHORTNAME =
        "SELECT id, name, short_name, description, thumbnail, tenant, tenant_root, course_count, assessment_count, collection_count, resource_count,"
        + " question_count, rubric_count, sequence_id, taxonomy FROM library WHERE id = ? OR short_name = ?";

    public static final String SELECT_LIBRARIES_BY_SHORTNAME =
        "SELECT id, name, short_name, description, thumbnail, tenant, tenant_root, course_count, assessment_count, collection_count, resource_count,"
        + " question_count, rubric_count, sequence_id, taxonomy FROM library WHERE short_name = ?";

    public static final List<String> LIBRARIES_FIELDS =
        Arrays.asList(ID, NAME, SHORT_NAME, DESCRIPTION, THUMBNAIL, TENANT, TENANT_ROOT, COURSE_COUNT, ASSESSMENT_COUNT,
            COLLECTION_COUNT, RESOURCE_COUNT, QUESTION_COUNT, RUBRIC_COUNT, SEQUENCE_ID, TAXONOMY);

    public static final List<String> LIBRARY_SUMMARY_FIELDS = Arrays.asList(ID, NAME, SHORT_NAME, DESCRIPTION, THUMBNAIL);
}
