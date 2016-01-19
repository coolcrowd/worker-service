package edu.kit.ipd.crowdcontrol;

import edu.kit.ipd.crowdcontrol.workerservice.Router;
import edu.kit.ipd.crowdcontrol.workerservice.command.Commands;
import edu.kit.ipd.crowdcontrol.workerservice.database.DatabaseManager;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.ExperimentOperations;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.PlatformOperations;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.PopulationsOperations;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.TaskOperations;
import edu.kit.ipd.crowdcontrol.workerservice.objectservice.Communication;
import edu.kit.ipd.crowdcontrol.workerservice.query.Query;
import org.jooq.SQLDialect;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

/**
 * the main class is responsible for initialising everything.
 * @author LeanderK
 * @version 1.0
 */
public class Main {
    public static void main(String[] args) {
        String propertyFileLocation = args[0];
        Properties config = new Properties();
        try {
            config.load(new FileInputStream(propertyFileLocation));
        } catch (IOException e) {
            System.err.println("unable to find file: " + config);
            System.exit(-1);
        }
        DatabaseManager databaseManager = null;
        try {
            databaseManager = new DatabaseManager(
                    config.getProperty("database_username"),
                    config.getProperty("database_password"),
                    config.getProperty("database_url"),
                    SQLDialect.valueOf(config.getProperty("database_dialect"))
            );
        } catch (SQLException e) {
            System.err.println("unable to find set up DatabaseManager");
            e.printStackTrace();
            System.exit(-1);
        }
        PopulationsOperations populationsOperations = new PopulationsOperations(databaseManager.getContext());
        ExperimentOperations experimentOperations = new ExperimentOperations(databaseManager.getContext());
        PlatformOperations platformOperations = new PlatformOperations(databaseManager.getContext());
        TaskOperations taskOperations = new TaskOperations(databaseManager.getContext());

        Communication communication = new Communication(
                config.getProperty("os_url"),
                config.getProperty("os_username"),
                config.getProperty("os_password")
        );
        Query query = new Query(populationsOperations, experimentOperations, platformOperations, communication, taskOperations);
        Commands commands = new Commands(communication, experimentOperations);
        Router router = new Router(query, commands);
        router.init();
    }
}
