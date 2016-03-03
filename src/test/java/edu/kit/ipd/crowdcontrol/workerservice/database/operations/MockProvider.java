package edu.kit.ipd.crowdcontrol.workerservice.database.operations;

import edu.kit.ipd.crowdcontrol.workerservice.database.model.enums.ExperimentsPlatformModeStopgap;
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
        } else if (sql.startsWith("SELECT 1 AS `ONE` FROM DUAL WHERE EXISTS (SELECT `CROWDCONTROL`.`EXPERIMENTS_CALIBRATION`")) {
            Param<Integer> val = DSL.val("1", Integer.class);
            Result<Record1<Integer>> result = create.newResult(val);
            if (dataHolder.belongsToWrongPopulation()) {
                Record1<Integer> record = create.newRecord(val);
                record.value1(1);
                result.add(record);
            }
            mock[0] = new MockResult(1, result);
        } else if (sql.startsWith("SELECT `CROWDCONTROL`.`EXPERIMENTS_PLATFORM_MODE`.`STOPGAP` FROM `CROWDCONTROL")) {
            Result<Record1<ExperimentsPlatformModeStopgap>> result = create.newResult(EXPERIMENTS_PLATFORM_MODE.STOPGAP);
            Record1<ExperimentsPlatformModeStopgap> record = create.newRecord(EXPERIMENTS_PLATFORM_MODE.STOPGAP);
            record.value1(dataHolder.getExperimentsPlatformModeRecord().getStopgap());
            result.add(record);
            mock[0] = new MockResult(1, result);
        } else if (sql.startsWith("SELECT COUNT(*) AS `C` FROM (SELECT `CROWDCONTROL`.`ANSWER`.`ID_ANSWER`, ")) {
            if (ctx.bindings().length == 4) {
                Param<BigInteger> c = DSL.val("C", BigInteger.class);
                Result<Record1<BigInteger>> result = create.newResult(c);
                Record1<BigInteger> record = create.newRecord(c);
                record.value1(BigInteger.valueOf(dataHolder.getAnswerCountTotal()));
                result.add(record);
                mock[0] = new MockResult(1, result);
            } else {
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
        } else if (sql.endsWith("JOIN `CROWDCONTROL`.`RATING` ON (`CROWDCONTROL`.`RATING`.`WORKER_ID` = ? AND " +
                "`CROWDCONTROL`.`RATING`.`EXPERIMENT` = ? AND `CROWDCONTROL`.`RATING`.`RATING` IS NULL)")) {
            ArrayList<Field<?>> fields = new ArrayList<>(Arrays.asList(ANSWER.fields()));
            fields.addAll(Arrays.asList(RATING.ID_RATING));
            Field<?>[] fieldsArr = fields.toArray(new Field<?>[fields.size()]);
            Result<Record> result = create.newResult(fieldsArr);

            mock[0] = new MockResult(1, result);
        } else if (sql.startsWith("SELECT `CROWDCONTROL`.`ANSWER`.`ID_ANSWER`, `CROWDCONTROL`.`ANSWER`.`EXPERIMENT`, ")) {
            ArrayList<Field<?>> fields = new ArrayList<>(Arrays.asList(ANSWER.fields()));
            Field<Integer> count = DSL.count(RATING.ID_RATING).as("count");
            fields.add(count);
            Field<?>[] fieldsArr = fields.toArray(new Field<?>[fields.size()]);
            Result<Record> result = create.newResult(fieldsArr);
            dataHolder.getAnswerRecords().stream()
                    .map(answerRecord -> {
                        Record record = create.newRecord(fieldsArr);
                        for (Field field : answerRecord.fields()) {
                            record.setValue(field, answerRecord.getValue(field));
                        }
                        record.setValue(count, 1);
                        return record;
                    })
                    .forEach(result::add);
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
        } else if (sql.startsWith("SELECT `CROWDCONTROL`.`RATING`.`ID_RATING`, `CROWDCONTROL`.")) {
            Result<RatingRecord> result = create.newResult(RATING);
            List<AnswerRecord> answerRecords = dataHolder.getAnswerRecords();
            for (int i = answerRecords.size() - 1; i >= 0; i--) {
                result.add(new RatingRecord(i, null, answerRecords.get(i).getIdAnswer(), null, null, null, null, null));
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
        }



        else if (sql.startsWith("UPDATE `CROWDCONTROL`.`RATING` SET `CROWDCONTROL`.`RATING`.`TIM")) {
            mock[0] = new MockResult(1, null);
        }

        return mock;
    }

    public DSLContext getMockCreate() {
        return mockCreate;
    }
}
