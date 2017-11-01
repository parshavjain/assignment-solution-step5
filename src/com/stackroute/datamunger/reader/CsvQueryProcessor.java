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
				String[] header = null;
				String[] data = null;
				int counter = 0;
				while ((line = reader.readLine()) != null && counter < 2) {
					counter++;
					/*
					 * read the first line which contains the header. Please note that the headers
					 * can contain spaces in between them. For eg: city, winner
					 */

					if (counter == 1) {
						header = line.split(",");
						continue;
					}

					/*
					 * read the next line which contains the first row of data. We are reading this
					 * line so that we can determine the data types of all the fields. Please note
					 * that ipl.csv file contains null value in the last column. If you do not
					 * consider this while splitting, this might cause exceptions later
					 */
					data = this.getColumnType(line.split(",", -1));
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
						if (null != dataValue && null != header) {
							int index = 0;
							rowMap = new Row();
							for (String colName : header) {
								rowMap.put(colName, index < dataValue.length ? dataValue[index] : null);
								index++;
							}
						}

						Filter filter = new Filter();

						rowMap = filter.evaluateExpression(queryParameter, rowMap);
						if (null != rowMap) {
							count++;
							dataSetMap.put((long) count, rowMap);
						}
					}
				}
			} catch (IOException ex) {
				System.err.format("IOException occured: {}", ex);
			}
		}
		/* return dataset object */
		return dataSetMap;
	}

	/*
	 * Method to loop through data part of fle to determne it's dataType.
	 * 
	 */
	public String[] getColumnType(String[] data) {
		List<String> dataTypes = new ArrayList<String>();
		if (null != data) {
			final DataTypeDefinitions dataTypeDefinitions = new DataTypeDefinitions();
			for (String string : data) {
				dataTypes.add(dataTypeDefinitions.getDataType(string).toString().split("class ")[1]);
			}
			return dataTypes.toArray(new String[dataTypes.size()]);
		}
		return dataTypes.toArray(new String[dataTypes.size()]);
	}
}