package com.stackroute.datamunger.query.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QueryParser {

	// Regex for white space.
	private static final String WHITE_SPACE = "\\s+";
	// Regex for Non word characters.
	private static final String NON_WORD_CHARACTER = "\\w+";
	// Regex for logical operators.
	private static final String LOGICAL_OPERATORS = "\\s+and\\s+|\\s+or\\s+|\\s+not\\s+";

	/*
	 * this method will parse the queryString and will return the object of
	 * QueryParameter class
	 */
	public QueryParameter parseQuery(String queryString) {
		if (null == queryString || queryString.isEmpty()) {
			return null;
		}

		QueryParameter queryParameter = new QueryParameter();

		/*
		 * extract the name of the file from the query. File name can be found after the
		 * "from" clause.
		 */
		queryParameter.setFile(this.getFile(queryString));

		//extract baseQuery
		String baseQuery = this.getBaseQuery(queryString);
		if(null != baseQuery) {
			queryParameter.setBaseQuery(baseQuery);
		}
		// extract fields
		List<String> fields = this.getFields(queryString);
		if (null != fields) {
			queryParameter.getFields().addAll(fields);
		}

		// extract the order by fields from the query string.
		List<String> orderByFields = this.getOrderByFields(queryString);
		if (null != orderByFields) {
			queryParameter.getOrderByFields().addAll(orderByFields);
		}

		// extract the group by fields from the query string.
		List<String> groupByFields = this.getGroupByFields(queryString);
		if (null != groupByFields) {
			queryParameter.getGroupByFields().addAll(groupByFields);
		}

		// Extracting Conditions.
		List<Restriction> restrictionList = this.getConditions(queryString);
		if (null != restrictionList) {
			queryParameter.setRestrictions(restrictionList);
		}

		// Extracting logical Operators
		List<String> operatorsList = this.getLogicalOperators(queryString);
		if (null != operatorsList) {
			queryParameter.setLogicalOperators(operatorsList);
		}

		// Extracting Aggregate functions.
		List<AggregateFunction> aggregateList = this.getAggregateFunctions(queryString);
		if (null != aggregateList) {
			queryParameter.getAggregateFunctions().addAll(aggregateList);
		}
		return queryParameter;
	}

	/*
	 * extract the name of the file from the query. File name can be found after the
	 * "from" clause.
	 */
	public String getFile(String queryString) {
		String[] fromSplit = queryString.trim().split(" from ");
		if (null != fromSplit && fromSplit.length > 1) {
			String[] tempStrArray = fromSplit[1].split(WHITE_SPACE);
			return tempStrArray[0];
		}
		return null;
	}

	/*
	 * extract the order by fields from the query string. Please note that we will
	 * need to extract the field(s) after "order by" clause in the query, if at all
	 * the order by clause exists. For eg: select city,winner,team1,team2 from
	 * data/ipl.csv order by city from the query mentioned above, we need to extract
	 * "city". Please note that we can have more than one order by fields.
	 */
	public List<String> getOrderByFields(String queryString) {
		String[] strArray = queryString.trim().split(WHITE_SPACE);
		boolean orderByFound = false;
		for (int i = 1; i < strArray.length; i++) {
			if (strArray[i].equalsIgnoreCase("Order") && strArray[i + 1].equalsIgnoreCase("by")) {
				orderByFound = true;
				i++;
				continue;
			}
			if (orderByFound) {
				return new ArrayList<String>(Arrays.asList(strArray[i].split(",")));
			}
		}
		return null;
	}

	/*
	 * extract the group by fields from the query string. Please note that we will
	 * need to extract the field(s) after "group by" clause in the query, if at all
	 * the group by clause exists. For eg: select city,max(win_by_runs) from
	 * data/ipl.csv group by city from the query mentioned above, we need to extract
	 * "city". Please note that we can have more than one group by fields.
	 */
	public List<String> getGroupByFields(String queryString) {
		String[] strArray = queryString.trim().split(WHITE_SPACE);
		boolean groupByFound = false;
		for (int i = 1; i < strArray.length; i++) {
			if (strArray[i].equalsIgnoreCase("Group") && strArray[i + 1].equalsIgnoreCase("by")) {
				groupByFound = true;
				i++;
				continue;
			}
			if (groupByFound) {
				return new ArrayList<String>(Arrays.asList(strArray[i].split(",")));
			}
		}
		return null;
	}

	/*
	 * extract the selected fields from the query string. Please note that we will
	 * need to extract the field(s) after "select" clause followed by a space from
	 * the query string. For eg: select city,win_by_runs from data/ipl.csv from the
	 * query mentioned above, we need to extract "city" and "win_by_runs". Please
	 * note that we might have a field containing name "from_date" or "from_hrs".
	 * Hence, consider this while parsing.
	 */
	public List<String> getFields(String queryString) {
		List<String> fields = new ArrayList<String>();
		String[] selectSplit = queryString.trim().split("select ");
		if (null != selectSplit && selectSplit.length > 1) {
			String[] fromSplit = selectSplit[1].split(" from ");
			if (null != fromSplit) {
				String[] commaSplit = fromSplit[0].split(",");
				if (null != commaSplit) {
					for (int i = 0; i < commaSplit.length; i++) {
						fields.add(commaSplit[i].trim());
					}
				}
			}
		}
		return fields.isEmpty() ? null : fields;
	}

	/*
	 * extract the conditions from the query string(if exists). for each condition,
	 * we need to capture the following: 1. Name of field 2. condition 3. value
	 * 
	 * For eg: select city,winner,team1,team2,player_of_match from data/ipl.csv
	 * where season >= 2008 or toss_decision != bat
	 * 
	 * here, for the first condition, "season>=2008" we need to capture: 1. Name of
	 * field: season 2. condition: >= 3. value: 2008
	 * 
	 * the query might contain multiple conditions separated by OR/AND operators.
	 * Please consider this while parsing the conditions.
	 * 
	 */
	public List<Restriction> getConditions(String queryString) {

		String conditionQuery = this.getConditionsPartQuery(queryString);

		if (null == conditionQuery) {
			return null;
		}

		String[] strArray = conditionQuery.split(LOGICAL_OPERATORS);
		List<Restriction> list = new ArrayList<Restriction>();
		for (String str : strArray) {
			Restriction restriction = new Restriction();
			str = str.replaceAll(WHITE_SPACE, "");
			str = str.replaceAll("'", "");
			String[] nonWordCharArray = str.split(NON_WORD_CHARACTER);
			if (null != nonWordCharArray && nonWordCharArray.length > 1) {
				String nonWordChar = nonWordCharArray[1];
				String alphaArray[] = str.split(nonWordChar);
				if (null != alphaArray && alphaArray.length > 1) {
					restriction.setPropertyName(alphaArray[0]);
					restriction.setPropertyValue(alphaArray[1]);
				}
				restriction.setCondition(nonWordChar);
				// System.out.println(restriction.toString());
				list.add(restriction);
			}
		}
		return list;
	}

	/*
	 * extract the logical operators(AND/OR) from the query, if at all it is
	 * present. For eg: select city,winner,team1,team2,player_of_match from
	 * data/ipl.csv where season >= 2008 or toss_decision != bat and city =
	 * bangalore
	 * 
	 * the query mentioned above in the example should return a List of Strings
	 * containing [or,and]
	 */
	public List<String> getLogicalOperators(String queryString) {
		List<String> conditions = new ArrayList<String>();
		String[] strArray = queryString.trim().split(WHITE_SPACE);
		boolean whereFound = false;
		for (int i = 1; i < strArray.length - 1; i++) {
			if (strArray[i].equalsIgnoreCase("WHERE")) {
				whereFound = true;
			}
			if (whereFound && ("and".equalsIgnoreCase(strArray[i]) || "or".equalsIgnoreCase(strArray[i])
					|| "not".equalsIgnoreCase(strArray[i]))) {
				conditions.add(strArray[i]);
			}
		}
		return conditions.isEmpty() ? null : conditions;
	}

	/*
	 * extract the aggregate functions from the query. The presence of the aggregate
	 * functions can determined if we have either "min" or "max" or "sum" or "count"
	 * or "avg" followed by opening braces"(" after "select" clause in the query
	 * string. in case it is present, then we will have to extract the same. For
	 * each aggregate functions, we need to know the following: 1. type of aggregate
	 * function(min/max/count/sum/avg) 2. field on which the aggregate function is
	 * being applied
	 * 
	 * Please note that more than one aggregate function can be present in a query
	 * 
	 * 
	 */
	public List<AggregateFunction> getAggregateFunctions(String queryString) {
		List<AggregateFunction> aggregateList = new ArrayList<AggregateFunction>();
		String[] strArray = queryString.trim().split("select ");
		if (null != strArray && strArray.length > 1) {
			String beforeFromQuery = strArray[1].split(" from ")[0];
			if (beforeFromQuery.contains("sum(") || beforeFromQuery.contains("count(")
					|| beforeFromQuery.contains("max(") || beforeFromQuery.contains("min(")
					|| beforeFromQuery.contains("avg(")) {

				String[] aggregateArr = beforeFromQuery.split(",");
				if (null != aggregateArr) {
					for (int j = 0; j < aggregateArr.length; j++) {
						AggregateFunction aggregateFunction = new AggregateFunction();
						String[] splittedStr = aggregateArr[j].split("\\(");
						if (null != splittedStr && splittedStr.length > 1) {
							aggregateFunction.setAggregateName(splittedStr[0]);
							aggregateFunction
									.setAggregateValue(splittedStr[1].substring(0, splittedStr[1].length() - 1));
							aggregateList.add(aggregateFunction);
						}
					}
				}
			}
		}

		return aggregateList.isEmpty() ? null : aggregateList;
	}

	/**
	 * Method to get condition part query.
	 * 
	 * @param queryString
	 * @return
	 */
	public String getConditionsPartQuery(String queryString) {
		if (queryString.contains(" where ")) {
			String afterWhere = queryString.split("\\s+where\\s+")[1];
			if (afterWhere.contains(" order by ")) {
				return afterWhere.split("\\s+order by\\s+")[0];
			} 
			if (afterWhere.contains(" group by ")) {
				return afterWhere.split("\\s+group by\\s+")[0];
			}
			return afterWhere;
		}
		return null;
	}
	
	public String getBaseQuery(String queryString) {
		String baseQuery = null;
		baseQuery = queryString.split("where")[0];
		if (baseQuery.contains(" order by ")) {
			return baseQuery = queryString.split("order by")[0].trim();
		}
		if (baseQuery.contains(" group by ")) {
			return baseQuery = queryString.split("group by")[0].trim();
		}
		return baseQuery.trim();
	}

}
