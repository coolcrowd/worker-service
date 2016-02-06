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
public class NotificationRecord extends org.jooq.impl.UpdatableRecordImpl<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.NotificationRecord> implements org.jooq.Record6<java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer, java.lang.String, java.lang.Boolean> {

	private static final long serialVersionUID = -1619520778;

	/**
	 * Setter for <code>crowdcontrol.Notification.id_notification</code>.
	 */
	public void setIdNotification(java.lang.Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Notification.id_notification</code>.
	 */
	public java.lang.Integer getIdNotification() {
		return (java.lang.Integer) getValue(0);
	}

	/**
	 * Setter for <code>crowdcontrol.Notification.name</code>.
	 */
	public void setName(java.lang.String value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Notification.name</code>.
	 */
	public java.lang.String getName() {
		return (java.lang.String) getValue(1);
	}

	/**
	 * Setter for <code>crowdcontrol.Notification.description</code>.
	 */
	public void setDescription(java.lang.String value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Notification.description</code>.
	 */
	public java.lang.String getDescription() {
		return (java.lang.String) getValue(2);
	}

	/**
	 * Setter for <code>crowdcontrol.Notification.checkPeriod</code>.
	 */
	public void setCheckperiod(java.lang.Integer value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Notification.checkPeriod</code>.
	 */
	public java.lang.Integer getCheckperiod() {
		return (java.lang.Integer) getValue(3);
	}

	/**
	 * Setter for <code>crowdcontrol.Notification.query</code>.
	 */
	public void setQuery(java.lang.String value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Notification.query</code>.
	 */
	public java.lang.String getQuery() {
		return (java.lang.String) getValue(4);
	}

	/**
	 * Setter for <code>crowdcontrol.Notification.send_once</code>.
	 */
	public void setSendOnce(java.lang.Boolean value) {
		setValue(5, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Notification.send_once</code>.
	 */
	public java.lang.Boolean getSendOnce() {
		return (java.lang.Boolean) getValue(5);
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
	// Record6 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row6<java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer, java.lang.String, java.lang.Boolean> fieldsRow() {
		return (org.jooq.Row6) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row6<java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer, java.lang.String, java.lang.Boolean> valuesRow() {
		return (org.jooq.Row6) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Notification.NOTIFICATION.ID_NOTIFICATION;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field2() {
		return edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Notification.NOTIFICATION.NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field3() {
		return edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Notification.NOTIFICATION.DESCRIPTION;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field4() {
		return edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Notification.NOTIFICATION.CHECKPERIOD;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field5() {
		return edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Notification.NOTIFICATION.QUERY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Boolean> field6() {
		return edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Notification.NOTIFICATION.SEND_ONCE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value1() {
		return getIdNotification();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value2() {
		return getName();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value3() {
		return getDescription();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value4() {
		return getCheckperiod();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value5() {
		return getQuery();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Boolean value6() {
		return getSendOnce();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NotificationRecord value1(java.lang.Integer value) {
		setIdNotification(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NotificationRecord value2(java.lang.String value) {
		setName(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NotificationRecord value3(java.lang.String value) {
		setDescription(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NotificationRecord value4(java.lang.Integer value) {
		setCheckperiod(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NotificationRecord value5(java.lang.String value) {
		setQuery(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NotificationRecord value6(java.lang.Boolean value) {
		setSendOnce(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public NotificationRecord values(java.lang.Integer value1, java.lang.String value2, java.lang.String value3, java.lang.Integer value4, java.lang.String value5, java.lang.Boolean value6) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached NotificationRecord
	 */
	public NotificationRecord() {
		super(edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Notification.NOTIFICATION);
	}

	/**
	 * Create a detached, initialised NotificationRecord
	 */
	public NotificationRecord(java.lang.Integer idNotification, java.lang.String name, java.lang.String description, java.lang.Integer checkperiod, java.lang.String query, java.lang.Boolean sendOnce) {
		super(edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Notification.NOTIFICATION);

		setValue(0, idNotification);
		setValue(1, name);
		setValue(2, description);
		setValue(3, checkperiod);
		setValue(4, query);
		setValue(5, sendOnce);
	}
}
