/**
 * This class is generated by jOOQ
 */
package edu.kit.ipd.crowdcontrol.workerservice.database.model.tables;


import edu.kit.ipd.crowdcontrol.workerservice.database.model.Crowdcontrol;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.Keys;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.enums.ExperimentsPlatformStatusPlatformStatus;
import edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentsPlatformStatusRecord;

import java.sql.Timestamp;
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
public class ExperimentsPlatformStatus extends TableImpl<ExperimentsPlatformStatusRecord> {

	private static final long serialVersionUID = -347946746;

	/**
	 * The reference instance of <code>crowdcontrol.Experiments_Platform_Status</code>
	 */
	public static final ExperimentsPlatformStatus EXPERIMENTS_PLATFORM_STATUS = new ExperimentsPlatformStatus();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<ExperimentsPlatformStatusRecord> getRecordType() {
		return ExperimentsPlatformStatusRecord.class;
	}

	/**
	 * The column <code>crowdcontrol.Experiments_Platform_Status.idExperiments_Platform_Status</code>.
	 */
	public final TableField<ExperimentsPlatformStatusRecord, Integer> IDEXPERIMENTS_PLATFORM_STATUS = createField("idExperiments_Platform_Status", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>crowdcontrol.Experiments_Platform_Status.platform_status</code>.
	 */
	public final TableField<ExperimentsPlatformStatusRecord, ExperimentsPlatformStatusPlatformStatus> PLATFORM_STATUS = createField("platform_status", org.jooq.util.mysql.MySQLDataType.VARCHAR.asEnumDataType(edu.kit.ipd.crowdcontrol.workerservice.database.model.enums.ExperimentsPlatformStatusPlatformStatus.class), this, "");

	/**
	 * The column <code>crowdcontrol.Experiments_Platform_Status.timestamp</code>.
	 */
	public final TableField<ExperimentsPlatformStatusRecord, Timestamp> TIMESTAMP = createField("timestamp", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>crowdcontrol.Experiments_Platform_Status.platform</code>.
	 */
	public final TableField<ExperimentsPlatformStatusRecord, Integer> PLATFORM = createField("platform", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * Create a <code>crowdcontrol.Experiments_Platform_Status</code> table reference
	 */
	public ExperimentsPlatformStatus() {
		this("Experiments_Platform_Status", null);
	}

	/**
	 * Create an aliased <code>crowdcontrol.Experiments_Platform_Status</code> table reference
	 */
	public ExperimentsPlatformStatus(String alias) {
		this(alias, EXPERIMENTS_PLATFORM_STATUS);
	}

	private ExperimentsPlatformStatus(String alias, Table<ExperimentsPlatformStatusRecord> aliased) {
		this(alias, aliased, null);
	}

	private ExperimentsPlatformStatus(String alias, Table<ExperimentsPlatformStatusRecord> aliased, Field<?>[] parameters) {
		super(alias, Crowdcontrol.CROWDCONTROL, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Identity<ExperimentsPlatformStatusRecord, Integer> getIdentity() {
		return Keys.IDENTITY_EXPERIMENTS_PLATFORM_STATUS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<ExperimentsPlatformStatusRecord> getPrimaryKey() {
		return Keys.KEY_EXPERIMENTS_PLATFORM_STATUS_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<ExperimentsPlatformStatusRecord>> getKeys() {
		return Arrays.<UniqueKey<ExperimentsPlatformStatusRecord>>asList(Keys.KEY_EXPERIMENTS_PLATFORM_STATUS_PRIMARY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ForeignKey<ExperimentsPlatformStatusRecord, ?>> getReferences() {
		return Arrays.<ForeignKey<ExperimentsPlatformStatusRecord, ?>>asList(Keys.STATUS_FOR_PLATFORM);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExperimentsPlatformStatus as(String alias) {
		return new ExperimentsPlatformStatus(alias, this);
	}

	/**
	 * Rename this table
	 */
	public ExperimentsPlatformStatus rename(String name) {
		return new ExperimentsPlatformStatus(name, null);
	}
}
