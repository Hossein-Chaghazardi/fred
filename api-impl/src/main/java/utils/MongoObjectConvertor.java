package utils;

import org.bson.Document;
import gdp.GdpFredEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * A static factory for conversion between Database and Server Objects.
 */
public class MongoObjectConvertor {

    private MongoObjectConvertor() {

    }

    public static Document convert(Object object) {
        if (object.getClass() == GdpFredEntry.class) {
            GdpFredEntry entry = (GdpFredEntry) object;
            if (entry.getValue() != 0) { // indexing phase
                return new Document("date", entry.getDate())
                        .append("value", entry.getValue());
            } else {
                return new Document("date", entry.getDate()); // query phase
            }
        } else {
            return null;
        }
    }

    public static List<Document> convert(List<?> objects) {
        return objects.parallelStream()
                .map(MongoObjectConvertor::convert)
                .filter(document -> document != null)
                .collect(Collectors.toList());
    }

    public static List<GdpFredEntry> convert(Iterable<Document> documents) {
        List<GdpFredEntry> gdpFredEntries = new ArrayList<>();
        documents.forEach((document -> {
            String value = document.get("value").toString();
            gdpFredEntries.add(
                    new GdpFredEntry(document.getString("date"), Float.parseFloat(value))

            );}));

        return gdpFredEntries;
    }
}
