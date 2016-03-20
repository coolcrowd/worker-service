package edu.kit.ipd.crowdcontrol.workerservice.database;

import com.zaxxer.hikari.HikariDataSource;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.Tables;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.DatabaseVersionRecord;
import org.apache.commons.io.IOUtils;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.jooq.DSLContext;
import org.jooq.Record1;
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
import java.sql.Timestamp;
import java.time.LocalDateTime;


import static edu.kit.ipd.crowdcontrol.workerservice.database.model.Tables.*;

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
    private final int currentVersion = 3;

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
        if (providedDBPoolName != null && !providedDBPoolName.isEmpty()) {
            ds = (DataSource) new InitialContext().lookup(providedDBPoolName);
        } else {
            HikariDataSource hds = new HikariDataSource();
            hds.setJdbcUrl(url);
            hds.setUsername(userName);
            hds.setPassword(password);
            ds = hds;
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
     * Initializes the database if not already initialized.
     * @throws SQLException if there was a problem establishing a connection to the database
     * @throws IllegalStateException if the version does not match the current version
     */
    public void initDatabase() throws SQLException, IllegalStateException {
        createSchemaIfNotExisting();
        checkDBVersion();
    }

    /**
     * Checks whether the db matches the current version and if not throws an exception.
     * @throws IllegalStateException if the version does not match the current version
     */
    private void checkDBVersion() throws IllegalStateException {
        Integer dbVersion = context.select(DATABASE_VERSION.VERSION)
                .from(DATABASE_VERSION)
                .orderBy(DATABASE_VERSION.TIMESTAMP.desc())
                .limit(1)
                .fetchOptional()
                .map(Record1::value1)
                .orElseGet(() -> {
                    context.executeInsert(new DatabaseVersionRecord(null, currentVersion, Timestamp.valueOf(LocalDateTime.now())));
                    return currentVersion;
                });
        if (dbVersion != currentVersion) {
            throw new IllegalStateException(String.format(
                    "Database Version is %d but the worker-service expects %d",
                    dbVersion,
                    currentVersion
            ));
        }
    }

    /**
     * Creates the db-schema if not already existing.
     * @throws SQLException if there was a problem establishing a connection to the database
     */
    private void createSchemaIfNotExisting() throws SQLException {
        try (InputStream in = DatabaseManager.class.getResourceAsStream("/db.sql")) {
            String initScript = IOUtils.toString(in, "UTF-8");
            if (Boolean.getBoolean("dropSchema")) {
                String drop = "DROP DATABASE `crowdcontrol`;";
                context.execute(drop);
            }
            try {
                context.selectFrom(Tables.EXPERIMENT).fetchAny();
            } catch (DataAccessException e) {
                //TODO: need better idea, but meta() and systable are not working
                String tables = initScript.substring(0, initScript.indexOf("DELIMITER $$"));
                ScriptRunner scriptRunner = new ScriptRunner(ds.getConnection());
                scriptRunner.setLogWriter(null);
                scriptRunner.setDelimiter(";");
                scriptRunner.runScript(new StringReader(tables));
                String delimiter = "DELIMITER $$";
                String trigger = initScript.substring(initScript.indexOf(delimiter) + delimiter.length(), initScript.lastIndexOf("DELIMITER ;"));
                scriptRunner = new ScriptRunner(ds.getConnection());
                scriptRunner.setLogWriter(null);
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
