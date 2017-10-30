package com.stackroute.datamunger.query;

import com.stackroute.datamunger.query.parser.QueryParameter;
import com.stackroute.datamunger.query.parser.Restriction;

//this class contains methods to evaluate expressions
public class Filter {

	/*
	 * the evaluateExpression() method of this class is responsible for evaluating
	 * the expressions mentioned in the query. It has to be noted that the process
	 * of evaluating expressions will be different for different data types. there
	 * are 6 operators that can exist within a query i.e. >=,<=,<,>,!=,= This method
	 * should be able to evaluate all of them. Note: while evaluating string
	 * expressions, please handle uppercase and lowercase
	 * 
	 */
	public Row evaluateExpression(QueryParameter queryParameter, Row row) {
		Row finalRowMap = null;
		if (null == queryParameter) {
			return finalRowMap;
		}
		boolean finalResult = false;
		if (null != queryParameter.getRestrictions() && !queryParameter.getRestrictions().isEmpty()) {
			int index = null != queryParameter.getLogicalOperators() ? queryParameter.getLogicalOperators().size() - 1 : 0;
			String logicalOp = "or";			
			for (Restriction restriction : queryParameter.getRestrictions()) {
				boolean result = false;
				String value = row.get(restriction.getPropertyName());
				switch (restriction.getCondition()) {
				case "=":
					result = this.equalsTo(restriction.getPropertyValue(), value);
					break;
				case "!=":
					result = !this.equalsTo(restriction.getPropertyValue(), value);
					break;
				case ">":
					result = this.greaterThan(restriction.getPropertyValue(), value);
					break;
				case "<":
					result = this.lessThan(restriction.getPropertyValue(), value);
					break;
				case "<=":
					result = (this.lessThan(restriction.getPropertyValue(), value)
							|| this.equalsTo(restriction.getPropertyValue(), value));
					break;
				case ">=":
					result = (this.equalsTo(restriction.getPropertyValue(), value)
							|| this.greaterThan(restriction.getPropertyValue(), value));
					break;
				default:
					System.out.println("No match found");
					break;
				}
				
				if(logicalOp.equals("and")) {
					finalResult = finalResult && result ; 
				} else if(logicalOp.matches("or")) {
					finalResult = finalResult || result ; 
				}
				if(null != queryParameter.getLogicalOperators()
						&& !queryParameter.getLogicalOperators().isEmpty()) {
					if(index >= 0) {
						logicalOp = queryParameter.getLogicalOperators().get(index);
						index--;
					}
					
				}
			}
		} else {
			finalResult = true;
		}
		
		if(finalResult) {
			finalRowMap = new Row();
			if(null != queryParameter.getFields() && !queryParameter.getFields().isEmpty()) {
				for(String fields : queryParameter.getFields()) {
					if(fields.equals("*")) {
						finalRowMap.putAll(row);
						break;
					}
					String value = null;
					if(null != (value = row.get(fields))) {
						finalRowMap.put(fields, value);
					}
				}
			}
		}
		return finalRowMap;
	}

	// method containing implementation of equalTo operator
	private boolean equalsTo(String value1, String value2) {
		if (null == value1 && null == value2) {
			return true;
		}
		if (null == value1 || null == value2) {
			return false;
		}

		return value1.equalsIgnoreCase(value2);
	}

	// method containing implementation of notEqualTo operator
	// Not Required.
	/*
	 * private boolean notEqualsTo(String value1, String value2) { if (null ==
	 * value1 && null == value2) { return false; } if (null == value1 || null ==
	 * value2) { return true; }
	 * 
	 * return !(value1.equalsIgnoreCase(value2)); }
	 */

	// method containing implementation of greaterThan operator
	private boolean greaterThan(String restrictionVal, String dataVal) {
		if (null == restrictionVal || null == dataVal) {
			return false;
		}

		try {
			int restriction = Integer.parseInt(restrictionVal);
			int data = Integer.parseInt(dataVal);
			return data > restriction;
		} catch (NumberFormatException e) {
			// TODO: handle exception
			return false;
		}
	}

	// method containing implementation of greaterThanOrEqualTo operator

	// method containing implementation of lessThan operator
	private boolean lessThan(String restrictionVal, String dataVal) {
		if (null == restrictionVal || null == dataVal) {
			return false;
		}

		try {
			int restriction = Integer.parseInt(restrictionVal);
			int data = Integer.parseInt(dataVal);
			return data < restriction;
		} catch (NumberFormatException e) {
			// TODO: handle exception
			return false;
		}
	}
	// method containing implementation of lessThanOrEqualTo operator

}
