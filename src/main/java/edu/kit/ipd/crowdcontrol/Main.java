package edu.kit.ipd.crowdcontrol;

import edu.kit.ipd.crowdcontrol.workerservice.database.DatabaseManager;
import edu.kit.ipd.crowdcontrol.workerservice.Router;
import edu.kit.ipd.crowdcontrol.workerservice.command.Commands;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.*;
import edu.kit.ipd.crowdcontrol.workerservice.objectservice.Communication;
import edu.kit.ipd.crowdcontrol.workerservice.query.Queries;
import org.jooq.SQLDialect;

import javax.naming.NamingException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.function.Function;

/**
 * the main class is responsible for initialising everything.
 * @author LeanderK
 * @version 1.0
 */
public class Main {
    public static void main(String[] args) {
        String propertyFileLocation = args[0];
        //used for testing
        boolean testing = false;
        if (args.length > 1) {
            testing = Boolean.valueOf(args[1]);
        }
        Properties config = new Properties();
        try {
            try {
                config.load(new FileInputStream(propertyFileLocation));
            } catch (FileNotFoundException e) {
                //used for testing
                config.load(DatabaseManager.class.getResourceAsStream(propertyFileLocation));
            }
        } catch (IOException e) {
            System.err.println("unable to find file: " + config);
            System.exit(-1);
        }
        Function<String, String> trimIfNotNull = s -> {
            if (s != null)
                return s.trim();
            else
                return s;
        };
        String url = trimIfNotNull.apply(config.getProperty("database.url"));
        String username = trimIfNotNull.apply(config.getProperty("database.username"));
        String password = trimIfNotNull.apply(config.getProperty("database.password"));
        String databasePool = trimIfNotNull.apply(config.getProperty("database.poolName"));

        SQLDialect dialect = SQLDialect.valueOf(config.getProperty("database.dialect").trim());
        DatabaseManager databaseManager = null;
        try {
            databaseManager = new DatabaseManager(username, password, url, databasePool, dialect, testing);
        } catch (NamingException | SQLException e) {
            if (testing) {
                throw new RuntimeException(e);
            } else {
                System.err.println("unable to establish database connection");
                e.printStackTrace();
                System.exit(-1);
            }
        }

        if (!testing) {
            databaseManager.initDatabase();
        }
        CalibrationsOperations calibrationsOperations = new CalibrationsOperations(databaseManager.getContext());
        ExperimentOperations experimentOperations = new ExperimentOperations(databaseManager.getContext());
        PlatformOperations platformOperations = new PlatformOperations(databaseManager.getContext());
        TaskOperations taskOperations = new TaskOperations(databaseManager.getContext());
        WorkerOperations workerOperations = new WorkerOperations(databaseManager.getContext());

        Communication communication = new Communication(
                config.getProperty("os_url"),
                config.getProperty("os_username"),
                config.getProperty("os_password")
        );
        Queries queries = new Queries(calibrationsOperations, experimentOperations, platformOperations, communication,
                taskOperations, workerOperations, testing);
        Commands commands = new Commands(communication, experimentOperations);
        Router router = new Router(queries, commands);
        if (!testing) {
            router.init();
        }
    }
}
