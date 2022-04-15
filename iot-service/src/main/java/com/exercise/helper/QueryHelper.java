package com.exercise.helper;

import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class QueryHelper {

    public static final String RETRIEVE_DATA_FOR_DEVICE_QUERY = "{ aggregate: '#deviceId#', pipeline: [{$match: {\n" +
            " timestamp: {\n" +
            "  $gte: '#fromDate#',\n" +
            "  $lt: '#toDate#'\n" +
            " }\n" +
            "}}, {$group: {\n" +
            " _id: {\n" +
            "  deviceId: '$deviceId',\n" +
            "  longitude: '$longitude',\n" +
            "  latitude: '$latitude'\n" +
            " },\n" +
            " data: {\n" +
            "  $push: {\n" +
            "   $mergeObjects: [\n" +
            "    '$data',\n" +
            "    {\n" +
            "     timestamp: '$timestamp'\n" +
            "    }\n" +
            "   ]\n" +
            "  }\n" +
            " }\n" +
            "}}, {$project: {\n" +
            " _id: 0,\n" +
            " deviceId: '$_id.deviceId',\n" +
            " longitude: '$_id.longitude',\n" +
            " latitude: '$_id.latitude',\n" +
            " data: '$data'\n" +
            "}}], cursor: { batchSize: 1 }}";

    /**
     * Return a Mongo aggregation query for given criteria
     * @param deviceId
     * @param fromDate
     * @param toDate
     * @return
     */
    public String buildGetDeviceDataQuery(String deviceId, String fromDate, String toDate) {
        // TODO: Not efficient to replace string like this
        String query = RETRIEVE_DATA_FOR_DEVICE_QUERY.replaceAll("#deviceId#", deviceId)
                .replaceAll("#fromDate#", fromDate)
                .replaceAll("#toDate#", toDate);
        log.debug("Generated query {}", query);
        return query;
    }

    /**
     * Get Json result
     * @param document
     * @return
     */
    public String getJsonResult(Document document) {
        String json = "{}";
        document = document.get("cursor", Document.class);
        List<Document> results = (List<Document>)document.get("firstBatch");
        if (results != null && results.size() > 0) {
            document = results.get(0);
            json = document.toJson();
        }
        return json;
    }
}
