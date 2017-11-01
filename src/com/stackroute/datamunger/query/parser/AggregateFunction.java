package com.stackroute.datamunger.query.parser;

/** This class is used for storing name of field, aggregate function for 
 * each aggregate function
 */
public class AggregateFunction {

	/**
	 * Holds AggregateName
	 */
	private transient String aggregateName;
	
	/**
	 * Holds AggregateValue.
	 */
	private transient String aggregateValue;
	
	/**
	 * Parameterized constructor.
	 * 
	 * @param aggregateName
	 * @param aggregateValue
	 */
	public AggregateFunction(final String aggregateName, final String aggregateValue) {
		this.aggregateName = aggregateName;
		this.aggregateValue= aggregateValue;
	}

	/**
	 * @param aggregateName
	 *            the aggregateName to set
	 */
	public void setAggregateName(final String aggregateName) {
		this.aggregateName = aggregateName;
	}

	/**
	 * @param aggregateValue
	 *            the aggregateValue to set
	 */
	public void setAggregateValue(final String aggregateValue) {
		this.aggregateValue = aggregateValue;
	}

	/**
	 * return aggregateValue
	 *            the aggregateValue to get
	 */
	public String getField() {
		return aggregateValue;
	}

	/**
	 * return aggregateName
	 *            the aggregateName to get
	 */
	public String getFunction() {
		return aggregateName;
	}
}