/**
 * This class is generated by jOOQ
 */
package edu.kit.ipd.crowdcontrol.workerservice.database.model.tables;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = { "http://www.jooq.org", "3.4.1" },
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Rating extends org.jooq.impl.TableImpl<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.RatingRecord> {

	private static final long serialVersionUID = -1574095396;

	/**
	 * The singleton instance of <code>crowdcontrol.Rating</code>
	 */
	public static final edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Rating RATING = new edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Rating();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.RatingRecord> getRecordType() {
		return edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.RatingRecord.class;
	}

	/**
	 * The column <code>crowdcontrol.Rating.id_rating</code>.
	 */
	public final org.jooq.TableField<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.RatingRecord, java.lang.Integer> ID_RATING = createField("id_rating", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>crowdcontrol.Rating.experiment</code>.
	 */
	public final org.jooq.TableField<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.RatingRecord, java.lang.Integer> EXPERIMENT = createField("experiment", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>crowdcontrol.Rating.answer_r</code>.
	 */
	public final org.jooq.TableField<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.RatingRecord, java.lang.Integer> ANSWER_R = createField("answer_r", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>crowdcontrol.Rating.timestamp</code>.
	 */
	public final org.jooq.TableField<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.RatingRecord, java.sql.Timestamp> TIMESTAMP = createField("timestamp", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaulted(true), this, "");

	/**
	 * The column <code>crowdcontrol.Rating.rating</code>.
	 */
	public final org.jooq.TableField<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.RatingRecord, java.lang.Integer> RATING_ = createField("rating", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>crowdcontrol.Rating.worker_id</code>.
	 */
	public final org.jooq.TableField<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.RatingRecord, java.lang.Integer> WORKER_ID = createField("worker_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>crowdcontrol.Rating.quality</code>.
	 */
	public final org.jooq.TableField<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.RatingRecord, java.lang.Integer> QUALITY = createField("quality", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * Create a <code>crowdcontrol.Rating</code> table reference
	 */
	public Rating() {
		this("Rating", null);
	}

	/**
	 * Create an aliased <code>crowdcontrol.Rating</code> table reference
	 */
	public Rating(java.lang.String alias) {
		this(alias, edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Rating.RATING);
	}

	private Rating(java.lang.String alias, org.jooq.Table<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.RatingRecord> aliased) {
		this(alias, aliased, null);
	}

	private Rating(java.lang.String alias, org.jooq.Table<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.RatingRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, edu.kit.ipd.crowdcontrol.workerservice.database.model.Crowdcontrol.CROWDCONTROL, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Identity<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.RatingRecord, java.lang.Integer> getIdentity() {
		return edu.kit.ipd.crowdcontrol.workerservice.database.model.Keys.IDENTITY_RATING;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.RatingRecord> getPrimaryKey() {
		return edu.kit.ipd.crowdcontrol.workerservice.database.model.Keys.KEY_RATING_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.RatingRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.RatingRecord>>asList(edu.kit.ipd.crowdcontrol.workerservice.database.model.Keys.KEY_RATING_PRIMARY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.ForeignKey<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.RatingRecord, ?>> getReferences() {
		return java.util.Arrays.<org.jooq.ForeignKey<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.RatingRecord, ?>>asList(edu.kit.ipd.crowdcontrol.workerservice.database.model.Keys.IDEXPERIMENTRATING, edu.kit.ipd.crowdcontrol.workerservice.database.model.Keys.IDANSWERSRATINS, edu.kit.ipd.crowdcontrol.workerservice.database.model.Keys.WORKERRATED);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Rating as(java.lang.String alias) {
		return new edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Rating(alias, this);
	}

	/**
	 * Rename this table
	 */
	public edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Rating rename(java.lang.String name) {
		return new edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Rating(name, null);
	}
}
