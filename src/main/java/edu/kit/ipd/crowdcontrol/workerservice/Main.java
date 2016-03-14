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
import java.time.LocalDateTime;
import java.util.Properties;

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

        // Disable jOOQ's self-advertising
        // http://stackoverflow.com/a/28283538/2373138
        System.setProperty("org.jooq.no-logo", "true");
    }

    private static Logger logger = LoggerFactory.getLogger(Main.class);
    private final Properties file;
    private final Properties system;
    private final boolean testing;

    public Main(boolean testing) {
        this.testing = testing;
        String configLocation = System.getProperty("workerservice.config");
        logger.debug("Initial config-location: {}", configLocation);
        if (configLocation == null) {
            configLocation = "./conf/configuration.properties";
        }
        logger.debug("Actual config-location: {}", configLocation);
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

        SQLDialect dialect = null;
        try {
            dialect = SQLDialect.valueOf(getProperty("database.dialect").trim());
        } catch (NullPointerException e) {
            logger.error("database.dialect not set");
            System.exit(-1);
        }
        DatabaseManager databaseManager = null;
        try {
            databaseManager = new DatabaseManager(username, password, url, databasePool, dialect, testing);
        } catch (NamingException | SQLException e) {
            if (testing) {
                throw new RuntimeException(e);
            } else {
                logger.error("unable to establish database connection", e);
                System.exit(-1);
            }
        }

        Communication communication = new Communication(
                getProperty("os.url"),
                getProperty("os.username"),
                getProperty("os.password")
        );

        boolean result = waitOnObjectService(communication);
        if (!result) {
            logger.error("object-service appears to be offline");
            System.exit(-1);
        }

        if (!testing) {
            try {
                databaseManager.initDatabase();
            } catch (SQLException e) {
                logger.error("Unable to establish database connection.", e);
                System.exit(-1);
            }
        }
        JWTHelper jwtHelper;
        try {
            jwtHelper = new JWTHelper(getProperty("jwt.secret"));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("jwt.secret must be set in the config file");
        }

        boolean cachingEnabled = true;
        if ("false".equals(getProperty("caching.enabled"))) {
            cachingEnabled = false;
        }
        CalibrationsOperations calibrationsOperations = new CalibrationsOperations(databaseManager.getContext(), cachingEnabled);
        ExperimentOperations experimentOperations = new ExperimentOperations(databaseManager.getContext(), cachingEnabled);
        PlatformOperations platformOperations = new PlatformOperations(databaseManager.getContext(), cachingEnabled);
        ExperimentsPlatformOperations experimentsPlatformOperations = new ExperimentsPlatformOperations(databaseManager.getContext(), cachingEnabled);
        WorkerOperations workerOperations = new WorkerOperations(databaseManager.getContext(), cachingEnabled);

        Queries queries = new Queries(calibrationsOperations, experimentOperations, platformOperations, communication,
                experimentsPlatformOperations, workerOperations, testing, jwtHelper);

        String portRaw = getProperty("router.port");

        int port = portRaw != null
                ? Integer.parseInt(portRaw)
                : 4567;

        logger.debug("workerservice is using port {}", port);

        Commands commands = new Commands(communication, experimentOperations, jwtHelper);
        RatpackRouter router = new RatpackRouter(queries, commands, jwtHelper, port);
        if (!testing) {
            try {
                router.init();
            } catch (Exception e) {
                logger.error("an error occured while trying to initialize the router", e);
            }
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
            return trimIfNotNull(sysProperty);
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

    /**
     * waits on the Object-Service to be available
     * @param communication the communication to use to communicate with the object-service
     * @return true if successful, false if not
     */
    private boolean waitOnObjectService(Communication communication) {
        String property = getProperty("os.wait");
        if (property != null && property.equals("true")) {
            LocalDateTime limit = LocalDateTime.now().plusSeconds(60);
            while (limit.isAfter(LocalDateTime.now())) {
                if (communication.isObjectServiceRunning()) {
                    return true;
                } else {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        logger.debug("interrupted while waiting on os", e);
                    }
                }
            }
            return false;
        }
        return true;
    }
}
