/**
 * This class is generated by jOOQ
 */
package edu.kit.ipd.crowdcontrol.workerservice.database.model.tables.pojos;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = { "http://www.jooq.org", "3.4.1" },
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TemplateConstraint implements java.io.Serializable {

	private static final long serialVersionUID = 1931735144;

	private java.lang.Integer idTeamplateConstraint;
	private java.lang.Integer template;
	private java.lang.String  constraint;

	public TemplateConstraint() {}

	public TemplateConstraint(
		java.lang.Integer idTeamplateConstraint,
		java.lang.Integer template,
		java.lang.String  constraint
	) {
		this.idTeamplateConstraint = idTeamplateConstraint;
		this.template = template;
		this.constraint = constraint;
	}

	public java.lang.Integer getIdTeamplateConstraint() {
		return this.idTeamplateConstraint;
	}

	public void setIdTeamplateConstraint(java.lang.Integer idTeamplateConstraint) {
		this.idTeamplateConstraint = idTeamplateConstraint;
	}

	public java.lang.Integer getTemplate() {
		return this.template;
	}

	public void setTemplate(java.lang.Integer template) {
		this.template = template;
	}

	public java.lang.String getConstraint() {
		return this.constraint;
	}

	public void setConstraint(java.lang.String constraint) {
		this.constraint = constraint;
	}
}
