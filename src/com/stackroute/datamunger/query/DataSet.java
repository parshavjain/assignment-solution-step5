package com.stackroute.datamunger.query;


import java.util.LinkedHashMap;

/**
 * this class will be acting as the DataSet containing multiple rows
 * @author PRATIK
 *
 */
public class DataSet extends LinkedHashMap<Long, Row> {
	
	/**
	 * Overriding get Method.
	 */
	@Override
	public Row get(final Object key) {
		Row value = null;
		if (containsKey(key)) {
			value = super.get(key);
        }
		return value;
	}

}
