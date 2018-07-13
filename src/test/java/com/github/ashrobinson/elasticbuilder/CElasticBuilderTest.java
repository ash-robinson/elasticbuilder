package com.github.ashrobinson.elasticbuilder;

import org.junit.*;

public class CElasticBuilderTest
{
	private static CElasticBuilder builder = null;
	
	private static void setProperties()
	{
		builder = new CElasticBuilder();
	}
	
	@BeforeClass
	public static void setUp()
	{
		setProperties();
	}
	
	@Test
	public void testQuery() throws Exception
	{		
		builder.query("terms", "author", "robbo").build().toString();
		builder.query("terms", "author", "robbo").query("terms", "publisher", "IET").build().toString();
	}

	@Test
	public void testAggregate() throws Exception
	{		
		builder.aggregation("terms", "version").build().toString();
		builder.aggregation("terms", "version").aggregation("terms", "pool").build().toString();
	}
	
	@Test
	public void testFilters() throws Exception
	{		
		builder.filter("term", "user", "robbo").build().toString();
		builder.filter("term", "user", "robbo").filter("term", "user", "sam").build().toString();
		builder.orFilter("term", "user", "robbo").build().toString();
		builder.orFilter("term", "user", "robbo").orFilter("term", "user", "sam").build().toString();			
		builder.notFilter("term", "user", "robbo").build().toString();
		builder.notFilter("term", "user", "robbo").notFilter("term", "user", "sam").build().toString();
		builder.filter("term", "user", "robbo").filter("term", "user", "alex").orFilter("term", "user", "stevo")
			.notFilter("term", "user", "sam").build().toString();
	}
	
	@Test
	public void testSort() throws Exception
	{
		builder.sortAsc("test").build().toString();
		builder.sortAsc("test").sortAsc("size").build().toString();
		builder.sortDesc("test").sortDesc("dep").sortAsc("size").build().toString();
	}
	
	@Test
	public void testMisc() throws Exception
	{
		builder.from(2).build().toString();
		builder.size(5).build().toString();
		builder.query("terms", "actor", "Christopher Lee").rawOption("series", "LOTR").build().toString();
	}
	
	@Test
	public void testCombined() throws Exception
	{	
		builder.query("terms", "author", "robbo").aggregation("terms", "year").build().toString();
		builder.query("terms", "author", "robbo").filter("term", "user", "sam").build().toString();
		builder.filter("term", "user", "ash").build().toString();
		builder.filter("term", "user", "kimchy").filter("term", "user", "herald").filter("term", "user", "ash").build().toString();

	}
	
	@Test
	public void testMain() throws Exception
	{
		System.out.println("TEST FINAL: " + builder.query("match", "message", "this is a test")
		  .filter("term", "user", "kimchy")
		  .filter("term", "user", "herald")
		  .orFilter("term", "user", "johnny")
		  .notFilter("term", "user", "cassie")
		  .aggregation("terms", "user")
		  .aggregation("terms", "waifu")
		  .sortDesc("user")
		  .from(4)
		  .size(10)
		  .rawOption("pass", "true")
		  .build().toString());
	}
}
