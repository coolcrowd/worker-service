/**
 * This class is generated by jOOQ
 */
package edu.kit.ipd.crowdcontrol.objectservice.database.model.tables.records;


import edu.kit.ipd.crowdcontrol.objectservice.database.model.tables.Template;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
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
public class TemplateRecord extends UpdatableRecordImpl<TemplateRecord> implements Record4<Integer, String, String, String> {

	private static final long serialVersionUID = 1612110174;

	/**
	 * Setter for <code>crowdcontrol.Template.id_template</code>.
	 */
	public void setIdTemplate(Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Template.id_template</code>.
	 */
	public Integer getIdTemplate() {
		return (Integer) getValue(0);
	}

	/**
	 * Setter for <code>crowdcontrol.Template.template</code>.
	 */
	public void setTemplate(String value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Template.template</code>.
	 */
	public String getTemplate() {
		return (String) getValue(1);
	}

	/**
	 * Setter for <code>crowdcontrol.Template.title</code>.
	 */
	public void setTitle(String value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Template.title</code>.
	 */
	public String getTitle() {
		return (String) getValue(2);
	}

	/**
	 * Setter for <code>crowdcontrol.Template.answer_type</code>.
	 */
	public void setAnswerType(String value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>crowdcontrol.Template.answer_type</code>.
	 */
	public String getAnswerType() {
		return (String) getValue(3);
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
	// Record4 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row4<Integer, String, String, String> fieldsRow() {
		return (Row4) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row4<Integer, String, String, String> valuesRow() {
		return (Row4) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field1() {
		return Template.TEMPLATE.ID_TEMPLATE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field2() {
		return Template.TEMPLATE.TEMPLATE_;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field3() {
		return Template.TEMPLATE.TITLE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field4() {
		return Template.TEMPLATE.ANSWER_TYPE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value1() {
		return getIdTemplate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value2() {
		return getTemplate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value3() {
		return getTitle();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value4() {
		return getAnswerType();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TemplateRecord value1(Integer value) {
		setIdTemplate(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TemplateRecord value2(String value) {
		setTemplate(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TemplateRecord value3(String value) {
		setTitle(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TemplateRecord value4(String value) {
		setAnswerType(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TemplateRecord values(Integer value1, String value2, String value3, String value4) {
		value1(value1);
		value2(value2);
		value3(value3);
		value4(value4);
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached TemplateRecord
	 */
	public TemplateRecord() {
		super(Template.TEMPLATE);
	}

	/**
	 * Create a detached, initialised TemplateRecord
	 */
	public TemplateRecord(Integer idTemplate, String template, String title, String answerType) {
		super(Template.TEMPLATE);

		setValue(0, idTemplate);
		setValue(1, template);
		setValue(2, title);
		setValue(3, answerType);
	}
}
