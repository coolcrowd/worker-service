/**
 * This class is generated by jOOQ
 */
package edu.ipd.kit.crowdcontrol.workerservice.database.model.tables.records;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = { "http://www.jooq.org", "3.4.1" },
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PlatformsRecord extends org.jooq.impl.UpdatableRecordImpl<edu.ipd.kit.crowdcontrol.workerservice.database.model.tables.records.PlatformsRecord> implements org.jooq.Record4<java.lang.Integer, java.lang.Boolean, java.lang.Boolean, java.lang.String> {

	private static final long serialVersionUID = 192240761;

	/**
	 * Setter for <code>crowdcontrol.Platforms.idPlatforms</code>.
	 */
	public void setIdplatforms(java.lang.Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Platforms.idPlatforms</code>.
	 */
	public java.lang.Integer getIdplatforms() {
		return (java.lang.Integer) getValue(0);
	}

	/**
	 * Setter for <code>crowdcontrol.Platforms.native_qualifications</code>.
	 */
	public void setNativeQualifications(java.lang.Boolean value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Platforms.native_qualifications</code>.
	 */
	public java.lang.Boolean getNativeQualifications() {
		return (java.lang.Boolean) getValue(1);
	}

	/**
	 * Setter for <code>crowdcontrol.Platforms.native_payment</code>.
	 */
	public void setNativePayment(java.lang.Boolean value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Platforms.native_payment</code>.
	 */
	public java.lang.Boolean getNativePayment() {
		return (java.lang.Boolean) getValue(2);
	}

	/**
	 * Setter for <code>crowdcontrol.Platforms.name</code>.
	 */
	public void setName(java.lang.String value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Platforms.name</code>.
	 */
	public java.lang.String getName() {
		return (java.lang.String) getValue(3);
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
	public org.jooq.Row4<java.lang.Integer, java.lang.Boolean, java.lang.Boolean, java.lang.String> fieldsRow() {
		return (org.jooq.Row4) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row4<java.lang.Integer, java.lang.Boolean, java.lang.Boolean, java.lang.String> valuesRow() {
		return (org.jooq.Row4) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return edu.ipd.kit.crowdcontrol.workerservice.database.model.tables.Platforms.PLATFORMS.IDPLATFORMS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Boolean> field2() {
		return edu.ipd.kit.crowdcontrol.workerservice.database.model.tables.Platforms.PLATFORMS.NATIVE_QUALIFICATIONS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Boolean> field3() {
		return edu.ipd.kit.crowdcontrol.workerservice.database.model.tables.Platforms.PLATFORMS.NATIVE_PAYMENT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field4() {
		return edu.ipd.kit.crowdcontrol.workerservice.database.model.tables.Platforms.PLATFORMS.NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value1() {
		return getIdplatforms();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Boolean value2() {
		return getNativeQualifications();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Boolean value3() {
		return getNativePayment();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value4() {
		return getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlatformsRecord value1(java.lang.Integer value) {
		setIdplatforms(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlatformsRecord value2(java.lang.Boolean value) {
		setNativeQualifications(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlatformsRecord value3(java.lang.Boolean value) {
		setNativePayment(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlatformsRecord value4(java.lang.String value) {
		setName(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlatformsRecord values(java.lang.Integer value1, java.lang.Boolean value2, java.lang.Boolean value3, java.lang.String value4) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached PlatformsRecord
	 */
	public PlatformsRecord() {
		super(edu.ipd.kit.crowdcontrol.workerservice.database.model.tables.Platforms.PLATFORMS);
	}

	/**
	 * Create a detached, initialised PlatformsRecord
	 */
	public PlatformsRecord(java.lang.Integer idplatforms, java.lang.Boolean nativeQualifications, java.lang.Boolean nativePayment, java.lang.String name) {
		super(edu.ipd.kit.crowdcontrol.workerservice.database.model.tables.Platforms.PLATFORMS);

		setValue(0, idplatforms);
		setValue(1, nativeQualifications);
		setValue(2, nativePayment);
		setValue(3, name);
	}
}
