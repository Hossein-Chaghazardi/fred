package gdp;

import db.mongo.MongoConnector;
import org.bson.Document;
import solutions.api.db.Connector;
import solutions.api.db.Database;
import utils.MongoObjectConvertor;

import java.util.List;

public class GdpDatabase extends Database<GdpFredEntry> {
    private Connector connector;

    public GdpDatabase(Connector connector) {
        super();
        this.connector = connector;
    }


    @Override
    public Iterable<GdpFredEntry> read(Object request) throws Exception {
        if (connector.getClass() == MongoConnector.class) {
            return MongoObjectConvertor.convert((Iterable<Document>) connector.get(request));
        } else {
            throw new Exception("Unsupported Connector. Make sure to define the conversion from connector response to GdpFredEntry.");
        }
    }

    @Override
    public void write(List<GdpFredEntry> element) {
        connector.put(element);
    }
}
