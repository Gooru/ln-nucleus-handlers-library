package org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.entities;

import java.util.Arrays;
import java.util.List;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

/**
 * @author szgooru
 * Created On: 31-May-2017
 */
@Table("users")
public class AJEntityUsers extends Model {
    
    private static final String ID = "id";
    private static final String USERNAME = "username";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String THUMBNAIL = "thumbnail";
    private static final String SCHOOL_DISTRICT_ID = "school_district_id";
    private static final String SCHOOL_DISTRICT = "school_district";
    private static final String COUNTRY_ID = "country_id";
    private static final String COUNTRY = "country";

    public static final String SELECT_MULTIPLE_BY_ID =
        "SELECT id, username, first_name, last_name, thumbnail, school_district_id, school_district, country, "
            + "country_id  FROM users WHERE id = ANY (?::uuid[])";
    
    public static final List<String> SUMMARY_FIELDS = Arrays
        .asList(ID, USERNAME, FIRST_NAME, LAST_NAME, THUMBNAIL, SCHOOL_DISTRICT_ID, SCHOOL_DISTRICT, COUNTRY_ID,
            COUNTRY);
}
