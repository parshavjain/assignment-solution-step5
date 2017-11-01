package com.stackroute.datamunger.query;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/*
 * implementation of DataTypeDefinitions class. This class contains a method getDataTypes() 
 * which will contain the logic for getting the datatype for a given field value. This
 * method will be called from QueryProcessors.   
 * In this assignment, we are going to use Regular Expression to find the 
 * appropriate data type of a field. 
 * Integers: should contain only digits without decimal point 
 * Double: should contain digits as well as decimal point 
 * Date: Dates can be written in many formats in the CSV file. 
 * However, in this assignment,we will test for the following date formats('dd/mm/yyyy',
 * 'mm/dd/yyyy','dd-mon-yy','dd-mon-yyyy','dd-month-yy','dd-month-yyyy','yyyy-mm-dd')
 */
public class DataTypeDefinitions {

	// Regular Expression for determining Integer value.
	private static final String INT_REGEX = "^[0-9]+$";

	// Regular Expression for determining Float value.
	private static final String FLOAT_REGEX = "[+-]?([0-9]*[.])[0-9]+";

	// Regular Expression for determining vaious Date Formats.
	// checking for date format dd/mm/yyyy
	private static final String DDMMYYYY_REGEX = "(([12][0-9]|3[01]|0?[1-9])/(0?[1-9]|1[012])/(?:19|20)[0-9]{1}[0-9]{1})";
	// checking for date format mm/dd/yyyy
	private static final String MMDDYYYY_REGEX = "((0?[1-9]|1[012])/([12][0-9]|3[01]|0?[1-9])/(?:19|20)[0-9]{1}[0-9]{1})";
	// date format dd-mon-yy
	private static final String DD_MON_YY_REGEX = "(([12][0-9]|3[01]|0?[1-9])-([a-z]{3})-(?)[0-9]{1}[0-9]{1})";
	// date format dd-mon-yyyy
	private static final String DD_MON_YYYY_REGEX = "(([12][0-9]|3[01]|0?[1-9])-([a-z]{3})-(?:19|20)[0-9]{1}[0-9]{1})";
	// date format dd-month-yy
	private static final String DD_MONTH_YY_REGEX = "(([12][0-9]|3[01]|0?[1-9])-([a-z])-(?)[0-9]{1}[0-9]{1})";
	// date format dd-month-yyyy
	private static final String DD_MONTH_YYYY_REGEX = "(([12][0-9]|3[01]|0?[1-9])-([a-z])-(?:19|20)[0-9]{1}[0-9]{1})";
	// date format yyyy-mm-dd
	private static final String YYYYMMDD_REGEX = "((?:19|20)[0-9]{1}[0-9]{1})-(0?[1-9]|1[012])-([12][0-9]|3[01]|0?[1-9])";

	// method stub
	public static Object getDataType(String input) {
		if (null == input || input.isEmpty()) {
			// System.out.println("matched Object");
			return Object.class;
		}
		// checking for Integer
		if (Pattern.matches(INT_REGEX, input)) {

			// System.out.println("matched Integer");
			return Integer.class;
		}
		// checking for floating point numbers
		if (Pattern.matches(FLOAT_REGEX, input)) {
			// System.out.println("matched float");
			return Float.class;
		}
		// checking for date format dd/mm/yyyy
		if (Pattern.matches(DDMMYYYY_REGEX, input)) {
			// System.out.println("matched date");
			return Date.class;
		}
		// checking for date format mm/dd/yyyy
		if (Pattern.matches(MMDDYYYY_REGEX, input)) {
			// System.out.println("matched date");
			return Date.class;
		}
		// checking for date format dd-mon-yy
		if (Pattern.matches(DD_MON_YY_REGEX, input)) {
			// System.out.println("matched date");
			return Date.class;
		}
		// checking for date format dd-mon-yyyy
		if (Pattern.matches(DD_MON_YYYY_REGEX, input)) {
			// System.out.println("matched date");
			return Date.class;
		}
		// checking for date format dd-month-yy
		if (Pattern.matches(DD_MONTH_YY_REGEX, input)) {
			// System.out.println("matched date");
			return Date.class;
		}
		// checking for date format dd-month-yyyy
		if (Pattern.matches(DD_MONTH_YYYY_REGEX, input)) {
			// System.out.println("matched date");
			return Date.class;
		}
		// checking for date format yyyy-mm-dd
		if (Pattern.matches(YYYYMMDD_REGEX, input)) {
			// System.out.println("matched date");
			return Date.class;
		}
		// System.out.println("matched String");
		return String.class;
	}
}