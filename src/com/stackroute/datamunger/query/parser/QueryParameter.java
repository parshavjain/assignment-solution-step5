package com.stackroute.datamunger.query.parser;

import java.util.ArrayList;
import java.util.List;

/* 
 * This class will contain the elements of the parsed Query String such as conditions,
 * logical operators,aggregate functions, file name, fields group by fields, order by
 * fields, Query Type
 * */
public class QueryParameter {

	private String file;
	private String baseQuery;
	private List<String> fields;
	private String queryType;
	private List<Restriction> restrictions;
	private List<String> logicalOperators;
	private List<AggregateFunction> aggregateFunctions;
	private List<String> orderByFields;
	private List<String> groupByFields;

	/**
	 * @return the file
	 */
	public String getFile() {
		return file;
	}

	/**
	 * @param file
	 *            the file to set
	 */
	public void setFile(String file) {
		this.file = file;
	}

	/**
	 * @return the baseQuery
	 */
	public String getBaseQuery() {
		return baseQuery;
	}

	/**
	 * @param baseQuery
	 *            the baseQuery to set
	 */
	public void setBaseQuery(String baseQuery) {
		this.baseQuery = baseQuery;
	}

	/**
	 * @return the qUERY_TYPE
	 */
	public String getQueryType() {
		return queryType;
	}

	/**
	 * @param qUERY_TYPE
	 *            the qUERY_TYPE to set
	 */
	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	/**
	 * @return the fields
	 */
	public List<String> getFields() {
		if (null == this.fields) {
			this.fields = new ArrayList<String>();
		}
		return this.fields;
	}

	/**
	 * @return the restrictions
	 */
	public List<Restriction> getRestrictions() {
		return this.restrictions;
	}

	/**
	 * @param restrictions
	 *            the restrictions to set
	 */
	public void setRestrictions(List<Restriction> restrictions) {
		this.restrictions = restrictions;
	}

	/**
	 * @return the logicalOperators
	 */
	public List<String> getLogicalOperators() {
		return this.logicalOperators;
	}

	/**
	 * @param logicalOperators
	 *            the logicalOperators to set
	 */
	public void setLogicalOperators(List<String> logicalOperators) {
		this.logicalOperators = logicalOperators;
	}

	/**
	 * @return the aggregateFunctions
	 */
	public List<AggregateFunction> getAggregateFunctions() {
		if (null == this.aggregateFunctions) {
			this.aggregateFunctions = new ArrayList<AggregateFunction>();
		}
		return aggregateFunctions;
	}

	/**
	 * @return the orderByFields
	 */
	public List<String> getOrderByFields() {
		if (null == this.orderByFields) {
			this.orderByFields = new ArrayList<String>();
		}
		return this.orderByFields;
	}

	/**
	 * @return the groupByFields
	 */
	public List<String> getGroupByFields() {
		if (null == this.groupByFields) {
			this.groupByFields = new ArrayList<String>();
		}
		return this.groupByFields;
	}
}