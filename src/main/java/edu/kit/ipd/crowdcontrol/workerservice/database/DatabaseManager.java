package edu.kit.ipd.crowdcontrol.workerservice.database;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.Tables;
import org.apache.commons.io.IOUtils;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

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
     *
     * @param userName the username for the database
     * @param password the password for the database
     * @param url the url to the database
     * @param providedDBPoolName if not null, it will use the built in Connection pool with the passed Name
     * @param sqlDialect the dialect to use
     * @param disableConnection true to disable the connection, used for testing
     * @throws NamingException if there was a problem establishing a connection to the provided database-pool
     * @throws SQLException if there was a problem establishing a connection to the database
     */
    public DatabaseManager(String userName, String password, String url, String providedDBPoolName, SQLDialect sqlDialect,
                           boolean disableConnection) throws NamingException, SQLException {
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
        if (!disableConnection) {
            this.connection = ds.getConnection();
            context = DSL.using(this.ds, sqlDialect);
        } else {
            this.connection = null;
            context = DSL.using(sqlDialect);
        }
    }

    /**
     * initializes the database if not already initialized.
     */
    public void initDatabase() {
        try (InputStream in = DatabaseManager.class.getResourceAsStream("/db.sql")) {
            String initScript = IOUtils.toString(in, "UTF-8");
            if (Boolean.getBoolean("dropSchema")) {
                String drop = "DROP DATABASE `crowdcontrol`;";
                context.fetch(drop);
            }
            try {
                context.selectFrom(Tables.EXPERIMENT).fetchAny();
            } catch (DataAccessException e) {
                //TODO: need better idea, but meta() and systable are not working
                String tables = initScript.substring(0, initScript.indexOf("DELIMITER $$"));
                context.execute(tables);
                String delimiter = "DELIMITER $$";
                String trigger = initScript.substring(initScript.indexOf(delimiter) + delimiter.length(), initScript.length());
                ScriptRunner scriptRunner = new ScriptRunner(connection);
                scriptRunner.setDelimiter("$$");
                scriptRunner.runScript(new StringReader(trigger));

            }
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
