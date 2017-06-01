package org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.dbhelpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gooru.nucleus.handlers.libraries.constants.CommonConstants;
import org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.entities.AJEntityCollection;
import org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.entities.AJEntityContent;
import org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.entities.AJEntityCourse;
import org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.entities.AJEntityLibraryContent;
import org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.entities.AJEntityOriginalResource;
import org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.entities.AJEntityRubric;
import org.gooru.nucleus.handlers.libraries.processors.repositories.activejdbc.formatter.JsonFormatterBuilder;
import org.gooru.nucleus.handlers.libraries.processors.utils.CommonUtils;
import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author szgooru Created On: 31-May-2017
 */
public final class FetchContentDetailsHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(FetchContentDetailsHelper.class);

    private FetchContentDetailsHelper() {
        throw new AssertionError();
    }

    public static JsonObject fetchContentDetails(String contentType, List<String> contentIds) {
        JsonObject result = new JsonObject();
        switch (contentType) {
        case AJEntityLibraryContent.CONTENT_TYPE_COURSE:
            result = fetchCoursesDetails(contentIds);
            break;
        case AJEntityLibraryContent.CONTENT_TYPE_COLLECTION:
            result = fetchCollectionsDetails(contentIds);
            break;
        case AJEntityLibraryContent.CONTENT_TYPE_ASSESSMENT:
            result = fetchAssessmentsDetails(contentIds);
            break;
        case AJEntityLibraryContent.CONTENT_TYPE_RESOURCE:
            result = fetchResourcesDetails(contentIds);
            break;
        case AJEntityLibraryContent.CONTENT_TYPE_QUESTION:
            result = fetchQuestionsDetails(contentIds);
            break;
        case AJEntityLibraryContent.CONTENT_TYPE_RUBRIC:
            result = fetchRubricsDetails(contentIds);
            break;
        }

        return result;
    }

    private static JsonObject fetchCoursesDetails(List<String> courseIds) {
        LazyList<AJEntityCourse> courses =
            AJEntityCourse.findBySQL(AJEntityCourse.SELECT_COURSES, CommonUtils.toPostgresArrayString(courseIds));
        Set<String> ownerIdList = new HashSet<>();
        JsonArray courseArray = new JsonArray();
        if (!courses.isEmpty()) {
            List<String> courseIdList = new ArrayList<>();
            courses.stream().forEach(course -> courseIdList.add(course.getString(AJEntityCourse.ID)));

            List<Map> unitCounts = Base.findAll(AJEntityCourse.SELECT_UNIT_COUNT_FOR_COURSES,
                CommonUtils.toPostgresArrayString(courseIdList));
            Map<String, Integer> unitCountByCourse = new HashMap<>();
            unitCounts.stream().forEach(map -> unitCountByCourse.put(map.get(AJEntityCourse.COURSE_ID).toString(),
                Integer.valueOf(map.get(AJEntityCourse.UNIT_COUNT).toString())));

            courses.stream().forEach(course -> {
                Integer unitCount = unitCountByCourse.get(course.getString(AJEntityCourse.ID));
                courseArray.add(new JsonObject(new JsonFormatterBuilder()
                    .buildSimpleJsonFormatter(false, AJEntityCourse.COURSE_LIST).toJson(course))
                        .put(AJEntityCourse.UNIT_COUNT, unitCount != null ? unitCount : 0));
            });

            courses.stream().forEach(course -> ownerIdList.add(course.getString(AJEntityCourse.OWNER_ID)));
        }

        JsonObject responseBody = new JsonObject();
        responseBody.put(CommonConstants.RESP_JSON_KEY_COURSES, courseArray);
        responseBody.put(CommonConstants.RESP_JSON_KEY_OWNER_DETAILS,
            FetchUserDeatailsHelper.getOwnerDemographics(ownerIdList));
        return responseBody;
    }

    private static JsonObject fetchCollectionsDetails(List<String> contentIds) {
        LazyList<AJEntityCollection> collections = AJEntityCollection.findBySQL(AJEntityCollection.SELECT_COLLECTIONS,
            CommonUtils.toPostgresArrayString(contentIds));
        JsonArray collectionArray = new JsonArray();
        Set<String> ownerIdList = new HashSet<>();
        if (!collections.isEmpty()) {
            LOGGER.debug("# Collections found: {}", collections.size());
            List<String> collectionIdList = new ArrayList<>();
            collections.stream()
                .forEach(collection -> collectionIdList.add(collection.getString(AJEntityCollection.ID)));

            List<Map> resourceCounts = Base.findAll(AJEntityCollection.SELECT_RESOURCES_COUNT_FOR_COLLECTION,
                CommonUtils.toPostgresArrayString(collectionIdList));
            Map<String, Integer> resourceCountByCollection = new HashMap<>();
            resourceCounts.stream()
                .forEach(map -> resourceCountByCollection.put(map.get(AJEntityCollection.COLLECTION_ID).toString(),
                    Integer.valueOf(map.get(AJEntityCollection.RESOURCE_COUNT).toString())));
            LOGGER.debug("# of Collectios has resources: {}", resourceCountByCollection.size());

            List<Map> questionCounts = Base.findAll(AJEntityCollection.SELECT_QUESTIONS_COUNT_FOR_COLLECTION,
                CommonUtils.toPostgresArrayString(collectionIdList));
            Map<String, Integer> questionCountByCollection = new HashMap<>();
            questionCounts.stream()
                .forEach(map -> questionCountByCollection.put(map.get(AJEntityCollection.COLLECTION_ID).toString(),
                    Integer.valueOf(map.get(AJEntityCollection.QUESTION_COUNT).toString())));
            LOGGER.debug("# of Collectios has questions: {}", resourceCountByCollection.size());

            List<String> courseIdList = new ArrayList<>();
            collections.stream()
                .filter(collection -> collection.getString(AJEntityCollection.COURSE_ID) != null
                    && !collection.getString(AJEntityCollection.COURSE_ID).isEmpty())
                .forEach(collection -> courseIdList.add(collection.getString(AJEntityCollection.COURSE_ID)));
            LOGGER.debug("# Courses are associated with collections: {}", courseIdList.size());

            LazyList<AJEntityCourse> courseList = AJEntityCourse.findBySQL(AJEntityCourse.SELECT_COURSE_FOR_COLLECTION,
                CommonUtils.toPostgresArrayString(courseIdList));
            Map<String, AJEntityCourse> courseMap = new HashMap<>();
            courseList.stream().forEach(course -> courseMap.put(course.getString(AJEntityCourse.ID), course));
            LOGGER.debug("# Courses returned from database: {}", courseMap.size());

            collections.forEach(collection -> {
                JsonObject result = new JsonObject(new JsonFormatterBuilder()
                    .buildSimpleJsonFormatter(false, AJEntityCollection.COLLECTION_LIST).toJson(collection));
                String courseId = collection.getString(AJEntityCollection.COURSE_ID);
                if (courseId != null && !courseId.isEmpty()) {
                    AJEntityCourse course = courseMap.get(courseId);
                    result.put(CommonConstants.RESP_JSON_KEY_COURSE,
                        new JsonObject(new JsonFormatterBuilder()
                            .buildSimpleJsonFormatter(false, AJEntityCourse.COURSE_FIELDS_FOR_COLLECTION)
                            .toJson(course)));
                }

                String collectionId = collection.getString(AJEntityCollection.ID);
                Integer resourceCount = resourceCountByCollection.get(collectionId);
                Integer questionCount = questionCountByCollection.get(collectionId);
                result.put(AJEntityCollection.RESOURCE_COUNT, resourceCount != null ? resourceCount : 0);
                result.put(AJEntityCollection.QUESTION_COUNT, questionCount != null ? questionCount : 0);
                collectionArray.add(result);
            });

            collections.stream()
                .forEach(collection -> ownerIdList.add(collection.getString(AJEntityCollection.OWNER_ID)));
        }

        JsonObject responseBody = new JsonObject();
        responseBody.put(CommonConstants.RESP_JSON_KEY_COLLECTIONS, collectionArray);
        responseBody.put(CommonConstants.RESP_JSON_KEY_OWNER_DETAILS,
            FetchUserDeatailsHelper.getOwnerDemographics(ownerIdList));
        return responseBody;
    }

    private static JsonObject fetchAssessmentsDetails(List<String> contentIds) {
        LazyList<AJEntityCollection> assessments = AJEntityCollection.findBySQL(AJEntityCollection.SELECT_ASSESSMENTS,
            CommonUtils.toPostgresArrayString(contentIds));
        JsonArray collectionArray = new JsonArray();
        Set<String> ownerIdList = new HashSet<>();
        if (!assessments.isEmpty()) {
            LOGGER.debug("# Assessments found: {}", assessments.size());
            List<String> collectionIdList = new ArrayList<>();
            assessments.stream()
                .forEach(collection -> collectionIdList.add(collection.getString(AJEntityCollection.ID)));

            List<Map> questionCounts = Base.findAll(AJEntityCollection.SELECT_QUESTIONS_COUNT_FOR_COLLECTION,
                CommonUtils.toPostgresArrayString(collectionIdList));
            Map<String, Integer> questionCountByCollection = new HashMap<>();
            questionCounts.stream()
                .forEach(map -> questionCountByCollection.put(map.get(AJEntityCollection.COLLECTION_ID).toString(),
                    Integer.valueOf(map.get(AJEntityCollection.QUESTION_COUNT).toString())));
            LOGGER.debug("# of assessments has questions: {}", questionCountByCollection.size());

            List<String> courseIdList = new ArrayList<>();
            assessments.stream()
                .filter(collection -> collection.getString(AJEntityCollection.COURSE_ID) != null
                    && !collection.getString(AJEntityCollection.COURSE_ID).isEmpty())
                .forEach(collection -> courseIdList.add(collection.getString(AJEntityCollection.COURSE_ID)));
            LOGGER.debug("# Courses are associated with assessments: {}", courseIdList.size());

            LazyList<AJEntityCourse> courseList = AJEntityCourse.findBySQL(AJEntityCourse.SELECT_COURSE_FOR_COLLECTION,
                CommonUtils.toPostgresArrayString(courseIdList));
            Map<String, AJEntityCourse> courseMap = new HashMap<>();
            courseList.stream().forEach(course -> courseMap.put(course.getString(AJEntityCourse.ID), course));
            LOGGER.debug("# Courses returned from database: {}", courseMap.size());

            assessments.forEach(collection -> {
                JsonObject result = new JsonObject(new JsonFormatterBuilder()
                    .buildSimpleJsonFormatter(false, AJEntityCollection.ASSESSMENT_LIST).toJson(collection));
                String courseId = collection.getString(AJEntityCollection.COURSE_ID);
                if (courseId != null && !courseId.isEmpty()) {
                    AJEntityCourse course = courseMap.get(courseId);
                    result.put(CommonConstants.RESP_JSON_KEY_COURSE,
                        new JsonObject(new JsonFormatterBuilder()
                            .buildSimpleJsonFormatter(false, AJEntityCourse.COURSE_FIELDS_FOR_COLLECTION)
                            .toJson(course)));
                }
                Integer questionCount = questionCountByCollection.get(collection.getString(AJEntityCollection.ID));
                result.put(AJEntityCollection.QUESTION_COUNT, questionCount != null ? questionCount : 0);
                collectionArray.add(result);
            });

            assessments.stream()
                .forEach(collection -> ownerIdList.add(collection.getString(AJEntityCollection.OWNER_ID)));
        }

        JsonObject responseBody = new JsonObject();
        responseBody.put(CommonConstants.RESP_JSON_KEY_ASSESSMENTS, collectionArray);
        responseBody.put(CommonConstants.RESP_JSON_KEY_OWNER_DETAILS,
            FetchUserDeatailsHelper.getOwnerDemographics(ownerIdList));
        return responseBody;
    }

    private static JsonObject fetchResourcesDetails(List<String> contentIds) {
        LazyList<AJEntityOriginalResource> resourceList = AJEntityOriginalResource
            .findBySQL(AJEntityOriginalResource.SELECT_RESOURCES, CommonUtils.toPostgresArrayString(contentIds));
        JsonArray resourceArray = new JsonArray();
        Set<String> ownerIdList = new HashSet<>();
        if (!resourceList.isEmpty()) {
            resourceList.stream()
                .forEach(resource -> ownerIdList.add(resource.getString(AJEntityOriginalResource.CREATOR_ID)));
            String strNull = null;
            resourceList.stream().forEach(resource -> {
                JsonObject resourceJson = new JsonObject(new JsonFormatterBuilder()
                    .buildSimpleJsonFormatter(false, AJEntityOriginalResource.RESOURCE_LIST).toJson(resource));
                resourceJson.put(AJEntityOriginalResource.CONTENT_FORMAT,
                    AJEntityOriginalResource.RESOURCE_CONTENT_FORMAT);
                resourceJson.put(AJEntityOriginalResource.ORIGINAL_CREATOR_ID, strNull);
                resourceArray.add(resourceJson);
            });
        }

        JsonObject responseBody = new JsonObject();
        responseBody.put(CommonConstants.RESP_JSON_KEY_RESOURCES, resourceArray);
        responseBody.put(CommonConstants.RESP_JSON_KEY_OWNER_DETAILS,
            FetchUserDeatailsHelper.getOwnerDemographics(ownerIdList));
        return responseBody;
    }

    private static JsonObject fetchQuestionsDetails(List<String> contentIds) {
        LazyList<AJEntityContent> questionList =
            AJEntityContent.findBySQL(AJEntityContent.SELECT_QUESTIONS, CommonUtils.toPostgresArrayString(contentIds));
        JsonArray questionArray = new JsonArray();
        Set<String> ownerIdList = new HashSet<>();
        if (!questionList.isEmpty()) {
            List<String> creatorIdList = new ArrayList<>();
            questionList.stream()
                .forEach(question -> creatorIdList.add(question.getString(AJEntityContent.CREATOR_ID)));

            List<String> assessmentIdList = new ArrayList<>();
            questionList.stream().filter(question -> question.getString(AJEntityContent.COLLECTION_ID) != null)
                .forEach(question -> assessmentIdList.add(question.getString(AJEntityContent.COLLECTION_ID)));
            LOGGER.debug("number of assessment found {}", assessmentIdList.size());

            LazyList<AJEntityCollection> assessmentList = AJEntityCollection.findBySQL(
                AJEntityCollection.SELECT_ASSESSMENT_FOR_QUESTION, CommonUtils.toPostgresArrayString(assessmentIdList));
            Map<String, AJEntityCollection> assessmentMap = new HashMap<>();
            assessmentList.stream()
                .forEach(assessment -> assessmentMap.put(assessment.getString(AJEntityCollection.ID), assessment));
            LOGGER.debug("assessment fetched from DB are {}", assessmentMap.size());

            questionList.stream().forEach(question -> {
                JsonObject result = new JsonObject(new JsonFormatterBuilder()
                    .buildSimpleJsonFormatter(false, AJEntityContent.QUESTION_LIST).toJson(question));
                String assessmentId = question.getString(AJEntityContent.COLLECTION_ID);
                if (assessmentId != null && !assessmentId.isEmpty()) {
                    AJEntityCollection assessment = assessmentMap.get(assessmentId);
                    result.put(CommonConstants.RESP_JSON_KEY_ASSESSMENT,
                        new JsonObject(new JsonFormatterBuilder()
                            .buildSimpleJsonFormatter(false, AJEntityCollection.ASSESSMENT_FIELDS_FOR_QUESTION)
                            .toJson(assessment)));
                }
                questionArray.add(result);
            });

            questionList.stream().forEach(question -> ownerIdList.add(question.getString(AJEntityContent.CREATOR_ID)));
        }

        JsonObject responseBody = new JsonObject();
        responseBody.put(CommonConstants.RESP_JSON_KEY_QUESTIONS, questionArray);
        responseBody.put(CommonConstants.RESP_JSON_KEY_OWNER_DETAILS,
            FetchUserDeatailsHelper.getOwnerDemographics(ownerIdList));
        return responseBody;
    }

    private static JsonObject fetchRubricsDetails(List<String> contentIds) {
        LazyList<AJEntityRubric> rubricList =
            AJEntityRubric.findBySQL(AJEntityRubric.SELECT_RUBRICS, CommonUtils.toPostgresArrayString(contentIds));
        JsonArray rubricArray = new JsonArray();
        Set<String> ownerIdList = new HashSet<>();
        if (!rubricList.isEmpty()) {
            rubricList.stream().forEach(rubric -> ownerIdList.add(rubric.getString(AJEntityRubric.CREATOR_ID)));

            rubricList.stream().forEach(rubric -> {
                JsonObject result = new JsonObject(new JsonFormatterBuilder()
                    .buildSimpleJsonFormatter(false, AJEntityRubric.RUBRIC_LIST).toJson(rubric));
                rubricArray.add(result);
            });
        }

        JsonObject responseBody = new JsonObject();
        responseBody.put(CommonConstants.RESP_JSON_KEY_RUBRICS, rubricArray);
        responseBody.put(CommonConstants.RESP_JSON_KEY_OWNER_DETAILS,
            FetchUserDeatailsHelper.getOwnerDemographics(ownerIdList));
        return responseBody;
    }
}
