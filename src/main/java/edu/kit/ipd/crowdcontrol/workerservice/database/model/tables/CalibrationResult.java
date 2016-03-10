/**
 * This class is generated by jOOQ
 */
package edu.kit.ipd.crowdcontrol.objectservice.database.model.tables;


import edu.kit.ipd.crowdcontrol.objectservice.database.model.Crowdcontrol;
import edu.kit.ipd.crowdcontrol.objectservice.database.model.Keys;
import edu.kit.ipd.crowdcontrol.objectservice.database.model.tables.records.CalibrationResultRecord;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.7.3"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class CalibrationResult extends TableImpl<CalibrationResultRecord> {

	private static final long serialVersionUID = -871089470;

	/**
	 * The reference instance of <code>crowdcontrol.Calibration_Result</code>
	 */
	public static final CalibrationResult CALIBRATION_RESULT = new CalibrationResult();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<CalibrationResultRecord> getRecordType() {
		return CalibrationResultRecord.class;
	}

	/**
	 * The column <code>crowdcontrol.Calibration_Result.id_calibration_result</code>.
	 */
	public final TableField<CalibrationResultRecord, Integer> ID_CALIBRATION_RESULT = createField("id_calibration_result", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>crowdcontrol.Calibration_Result.worker</code>.
	 */
	public final TableField<CalibrationResultRecord, Integer> WORKER = createField("worker", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>crowdcontrol.Calibration_Result.answer</code>.
	 */
	public final TableField<CalibrationResultRecord, Integer> ANSWER = createField("answer", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * Create a <code>crowdcontrol.Calibration_Result</code> table reference
	 */
	public CalibrationResult() {
		this("Calibration_Result", null);
	}

	/**
	 * Create an aliased <code>crowdcontrol.Calibration_Result</code> table reference
	 */
	public CalibrationResult(String alias) {
		this(alias, CALIBRATION_RESULT);
	}

	private CalibrationResult(String alias, Table<CalibrationResultRecord> aliased) {
		this(alias, aliased, null);
	}

	private CalibrationResult(String alias, Table<CalibrationResultRecord> aliased, Field<?>[] parameters) {
		super(alias, Crowdcontrol.CROWDCONTROL, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Identity<CalibrationResultRecord, Integer> getIdentity() {
		return Keys.IDENTITY_CALIBRATION_RESULT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<CalibrationResultRecord> getPrimaryKey() {
		return Keys.KEY_CALIBRATION_RESULT_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<CalibrationResultRecord>> getKeys() {
		return Arrays.<UniqueKey<CalibrationResultRecord>>asList(Keys.KEY_CALIBRATION_RESULT_PRIMARY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ForeignKey<CalibrationResultRecord, ?>> getReferences() {
		return Arrays.<ForeignKey<CalibrationResultRecord, ?>>asList(Keys.REFERENCEDWORKER, Keys.CHOSENANSWER);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CalibrationResult as(String alias) {
		return new CalibrationResult(alias, this);
	}

	/**
	 * Rename this table
	 */
	public CalibrationResult rename(String name) {
		return new CalibrationResult(name, null);
	}
}
