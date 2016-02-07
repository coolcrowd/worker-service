package edu.kit.ipd.crowdcontrol.workerservice;

import edu.kit.ipd.crowdcontrol.workerservice.database.DatabaseManager;
import edu.kit.ipd.crowdcontrol.workerservice.command.Commands;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.*;
import edu.kit.ipd.crowdcontrol.workerservice.objectservice.Communication;
import edu.kit.ipd.crowdcontrol.workerservice.query.Queries;
import org.jooq.SQLDialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.NamingException;
import java.io.File;
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
    static {
        if (System.getProperty("logback.configurationFile") == null) {
            System.setProperty("logback.configurationFile", "./conf/logging.xml");
        }
    }
    private static Logger logger = LoggerFactory.getLogger(Main.class);
    private final Properties file;
    private final Properties system;
    private final boolean testing;

    public Main(boolean testing) {
        this.testing = testing;
        String configLocation = System.getProperty("workerservice.config");
        if (configLocation == null) {
            configLocation = "./conf/configuration.properties";
        }
        file = new Properties();
        try {
            try {
                file.load(new FileInputStream(configLocation));
            } catch (FileNotFoundException e) {
                //used for testing
                try {
                    file.load(Main.class.getResourceAsStream(configLocation));
                } catch (NullPointerException ignored) {
                    //just means it's also not in the jar
                }
            }
        } catch (IOException e) {
            logger.error("unable to find file {}", new File(configLocation).getAbsolutePath());
            System.exit(-1);
        }
        system = System.getProperties();
    }

    public static void main(String[] args) {
        //used for testing
        boolean testing = false;
        if (args.length == 1) {
            testing = Boolean.valueOf(args[0]);
        }
        Main main = new Main(testing);
        main.boot();
    }

    /**
     * initializes and starts the worker-service.
     */
    public void boot() {
        String url = getProperty("database.url");
        String username = getProperty("database.username");
        String password = getProperty("database.password");
        String databasePool = getProperty("database.poolName");

        SQLDialect dialect = SQLDialect.valueOf(getProperty("database.dialect").trim());
        DatabaseManager databaseManager = null;
        try {
            databaseManager = new DatabaseManager(username, password, url, databasePool, dialect, testing);
        } catch (NamingException | SQLException e) {
            if (testing) {
                throw new RuntimeException(e);
            } else {
                logger.error("unable to establish database connection", e);
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
                getProperty("os.url"),
                getProperty("os.username"),
                getProperty("os.password")
        );
        Queries queries = new Queries(calibrationsOperations, experimentOperations, platformOperations, communication,
                taskOperations, workerOperations, testing);

        String portRaw = getProperty("router.port");

        int port = portRaw != null
                ? Integer.parseInt(portRaw)
                : 4567;

        logger.debug("workerservice is using port {}", port);

        Commands commands = new Commands(communication, experimentOperations);
        Router router = new Router(queries, commands, port);
        if (!testing) {
            router.init();
        }
        logger.debug("router initialized");
    }

    /**
     * returns the system-property if present and if not falls back to the
     * config file
     * @param key the key to search for
     * @return the String
     */
    private String getProperty(String key) {
        String sysProperty = trimIfNotNull(system.getProperty(key));
        if (sysProperty == null) {
            return trimIfNotNull(file.getProperty(key));
        } else {
            return sysProperty;
        }
    }

    /**
     * returns the trimmed string if the string is not null
     * @param s the string to trim
     * @return the trimmed string or null
     */
    private String trimIfNotNull(String s) {
        if (s != null)
            return s.trim();
        else
            return s;
    }
}
