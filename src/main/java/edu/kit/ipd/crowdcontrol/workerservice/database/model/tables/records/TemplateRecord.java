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
public class TemplateRecord extends org.jooq.impl.UpdatableRecordImpl<edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.records.TemplateRecord> implements org.jooq.Record4<java.lang.Integer, java.lang.String, java.lang.String, java.lang.String> {

	private static final long serialVersionUID = 25989027;

	/**
	 * Setter for <code>crowdcontrol.Template.id_template</code>.
	 */
	public void setIdTemplate(java.lang.Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Template.id_template</code>.
	 */
	public java.lang.Integer getIdTemplate() {
		return (java.lang.Integer) getValue(0);
	}

	/**
	 * Setter for <code>crowdcontrol.Template.template</code>.
	 */
	public void setTemplate(java.lang.String value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Template.template</code>.
	 */
	public java.lang.String getTemplate() {
		return (java.lang.String) getValue(1);
	}

	/**
	 * Setter for <code>crowdcontrol.Template.title</code>.
	 */
	public void setTitle(java.lang.String value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Template.title</code>.
	 */
	public java.lang.String getTitle() {
		return (java.lang.String) getValue(2);
	}

	/**
	 * Setter for <code>crowdcontrol.Template.answer_type</code>.
	 */
	public void setAnswerType(java.lang.String value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Template.answer_type</code>.
	 */
	public java.lang.String getAnswerType() {
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
	public org.jooq.Row4<java.lang.Integer, java.lang.String, java.lang.String, java.lang.String> fieldsRow() {
		return (org.jooq.Row4) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Row4<java.lang.Integer, java.lang.String, java.lang.String, java.lang.String> valuesRow() {
		return (org.jooq.Row4) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.Integer> field1() {
		return edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Template.TEMPLATE.ID_TEMPLATE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field2() {
		return edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Template.TEMPLATE.TEMPLATE_;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field3() {
		return edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Template.TEMPLATE.TITLE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public org.jooq.Field<java.lang.String> field4() {
		return edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Template.TEMPLATE.ANSWER_TYPE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.Integer value1() {
		return getIdTemplate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value2() {
		return getTemplate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value3() {
		return getTitle();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public java.lang.String value4() {
		return getAnswerType();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TemplateRecord value1(java.lang.Integer value) {
		setIdTemplate(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TemplateRecord value2(java.lang.String value) {
		setTemplate(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TemplateRecord value3(java.lang.String value) {
		setTitle(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TemplateRecord value4(java.lang.String value) {
		setAnswerType(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TemplateRecord values(java.lang.Integer value1, java.lang.String value2, java.lang.String value3, java.lang.String value4) {
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached TemplateRecord
	 */
	public TemplateRecord() {
		super(edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Template.TEMPLATE);
	}

	/**
	 * Create a detached, initialised TemplateRecord
	 */
	public TemplateRecord(java.lang.Integer idTemplate, java.lang.String template, java.lang.String title, java.lang.String answerType) {
		super(edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.Template.TEMPLATE);

		setValue(0, idTemplate);
		setValue(1, template);
		setValue(2, title);
		setValue(3, answerType);
	}
}
