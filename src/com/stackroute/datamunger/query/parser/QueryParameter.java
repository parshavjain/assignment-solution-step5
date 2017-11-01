package com.stackroute.datamunger.query.parser;

import java.util.ArrayList;
import java.util.List;

/** 
 * This class will contain the elements of the parsed Query String.
 */
public class QueryParameter {

	/**
	 * contains File Name.
	 */
	private String file;
	
	/**
	 * Contains base part of query.
	 */
	private String baseQuery;
	
	/**
	 * contains all the fields of query.
	 */
	private transient List<String> fields;
	
	/**
	 * contains type of query.
	 */
	final private String QUERY_TYPE;
	
	/**
	 * Contains Restriction.
	 */
	private List<Restriction> restrictions;
	
	/**
	 * Contains Logical operator.
	 */
	private List<String> logicalOperators;
	
	/**
	 * contains Aggregate functions.
	 */
	private transient List<AggregateFunction> aggregateFunc;
	
	/**
	 * Contains Order By Fields.
	 */
	private transient List<String> orderByFields;
	
	/**
	 * Contains Group by Fields.
	 */
	private transient List<String> groupByFields;
	
	/**
	 * @param queryType
	 *            the queryType to set
	 */
	public QueryParameter(final String queryType) {
		// TODO Auto-generated constructor stub
		this.QUERY_TYPE = queryType;
	}

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
	public void setFile(final String file) {
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
	public void setBaseQuery(final String baseQuery) {
		this.baseQuery = baseQuery;
	}

	/**
	 * @return the qUERY_TYPE
	 */
	public String getQUERY_TYPE() {
		return QUERY_TYPE;
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
	public void setRestrictions(final List<Restriction> restrictions) {
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
	public void setLogicalOperators(final List<String> logicalOperators) {
		this.logicalOperators = logicalOperators;
	}

	/**
	 * @return the aggregateFunctions
	 */
	public List<AggregateFunction> getAggregateFunctions() {
		if (null == this.aggregateFunc) {
			this.aggregateFunc = new ArrayList<AggregateFunction>();
		}
		return aggregateFunc;
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

	/**
	 * @param fields the fields to set
	 */
	public void setFields(final List<String> fields) {
		this.fields = fields;
	}

	/**
	 * @param orderByFields the orderByFields to set
	 */
	public void setOrderByFields(final List<String> orderByFields) {
		this.orderByFields = orderByFields;
	}

	/**
	 * @param groupByFields the groupByFields to set
	 */
	public void setGroupByFields(final List<String> groupByFields) {
		this.groupByFields = groupByFields;
	}

	/**
	 * @param aggregateFunc the aggregateFunc to set
	 */
	public void setAggregateFunc(final List<AggregateFunction> aggregateFunc) {
		this.aggregateFunc = aggregateFunc;
	}
}