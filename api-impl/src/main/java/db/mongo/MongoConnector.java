package db.mongo;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import solutions.api.db.Connector;
import utils.MongoObjectConvertor;

import java.util.List;


/***
 * {@link MongoConnector} is the implementation of {@link Connector} for carrying out the duty of transfering data to/from a database
 */
public class MongoConnector implements Connector {
    private MongoClient client;
    private MongoDatabase database;
    public MongoCollection<Document> collection;

    public MongoConnector(String host, int port, String databaseName, String collectionName) {
        this.client = new MongoClient(host , port);
        this.database = this.client.getDatabase(databaseName);
        this.collection = this.database.getCollection(collectionName);
    }

    @Override
    public Iterable<Document> get(Object request) throws Exception {
        Document query = MongoObjectConvertor.convert(request);
        if (query == null) {  // conversion could not happen due to unsupported conversion type
            throw new Exception("Conversion of request to Mongo Object failed. Make sure to implement the converion of " +
                    "requested object to Mongo document");
        }
        return collection.find(query);
    }

    @Override
    public void put(List<?> objects) {

        //----Converting objects to Mongo Document Objects----
        List<Document> mongoDocuments = MongoObjectConvertor.convert(objects);
        if (objects.size() > 0 && mongoDocuments.size() == 0) {
            // conversion could not happen due to unsupported conversion type
            return;
        }

        //----Inserting----
        collection.insertMany(mongoDocuments);
    }

}
