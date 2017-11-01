package com.stackroute.datamunger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.stackroute.datamunger.query.Query;
import com.stackroute.datamunger.writer.JsonWriter;

/**
 * contains Main method.
 * 
 */
public abstract class DataMunger {
	
	/**
	 * Main method Call.
	 * @param args
	 */
	public static void main(final String[] args) {
		
		String queryString=null;
		try {
		//read the query from the user
		final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		queryString = reader.readLine();
		} catch (IOException ex) {
			// TODO: handle exception
			System.err.format("IOException occured: {}", ex);
		}
		
		
		/**
		 * Instantiate Query class. This class is responsible for: 
		 * 1. Parsing the query
		 * 2. Select the appropriate type of query processor 
		 * 3. Get the resultSet which
		 * is populated by the Query Processor
		 */
		final Query query=new Query();
		
		/*
		 * Instantiate JsonWriter class. This class is responsible for writing the
		 * ResultSet into a JSON file
		 */
		final JsonWriter writer=new JsonWriter();
		/*
		 * call executeQuery() method of Query class to get the resultSet. Pass this
		 * resultSet as parameter to writeToJson() method of JsonWriter class to write
		 * the resultSet into a JSON file
		 */
		writer.writeToJson(query.executeQuery(queryString));

	}
}
