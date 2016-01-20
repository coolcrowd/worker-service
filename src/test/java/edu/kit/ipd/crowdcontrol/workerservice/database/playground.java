package edu.kit.ipd.crowdcontrol.workerservice.database;

import edu.kit.ipd.crowdcontrol.workerservice.database.DatabaseManager;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.Tables;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.AnswerRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.RatingRecord;
import edu.kit.ipd.crowdcontrol.workerservice.database.operations.TaskOperations;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LeanderK
 * @version 1.0
 */
public class playground {

    @Test
    @Ignore
    public void test() {
        DatabaseManager databaseManager = null;
        try {
            databaseManager = new DatabaseManager("LeanderK", "y560", "jdbc:mysql://localhost:8888", SQLDialect.MYSQL, false);
        } catch (SQLException e) {
            System.err.println("unable to find set up DatabaseManager");
            e.printStackTrace();
            System.exit(-1);
        }
        TaskOperations taskOperations = new TaskOperations(databaseManager.getContext());
        DSLContext create = databaseManager.getContext();
        //List<AnswerRecord> answerRecords = prepareRating(create, 2, 2, 3, 1);
        //List<AnswerRecord> answerRecords = taskOperations.prepareRating(2, 1, 3);
        System.out.println(taskOperations);
    }
}
