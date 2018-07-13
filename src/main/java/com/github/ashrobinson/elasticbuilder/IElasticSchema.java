package com.github.ashrobinson.elasticbuilder;

/**
 * Statics for use within the elastic builder
 * @author ARR
 *
 */
public interface IElasticSchema
{
	//Filter constants
	final static String FILTER_STANDARD_TYPE = "must";
	final static String FILTER_OR_TYPE = "should";
	final static String FILTER_NOT_TYPE = "must_not";
	
	//query constants
	final static String QUERY_STANDARD_TYPE = "must";
	
	//aggregate constants
	final static String AGGREGATE_NAMETYPE_PREFIX = "agg_";
	final static String AGGREGATE_FIELD_TYPE = "field";
	
	//Query var constants	
	final static String TYPE_QUERY = "query";
	final static String TYPE_BOOL = "bool";
	final static String TYPE_FILTER = "filter";
	final static String TYPE_AGGREGATION= "aggregations";
	final static String TYPE_SORT = "sort";
	final static String TYPE_FROM = "from";
	final static String TYPE_SIZE = "size";
	final static String TYPE_RAW = "size";
	
	//Sort constants
	final static String SORT_ORDER = "order";
	
	//Directions
	final static String DIRECTION_ASC = "asc";
	final static String DIRECTION_DESC = "desc";
}
