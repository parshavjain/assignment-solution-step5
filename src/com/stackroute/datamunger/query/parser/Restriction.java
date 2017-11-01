package com.stackroute.datamunger.query.parser;

/**
 * This class is used for storing name of field, condition and value for 
 * each conditions
 */
public class Restriction {

	/**
	 * Holds propertyName.
	 */
	private String propertyName;
	
	/**
	 * Holds propertyValue.
	 */
	private String propertyValue;
	
	/**
	 * Holds condition.
	 */
	private String condition;
	
	/**
	 * Parameterized constructor.
	 */
	public Restriction(final String propertyName, final String propertyValue, final String condition) {
		this.propertyName = propertyName;
		this.propertyValue = propertyValue;
		this.condition = condition;
	}

	/**
	 * @return the propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * @param propertyName
	 *            the propertyName to set
	 */
	public void setPropertyName(final String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * @return the propertyValue
	 */
	public String getPropertyValue() {
		return propertyValue;
	}

	/**
	 * @param propertyValue
	 *            the propertyValue to set
	 */
	public void setPropertyValue(final String propertyValue) {
		this.propertyValue = propertyValue;
	}

	/**
	 * @return the condition
	 */
	public String getCondition() {
		return condition;
	}

	/**
	 * @param condition
	 *            the condition to set
	 */
	public void setCondition(final String condition) {
		this.condition = condition;
	}
}
