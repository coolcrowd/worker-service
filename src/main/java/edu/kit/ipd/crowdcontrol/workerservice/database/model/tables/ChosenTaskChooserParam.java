/**
 * This class is generated by jOOQ
 */
package edu.kit.ipd.crowdcontrol.objectservice.database.model.tables;


import edu.kit.ipd.crowdcontrol.objectservice.database.model.Crowdcontrol;
import edu.kit.ipd.crowdcontrol.objectservice.database.model.Keys;
import edu.kit.ipd.crowdcontrol.objectservice.database.model.tables.records.ChosenTaskChooserParamRecord;

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
public class ChosenTaskChooserParam extends TableImpl<ChosenTaskChooserParamRecord> {

	private static final long serialVersionUID = -1746333626;

	/**
	 * The reference instance of <code>crowdcontrol.Chosen_Task_Chooser_Param</code>
	 */
	public static final ChosenTaskChooserParam CHOSEN_TASK_CHOOSER_PARAM = new ChosenTaskChooserParam();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<ChosenTaskChooserParamRecord> getRecordType() {
		return ChosenTaskChooserParamRecord.class;
	}

	/**
	 * The column <code>crowdcontrol.Chosen_Task_Chooser_Param.id_Choosen_Task_Chooser_Param</code>.
	 */
	public final TableField<ChosenTaskChooserParamRecord, Integer> ID_CHOOSEN_TASK_CHOOSER_PARAM = createField("id_Choosen_Task_Chooser_Param", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>crowdcontrol.Chosen_Task_Chooser_Param.value</code>.
	 */
	public final TableField<ChosenTaskChooserParamRecord, String> VALUE = createField("value", org.jooq.impl.SQLDataType.VARCHAR.length(191).nullable(false), this, "");

	/**
	 * The column <code>crowdcontrol.Chosen_Task_Chooser_Param.experiment</code>.
	 */
	public final TableField<ChosenTaskChooserParamRecord, Integer> EXPERIMENT = createField("experiment", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>crowdcontrol.Chosen_Task_Chooser_Param.param</code>.
	 */
	public final TableField<ChosenTaskChooserParamRecord, Integer> PARAM = createField("param", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * Create a <code>crowdcontrol.Chosen_Task_Chooser_Param</code> table reference
	 */
	public ChosenTaskChooserParam() {
		this("Chosen_Task_Chooser_Param", null);
	}

	/**
	 * Create an aliased <code>crowdcontrol.Chosen_Task_Chooser_Param</code> table reference
	 */
	public ChosenTaskChooserParam(String alias) {
		this(alias, CHOSEN_TASK_CHOOSER_PARAM);
	}

	private ChosenTaskChooserParam(String alias, Table<ChosenTaskChooserParamRecord> aliased) {
		this(alias, aliased, null);
	}

	private ChosenTaskChooserParam(String alias, Table<ChosenTaskChooserParamRecord> aliased, Field<?>[] parameters) {
		super(alias, Crowdcontrol.CROWDCONTROL, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Identity<ChosenTaskChooserParamRecord, Integer> getIdentity() {
		return Keys.IDENTITY_CHOSEN_TASK_CHOOSER_PARAM;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<ChosenTaskChooserParamRecord> getPrimaryKey() {
		return Keys.KEY_CHOSEN_TASK_CHOOSER_PARAM_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<ChosenTaskChooserParamRecord>> getKeys() {
		return Arrays.<UniqueKey<ChosenTaskChooserParamRecord>>asList(Keys.KEY_CHOSEN_TASK_CHOOSER_PARAM_PRIMARY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ForeignKey<ChosenTaskChooserParamRecord, ?>> getReferences() {
		return Arrays.<ForeignKey<ChosenTaskChooserParamRecord, ?>>asList(Keys.TASKCHOOSERPARAMREFEXPERIMENT, Keys.CHOOSENTASKCHOOSERPARAM);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ChosenTaskChooserParam as(String alias) {
		return new ChosenTaskChooserParam(alias, this);
	}

	/**
	 * Rename this table
	 */
	public ChosenTaskChooserParam rename(String name) {
		return new ChosenTaskChooserParam(name, null);
	}
}
