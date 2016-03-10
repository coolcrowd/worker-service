/**
 * This class is generated by jOOQ
 */
package edu.kit.ipd.crowdcontrol.objectservice.database.model.tables.records;


import edu.kit.ipd.crowdcontrol.objectservice.database.model.tables.CalibrationResult;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;


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
public class CalibrationResultRecord extends UpdatableRecordImpl<CalibrationResultRecord> implements Record3<Integer, Integer, Integer> {

	private static final long serialVersionUID = 469511943;

	/**
	 * Setter for <code>crowdcontrol.Calibration_Result.id_calibration_result</code>.
	 */
	public void setIdCalibrationResult(Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Calibration_Result.id_calibration_result</code>.
	 */
	public Integer getIdCalibrationResult() {
		return (Integer) getValue(0);
	}

	/**
	 * Setter for <code>crowdcontrol.Calibration_Result.worker</code>.
	 */
	public void setWorker(Integer value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Calibration_Result.worker</code>.
	 */
	public Integer getWorker() {
		return (Integer) getValue(1);
	}

	/**
	 * Setter for <code>crowdcontrol.Calibration_Result.answer</code>.
	 */
	public void setAnswer(Integer value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Calibration_Result.answer</code>.
	 */
	public Integer getAnswer() {
		return (Integer) getValue(2);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record1<Integer> key() {
		return (Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record3 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row3<Integer, Integer, Integer> fieldsRow() {
		return (Row3) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row3<Integer, Integer, Integer> valuesRow() {
		return (Row3) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field1() {
		return CalibrationResult.CALIBRATION_RESULT.ID_CALIBRATION_RESULT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field2() {
		return CalibrationResult.CALIBRATION_RESULT.WORKER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field3() {
		return CalibrationResult.CALIBRATION_RESULT.ANSWER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value1() {
		return getIdCalibrationResult();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value2() {
		return getWorker();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value3() {
		return getAnswer();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CalibrationResultRecord value1(Integer value) {
		setIdCalibrationResult(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CalibrationResultRecord value2(Integer value) {
		setWorker(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CalibrationResultRecord value3(Integer value) {
		setAnswer(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CalibrationResultRecord values(Integer value1, Integer value2, Integer value3) {
		value1(value1);
		value2(value2);
		value3(value3);
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached CalibrationResultRecord
	 */
	public CalibrationResultRecord() {
		super(CalibrationResult.CALIBRATION_RESULT);
	}

	/**
	 * Create a detached, initialised CalibrationResultRecord
	 */
	public CalibrationResultRecord(Integer idCalibrationResult, Integer worker, Integer answer) {
		super(CalibrationResult.CALIBRATION_RESULT);

		setValue(0, idCalibrationResult);
		setValue(1, worker);
		setValue(2, answer);
	}
}
