/**
 * This class is generated by jOOQ
 */
package edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records;


import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.AlgorithmTaskChooserParam;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Row5;
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
public class AlgorithmTaskChooserParamRecord extends UpdatableRecordImpl<AlgorithmTaskChooserParamRecord> implements Record5<Integer, String, String, String, String> {

	private static final long serialVersionUID = 1250399162;

	/**
	 * Setter for <code>crowdcontrol.Algorithm_Task_Chooser_Param.id_Algorithm_Task_Chooser_Param</code>.
	 */
	public void setIdAlgorithmTaskChooserParam(Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Algorithm_Task_Chooser_Param.id_Algorithm_Task_Chooser_Param</code>.
	 */
	public Integer getIdAlgorithmTaskChooserParam() {
		return (Integer) getValue(0);
	}

	/**
	 * Setter for <code>crowdcontrol.Algorithm_Task_Chooser_Param.description</code>.
	 */
	public void setDescription(String value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Algorithm_Task_Chooser_Param.description</code>.
	 */
	public String getDescription() {
		return (String) getValue(1);
	}

	/**
	 * Setter for <code>crowdcontrol.Algorithm_Task_Chooser_Param.regex</code>.
	 */
	public void setRegex(String value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Algorithm_Task_Chooser_Param.regex</code>.
	 */
	public String getRegex() {
		return (String) getValue(2);
	}

	/**
	 * Setter for <code>crowdcontrol.Algorithm_Task_Chooser_Param.algorithm</code>.
	 */
	public void setAlgorithm(String value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Algorithm_Task_Chooser_Param.algorithm</code>.
	 */
	public String getAlgorithm() {
		return (String) getValue(3);
	}

	/**
	 * Setter for <code>crowdcontrol.Algorithm_Task_Chooser_Param.data</code>.
	 */
	public void setData(String value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Algorithm_Task_Chooser_Param.data</code>.
	 */
	public String getData() {
		return (String) getValue(4);
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
	// Record5 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row5<Integer, String, String, String, String> fieldsRow() {
		return (Row5) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row5<Integer, String, String, String, String> valuesRow() {
		return (Row5) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field1() {
		return AlgorithmTaskChooserParam.ALGORITHM_TASK_CHOOSER_PARAM.ID_ALGORITHM_TASK_CHOOSER_PARAM;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field2() {
		return AlgorithmTaskChooserParam.ALGORITHM_TASK_CHOOSER_PARAM.DESCRIPTION;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field3() {
		return AlgorithmTaskChooserParam.ALGORITHM_TASK_CHOOSER_PARAM.REGEX;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field4() {
		return AlgorithmTaskChooserParam.ALGORITHM_TASK_CHOOSER_PARAM.ALGORITHM;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field5() {
		return AlgorithmTaskChooserParam.ALGORITHM_TASK_CHOOSER_PARAM.DATA;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value1() {
		return getIdAlgorithmTaskChooserParam();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value2() {
		return getDescription();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value3() {
		return getRegex();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value4() {
		return getAlgorithm();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value5() {
		return getData();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AlgorithmTaskChooserParamRecord value1(Integer value) {
		setIdAlgorithmTaskChooserParam(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AlgorithmTaskChooserParamRecord value2(String value) {
		setDescription(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AlgorithmTaskChooserParamRecord value3(String value) {
		setRegex(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AlgorithmTaskChooserParamRecord value4(String value) {
		setAlgorithm(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AlgorithmTaskChooserParamRecord value5(String value) {
		setData(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AlgorithmTaskChooserParamRecord values(Integer value1, String value2, String value3, String value4, String value5) {
		value1(value1);
		value2(value2);
		value3(value3);
		value4(value4);
		value5(value5);
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached AlgorithmTaskChooserParamRecord
	 */
	public AlgorithmTaskChooserParamRecord() {
		super(AlgorithmTaskChooserParam.ALGORITHM_TASK_CHOOSER_PARAM);
	}

	/**
	 * Create a detached, initialised AlgorithmTaskChooserParamRecord
	 */
	public AlgorithmTaskChooserParamRecord(Integer idAlgorithmTaskChooserParam, String description, String regex, String algorithm, String data) {
		super(AlgorithmTaskChooserParam.ALGORITHM_TASK_CHOOSER_PARAM);

		setValue(0, idAlgorithmTaskChooserParam);
		setValue(1, description);
		setValue(2, regex);
		setValue(3, algorithm);
		setValue(4, data);
	}
}
