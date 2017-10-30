package com.stackroute.datamunger.reader;

import java.io.BufferedReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.stackroute.datamunger.query.DataSet;
import com.stackroute.datamunger.query.DataTypeDefinitions;
import com.stackroute.datamunger.query.Filter;
import com.stackroute.datamunger.query.Header;
import com.stackroute.datamunger.query.Row;
import com.stackroute.datamunger.query.RowDataTypeDefinitions;
import com.stackroute.datamunger.query.parser.QueryParameter;
import com.stackroute.datamunger.query.parser.Restriction;

public class CsvQueryProcessor implements QueryProcessingEngine {
	/*
	 * This method will take QueryParameter object as a parameter which contains the
	 * parsed query and will process and populate the ResultSet
	 */
	public DataSet getResultSet(QueryParameter queryParameter) {
		if (null == queryParameter) {
			return null;
		}

		/*
		 * initialize BufferedReader to read from the file which is mentioned in
		 * QueryParameter. Consider Handling Exception related to file reading.
		 */
		DataSet dataSetMap = null;
		if (null != queryParameter.getFile() && !queryParameter.getFile().isEmpty()) {
			dataSetMap = new DataSet();
			Path filePath = FileSystems.getDefault().getPath(queryParameter.getFile());
			try {
				BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8);
				String line = null;
				boolean flag = true;
				String[] header = null;
				String[] data = null;
				while ((line = reader.readLine()) != null) {
					/*
					 * read the first line which contains the header. Please note that the headers
					 * can contain spaces in between them. For eg: city, winner
					 */

					if (flag) {
						header = line.split(",");
						flag = false;
						continue;
					}

					/*
					 * read the next line which contains the first row of data. We are reading this
					 * line so that we can determine the data types of all the fields. Please note
					 * that ipl.csv file contains null value in the last column. If you do not
					 * consider this while splitting, this might cause exceptions later
					 */
					data = this.getColumnType(line.split(",", -1));
					break;
				}

				/*
				 * populate the header Map object from the header array. header map is having
				 * data type <String,Integer> to contain the header and it's index.
				 */
				Header headerMap = null;
				if (null != header) {
					headerMap = new Header();
					for (int i = 0; i < header.length; i++) {
						headerMap.put(header[i], i + 1);
					}
				}

				/*
				 * We have read the first line of text already and kept it in an array. Now, we
				 * can populate the RowDataTypeDefinition Map object. RowDataTypeDefinition map
				 * is having data type <Integer,String> to contain the index of the field and
				 * it's data type. To find the dataType by the field value, we will use
				 * getDataType() method of DataTypeDefinitions class
				 */
				RowDataTypeDefinitions rowDataTypeDefinitionsMap = null;
				if (null != data) {
					rowDataTypeDefinitionsMap = new RowDataTypeDefinitions();
					for (int i = 0; i < data.length; i++) {
						rowDataTypeDefinitionsMap.put(i + 1, data[i]);
					}
				}

				/*
				 * once we have the header and dataTypeDefinitions maps populated, we can start
				 * reading from the first line. We will read one line at a time, then check
				 * whether the field values satisfy the conditions mentioned in the query,if
				 * yes, then we will add it to the resultSet. Otherwise, we will continue to
				 * read the next line. We will continue this till we have read till the last
				 * line of the CSV file.
				 */
				/* reset the buffered reader so that it can start reading from the first line */
				reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8);
				int count = 0;
				int recordCount = 0;
				/* read one line at a time from the CSV file till we have any lines left */
				while ((line = reader.readLine()) != null) {
					recordCount++;
					/*
					 * skip the first line as it is already read earlier which contained the header
					 */
					if (recordCount > 1) {
						// Populating row Map
						Row rowMap = null;
						/*
						 * once we have read one line, we will split it into a String Array. This array
						 * will continue all the fields of the row. Please note that fields might
						 * contain spaces in between. Also, few fields might be empty.
						 */
						String[] dataValue = line.split(",", -1);
						if (null != dataValue && null != header ) {
							int index = 0;
							rowMap = new Row();
							for (String colName : header) {
								rowMap.put(colName, index < dataValue.length ? dataValue[index] : null);
								index++;
							}
						}
						
						Filter filter = new Filter();
						rowMap = filter.evaluateExpression(queryParameter, rowMap);
						if(null != rowMap) {
							count++;
							dataSetMap.put((long)count, rowMap);
						}
						
						/*if (null != rowMap && !rowMap.isEmpty() && null != queryParameter.getRestrictions()
								&& !queryParameter.getRestrictions().isEmpty()) {
							/*
							 * if there are where condition(s) in the query, test the row fields against
							 * those conditions to check whether the selected row satifies the conditions
							 */
							/*for (Restriction restriction : queryParameter.getRestrictions()) {
								String value = rowMap.get(restriction.getPropertyName());
								if (null != value) {
									switch (restriction.getCondition()) {
									case "=":
										if (value.equalsIgnoreCase(restriction.getPropertyValue())) {
											count++;
											dataSetMap.put((long) count, rowMap);
										}
										break;
									case ">":
										int intData = getActualValue(headerMap, rowDataTypeDefinitionsMap, restriction,
												value);
										int compareData = getActualValue(headerMap, rowDataTypeDefinitionsMap,
												restriction, restriction.getPropertyValue());
										if (intData > compareData) {
											count++;
											dataSetMap.put((long) count, rowMap);
										}
										break;
									case "<":
										int intData1 = getActualValue(headerMap, rowDataTypeDefinitionsMap, restriction,
												value);
										int compareData1 = getActualValue(headerMap, rowDataTypeDefinitionsMap,
												restriction, restriction.getPropertyValue());
										if (intData1 < compareData1) {
											count++;
											dataSetMap.put((long) count, rowMap);
										}
										break;
									default:
										System.out.println("No case match found");
										break;
									}
								}
							}
						}*/

					}
				}
			} catch (IOException ex) {
				System.err.format("IOException occured: {}", ex);
			}
		}
		

		/*
		 * from QueryParameter object, read one condition at a time and evaluate the
		 * same. For evaluating the conditions, we will use evaluateExpressions() method
		 * of Filter class. Please note that evaluation of expression will be done
		 * differently based on the data type of the field. In case the query is having
		 * multiple conditions, you need to evaluate the overall expression i.e. if we
		 * have OR operator between two conditions, then the row will be selected if any
		 * of the condition is satisfied. However, in case of AND operator, the row will
		 * be selected only if both of them are satisfied.
		 */

		/*
		 * check for multiple conditions in where clause for eg: where salary>20000 and
		 * city=Bangalore for eg: where salary>20000 or city=Bangalore and dept!=Sales
		 */

		/*
		 * if the overall condition expression evaluates to true, then we need to check
		 * if all columns are to be selected(select *) or few columns are to be
		 * selected(select col1,col2). In either of the cases, we will have to populate
		 * the row map object. Row Map object is having type <String,String> to contain
		 * field Index and field value for the selected fields. Once the row object is
		 * populated, add it to DataSet Map Object. DataSet Map object is having type
		 * <Long,Row> to hold the rowId (to be manually generated by incrementing a Long
		 * variable) and it's corresponding Row Object.
		 */

		/* return dataset object */
		return dataSetMap;
	}

	private int getActualValue(Header headerMap, RowDataTypeDefinitions rowDataTypeDefinitionsMap,
			Restriction restriction, String value) {
		try {
			if (null != headerMap && !headerMap.isEmpty()) {
				int index = headerMap.get(restriction.getPropertyName());
				if (null != rowDataTypeDefinitionsMap && !rowDataTypeDefinitionsMap.isEmpty()) {
					String classType = rowDataTypeDefinitionsMap.get(index);
					if (classType.contains("Integer")) {
						return Integer.parseInt(value);
					}
				}
			}
		} catch (NumberFormatException ex) {
			System.out.println("Cannot parse: {}" + ex.getMessage());
			return -1;
		}
		return -1;
	}

	/*
	 * Method to loop through data part of fle to determne it's dataType.
	 * 
	 */
	public String[] getColumnType(String[] data) {
		if (null != data) {
			List<String> dataTypes = new ArrayList<String>();
			for (String string : data) {
				dataTypes.add(DataTypeDefinitions.getDataType(string).toString().split("class ")[1]);
			}
			return dataTypes.toArray(new String[dataTypes.size()]);
		}
		return null;
	}
}