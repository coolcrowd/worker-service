/**
 * This class is generated by jOOQ
 */
package edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = { "http://www.jooq.org", "3.4.1" },
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class ExperimentsPlatformStatusRecord extends org.jooq.impl.UpdatableRecordImpl<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentsPlatformStatusRecord> implements org.jooq.Record4<java.lang.Integer, edu.kit.ipd.crowdcontrol.workerservice.database.model.enums.ExperimentsPlatformStatusPlatformStatus, java.sql.Timestamp, java.lang.Integer> {

	private static final long serialVersionUID = 234302750;

	/**
	 * Setter for <code>crowdcontrol.Experiments_Platform_Status.idExperiments_Platform_Status</code>.
	 */
	public void setIdexperimentsPlatformStatus(java.lang.Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Experiments_Platform_Status.idExperiments_Platform_Status</code>.
	 */
	public java.lang.Integer getIdexperimentsPlatformStatus() {
		return (java.lang.Integer) getValue(0);
	}

	/**
	 * Setter for <code>crowdcontrol.Experiments_Platform_Status.platform_status</code>.
	 */
	public void setPlatformStatus(edu.kit.ipd.crowdcontrol.workerservice.database.model.enums.ExperimentsPlatformStatusPlatformStatus value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Experiments_Platform_Status.platform_status</code>.
	 */
	public edu.kit.ipd.crowdcontrol.workerservice.database.model.enums.ExperimentsPlatformStatusPlatformStatus getPlatformStatus() {
		return (edu.kit.ipd.crowdcontrol.workerservice.database.model.enums.ExperimentsPlatformStatusPlatformStatus) getValue(1);
	}

	/**
	 * Setter for <code>crowdcontrol.Experiments_Platform_Status.timestamp</code>.
	 */
	public void setTimestamp(java.sql.Timestamp value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Experiments_Platform_Status.timestamp</code>.
	 */
	public java.sql.Timestamp getTimestamp() {
		return (java.sql.Timestamp) getValue(2);
	}

	/**
	 * Setter for <code>crowdcontrol.Experiments_Platform_Status.platform</code>.
	 */
	public void setPlatform(java.lang.Integer value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Experiments_Platform_Status.platform</code>.
	 */
	public java.lang.Integer getPlatform() {
		return (java.lang.Integer) getValue(3);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Record1<java.lang.Integer> key() {
		return (org.jooq.Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record4 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row4<java.lang.Integer, edu.kit.ipd.crowdcontrol.workerservice.database.model.enums.ExperimentsPlatformStatusPlatformStatus, java.sql.Timestamp, java.lang.Integer> fieldsRow() {
		return (org.jooq.Row4) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row4<java.lang.Integer, edu.kit.ipd.crowdcontrol.workerservice.database.model.enums.ExperimentsPlatformStatusPlatformStatus, java.sql.Timestamp, java.lang.Integer> valuesRow() {
		return (org.jooq.Row4) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.ExperimentsPlatformStatus.EXPERIMENTS_PLATFORM_STATUS.IDEXPERIMENTS_PLATFORM_STATUS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<edu.kit.ipd.crowdcontrol.workerservice.database.model.enums.ExperimentsPlatformStatusPlatformStatus> field2() {
		return edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.ExperimentsPlatformStatus.EXPERIMENTS_PLATFORM_STATUS.PLATFORM_STATUS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.sql.Timestamp> field3() {
		return edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.ExperimentsPlatformStatus.EXPERIMENTS_PLATFORM_STATUS.TIMESTAMP;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field4() {
		return edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.ExperimentsPlatformStatus.EXPERIMENTS_PLATFORM_STATUS.PLATFORM;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value1() {
		return getIdexperimentsPlatformStatus();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public edu.kit.ipd.crowdcontrol.workerservice.database.model.enums.ExperimentsPlatformStatusPlatformStatus value2() {
		return getPlatformStatus();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.sql.Timestamp value3() {
		return getTimestamp();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value4() {
		return getPlatform();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExperimentsPlatformStatusRecord value1(java.lang.Integer value) {
		setIdexperimentsPlatformStatus(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExperimentsPlatformStatusRecord value2(edu.kit.ipd.crowdcontrol.workerservice.database.model.enums.ExperimentsPlatformStatusPlatformStatus value) {
		setPlatformStatus(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExperimentsPlatformStatusRecord value3(java.sql.Timestamp value) {
		setTimestamp(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExperimentsPlatformStatusRecord value4(java.lang.Integer value) {
		setPlatform(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExperimentsPlatformStatusRecord values(java.lang.Integer value1, edu.kit.ipd.crowdcontrol.workerservice.database.model.enums.ExperimentsPlatformStatusPlatformStatus value2, java.sql.Timestamp value3, java.lang.Integer value4) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached ExperimentsPlatformStatusRecord
	 */
	public ExperimentsPlatformStatusRecord() {
		super(edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.ExperimentsPlatformStatus.EXPERIMENTS_PLATFORM_STATUS);
	}

	/**
	 * Create a detached, initialised ExperimentsPlatformStatusRecord
	 */
	public ExperimentsPlatformStatusRecord(java.lang.Integer idexperimentsPlatformStatus, edu.kit.ipd.crowdcontrol.workerservice.database.model.enums.ExperimentsPlatformStatusPlatformStatus platformStatus, java.sql.Timestamp timestamp, java.lang.Integer platform) {
		super(edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.ExperimentsPlatformStatus.EXPERIMENTS_PLATFORM_STATUS);

		setValue(0, idexperimentsPlatformStatus);
		setValue(1, platformStatus);
		setValue(2, timestamp);
		setValue(3, platform);
	}
}
