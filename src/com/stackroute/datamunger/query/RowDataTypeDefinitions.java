package com.stackroute.datamunger.query;

import java.util.HashMap;

/**
 * this class will be used to store the column data types as
 * columnIndex/DataType
 * 
 * @author PARSAV
 *
 */
public class RowDataTypeDefinitions extends HashMap<Integer, String> {

	/**
	 * Overriding get Method.
	 */
	@Override
	public String get(final Object key) {
		String value = null;
		if (containsKey(key)) {
			value = super.get(key);
		}
		return value;
	}

}
