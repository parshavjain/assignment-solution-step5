package com.stackroute.datamunger.query;

import java.util.HashMap;

/**
 * contains the row object as ColumnName/Value. Hence, HashMap is being used
 * @author PARSAV
 *
 */
public class Row extends HashMap<String, String>{
	
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
