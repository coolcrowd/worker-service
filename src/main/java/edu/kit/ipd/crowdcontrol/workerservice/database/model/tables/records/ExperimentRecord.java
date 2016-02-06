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
public class ExperimentRecord extends org.jooq.impl.UpdatableRecordImpl<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.ExperimentRecord> implements org.jooq.Record18<java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer> {

	private static final long serialVersionUID = -843345253;

	/**
	 * Setter for <code>crowdcontrol.Experiment.id_experiment</code>.
	 */
	public void setIdExperiment(java.lang.Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Experiment.id_experiment</code>.
	 */
	public java.lang.Integer getIdExperiment() {
		return (java.lang.Integer) getValue(0);
	}

	/**
	 * Setter for <code>crowdcontrol.Experiment.title</code>.
	 */
	public void setTitle(java.lang.String value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Experiment.title</code>.
	 */
	public java.lang.String getTitle() {
		return (java.lang.String) getValue(1);
	}

	/**
	 * Setter for <code>crowdcontrol.Experiment.description</code>.
	 */
	public void setDescription(java.lang.String value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Experiment.description</code>.
	 */
	public java.lang.String getDescription() {
		return (java.lang.String) getValue(2);
	}

	/**
	 * Setter for <code>crowdcontrol.Experiment.needed_answers</code>.
	 */
	public void setNeededAnswers(java.lang.Integer value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Experiment.needed_answers</code>.
	 */
	public java.lang.Integer getNeededAnswers() {
		return (java.lang.Integer) getValue(3);
	}

	/**
	 * Setter for <code>crowdcontrol.Experiment.ratings_per_answer</code>.
	 */
	public void setRatingsPerAnswer(java.lang.Integer value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Experiment.ratings_per_answer</code>.
	 */
	public java.lang.Integer getRatingsPerAnswer() {
		return (java.lang.Integer) getValue(4);
	}

	/**
	 * Setter for <code>crowdcontrol.Experiment.anwers_per_worker</code>.
	 */
	public void setAnwersPerWorker(java.lang.Integer value) {
		setValue(5, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Experiment.anwers_per_worker</code>.
	 */
	public java.lang.Integer getAnwersPerWorker() {
		return (java.lang.Integer) getValue(5);
	}

	/**
	 * Setter for <code>crowdcontrol.Experiment.ratings_per_worker</code>.
	 */
	public void setRatingsPerWorker(java.lang.Integer value) {
		setValue(6, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Experiment.ratings_per_worker</code>.
	 */
	public java.lang.Integer getRatingsPerWorker() {
		return (java.lang.Integer) getValue(6);
	}

	/**
	 * Setter for <code>crowdcontrol.Experiment.answer_type</code>.
	 */
	public void setAnswerType(java.lang.String value) {
		setValue(7, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Experiment.answer_type</code>.
	 */
	public java.lang.String getAnswerType() {
		return (java.lang.String) getValue(7);
	}

	/**
	 * Setter for <code>crowdcontrol.Experiment.algorithm_task_chooser</code>.
	 */
	public void setAlgorithmTaskChooser(java.lang.String value) {
		setValue(8, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Experiment.algorithm_task_chooser</code>.
	 */
	public java.lang.String getAlgorithmTaskChooser() {
		return (java.lang.String) getValue(8);
	}

	/**
	 * Setter for <code>crowdcontrol.Experiment.algorithm_quality_answer</code>.
	 */
	public void setAlgorithmQualityAnswer(java.lang.String value) {
		setValue(9, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Experiment.algorithm_quality_answer</code>.
	 */
	public java.lang.String getAlgorithmQualityAnswer() {
		return (java.lang.String) getValue(9);
	}

	/**
	 * Setter for <code>crowdcontrol.Experiment.algorithm_quality_rating</code>.
	 */
	public void setAlgorithmQualityRating(java.lang.String value) {
		setValue(10, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Experiment.algorithm_quality_rating</code>.
	 */
	public java.lang.String getAlgorithmQualityRating() {
		return (java.lang.String) getValue(10);
	}

	/**
	 * Setter for <code>crowdcontrol.Experiment.base_payment</code>.
	 */
	public void setBasePayment(java.lang.Integer value) {
		setValue(11, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Experiment.base_payment</code>.
	 */
	public java.lang.Integer getBasePayment() {
		return (java.lang.Integer) getValue(11);
	}

	/**
	 * Setter for <code>crowdcontrol.Experiment.bonus_answer</code>.
	 */
	public void setBonusAnswer(java.lang.Integer value) {
		setValue(12, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Experiment.bonus_answer</code>.
	 */
	public java.lang.Integer getBonusAnswer() {
		return (java.lang.Integer) getValue(12);
	}

	/**
	 * Setter for <code>crowdcontrol.Experiment.bonus_rating</code>.
	 */
	public void setBonusRating(java.lang.Integer value) {
		setValue(13, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Experiment.bonus_rating</code>.
	 */
	public java.lang.Integer getBonusRating() {
		return (java.lang.Integer) getValue(13);
	}

	/**
	 * Setter for <code>crowdcontrol.Experiment.template_data</code>.
	 */
	public void setTemplateData(java.lang.String value) {
		setValue(14, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Experiment.template_data</code>.
	 */
	public java.lang.String getTemplateData() {
		return (java.lang.String) getValue(14);
	}

	/**
	 * Setter for <code>crowdcontrol.Experiment.template</code>.
	 */
	public void setTemplate(java.lang.Integer value) {
		setValue(15, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Experiment.template</code>.
	 */
	public java.lang.Integer getTemplate() {
		return (java.lang.Integer) getValue(15);
	}

	/**
	 * Setter for <code>crowdcontrol.Experiment.worker_quality_threshold</code>.
	 */
	public void setWorkerQualityThreshold(java.lang.Integer value) {
		setValue(16, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Experiment.worker_quality_threshold</code>.
	 */
	public java.lang.Integer getWorkerQualityThreshold() {
		return (java.lang.Integer) getValue(16);
	}

	/**
	 * Setter for <code>crowdcontrol.Experiment.payment_quality_threshold</code>.
	 */
	public void setPaymentQualityThreshold(java.lang.Integer value) {
		setValue(17, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Experiment.payment_quality_threshold</code>.
	 */
	public java.lang.Integer getPaymentQualityThreshold() {
		return (java.lang.Integer) getValue(17);
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
	// Record18 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row18<java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer> fieldsRow() {
		return (org.jooq.Row18) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row18<java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer> valuesRow() {
		return (org.jooq.Row18) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Experiment.EXPERIMENT.ID_EXPERIMENT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field2() {
		return edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Experiment.EXPERIMENT.TITLE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field3() {
		return edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Experiment.EXPERIMENT.DESCRIPTION;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field4() {
		return edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Experiment.EXPERIMENT.NEEDED_ANSWERS;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field5() {
		return edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Experiment.EXPERIMENT.RATINGS_PER_ANSWER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field6() {
		return edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Experiment.EXPERIMENT.ANWERS_PER_WORKER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field7() {
		return edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Experiment.EXPERIMENT.RATINGS_PER_WORKER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field8() {
		return edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Experiment.EXPERIMENT.ANSWER_TYPE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field9() {
		return edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Experiment.EXPERIMENT.ALGORITHM_TASK_CHOOSER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field10() {
		return edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Experiment.EXPERIMENT.ALGORITHM_QUALITY_ANSWER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field11() {
		return edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Experiment.EXPERIMENT.ALGORITHM_QUALITY_RATING;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field12() {
		return edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Experiment.EXPERIMENT.BASE_PAYMENT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field13() {
		return edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Experiment.EXPERIMENT.BONUS_ANSWER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field14() {
		return edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Experiment.EXPERIMENT.BONUS_RATING;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field15() {
		return edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Experiment.EXPERIMENT.TEMPLATE_DATA;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field16() {
		return edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Experiment.EXPERIMENT.TEMPLATE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field17() {
		return edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Experiment.EXPERIMENT.WORKER_QUALITY_THRESHOLD;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field18() {
		return edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Experiment.EXPERIMENT.PAYMENT_QUALITY_THRESHOLD;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value1() {
		return getIdExperiment();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value2() {
		return getTitle();
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
		return getNeededAnswers();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value5() {
		return getRatingsPerAnswer();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value6() {
		return getAnwersPerWorker();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value7() {
		return getRatingsPerWorker();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value8() {
		return getAnswerType();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value9() {
		return getAlgorithmTaskChooser();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value10() {
		return getAlgorithmQualityAnswer();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value11() {
		return getAlgorithmQualityRating();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value12() {
		return getBasePayment();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value13() {
		return getBonusAnswer();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value14() {
		return getBonusRating();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value15() {
		return getTemplateData();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value16() {
		return getTemplate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value17() {
		return getWorkerQualityThreshold();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value18() {
		return getPaymentQualityThreshold();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExperimentRecord value1(java.lang.Integer value) {
		setIdExperiment(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExperimentRecord value2(java.lang.String value) {
		setTitle(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExperimentRecord value3(java.lang.String value) {
		setDescription(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExperimentRecord value4(java.lang.Integer value) {
		setNeededAnswers(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExperimentRecord value5(java.lang.Integer value) {
		setRatingsPerAnswer(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExperimentRecord value6(java.lang.Integer value) {
		setAnwersPerWorker(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExperimentRecord value7(java.lang.Integer value) {
		setRatingsPerWorker(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExperimentRecord value8(java.lang.String value) {
		setAnswerType(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExperimentRecord value9(java.lang.String value) {
		setAlgorithmTaskChooser(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExperimentRecord value10(java.lang.String value) {
		setAlgorithmQualityAnswer(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExperimentRecord value11(java.lang.String value) {
		setAlgorithmQualityRating(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExperimentRecord value12(java.lang.Integer value) {
		setBasePayment(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExperimentRecord value13(java.lang.Integer value) {
		setBonusAnswer(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExperimentRecord value14(java.lang.Integer value) {
		setBonusRating(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExperimentRecord value15(java.lang.String value) {
		setTemplateData(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExperimentRecord value16(java.lang.Integer value) {
		setTemplate(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExperimentRecord value17(java.lang.Integer value) {
		setWorkerQualityThreshold(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExperimentRecord value18(java.lang.Integer value) {
		setPaymentQualityThreshold(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ExperimentRecord values(java.lang.Integer value1, java.lang.String value2, java.lang.String value3, java.lang.Integer value4, java.lang.Integer value5, java.lang.Integer value6, java.lang.Integer value7, java.lang.String value8, java.lang.String value9, java.lang.String value10, java.lang.String value11, java.lang.Integer value12, java.lang.Integer value13, java.lang.Integer value14, java.lang.String value15, java.lang.Integer value16, java.lang.Integer value17, java.lang.Integer value18) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached ExperimentRecord
	 */
	public ExperimentRecord() {
		super(edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Experiment.EXPERIMENT);
	}

	/**
	 * Create a detached, initialised ExperimentRecord
	 */
	public ExperimentRecord(java.lang.Integer idExperiment, java.lang.String title, java.lang.String description, java.lang.Integer neededAnswers, java.lang.Integer ratingsPerAnswer, java.lang.Integer anwersPerWorker, java.lang.Integer ratingsPerWorker, java.lang.String answerType, java.lang.String algorithmTaskChooser, java.lang.String algorithmQualityAnswer, java.lang.String algorithmQualityRating, java.lang.Integer basePayment, java.lang.Integer bonusAnswer, java.lang.Integer bonusRating, java.lang.String templateData, java.lang.Integer template, java.lang.Integer workerQualityThreshold, java.lang.Integer paymentQualityThreshold) {
		super(edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Experiment.EXPERIMENT);

		setValue(0, idExperiment);
		setValue(1, title);
		setValue(2, description);
		setValue(3, neededAnswers);
		setValue(4, ratingsPerAnswer);
		setValue(5, anwersPerWorker);
		setValue(6, ratingsPerWorker);
		setValue(7, answerType);
		setValue(8, algorithmTaskChooser);
		setValue(9, algorithmQualityAnswer);
		setValue(10, algorithmQualityRating);
		setValue(11, basePayment);
		setValue(12, bonusAnswer);
		setValue(13, bonusRating);
		setValue(14, templateData);
		setValue(15, template);
		setValue(16, workerQualityThreshold);
		setValue(17, paymentQualityThreshold);
	}
}
