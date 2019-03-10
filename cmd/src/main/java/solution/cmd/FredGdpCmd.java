package solution.cmd;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import db.mongo.MongoConnector;
import gdp.FredCaller;
import gdp.GdpDatabase;
import gdp.GdpFredEntry;
import solutions.api.db.Connector;
import solutions.api.db.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.cli.*;


/***
 * Command line utility for indexing and retrieving data from FRED into a Mongo Server. Mongo server has to be running on
 * localhost:27017
 */
public class FredGdpCmd {
    Connector mongoConnector = new MongoConnector("127.0.0.1", 27017, "FRED", "gdp");
    Database<GdpFredEntry> database = new GdpDatabase(mongoConnector);

    private void readAndReturn(String date) {

        Iterable<GdpFredEntry> responses = new ArrayList<>();
        try {
            responses =  database.read(new GdpFredEntry(date));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        //---- Printing to Console----
        System.out.println("=============== Results ==============");
        for (GdpFredEntry response : responses) {
            System.out.print(String.format("Year: %s --> GDP: %f", response.getDate(), response.getValue()));
        }
        System.out.println("\n=============== End ==============");
    }


    private void getAndIndex(String startDate, String endDate) {

        //----Getting data from API----
        FredCaller caller = new FredCaller();
        String response = null;
        try {
            response = caller.collectGdpFromAPI(startDate, endDate);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }


        //----Indexing Data into Mongo----
        database.write(response2Fred(response));
    }

    private List<GdpFredEntry> response2Fred(String jsonString) {
        Map<String, Object> retMap = new Gson().fromJson(
                jsonString, new TypeToken<HashMap<String, Object>>() {}.getType()
        );
        // extracting entries
        List<GdpFredEntry> entries = new ArrayList<>();
        for (Map<String, String> observation : (List<Map<String,String>>) retMap.get("observations")) {
            entries.add(new GdpFredEntry(observation.get("date"), Float.parseFloat(observation.get("value"))));
        }
        return entries;
    }

    public static void main(String[] args) throws Exception {
        Options options = new Options();
        Option function = new Option("f", "function", true, "Specify the function. Choose \"retrieve\" for collection form the Mongo index, or \n" +
                "\"index\" for calling the FRED API and storing in Mongo.");
        function.setRequired(true);
        options.addOption(function);

        Option date1 = new Option("d1", "date1", true, "Date from when data should be recorded/Date at which the data should be read from database.");
        date1.setRequired(true);
        options.addOption(date1);

        Option date2 = new Option("d2", "date2", true, "The end date of data observation from API.");
        date2.setRequired(false);
        options.addOption(date2);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }

        if (!"retrieve".equals(cmd.getOptionValue("function")) && !"index".equals(cmd.getOptionValue("function"))) {
            System.out.println("No valid function has been provided! valid are \"retrieve\" or \" index\" ");
            System.exit(1);
        } else if (cmd.getOptionValue("function") == "retrieve" && cmd.getOptionValue("date2") != null) {
            System.out.println("In retrieve mode d2 should not be provided");
            System.exit(1);
        } else if (cmd.getOptionValue("function") == "index" &&
                (cmd.getOptionValue("date1") == null || cmd.getOptionValue("date2") == null)) {
            System.out.println("In index mode, two dates have to be provided.");
            System.exit(1);
        }

        FredGdpCmd fredGdpCmd = new FredGdpCmd();
        if ("index".equals(cmd.getOptionValue("function"))) {
            fredGdpCmd.getAndIndex(cmd.getOptionValue("date1"), cmd.getOptionValue("date2"));
        } else {
            fredGdpCmd.readAndReturn(cmd.getOptionValue("date1"));
        }
    }
}
