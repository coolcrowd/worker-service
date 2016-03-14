package edu.kit.ipd.crowdcontrol.workerservice.database.operations;

import edu.kit.ipd.crowdcontrol.workerservice.database.model.enums.ExperimentsPlatformModeMode;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.RatingReservation;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.*;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.tools.jdbc.MockConnection;
import org.jooq.tools.jdbc.MockDataProvider;
import org.jooq.tools.jdbc.MockExecuteContext;
import org.jooq.tools.jdbc.MockResult;

import java.math.BigInteger;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static edu.kit.ipd.crowdcontrol.workerservice.database.model.Tables.*;

/**
 * @author LeanderK
 * @version 1.0
 */
public class MockProvider implements MockDataProvider {
    private final OperationsDataHolder dataHolder;
    private final DSLContext create;
    private final DSLContext mockCreate;

    public MockProvider(OperationsDataHolder dataHolder) {
        this.dataHolder = dataHolder;
        this.create = dataHolder.getCreate();
        MockConnection connection = new MockConnection(this) {
            @Override
            public Savepoint setSavepoint() throws SQLException {
                super.setSavepoint();
                return new MockSavepoint(OperationsDataHolder.nextRandomString());
            }

            @Override
            public Savepoint setSavepoint(String name) throws SQLException {
                super.setSavepoint(name);
                return new MockSavepoint(name);
            }
        };
        this.mockCreate = DSL.using(connection, SQLDialect.MYSQL);
    }

    @Override
    public MockResult[] execute(MockExecuteContext ctx) throws SQLException {
        // You might need a DSLContext to create org.jooq.Result and org.jooq.Record objects
        MockResult[] mock = new MockResult[1];

        // The execute context contains SQL string(s), bind values, and other meta-data
        String sqlRaw = ctx.sql();
        String sql = sqlRaw.toUpperCase();

        // You decide, whether any given statement returns results, and how many
        if (sql.startsWith("SELECT `CROWDCONTROL`.`EXPERIMENT`.`ID_EXPERIMENT`")) {
            Result<ExperimentRecord> result = create.newResult(EXPERIMENT);
            result.add(dataHolder.getExperimentRecord());
            mock[0] = new MockResult(1, result);
        } else if (sql.startsWith("SELECT 1 AS `ONE` FROM DUAL WHERE EXISTS (SELECT `CROWDCONTROL`.`ALGORITHM_TASK_CHOOSER_PARAM`.")) {
            Param<Integer> val = DSL.val("1", Integer.class);
            Result<Record1<Integer>> result = create.newResult(val);
            mock[0] = new MockResult(1, result);
        } else if (sql.startsWith("SELECT `CROWDCONTROL`.`CONSTRAINT`.`ID_CONSTRAINT`")) {
            Result<ConstraintRecord> result = create.newResult(CONSTRAINT);
            dataHolder.getConstraints().stream()
                    .map(constraint -> {
                        ConstraintRecord constraintRecord = create.newRecord(CONSTRAINT);
                        constraintRecord.setConstraint(constraint);
                        constraintRecord.setExperiment(dataHolder.getExperimentRecord().getIdExperiment());
                        constraintRecord.setIdConstraint(OperationsDataHolder.nextRandomInt());
                        return constraintRecord;
                    })
                    .forEach(result::add);
            mock[0] = new MockResult(1, result);
        } else if (sql.startsWith("SELECT `CROWDCONTROL`.`ALGORITHM_TASK_CHOOSER_PARAM`.`DATA`")) {
            Result<Record2<String, String>> result =
                    create.newResult(ALGORITHM_TASK_CHOOSER_PARAM.DATA, CHOSEN_TASK_CHOOSER_PARAM.VALUE);
            dataHolder.getTaskChooserParams().entrySet().stream()
                    .map(entry -> {
                        Record2<String, String> record =
                                create.newRecord(ALGORITHM_TASK_CHOOSER_PARAM.DATA, CHOSEN_TASK_CHOOSER_PARAM.VALUE);
                        record.setValue(ALGORITHM_TASK_CHOOSER_PARAM.DATA, entry.getKey());
                        record.setValue(CHOSEN_TASK_CHOOSER_PARAM.VALUE, entry.getValue());
                        return record;
                    })
                    .forEach(result::add);
            mock[0] = new MockResult(1, result);
        } else if (sql.startsWith("SELECT `CROWDCONTROL`.`PLATFORM`.`ID_PLATFORM`")) {
            Result<PlatformRecord> result = create.newResult(PLATFORM);
            result.add(dataHolder.getPlatformRecord());
            mock[0] = new MockResult(1, result);
        } else if (sql.startsWith("SELECT `CROWDCONTROL`.`CALIBRATION`.`ID_CALIBRATION`")) {
            ArrayList<Field<?>> fields = new ArrayList<>(Arrays.asList(CALIBRATION.fields()));
            fields.addAll(Arrays.asList(CALIBRATION_ANSWER_OPTION.fields()));
            Field<?>[] fieldsArr = fields.toArray(new Field<?>[fields.size()]);
            Result<Record> result = create.newResult(fieldsArr);
            dataHolder.getCalibrations().entrySet().stream()
                    .flatMap(entry -> entry.getValue().stream().map(answerOption -> {
                        Record record = create.newRecord(fieldsArr);
                        for (Field answerField : answerOption.fields()) {
                            for (Field calibrationField : entry.getKey().fields()) {
                                record.setValue(calibrationField, entry.getKey().getValue(calibrationField));
                            }
                            record.setValue(answerField, answerOption.getValue(answerField));
                        }
                        return record;
                    }))
                    .forEach(record -> result.add((Record) record));
            mock[0] = new MockResult(1, result);
        } else if (sql.startsWith("SELECT 1 AS `ONE` FROM DUAL WHERE EXISTS (SELECT `CROWDCONTROL`.`CALIBRATION_ANS")) {
            Param<Integer> val = DSL.val("1", Integer.class);
            Result<Record1<Integer>> result = create.newResult(val);
            if (dataHolder.belongsToWrongPopulation()) {
                Record1<Integer> record = create.newRecord(val);
                record.value1(1);
                result.add(record);
            }
            mock[0] = new MockResult(1, result);
        } else if (sql.startsWith("SELECT `CROWDCONTROL`.`EXPERIMENTS_PLATFORM_MODE`.`MODE` FROM `CROWDCONTROL`.")) {
            Result<Record1<ExperimentsPlatformModeMode>> result = create.newResult(EXPERIMENTS_PLATFORM_MODE.MODE);
            Record1<ExperimentsPlatformModeMode> record = create.newRecord(EXPERIMENTS_PLATFORM_MODE.MODE);
            record.value1(dataHolder.getExperimentsPlatformModeRecord().getMode());
            result.add(record);
            mock[0] = new MockResult(1, result);
        } else if (sql.startsWith("SELECT COUNT(*) AS `C` FROM (SELECT `CROWDCONTROL`.`ANSWER`.`ID_ANSWER`, ")) {
            if (sql.endsWith("UALITY_ASSURED` = ? AND `CROWDCONTROL`.`ANSWER`.`QUALITY` <> ?) OR ?)) AS `Q`") || ctx.bindings().length == 4) {
                Param<BigInteger> c = DSL.val("C", BigInteger.class);
                Result<Record1<BigInteger>> result = create.newResult(c);
                Record1<BigInteger> record = create.newRecord(c);
                record.value1(BigInteger.valueOf(dataHolder.getAnswerCountTotal()));
                result.add(record);
                mock[0] = new MockResult(1, result);
            }  else {
                Param<BigInteger> c = DSL.val("C", BigInteger.class);
                Result<Record1<BigInteger>> result = create.newResult(c);
                Record1<BigInteger> record = create.newRecord(c);
                record.value1(BigInteger.valueOf(dataHolder.getAnswerGiveCountWorker()));
                result.add(record);
                mock[0] = new MockResult(1, result);
            }
        } else if (sql.startsWith("SELECT COUNT(*) AS `C` FROM (SELECT `CROWDCONTROL`.`RATING`.`ID_RATING`, ")) {
            Param<BigInteger> c = DSL.val("C", BigInteger.class);
            Result<Record1<BigInteger>> result = create.newResult(c);
            Record1<BigInteger> record = create.newRecord(c);
            record.value1(BigInteger.valueOf(dataHolder.getRatingGivenCountWorker()));
            result.add(record);
            mock[0] = new MockResult(1, result);
        } else if (sql.endsWith("`RATING_RESERVATION`.`WORKER` = ? AND `CROWDCONTROL`.`RATING_RESERVATION`.`EXPERIMENT` = ?)")) {
            ArrayList<Field<?>> fields = new ArrayList<>(Arrays.asList(ANSWER.fields()));
            fields.addAll(Arrays.asList(RATING_RESERVATION.IDRESERVERD_RATING));
            Field<?>[] fieldsArr = fields.toArray(new Field<?>[fields.size()]);
            Result<Record> result = create.newResult(fieldsArr);
            mock[0] = new MockResult(1, result);
        } else if (sql.startsWith("SELECT `CROWDCONTROL`.`ANSWER`.`ID_ANSWER`, `CROWDCONTROL`.`ANSWER`.`EXPERIMENT`, ")) {
            ArrayList<Field<?>> fields = new ArrayList<>(Arrays.asList(ANSWER.fields()));
            Field<Integer> count = DSL.count(RATING_RESERVATION.IDRESERVERD_RATING).as("count");
            fields.add(count);
            Field<?>[] fieldsArr = fields.toArray(new Field<?>[fields.size()]);
            Result<Record> result = create.newResult(fieldsArr);
            for (int i = 0; i < dataHolder.getAnswerRecords().size(); i++) {
                AnswerRecord answerRecord = dataHolder.getAnswerRecords().get(i);
                Record record = create.newRecord(fieldsArr);
                for (Field field : answerRecord.fields()) {
                    record.setValue(field, answerRecord.getValue(field));
                }
                record.setValue(count, i);
                result.add(record);
            }
            mock[0] = new MockResult(1, result);
        } else if (sql.startsWith("SELECT `CROWDCONTROL`.`ANSWER_RESERVATION`.`IDANSWER_RESERVATION` FROM `CROWDCON")) {
            Result<Record1<Integer>> result = create.newResult(ANSWER_RESERVATION.IDANSWER_RESERVATION);
            int answerGivenCountWorker = dataHolder.getAnswerGiveCountWorker();
            int answersTotalPerWorker = dataHolder.getExperimentRecord().getAnwersPerWorker();
            for (int i = 0; i < answersTotalPerWorker - answerGivenCountWorker; i++) {
                Record1<Integer> record = create.newRecord(ANSWER_RESERVATION.IDANSWER_RESERVATION);
                record.values(i);
                result.add(record);
            }
            mock[0] = new MockResult(1, result);
        } else if (sql.startsWith("SELECT COUNT(*) FROM `CROWDCONTROL`.`ANSWER_RESERVATION` WHERE")) {
            Param<BigInteger> c = DSL.val("C", BigInteger.class);
            Result<Record1<BigInteger>> result = create.newResult(c);
            Record1<BigInteger> record = create.newRecord(c);
            record.value1(BigInteger.valueOf(0));
            result.add(record);
            mock[0] = new MockResult(1, result);
        } else if (sql.startsWith("SELECT `CROWDCONTROL`.`WORKER`.`QUALITY` FROM `CROWDCONTROL`.`WORKER` ")) {
            Result<Record1<Integer>> result = create.newResult(WORKER.QUALITY);
            Record1<Integer> record = create.newRecord(WORKER.QUALITY);
            record.value1(dataHolder.getWorkerQuality());
            result.add(record);
            mock[0] = new MockResult(1, result);
        } else if (sql.startsWith("SELECT `CROWDCONTROL`.`RATING_OPTION_EXPERIMENT`")) {
            Result<RatingOptionExperimentRecord> result = create.newResult(RATING_OPTION_EXPERIMENT);
            mock[0] = new MockResult(1, result);
        } else if (sql.startsWith("SELECT `CROWDCONTROL`.`RATING_RESERVATION`.`IDRESERVERD_RATING`, `CROWDCONTROL`.")) {
            Result<RatingReservationRecord> result = create.newResult(RATING_RESERVATION);
            List<AnswerRecord> answerRecords = dataHolder.getAnswerRecords();
            for (int i = answerRecords.size() - 1; i >= 0; i--) {
                result.add(new RatingReservationRecord(i, dataHolder.getWorkerID(),
                        dataHolder.getExperimentRecord().getIdExperiment(), answerRecords.get(i).getIdAnswer(), null, null));
            }
            mock[0] = new MockResult(1, result);
        }

        // You can detect batch statements easily
        else if (ctx.batch()) {
            mock[0] = new MockResult(1, null);
        }

        else if (sql.startsWith("INSERT INTO `CROWDCONTROL`.`ALGORITHM_TASK_CHOOSER` ")) {
            mock[0] = new MockResult(1, null);
        } else if (sql.startsWith("INSERT INTO `CROWDCONTROL`.`ALGORITHM_TASK_CHOOSER_PARAM` (`ID_ALGORITHM_TASK_CHOOSER_PARAM`")) {
            mock[0] = new MockResult(1, null);
        } else if (sql.startsWith("INSERT INTO `CROWDCONTROL`.`RATING` (`EXPERIMENT`, `ANSWER_R`, `WORKER_ID`")) {
            mock[0] = new MockResult(1, null);
        } else if (sql.startsWith("INSERT INTO `CROWDCONTROL`.`ANSWER_RESERVATION` (`IDANSWER_RESERVATION`, `WORKER`, ")) {
            mock[0] = new MockResult(1, null);
        }



        else if (sql.startsWith("UPDATE `CROWDCONTROL`.`RATING_RESERVATION` SET `CROWDCONTROL`.`RATING_RESERVATION`")) {
            mock[0] = new MockResult(1, null);
        } else if (sql.startsWith("UPDATE `CROWDCONTROL`.`ANSWER_RESERVATION` SET `CROWDCONTROL`.`ANSWER_RESERVATION`")) {
            mock[0] = new MockResult(1, null);
        }

        return mock;
    }

    public DSLContext getMockCreate() {
        return mockCreate;
    }
}
