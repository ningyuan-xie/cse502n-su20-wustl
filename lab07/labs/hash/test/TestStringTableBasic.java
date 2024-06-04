package hash.test;
import hash.StringTable;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;

import org.junit.BeforeClass;
import org.junit.Test;

import hash.Record;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class TestStringTableBasic {

	public static StringTable table;

	@BeforeClass
	public static void setup() {
		table = new StringTable(4);
	}
	
	private String [] testStrings = {
			"abcdefg",
			"bcdefgh",
			"cdefghix",
			"defghij",
			"efghijkxx",
			"fghijkl",
			"ghijklm",
			"hijklmnxxx",
			"ijklmno",
			"jklmnop",
			"klmnopqxxxx",
			"mnopqrs",
			"nopqrst",
			"opqrstuxxxxx",
			"pqrstuv",
			"qrstuvwxxxxxx"
	};

	private String [] missingStrings = {
			"foo",
			"bar",
			"baz",
			"quux"
	};

	@Test
	public void testInit() 
	{
		StringTable table = new StringTable(4);
		assertEquals("The table's size after initialization should be 0",
				table.size, 0);
	}

	@Test
	public void testInsert() 
	{
		StringTable table = new StringTable(4);
		int size = 0;
		for (int j = 0; j < testStrings.length; j++)
		{
			Record r = new Record(testStrings[j]);
			assertTrue("Could not insert record with key " 
					+  testStrings[j] + " into table",
					table.insert(r));
	//		assertEquals("Cannot find just-inserted record with key "
	//				+ testStrings[j] + " in table",
	//				r, table.find(testStrings[j]));
			size++;
			assertEquals("Table size does not match the anticipated size",
					size, table.size);
		}

		for (int j = 0; j < testStrings.length; j++)
		{
			Record r = new Record(testStrings[j]);
			assertFalse("Insertion of record with duplicate string "
					+  testStrings[j] + " should return false",
					table.insert(r));
		}	
	}	

	@Test
	public void testFind()
	{	
		StringTable table = new StringTable(4);
		for (int j = 0; j < testStrings.length; j++)
		{	
			Record r = new Record(testStrings[j]);
			table.insert(r);
		}	

		for (int j = 0; j < testStrings.length; j++)
		{
			Record r = table.find(testStrings[j]);
			assertTrue("Could not find previously inserted record with string"
					+ testStrings[j],
					r != null);

			assertEquals("Returned record from find has key "
					+  r.key + ", which does not match input "
					+ testStrings[j],
					r.key, testStrings[j]);

		}

		for (int j = 0; j < missingStrings.length; j++)
		{
			Record r = table.find(missingStrings[j]);
			assertTrue("Find of string " + missingStrings[j] 
					+ "not in table should return null!",
					r == null);
		}
	}
	
	@Test 
	public void testZeroHash() throws NoSuchMethodException, SecurityException, 
	IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method method = StringTable.class.getDeclaredMethod("toIndex", int.class);
		method.setAccessible(true);		
		int countZeros = 0;
		for(int i=0; i<20; i++) {
			int hash = (int) method.invoke(table, (int)(Math.random()*10000));
			if(hash==0) {
				countZeros++;
			}
		}
		assertFalse("All of your hash values return a hashcode of 0", countZeros==20);
	}


	@Test
	public void testDelete() 
	{
		StringTable table = new StringTable(4);
		int size = 0;

		for (int j = 0; j < testStrings.length; j++)
		{
			Record r = new Record(testStrings[j]);
			table.insert(r);
			size++;
		}

		for (int j = 0; j < testStrings.length/2; j++)
		{
			table.remove(testStrings[j]);
			size--;
		}

		assertEquals("Table size after deletions should be " + size,
				table.size, size);

		for (int j = 0; j < testStrings.length/2; j++)
		{	
			Record r = table.find(testStrings[j]);
			assertTrue("String " + testStrings[j] 
					+ " should no longer be in table!",
					r == null);
		}

		for (int j = testStrings.length/2; j < testStrings.length; j++)
		{
			Record r = table.find(testStrings[j]);

			assertTrue("Could not find previously inserted record with string"
					+ testStrings[j],
					r != null);

			assertEquals("Returned record from find has key "
					+  r.key + ", which does not match input "
					+ testStrings[j],
					r.key, testStrings[j]);
		}
	}
}
