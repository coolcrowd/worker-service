/**
 * This class is generated by jOOQ
 */
package edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records;


import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.ExperimentsCalibration;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
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
public class ExperimentsCalibrationRecord extends UpdatableRecordImpl<ExperimentsCalibrationRecord> implements Record4<Integer, Integer, Integer, Boolean> {

	private static final long serialVersionUID = 870997839;

	/**
	 * Setter for <code>crowdcontrol.Experiments_Calibration.id_experiments_calibration</code>.
	 */
	public void setIdExperimentsCalibration(Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Experiments_Calibration.id_experiments_calibration</code>.
	 */
	public Integer getIdExperimentsCalibration() {
		return (Integer) getValue(0);
	}

	/**
	 * Setter for <code>crowdcontrol.Experiments_Calibration.experiments_platform</code>.
	 */
	public void setExperimentsPlatform(Integer value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Experiments_Calibration.experiments_platform</code>.
	 */
	public Integer getExperimentsPlatform() {
		return (Integer) getValue(1);
	}

	/**
	 * Setter for <code>crowdcontrol.Experiments_Calibration.answer</code>.
	 */
	public void setAnswer(Integer value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Experiments_Calibration.answer</code>.
	 */
	public Integer getAnswer() {
		return (Integer) getValue(2);
	}

	/**
	 * Setter for <code>crowdcontrol.Experiments_Calibration.not</code>.
	 */
	public void setNot(Boolean value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Experiments_Calibration.not</code>.
	 */
	public Boolean getNot() {
		return (Boolean) getValue(3);
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
	// Record4 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row4<Integer, Integer, Integer, Boolean> fieldsRow() {
		return (Row4) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row4<Integer, Integer, Integer, Boolean> valuesRow() {
		return (Row4) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field1() {
		return ExperimentsCalibration.EXPERIMENTS_CALIBRATION.ID_EXPERIMENTS_CALIBRATION;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field2() {
		return ExperimentsCalibration.EXPERIMENTS_CALIBRATION.EXPERIMENTS_PLATFORM;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field3() {
		return ExperimentsCalibration.EXPERIMENTS_CALIBRATION.ANSWER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Boolean> field4() {
		return ExperimentsCalibration.EXPERIMENTS_CALIBRATION.NOT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value1() {
		return getIdExperimentsCalibration();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value2() {
		return getExperimentsPlatform();
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
	public Boolean value4() {
		return getNot();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExperimentsCalibrationRecord value1(Integer value) {
		setIdExperimentsCalibration(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExperimentsCalibrationRecord value2(Integer value) {
		setExperimentsPlatform(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExperimentsCalibrationRecord value3(Integer value) {
		setAnswer(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExperimentsCalibrationRecord value4(Boolean value) {
		setNot(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExperimentsCalibrationRecord values(Integer value1, Integer value2, Integer value3, Boolean value4) {
		value1(value1);
		value2(value2);
		value3(value3);
		value4(value4);
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached ExperimentsCalibrationRecord
	 */
	public ExperimentsCalibrationRecord() {
		super(ExperimentsCalibration.EXPERIMENTS_CALIBRATION);
	}

	/**
	 * Create a detached, initialised ExperimentsCalibrationRecord
	 */
	public ExperimentsCalibrationRecord(Integer idExperimentsCalibration, Integer experimentsPlatform, Integer answer, Boolean not) {
		super(ExperimentsCalibration.EXPERIMENTS_CALIBRATION);

		setValue(0, idExperimentsCalibration);
		setValue(1, experimentsPlatform);
		setValue(2, answer);
		setValue(3, not);
	}
}
