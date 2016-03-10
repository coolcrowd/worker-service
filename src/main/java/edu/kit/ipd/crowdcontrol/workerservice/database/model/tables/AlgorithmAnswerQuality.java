/**
 * This class is generated by jOOQ
 */
package edu.kit.ipd.crowdcontrol.objectservice.database.model.tables;


import edu.kit.ipd.crowdcontrol.objectservice.database.model.Crowdcontrol;
import edu.kit.ipd.crowdcontrol.objectservice.database.model.Keys;
import edu.kit.ipd.crowdcontrol.objectservice.database.model.tables.records.AlgorithmAnswerQualityRecord;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
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
public class AlgorithmAnswerQuality extends TableImpl<AlgorithmAnswerQualityRecord> {

	private static final long serialVersionUID = 1529336931;

	/**
	 * The reference instance of <code>crowdcontrol.Algorithm_Answer_Quality</code>
	 */
	public static final AlgorithmAnswerQuality ALGORITHM_ANSWER_QUALITY = new AlgorithmAnswerQuality();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<AlgorithmAnswerQualityRecord> getRecordType() {
		return AlgorithmAnswerQualityRecord.class;
	}

	/**
	 * The column <code>crowdcontrol.Algorithm_Answer_Quality.id_Algorithm_Answer_Quality</code>.
	 */
	public final TableField<AlgorithmAnswerQualityRecord, String> ID_ALGORITHM_ANSWER_QUALITY = createField("id_Algorithm_Answer_Quality", org.jooq.impl.SQLDataType.VARCHAR.length(191).nullable(false), this, "");

	/**
	 * The column <code>crowdcontrol.Algorithm_Answer_Quality.description</code>.
	 */
	public final TableField<AlgorithmAnswerQualityRecord, String> DESCRIPTION = createField("description", org.jooq.impl.SQLDataType.CLOB.nullable(false), this, "");

	/**
	 * Create a <code>crowdcontrol.Algorithm_Answer_Quality</code> table reference
	 */
	public AlgorithmAnswerQuality() {
		this("Algorithm_Answer_Quality", null);
	}

	/**
	 * Create an aliased <code>crowdcontrol.Algorithm_Answer_Quality</code> table reference
	 */
	public AlgorithmAnswerQuality(String alias) {
		this(alias, ALGORITHM_ANSWER_QUALITY);
	}

	private AlgorithmAnswerQuality(String alias, Table<AlgorithmAnswerQualityRecord> aliased) {
		this(alias, aliased, null);
	}

	private AlgorithmAnswerQuality(String alias, Table<AlgorithmAnswerQualityRecord> aliased, Field<?>[] parameters) {
		super(alias, Crowdcontrol.CROWDCONTROL, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<AlgorithmAnswerQualityRecord> getPrimaryKey() {
		return Keys.KEY_ALGORITHM_ANSWER_QUALITY_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<AlgorithmAnswerQualityRecord>> getKeys() {
		return Arrays.<UniqueKey<AlgorithmAnswerQualityRecord>>asList(Keys.KEY_ALGORITHM_ANSWER_QUALITY_PRIMARY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AlgorithmAnswerQuality as(String alias) {
		return new AlgorithmAnswerQuality(alias, this);
	}

	/**
	 * Rename this table
	 */
	public AlgorithmAnswerQuality rename(String name) {
		return new AlgorithmAnswerQuality(name, null);
	}
}
