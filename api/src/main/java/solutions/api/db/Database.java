package solutions.api.db;

import java.util.List;

/**
 * Default abstract class for implementing any datastore (Database).
 * @param <T>
 */
public abstract class Database<T> {

    /***
     * In order to make any database compatible with any server technology, we have to allocate a connector that is able
     * to connect database and manage the communications between database and the server.
     */
    private Connector connector;

    public Database() { }

    public Database(Connector connector) {
        this.connector = connector;
    }

    /**
     * Method for reading from database
     * @param request request Object
     * @return
     * @throws Exception
     */
    public Iterable<T> read (Object request) throws Exception {
        return null;
    }

    /**
     * Method for writing a list of elements to database
     * @param elements
     */
    public void write(List<T> elements) {

    }
}
