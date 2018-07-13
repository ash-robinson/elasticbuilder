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
//		System.out.println("TEST 1: " + builder.query("terms", "author", "robbo").build().toString());
//		System.out.println("TEST 2: " + builder.query("terms", "author", "robbo").query("terms", "publisher", "IET").build().toString());
	}

	@Test
	public void testAggregate() throws Exception
	{		
//		System.out.println("TEST 1: " + builder.aggregation("terms", "version").build().toString());
//		System.out.println("TEST 2: " + builder.aggregation("terms", "version").aggregation("terms", "pool").build().toString());	
//	
	}
	
	@Test
	public void testFilters() throws Exception
	{		
//		System.out.println("TEST 1: " + builder.filter("term", "user", "robbo").build().toString());
//		System.out.println("TEST 2: " + builder.filter("term", "user", "robbo").filter("term", "user", "sam").build().toString());
//		System.out.println("TEST 3: " + builder.orFilter("term", "user", "robbo").build().toString());
//		System.out.println("TEST 4: " + builder.orFilter("term", "user", "robbo").orFilter("term", "user", "sam").build().toString());			
//		System.out.println("TEST 5: " + builder.notFilter("term", "user", "robbo").build().toString());
//		System.out.println("TEST 6: " + builder.notFilter("term", "user", "robbo").notFilter("term", "user", "sam").build().toString());
//		
//
//		System.out.println("TEST BODY: " + builder.filter("term", "user", "robbo")
//														.filter("term", "user", "alex")
//														.orFilter("term", "user", "stevo")
//														.notFilter("term", "user", "sam").build().toString());
	}
	
	@Test
	public void testSort() throws Exception
	{
//		System.out.println("TEST 1: " + builder.sortAsc("test").build().toString());
//		System.out.println("TEST 2: " + builder.sortAsc("test").sortAsc("size").build().toString());
//		System.out.println("TEST 3: " + builder.sortDesc("test").sortDesc("dep").sortAsc("size").build().toString());
	}
	
	@Test
	public void testMisc() throws Exception
	{
		System.out.println("TEST 1: " + builder.from(2).build().toString());
		System.out.println("TEST 2: " + builder.size(5).build().toString());
		System.out.println("TEST 3: " + builder.query("terms", "actor", "Christopher Lee").rawOption("series", "LOTR").build().toString());
	}
	
	@Test
	public void testCombined() throws Exception
	{	
//		System.out.println("TEST 1: " + builder.query("terms", "author", "robbo").aggregation("terms", "year").build().toString());
//		System.out.println("TEST 2: " + builder.query("terms", "author", "robbo").filter("term", "user", "sam").build().toString());
//		System.out.println("TEST 3: " + builder.filter("term", "user", "ash").build().toString());
//		System.out.println("TEST 4: " + builder.filter("term", "user", "kimchy").filter("term", "user", "herald").filter("term", "user", "ash").build().toString());
		System.out.println("TEST FINAL: " + builder.query("match", "message", "this is a test")
		  .filter("term", "user", "kimchy")
		  .filter("term", "user", "herald")
		  .orFilter("term", "user", "johnny")
		  .notFilter("term", "user", "cassie")
		  .aggregation("terms", "user")
		  .aggregation("terms", "waifu")
		  .sortDesc("user")
		  .build().toString());
	}
}
