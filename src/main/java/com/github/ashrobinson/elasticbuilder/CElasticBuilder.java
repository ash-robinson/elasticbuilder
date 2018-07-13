package com.github.ashrobinson.elasticbuilder;

import com.google.gson.*;

/**
 * Builder class for elastic queries, to make process easier
 * @author ARR
 *
 */
public class CElasticBuilder
{
	private JsonObject currentObject = null;
	
	public CElasticBuilder()
	{
		super();
	}
	
	public CElasticBuilder rawOption(String key, String value)
	{
		if (currentObject == null)
		{
			JsonObject jsonSize = new JsonObject();		
			jsonSize.addProperty(key, value);
			currentObject = jsonSize;
		}
		else
		{
			if (!currentObject.has(key))
			{
				currentObject.addProperty(key, value);
			}
			else
			{
				currentObject.remove(key);
				currentObject.addProperty(key, value);
			}
		}
		return this;
	}	
	
	public CElasticBuilder size(int index)
	{
		if (currentObject == null)
		{
			JsonObject jsonSize = new JsonObject();		
			jsonSize.addProperty(IElasticSchema.TYPE_SIZE, index);
			currentObject = jsonSize;
		}
		else
		{
			if (!currentObject.has(IElasticSchema.TYPE_SIZE))
			{
				currentObject.addProperty(IElasticSchema.TYPE_SIZE, index);
			}
			else
			{
				currentObject.remove(IElasticSchema.TYPE_SIZE);
				currentObject.addProperty(IElasticSchema.TYPE_SIZE, index);
			}
		}
		return this;
	}	
	
	public CElasticBuilder from(int index)
	{
		if (currentObject == null)
		{
			JsonObject jsonFrom = new JsonObject();		
			jsonFrom.addProperty(IElasticSchema.TYPE_FROM, index);
			currentObject = jsonFrom;
		}
		else
		{
			if (!currentObject.has(IElasticSchema.TYPE_FROM))
			{
				currentObject.addProperty(IElasticSchema.TYPE_FROM, index);
			}
			else
			{
				currentObject.remove(IElasticSchema.TYPE_FROM);
				currentObject.addProperty(IElasticSchema.TYPE_FROM, index);
			}
		}
		return this;
	}	
	
	//private for safety
	private CElasticBuilder sort(String field, String direction)
	{
		if (currentObject == null)
		{
			JsonObject jsonQueryWrapper = new JsonObject();	
			JsonArray jsonSortList = new JsonArray();
			JsonObject jsonQuery = new JsonObject();	
			JsonObject jsonOrder= new JsonObject();			
			jsonOrder.addProperty(IElasticSchema.SORT_ORDER, direction);
			jsonQuery.add(field, jsonOrder);
			jsonSortList.add(jsonQuery);
			jsonQueryWrapper.add(IElasticSchema.TYPE_SORT, jsonSortList);
			currentObject = jsonQueryWrapper;			
		}
		else
		{
			if (!currentObject.has(IElasticSchema.TYPE_SORT))
			{
				JsonArray jsonSortList = new JsonArray();
				JsonObject jsonQuery = new JsonObject();	
				JsonObject jsonOrder = new JsonObject();			
				jsonOrder.addProperty(IElasticSchema.SORT_ORDER, direction);
				jsonQuery.add(field, jsonOrder);
				jsonSortList.add(jsonQuery);
				currentObject.add(IElasticSchema.TYPE_SORT, jsonQuery);
			}
			else
			{
				JsonObject jsonQuery = new JsonObject();	
				JsonObject jsonOrder= new JsonObject();			
				jsonOrder.addProperty(IElasticSchema.SORT_ORDER, direction);
				jsonQuery.add(field, jsonOrder);
				currentObject.get(IElasticSchema.TYPE_SORT).getAsJsonArray().add(jsonQuery);
			}
		}	
		return this;
	}
	
	public CElasticBuilder sortAsc(String field) 
	{
		return sort(field, IElasticSchema.DIRECTION_ASC);
	}
	
	public CElasticBuilder sortDesc(String field) 
	{
		return sort(field, IElasticSchema.DIRECTION_DESC);
	}
	
	public CElasticBuilder query(String queryType, String field, String term)
	{
		//TODO: Add debug
		if (currentObject == null)
		{			
			JsonObject jsonQueryWrapper = new JsonObject();
			JsonObject jsonQuery = new JsonObject();	
			JsonObject jsonTerm = new JsonObject();	
		
			jsonTerm.addProperty(field, term);
			jsonQuery.add(queryType, jsonTerm);
			jsonQueryWrapper.add("query", jsonQuery);
			
			//TODO: Add debug
			currentObject = jsonQueryWrapper;
		}
		else
		{		
			if (currentObject.has(IElasticSchema.TYPE_QUERY) && currentObject.get(IElasticSchema.TYPE_QUERY).isJsonObject() && currentObject.get(IElasticSchema.TYPE_QUERY).getAsJsonObject().has(IElasticSchema.TYPE_BOOL) 
					&& currentObject.get(IElasticSchema.TYPE_QUERY).getAsJsonObject().get(IElasticSchema.TYPE_BOOL).isJsonObject())					
			{
				//bool already exists
				JsonObject jsonQuery = new JsonObject();
				JsonObject jsonTerm = new JsonObject();	
			
				jsonTerm.addProperty(field, term);
				jsonQuery.add(queryType, jsonTerm);
				
				if (!currentObject.get(IElasticSchema.TYPE_QUERY).getAsJsonObject().get(IElasticSchema.TYPE_BOOL).getAsJsonObject().has(IElasticSchema.QUERY_STANDARD_TYPE))
				{
					currentObject.get(IElasticSchema.TYPE_QUERY).getAsJsonObject().get(IElasticSchema.TYPE_BOOL).getAsJsonObject().get(IElasticSchema.QUERY_STANDARD_TYPE).getAsJsonObject().add(IElasticSchema.QUERY_STANDARD_TYPE, jsonQuery);
				}
				else
				{
					currentObject.get(IElasticSchema.TYPE_QUERY).getAsJsonObject().get(IElasticSchema.TYPE_BOOL).getAsJsonObject().get(IElasticSchema.QUERY_STANDARD_TYPE).getAsJsonArray().add(jsonQuery);
				}
			}
			else
			{
				//convert to bool taking existing one
				JsonObject jsonQueryWrapper = new JsonObject();
				JsonObject jsonBool = new JsonObject();
				JsonArray  jsonTypeArray = new JsonArray();
				JsonObject jsonType = new JsonObject();
				JsonObject jsonQuery = new JsonObject();
				JsonObject jsonTerm = new JsonObject();	
			
				jsonTerm.addProperty(field, term);
				jsonQuery.add(queryType, jsonTerm);
				
				//add initial
				for (String key : currentObject.get(IElasticSchema.TYPE_QUERY).getAsJsonObject().keySet())
				{
					jsonTypeArray.add(currentObject.get(IElasticSchema.TYPE_QUERY).getAsJsonObject().get(key).getAsJsonObject());
				}
				
				jsonTypeArray.add(jsonQuery);				
				jsonType.add(IElasticSchema.QUERY_STANDARD_TYPE, jsonTypeArray);
				jsonBool.add(IElasticSchema.TYPE_BOOL, jsonType);
				currentObject.add("query", jsonBool);				
			}
		}
		return this;
	}
	
	public CElasticBuilder aggregation(String aggregationType, String fieldToAggregate)
	{
		//TODO: Add debug
		
		if (currentObject == null)		
		{
			JsonObject jsonAggregationWrapper = new JsonObject();
			JsonObject jsonAggregationName = new JsonObject();	
			JsonObject jsonAggregationType = new JsonObject();	
			JsonObject jsonTerm = new JsonObject();	
			
			//build aggregation name
			String aggName = IElasticSchema.AGGREGATE_NAMETYPE_PREFIX + aggregationType + "_" + fieldToAggregate;
			
			jsonTerm.addProperty(IElasticSchema.AGGREGATE_FIELD_TYPE, fieldToAggregate);
			jsonAggregationType.add(aggregationType, jsonTerm);
			jsonAggregationName.add(aggName, jsonAggregationType);
			jsonAggregationWrapper.add(IElasticSchema.TYPE_AGGREGATION, jsonAggregationName);
			
			currentObject = jsonAggregationWrapper;
		}
		else
		{
			//nesting/chaining
			if (currentObject.has(IElasticSchema.TYPE_AGGREGATION) && currentObject.get(IElasticSchema.TYPE_AGGREGATION).isJsonObject())
			{
				//build nesting
				JsonObject jsonAggregationType = new JsonObject();	
				JsonObject jsonTerm = new JsonObject();	
				
				//build aggregation name
				String aggName = IElasticSchema.AGGREGATE_NAMETYPE_PREFIX + aggregationType + "_" + fieldToAggregate;

				jsonTerm.addProperty(IElasticSchema.AGGREGATE_FIELD_TYPE, fieldToAggregate);
				jsonAggregationType.add(aggregationType, jsonTerm);				
				currentObject.get(IElasticSchema.TYPE_AGGREGATION).getAsJsonObject().add(aggName, jsonAggregationType);				
			}
			else
			{
				JsonObject jsonAggregationName = new JsonObject();	
				JsonObject jsonAggregationType = new JsonObject();	
				JsonObject jsonTerm = new JsonObject();	
				
				//build aggregation name
				String aggName = IElasticSchema.AGGREGATE_NAMETYPE_PREFIX + aggregationType + "_" + fieldToAggregate;
				
				jsonTerm.addProperty(IElasticSchema.AGGREGATE_FIELD_TYPE, fieldToAggregate);
				jsonAggregationType.add(aggregationType, jsonTerm);
				jsonAggregationName.add(aggName, jsonAggregationType);
				currentObject.add(IElasticSchema.TYPE_AGGREGATION, jsonAggregationName);
			}
		}
		return this;
	}
	
	private CElasticBuilder updateFilter(String filterType, String field, String term, String type)
	{
		if (currentObject == null)
		{
			JsonObject jsonQueryWrapper = new JsonObject();
			JsonObject jsonQuery = new JsonObject();
			JsonObject jsonBool = new JsonObject();
			JsonObject jsonFilterType = new JsonObject();
			JsonObject jsonFilter = new JsonObject();
			JsonObject jsonTerm = new JsonObject();

			jsonTerm.addProperty(field, term);
			jsonFilter.add(filterType, jsonTerm);
			jsonFilterType.add(type, jsonFilter);
			jsonQuery.add(IElasticSchema.TYPE_FILTER, jsonFilterType);
			jsonBool.add(IElasticSchema.TYPE_BOOL, jsonQuery);
			jsonQueryWrapper.add(IElasticSchema.TYPE_QUERY, jsonBool);
			
			currentObject = jsonQueryWrapper;
		}
		else
		{
			if (currentObject.has(IElasticSchema.TYPE_QUERY) && currentObject.get(IElasticSchema.TYPE_QUERY).isJsonObject() && !currentObject.get(IElasticSchema.TYPE_QUERY).getAsJsonObject().has(IElasticSchema.TYPE_BOOL))
			{
				//bool not created yet
				JsonObject jsonBody = new JsonObject();
				JsonObject jsonBool = new JsonObject();
				JsonObject jsonQuery = new JsonObject();

				jsonBody = currentObject.get(IElasticSchema.TYPE_QUERY).getAsJsonObject();
				jsonBool.add(IElasticSchema.TYPE_BOOL, jsonBody);
				jsonQuery.add(IElasticSchema.TYPE_QUERY, jsonBool);
				currentObject = jsonQuery;
				
				//manually add the filter object here
				JsonObject jsonFilterWrapper = new JsonObject();
				JsonObject jsonFilter = new JsonObject();
				JsonObject jsonTerm = new JsonObject();
				jsonTerm.addProperty(field, term);
				jsonFilter.add(filterType, jsonTerm);
				jsonFilterWrapper.add(type, jsonFilter);
				
				//safely add to struct
				if (currentObject.has(IElasticSchema.TYPE_QUERY) && currentObject.get(IElasticSchema.TYPE_QUERY).isJsonObject() && currentObject.get(IElasticSchema.TYPE_QUERY).getAsJsonObject().has(IElasticSchema.TYPE_BOOL) 
						&& currentObject.get(IElasticSchema.TYPE_QUERY).getAsJsonObject().get(IElasticSchema.TYPE_BOOL).isJsonObject())
				{
					currentObject.get(IElasticSchema.TYPE_QUERY).getAsJsonObject().get(IElasticSchema.TYPE_BOOL).getAsJsonObject().add(IElasticSchema.TYPE_FILTER, jsonFilterWrapper);
				}				
			}
			else
			//nesting/chaining
			if (currentObject.has(IElasticSchema.TYPE_QUERY) && currentObject.get(IElasticSchema.TYPE_QUERY).isJsonObject() && currentObject.get(IElasticSchema.TYPE_QUERY).getAsJsonObject().has(IElasticSchema.TYPE_BOOL) && currentObject.get(IElasticSchema.TYPE_QUERY).getAsJsonObject().get(IElasticSchema.TYPE_BOOL).isJsonObject()
					&& currentObject.get(IElasticSchema.TYPE_QUERY).getAsJsonObject().get(IElasticSchema.TYPE_BOOL).getAsJsonObject().has(IElasticSchema.TYPE_FILTER) && currentObject.get(IElasticSchema.TYPE_QUERY).getAsJsonObject().get(IElasticSchema.TYPE_BOOL).getAsJsonObject().get(IElasticSchema.TYPE_FILTER).isJsonObject())
			{
				if (currentObject.get(IElasticSchema.TYPE_QUERY).getAsJsonObject().get(IElasticSchema.TYPE_BOOL).getAsJsonObject().get(IElasticSchema.TYPE_FILTER).getAsJsonObject().has(IElasticSchema.TYPE_BOOL) 
						&& currentObject.get(IElasticSchema.TYPE_QUERY).getAsJsonObject().get(IElasticSchema.TYPE_BOOL).getAsJsonObject().get(IElasticSchema.TYPE_FILTER).getAsJsonObject().get(IElasticSchema.TYPE_BOOL).isJsonObject())
				{
					//already array for field

					JsonObject jsonFilter = new JsonObject();
					JsonObject jsonTerm = new JsonObject();

					jsonTerm.addProperty(field, term);
					jsonFilter.add(filterType, jsonTerm);
					
					//check if type exists if not create
					if (!currentObject.get(IElasticSchema.TYPE_QUERY).getAsJsonObject().get(IElasticSchema.TYPE_BOOL).getAsJsonObject().get(IElasticSchema.TYPE_FILTER).getAsJsonObject().get(IElasticSchema.TYPE_BOOL).getAsJsonObject().has(type))
					{
						JsonObject jsonFilterType = new JsonObject();
						JsonArray jsonFilterArray = new JsonArray();
						jsonTerm.addProperty(field, term);
						jsonFilter.add(filterType, jsonTerm);
						jsonFilterArray.add(jsonFilter);						
						currentObject.get(IElasticSchema.TYPE_QUERY).getAsJsonObject().get(IElasticSchema.TYPE_BOOL).getAsJsonObject().get(IElasticSchema.TYPE_FILTER).getAsJsonObject().get(IElasticSchema.TYPE_BOOL).getAsJsonObject().add(type, jsonFilterArray);
					}
					else
					{
						currentObject.get(IElasticSchema.TYPE_QUERY).getAsJsonObject().get(IElasticSchema.TYPE_BOOL).getAsJsonObject().get(IElasticSchema.TYPE_FILTER).getAsJsonObject().get(IElasticSchema.TYPE_BOOL).getAsJsonObject().get(type).getAsJsonArray().add(jsonFilter);
					}
				}
				else
				if (currentObject.get(IElasticSchema.TYPE_QUERY).getAsJsonObject().get(IElasticSchema.TYPE_BOOL).getAsJsonObject().has(IElasticSchema.TYPE_FILTER)
						&& currentObject.get(IElasticSchema.TYPE_QUERY).getAsJsonObject().get(IElasticSchema.TYPE_BOOL).getAsJsonObject().get(IElasticSchema.TYPE_FILTER).isJsonObject())
				{
					//build new filter 
					JsonObject jsonFilter = new JsonObject();
					JsonObject jsonTerm = new JsonObject();
					JsonObject jsonType = new JsonObject();
					JsonArray jsonFilterArray = new JsonArray();
					
					jsonTerm.addProperty(field, term);
					jsonFilter.add(filterType, jsonTerm);

					//check if type exists if not create
					if (!currentObject.get(IElasticSchema.TYPE_QUERY).getAsJsonObject().get(IElasticSchema.TYPE_BOOL).getAsJsonObject().get(IElasticSchema.TYPE_FILTER).getAsJsonObject().has(type))
					{
						JsonObject jsonFilterType = new JsonObject();
						jsonFilterArray.add(jsonFilter);
						jsonType.add(type, jsonFilterArray);
						currentObject.get(IElasticSchema.TYPE_QUERY).getAsJsonObject().get(IElasticSchema.TYPE_BOOL).getAsJsonObject().get(IElasticSchema.TYPE_FILTER).getAsJsonObject().add(IElasticSchema.TYPE_BOOL, jsonType);
						
					}
					else
					{
						for (String key : currentObject.get(IElasticSchema.TYPE_QUERY).getAsJsonObject().get(IElasticSchema.TYPE_BOOL).getAsJsonObject().get(IElasticSchema.TYPE_FILTER).getAsJsonObject().keySet())
						{
							jsonFilterArray.add(currentObject.get(IElasticSchema.TYPE_QUERY).getAsJsonObject().get(IElasticSchema.TYPE_BOOL).getAsJsonObject().get(IElasticSchema.TYPE_FILTER).getAsJsonObject().get(key).getAsJsonObject());
						}
						jsonFilterArray.add(jsonFilter);	
						jsonType.add(type, jsonFilterArray);					
						currentObject.get(IElasticSchema.TYPE_QUERY).getAsJsonObject().get(IElasticSchema.TYPE_BOOL).getAsJsonObject().get(IElasticSchema.TYPE_FILTER).getAsJsonObject().add(IElasticSchema.TYPE_BOOL, jsonType);
						if (currentObject.get(IElasticSchema.TYPE_QUERY).getAsJsonObject().get(IElasticSchema.TYPE_BOOL).getAsJsonObject().get(IElasticSchema.TYPE_FILTER).getAsJsonObject().has(type))
						{
							currentObject.get(IElasticSchema.TYPE_QUERY).getAsJsonObject().get(IElasticSchema.TYPE_BOOL).getAsJsonObject().get(IElasticSchema.TYPE_FILTER).getAsJsonObject().remove(type);
						}
					}
				}
				else
				if (currentObject.has(IElasticSchema.TYPE_QUERY) && currentObject.get(IElasticSchema.TYPE_QUERY).isJsonObject() && currentObject.get(IElasticSchema.TYPE_QUERY).getAsJsonObject().has(IElasticSchema.TYPE_BOOL)
						&& currentObject.get(IElasticSchema.TYPE_QUERY).getAsJsonObject().get(IElasticSchema.TYPE_BOOL).isJsonObject())
				{
					//bool already exists
					JsonObject jsonFilter = new JsonObject();
					JsonObject jsonTerm = new JsonObject();

					jsonTerm.addProperty(field, term);
					jsonFilter.add(filterType, jsonTerm);

					if (!currentObject.get(IElasticSchema.TYPE_QUERY).getAsJsonObject().get(IElasticSchema.TYPE_BOOL).getAsJsonObject().has(IElasticSchema.QUERY_STANDARD_TYPE))
					{
						currentObject.get(IElasticSchema.TYPE_QUERY).getAsJsonObject().get(IElasticSchema.TYPE_BOOL).getAsJsonObject().add(IElasticSchema.QUERY_STANDARD_TYPE, jsonFilter);
					}
					else
					{
						currentObject.get(IElasticSchema.TYPE_QUERY).getAsJsonObject().get(IElasticSchema.TYPE_BOOL).getAsJsonObject().get(IElasticSchema.QUERY_STANDARD_TYPE).getAsJsonArray().add(jsonFilter);
					}
				}
				else
				{
					//build new object and add existing
					JsonObject jsonQueryWrapper = new JsonObject();
					JsonObject jsonQuery = new JsonObject();
					JsonObject jsonBool = new JsonObject();
					JsonObject jsonQueryBool = new JsonObject();
					JsonObject jsonFilterType = new JsonObject();
					JsonArray jsonFilterArray = new JsonArray();
					JsonObject jsonFilter = new JsonObject();
					JsonObject jsonTerm = new JsonObject();

					jsonTerm.addProperty(field, term);
					jsonFilter.add(filterType, jsonTerm);

					//add inital
					for (String key : currentObject.get(IElasticSchema.TYPE_QUERY).getAsJsonObject().get(IElasticSchema.TYPE_BOOL).getAsJsonObject().get(IElasticSchema.TYPE_FILTER).getAsJsonObject().keySet())
					{
						jsonFilterArray.add(currentObject.get(IElasticSchema.TYPE_QUERY).getAsJsonObject().get(IElasticSchema.TYPE_BOOL).getAsJsonObject().get(IElasticSchema.TYPE_FILTER).getAsJsonObject().get(key).getAsJsonObject());
					}
					jsonFilterArray.add(jsonFilter);

					jsonFilterType.add(type, jsonFilterArray);
					jsonQueryBool.add(IElasticSchema.TYPE_BOOL, jsonFilterType);
					jsonQuery.add(IElasticSchema.TYPE_FILTER, jsonQueryBool);
					jsonBool.add(IElasticSchema.TYPE_BOOL, jsonQuery);
				
					currentObject.add(IElasticSchema.TYPE_QUERY, jsonBool);
				}
			}		
		}
		return this;
	}
	
	public CElasticBuilder filter(String filterType, String field, String term)
	{
		return updateFilter(filterType, field, term, IElasticSchema.FILTER_STANDARD_TYPE);
	}
	
	public CElasticBuilder orFilter(String filterType, String field, String term)
	{
		return updateFilter(filterType, field, term, IElasticSchema.FILTER_OR_TYPE);
	}
	
	public CElasticBuilder notFilter(String filterType, String field, String term)
	{
		return updateFilter(filterType, field, term, IElasticSchema.FILTER_NOT_TYPE);
	}
	
	public String toString()
	{
		if (currentObject == null)
		{
			return "[null]";
		}
		else
		{
			return currentObject.toString();
		}
	}
	
	public JsonObject build()
	{
		JsonObject json = currentObject;
		//reinit objects
		currentObject = null;		
		return json;
	}
	
	public void reset()
	{		
		currentObject = null;	
	}
}
