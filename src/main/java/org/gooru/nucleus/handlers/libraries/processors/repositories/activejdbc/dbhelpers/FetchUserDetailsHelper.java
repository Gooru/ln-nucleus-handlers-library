package org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.dbhelpers;

import java.util.Set;

import org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.entities.AJEntityUsers;
import org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.formatter.JsonFormatterBuilder;
import org.gooru.nucleus.handlers.libraries.processors.utils.CommonUtils;
import org.javalite.activejdbc.LazyList;

import io.vertx.core.json.JsonArray;

/**
 * @author szgooru
 * Created On: 31-May-2017
 */
final class FetchUserDetailsHelper {

    private FetchUserDetailsHelper() {
        throw new AssertionError();
    }

    static JsonArray getOwnerDemographics(Set<String> idlist) {
        LazyList<AJEntityUsers> userDemographics = AJEntityUsers.findBySQL(
            AJEntityUsers.SELECT_MULTIPLE_BY_ID, CommonUtils.toPostgresArrayString(idlist));
        return new JsonArray(JsonFormatterBuilder
            .buildSimpleJsonFormatter(false, AJEntityUsers.SUMMARY_FIELDS).toJson(userDemographics));
    }
}
