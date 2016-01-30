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
public class Experiment extends org.jooq.impl.TableImpl<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord> {

	private static final long serialVersionUID = -958567463;

	/**
	 * The singleton instance of <code>crowdcontrol.Experiment</code>
	 */
	public static final edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Experiment EXPERIMENT = new edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Experiment();

	/**
	 * The class holding records for this type
	 */
	@Override
	public java.lang.Class<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord> getRecordType() {
		return edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord.class;
	}

	/**
	 * The column <code>crowdcontrol.Experiment.id_experiment</code>.
	 */
	public final org.jooq.TableField<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord, java.lang.Integer> ID_EXPERIMENT = createField("id_experiment", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>crowdcontrol.Experiment.title</code>.
	 */
	public final org.jooq.TableField<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord, java.lang.String> TITLE = createField("title", org.jooq.impl.SQLDataType.VARCHAR.length(191), this, "");

	/**
	 * The column <code>crowdcontrol.Experiment.description</code>.
	 */
	public final org.jooq.TableField<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord, java.lang.String> DESCRIPTION = createField("description", org.jooq.impl.SQLDataType.CLOB, this, "");

	/**
	 * The column <code>crowdcontrol.Experiment.needed_answers</code>.
	 */
	public final org.jooq.TableField<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord, java.lang.Integer> NEEDED_ANSWERS = createField("needed_answers", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>crowdcontrol.Experiment.ratings_per_answer</code>.
	 */
	public final org.jooq.TableField<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord, java.lang.Integer> RATINGS_PER_ANSWER = createField("ratings_per_answer", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>crowdcontrol.Experiment.anwers_per_worker</code>.
	 */
	public final org.jooq.TableField<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord, java.lang.Integer> ANWERS_PER_WORKER = createField("anwers_per_worker", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>crowdcontrol.Experiment.ratings_per_worker</code>.
	 */
	public final org.jooq.TableField<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord, java.lang.Integer> RATINGS_PER_WORKER = createField("ratings_per_worker", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>crowdcontrol.Experiment.answer_type</code>.
	 */
	public final org.jooq.TableField<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord, java.lang.String> ANSWER_TYPE = createField("answer_type", org.jooq.impl.SQLDataType.VARCHAR.length(191), this, "");

	/**
	 * The column <code>crowdcontrol.Experiment.algorithm_task_chooser</code>.
	 */
	public final org.jooq.TableField<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord, java.lang.String> ALGORITHM_TASK_CHOOSER = createField("algorithm_task_chooser", org.jooq.impl.SQLDataType.VARCHAR.length(191), this, "");

	/**
	 * The column <code>crowdcontrol.Experiment.algorithm_quality_answer</code>.
	 */
	public final org.jooq.TableField<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord, java.lang.String> ALGORITHM_QUALITY_ANSWER = createField("algorithm_quality_answer", org.jooq.impl.SQLDataType.VARCHAR.length(191), this, "");

	/**
	 * The column <code>crowdcontrol.Experiment.algorithm_quality_rating</code>.
	 */
	public final org.jooq.TableField<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord, java.lang.String> ALGORITHM_QUALITY_RATING = createField("algorithm_quality_rating", org.jooq.impl.SQLDataType.VARCHAR.length(191), this, "");

	/**
	 * The column <code>crowdcontrol.Experiment.base_payment</code>.
	 */
	public final org.jooq.TableField<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord, java.lang.Integer> BASE_PAYMENT = createField("base_payment", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>crowdcontrol.Experiment.bonus_answer</code>.
	 */
	public final org.jooq.TableField<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord, java.lang.Integer> BONUS_ANSWER = createField("bonus_answer", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>crowdcontrol.Experiment.bonus_rating</code>.
	 */
	public final org.jooq.TableField<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord, java.lang.Integer> BONUS_RATING = createField("bonus_rating", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>crowdcontrol.Experiment.template_data</code>.
	 */
	public final org.jooq.TableField<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord, java.lang.String> TEMPLATE_DATA = createField("template_data", org.jooq.impl.SQLDataType.CLOB.length(16777215), this, "");

	/**
	 * The column <code>crowdcontrol.Experiment.template</code>.
	 */
	public final org.jooq.TableField<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord, java.lang.Integer> TEMPLATE = createField("template", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * The column <code>crowdcontrol.Experiment.worker_quality_threshold</code>.
	 */
	public final org.jooq.TableField<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord, java.lang.Integer> WORKER_QUALITY_THRESHOLD = createField("worker_quality_threshold", org.jooq.impl.SQLDataType.INTEGER, this, "");

	/**
	 * Create a <code>crowdcontrol.Experiment</code> table reference
	 */
	public Experiment() {
		this("Experiment", null);
	}

	/**
	 * Create an aliased <code>crowdcontrol.Experiment</code> table reference
	 */
	public Experiment(java.lang.String alias) {
		this(alias, edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Experiment.EXPERIMENT);
	}

	private Experiment(java.lang.String alias, org.jooq.Table<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord> aliased) {
		this(alias, aliased, null);
	}

	private Experiment(java.lang.String alias, org.jooq.Table<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord> aliased, org.jooq.Field<?>[] parameters) {
		super(alias, edu.kit.ipd.crowdcontrol.workerservice.database.model.Crowdcontrol.CROWDCONTROL, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Identity<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord, java.lang.Integer> getIdentity() {
		return edu.kit.ipd.crowdcontrol.workerservice.database.model.Keys.IDENTITY_EXPERIMENT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.UniqueKey<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord> getPrimaryKey() {
		return edu.kit.ipd.crowdcontrol.workerservice.database.model.Keys.KEY_EXPERIMENT_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.UniqueKey<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord>> getKeys() {
		return java.util.Arrays.<org.jooq.UniqueKey<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord>>asList(edu.kit.ipd.crowdcontrol.workerservice.database.model.Keys.KEY_EXPERIMENT_PRIMARY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.util.List<org.jooq.ForeignKey<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord, ?>> getReferences() {
		return java.util.Arrays.<org.jooq.ForeignKey<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord, ?>>asList(edu.kit.ipd.crowdcontrol.workerservice.database.model.Keys.USEDTASKCHOOSER, edu.kit.ipd.crowdcontrol.workerservice.database.model.Keys.USEDANSWERQUALITY, edu.kit.ipd.crowdcontrol.workerservice.database.model.Keys.USEDRATINGQUALITY, edu.kit.ipd.crowdcontrol.workerservice.database.model.Keys.USEDTEMPLATE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Experiment as(java.lang.String alias) {
		return new edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Experiment(alias, this);
	}

	/**
	 * Rename this table
	 */
	public edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Experiment rename(java.lang.String name) {
		return new edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Experiment(name, null);
	}
}
