package solutions.api.db;


import java.util.List;

/**
 * Base interface for any connector to the Database server. This project contains the implementation of MongoDB, but the users can
 * implement their own connector with the proper Java Bindings (SQL, PostGres), etc..
 */
public interface Connector {

    /***
     * Default POST for posting to server
     */
    default void post() { }

    /**
     * Default GET for retrieving from server.
     * @param request
     * @return
     * @throws Exception
     */
    Iterable<?> get(Object request) throws Exception;

    /***
     * Default PUT for creating and index
     * @param objects
     */
    void put(List<?> objects);

    /**
     * Default DELETE for dropping an index
     */
    default void delete() { };

}
