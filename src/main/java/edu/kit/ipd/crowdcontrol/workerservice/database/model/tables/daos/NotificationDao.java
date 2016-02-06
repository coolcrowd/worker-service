/**
 * This class is generated by jOOQ
 */
package edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.daos;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = { "http://www.jooq.org", "3.4.1" },
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class NotificationDao extends org.jooq.impl.DAOImpl<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.NotificationRecord, edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.pojos.Notification, java.lang.Integer> {

	/**
	 * Create a new NotificationDao without any configuration
	 */
	public NotificationDao() {
		super(edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Notification.NOTIFICATION, edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.pojos.Notification.class);
	}

	/**
	 * Create a new NotificationDao with an attached configuration
	 */
	public NotificationDao(org.jooq.Configuration configuration) {
		super(edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Notification.NOTIFICATION, edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.pojos.Notification.class, configuration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected java.lang.Integer getId(edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.pojos.Notification object) {
		return object.getIdNotification();
	}

	/**
	 * Fetch records that have <code>id_notification IN (values)</code>
	 */
	public java.util.List<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.pojos.Notification> fetchByIdNotification(java.lang.Integer... values) {
		return fetch(edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Notification.NOTIFICATION.ID_NOTIFICATION, values);
	}

	/**
	 * Fetch a unique record that has <code>id_notification = value</code>
	 */
	public edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.pojos.Notification fetchOneByIdNotification(java.lang.Integer value) {
		return fetchOne(edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Notification.NOTIFICATION.ID_NOTIFICATION, value);
	}

	/**
	 * Fetch records that have <code>name IN (values)</code>
	 */
	public java.util.List<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.pojos.Notification> fetchByName(java.lang.String... values) {
		return fetch(edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Notification.NOTIFICATION.NAME, values);
	}

	/**
	 * Fetch records that have <code>description IN (values)</code>
	 */
	public java.util.List<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.pojos.Notification> fetchByDescription(java.lang.String... values) {
		return fetch(edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Notification.NOTIFICATION.DESCRIPTION, values);
	}

	/**
	 * Fetch records that have <code>checkPeriod IN (values)</code>
	 */
	public java.util.List<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.pojos.Notification> fetchByCheckperiod(java.lang.Integer... values) {
		return fetch(edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Notification.NOTIFICATION.CHECKPERIOD, values);
	}

	/**
	 * Fetch records that have <code>query IN (values)</code>
	 */
	public java.util.List<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.pojos.Notification> fetchByQuery(java.lang.String... values) {
		return fetch(edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Notification.NOTIFICATION.QUERY, values);
	}

	/**
	 * Fetch records that have <code>send_once IN (values)</code>
	 */
	public java.util.List<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.pojos.Notification> fetchBySendOnce(java.lang.Boolean... values) {
		return fetch(edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Notification.NOTIFICATION.SEND_ONCE, values);
	}
}
