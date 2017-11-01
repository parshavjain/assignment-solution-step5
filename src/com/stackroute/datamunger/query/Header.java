package com.stackroute.datamunger.query;

import java.util.HashMap;

/**
 * header class containing a Collection containing the headers
 * @author PARSAV
 *
 */
public class Header extends HashMap<String, Integer> {
	
	/**
	 * Overriding get Method.
	 */
	@Override
	public Integer get(final Object key) {
		Integer value = null;
		if (containsKey(key)) {
			value = super.get(key);
        }
		return value;
		
	}
}
