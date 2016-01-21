package edu.kit.ipd.crowdcontrol.objectservice.database;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Initializes and holds the connection to the database and eventually the database itself.
 * When the system-property dropSchema is set to true, if will drop the schema first.
 * @author LeanderK
 * @version 1.0
 */
public class DatabaseManager {
    private final DSLContext context;
    private final String url;
    private final DataSource ds;
    private final Connection connection;

    /**
     * creates new DatabaseManager.
     * @param providedDBPoolName the name of the provided ConnectionPool
     * @param sqlDialect the dialect to use
     * @throws NamingException if there was a problem establishing a connection to the provided database-pool
     * @throws SQLException if there was a problem establishing a connection to the database
     */
    public DatabaseManager(String providedDBPoolName, SQLDialect sqlDialect) throws NamingException, SQLException {
        this(null, null, null, Objects.requireNonNull(providedDBPoolName), sqlDialect);
    }

    /**
     * creates new DatabaseManager.
     * <p>
     * if the system-property 'providedDBPool' is set, it will use the ConnectionPool provided by the application-server
     * instead of creating an own connection-pool.
     * @param userName the username for the database
     * @param password the password for the database
     * @param url the url to the database
     * @param sqlDialect the dialect to use
     * @throws NamingException if there was a problem establishing a connection to the provided database-pool
     * @throws SQLException if there was a problem establishing a connection to the database
     */
    public DatabaseManager(String userName, String password, String url, SQLDialect sqlDialect) throws NamingException, SQLException {
        this(userName, password, url, System.getProperty("providedDBPool"), sqlDialect);
    }

    /**
     * creates new DatabaseManager.
     *
     * @param userName the username for the database
     * @param password the password for the database
     * @param url the url to the database
     * @param providedDBPoolName if not null, it will use the built in Connection pool with the passed Name
     * @param sqlDialect the dialect to use
     * @throws NamingException if there was a problem establishing a connection to the provided database-pool
     * @throws SQLException if there was a problem establishing a connection to the database
     */
    public DatabaseManager(String userName, String password, String url, String providedDBPoolName, SQLDialect sqlDialect) throws NamingException, SQLException {
        this.url = url;
        DataSource ds = null;
        if (providedDBPoolName != null) {
            ds = (DataSource) new InitialContext().lookup(providedDBPoolName);
        } else {
            ComboPooledDataSource cpds = new ComboPooledDataSource();
            cpds.setJdbcUrl(url);
            cpds.setUser(userName);
            cpds.setPassword(password);
            cpds.setMaxStatements(30);
            ds = cpds;
        }
        this.ds = ds;
        this.connection = ds.getConnection();
        context = DSL.using(this.ds, sqlDialect);
    }

    /**
     * initializes the database if not already initialized.
     */
    public void initDatabase() {
        try {
            String initScript = Files.lines(new File("db.sql").toPath()).collect(Collectors.joining());
            if (Boolean.getBoolean("dropSchema")) {
                String drop = "DROP DATABASE `crowdcontrol`;";
                context.fetch(drop);
            }
            context.fetch(initScript);
        } catch (IOException e) {
            System.err.println("unable to read database-init script");
            e.printStackTrace();
        }
    }

    /**
     * returns the Context used to communicate with the database
     * @return an instance of DSLContext
     */
    public DSLContext getContext() {
        return context;
    }

    /**
     * returns the Connection used to communicate with the database.
     * @return an instance of Connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * returns the URL to the database
     * @return the url as a String
     */
    public String getUrl() {
        return url;
    }
}
