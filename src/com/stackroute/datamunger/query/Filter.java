package com.stackroute.datamunger.query;

import com.stackroute.datamunger.query.parser.QueryParameter;
import com.stackroute.datamunger.query.parser.Restriction;

/**
 * this class contains methods to evaluate expressions
 * 
 * @author PARSAV
 *
 */
public class Filter {

	/**
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
			int index = 0;
			if (null != queryParameter.getLogicalOperators()) {
				index = queryParameter.getLogicalOperators().size() - 1;
			}
			String logicalOp = "or";

			/*
			 * from QueryParameter object, read one condition at a time and evaluate the
			 * same. For evaluating the conditions, we will use evaluateExpressions() .
			 */
			for (int i = queryParameter.getRestrictions().size() - 1; i >= 0; i--) {
				
				final boolean result = isValidData(queryParameter, row, i);

				/*
				 * check for multiple conditions in where clause for eg: where salary>20000 and
				 * city=Bangalore for eg: where salary>20000 or city=Bangalore and dept!=Sales
				 */
				if ("and".equals(logicalOp)) {
					finalResult = finalResult && result;
				} else if ("or".matches(logicalOp)) {
					finalResult = finalResult || result;
				}
				if (null != queryParameter.getLogicalOperators() && !queryParameter.getLogicalOperators().isEmpty()
						&& index >= 0) {
					logicalOp = queryParameter.getLogicalOperators().get(index);
					index--;
				}
			}
		}

		/**
		 * if the overall condition expression evaluates to true, then we need to check
		 * if all columns are to be selected(select *) or few columns are to be
		 * selected(select col1,col2). In either of the cases, we will have to populate
		 * the row map object.
		 */
		/**
		 * Row Map object is having type <String,String> to contain field Index and
		 * field value for the selected fields. Once the row object is populated, add it
		 * to DataSet Map Object. DataSet Map object is having type <Long,Row> to hold
		 * the rowId (to be manually generated by incrementing a Long variable) and it's
		 * corresponding Row Object.
		 */
		if (finalResult || null == queryParameter.getRestrictions() || queryParameter.getRestrictions().isEmpty()) {
			finalRowMap = new Row();
			if (null != queryParameter.getFields() && !queryParameter.getFields().isEmpty()) {
				for (final String fields : queryParameter.getFields()) {
					if ("*".equals(fields)) {
						finalRowMap.putAll(row);
						break;
					}
					String value = null;
					if (null != (value = row.get(fields))) {
						finalRowMap.put(fields, value);
					}
				}
			}
		}
		return finalRowMap;
	}

	private boolean isValidData(final QueryParameter queryParameter, Row row, final int index) {
		final Restriction restriction = queryParameter.getRestrictions().get(index);
		boolean result = false;
		final String value = row.get(restriction.getPropertyName());
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
			result = this.lessThan(restriction.getPropertyValue(), value)
					|| this.equalsTo(restriction.getPropertyValue(), value);
			break;
		case ">=":
			result = this.equalsTo(restriction.getPropertyValue(), value)
					|| this.greaterThan(restriction.getPropertyValue(), value);
			break;
		default:
			break;
		}
		return result;
	}

	// method containing implementation of equalTo operator
	private boolean equalsTo(final String value1, final String value2) {
		boolean flag = false;
		if (null != value1 && null != value2) {
			flag = value1.equalsIgnoreCase(value2);
		}
		return flag;
	}

	// method containing implementation of greaterThan operator
	private boolean greaterThan(final String restrictionVal, final String dataVal) {
		boolean flag = false;
		if (null != restrictionVal && null != dataVal) {
			try {
				final int restriction = Integer.parseInt(restrictionVal);
				final int data = Integer.parseInt(dataVal);
				flag = data > restriction;
			} catch (NumberFormatException e) {
				// TODO: handle exception
				flag = false;
			}
		}
		return flag;
	}

	// method containing implementation of lessThan operator
	private boolean lessThan(final String restrictionVal, final String dataVal) {
		boolean flag = false;
		if (null != restrictionVal && null != dataVal) {
			try {
				final int restriction = Integer.parseInt(restrictionVal);
				final int data = Integer.parseInt(dataVal);
				flag = data < restriction;
			} catch (NumberFormatException e) {
				// TODO: handle exception
				flag = false;
			}
		}
		return flag;
	}
}
