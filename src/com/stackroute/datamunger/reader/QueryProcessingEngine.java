package com.stackroute.datamunger.reader;

import com.stackroute.datamunger.query.DataSet;
import com.stackroute.datamunger.query.parser.QueryParameter;
/**
 * 
 * @author PARSAV
 *
 */
public interface QueryProcessingEngine {

	/**
	 * 
	 * @param queryParameter
	 * @return
	 */
	 DataSet getResultSet(QueryParameter queryParameter);
	
}
