package hash.test;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.*;

import hash.Record;
import hash.StringTable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class TestStringTable {

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
			assertEquals("Cannot find just-inserted record with key "
					+ testStrings[j] + " in table",
					r, table.find(testStrings[j]));
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

	private final String baseURL = "https://classes.engineering.wustl.edu/cse247/test/";
	private final String[] testCases = 
		{
				"4 case1-corpus case1-pattern case1-mask",
				"16 case2-corpus case2-pattern case2-mask",
				"18 case3-corpus case3-pattern",
				"180 case4-corpus case4-pattern case4-mask",
		};

	private String toURL(String caseName) {
		return baseURL + "test-cases/" + caseName + ".txt";
	}

	@Test
	public void testDNA1()
	{
		testWithDNA(1, testCases[0]);
	}

	@Test
	public void testDNA2()
	{
		testWithDNA(2, testCases[1]);
	}

	@Test
	public void testDNA3()
	{
		testWithDNA(3, testCases[2]);
	}

	@Test
	public void testDNA4()
	{
		testWithDNA(4, testCases[3]);
	}

	private void testWithDNA(int id, String argstring) 
	{
		PrintStream stdout = System.out;
		System.setOut(stdout);
		System.out.println("Testing DNA test case " + id);
		try {
			System.setOut(new PrintStream(new FileOutputStream("labs/hash/output/case"
					+ id + "-observed.txt")));
		} catch (FileNotFoundException e) {	
			e.printStackTrace();
		}

		String[] testInfo = argstring.split(" ");
		for(int i=1; i<testInfo.length; i++) {
			testInfo[i]= toURL(testInfo[i]);
		}

		hash.Main.main(testInfo);

		// reset out to print on the console window
		System.setOut(stdout);

		// now need to compare outputs
		try {
			boolean testCase = compareOutput("labs/hash/output/case" 
					+ id +"-observed.txt", 
					"labs/hash/output/case"
							+ id +"-expected.txt");
			assertTrue("The observed output for case" 
					+ id +" differs from the expected output.",
					testCase);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Observed output for case" 
				+ id +" matches expected output!\n");
	}

	private boolean compareOutput(String observed, String expected) 
			throws IOException {
		BufferedReader reader1 = new BufferedReader(new FileReader(observed));
		BufferedReader reader2 = new BufferedReader(new FileReader(expected));

		String line1 = reader1.readLine();
		String line2 = reader2.readLine();

		boolean areEqual = true;
		int lineNum = 1;

		while(line1 != null || line2 != null) {
			if(line1==null || line2==null) {
				areEqual = false;
				break;	// extra lines in one of them
			}
			if(!line1.equals(line2)) {
				areEqual = false;
				break;
			}
			line1 = reader1.readLine();
			line2 = reader2.readLine();
			lineNum++;
		}
		reader1.close();
		reader2.close();
		if(!areEqual) {

			if(line1==null || line1.trim().equals("")) {
				System.out.println("The observed output is missing lines starting at line " + lineNum + "\n\texpected: "+line2);
			}
			else if(line2==null || line2.trim().equals("")) {
				System.out.println("The observed output has extra lines starting at line " + lineNum + "\n\tobserved: "+line1);
			}
			else{
				System.out.println("The output files do not match starting at line " + lineNum + " \n\tobserved: " +line1 + " \n\texpected: "+line2);
			}
		}
		return areEqual;
	}
}
