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
public class Constraint implements java.io.Serializable {

	private static final long serialVersionUID = -1981472900;

	private java.lang.Integer idConstraint;
	private java.lang.String  constraint;
	private java.lang.Integer experiment;

	public Constraint() {}

	public Constraint(
		java.lang.Integer idConstraint,
		java.lang.String  constraint,
		java.lang.Integer experiment
	) {
		this.idConstraint = idConstraint;
		this.constraint = constraint;
		this.experiment = experiment;
	}

	public java.lang.Integer getIdConstraint() {
		return this.idConstraint;
	}

	public void setIdConstraint(java.lang.Integer idConstraint) {
		this.idConstraint = idConstraint;
	}

	public java.lang.String getConstraint() {
		return this.constraint;
	}

	public void setConstraint(java.lang.String constraint) {
		this.constraint = constraint;
	}

	public java.lang.Integer getExperiment() {
		return this.experiment;
	}

	public void setExperiment(java.lang.Integer experiment) {
		this.experiment = experiment;
	}
}
