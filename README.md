# fred
Utility for indexing Fred GDP data into MongoDB

## How to run?


You must have Maven 3 and MongoDb running at localhost:27017:
1. Inside direcotry:

```$ mvn clean install```

2. For indexing:

```$ ./script/run-java.sh solution.cmd.FredGdpCmd -f "index" -d1 "2012-10-01" -d2 "2015-10-01"```

3. For Retrieving a specific date from Database:

```$ ./run-java.sh solution.cmd.FredGdpCmd -f "retrieve" -d1 "2014-04-01"```

output:
```
=============== Results ==============
Year: 2014-04-01 --> GDP: 17425.765625Year: 2014-04-01 --> GDP: 17425.765625
=============== End ==============

```

## Design

This project is meant to be a database abstraction layer for a general use case of calling APIs and storing in a database server. 
I have tried to encapsulate the concepts of ***connector, database, caller, and entry(Type of data stored in DB)*** as much as possible.
The module, _api_, is what should be implemented for future additions to the code. For example,
* Adding a new server with different technology (PostgreSQL):
One must implement a `Connector.java` and create the `GdpDatabase` by passing that connector instead of `MongoConnector.java`.
* Adding a new Database:
By defining they type of data by implementing `Entry.java` and extending a new `Database<NewType>`, one can have a connection to 
a new Database on a remote server.
* Getting different data from different APIs:
One can implment `Caller.java` to make proper calls using Appache `HttpClient` to different endpoints.

As said, the design is meant to separate the server side and the client side by defining connections rather than explicit merge of
database instances and server technology. (This happened through abstraction of each Database carrying a `Connector` that could have any technology.)

For support/help/questions, contact hossein.chaghazardi@gmail.com
