package hash;

import java.util.LinkedList;
import java.util.ListIterator;

//
// STRINGTABLE.JAVA
// A hash table mapping Strings to their positions in the the pattern sequence
// You get to fill in the methods for this part.
//
public class StringTable {
    
    private LinkedList<Record>[] buckets;
    private int nBuckets;

    //
    // number of records currently stored in table --
    // must be maintained by all operations
    //
    public int size;
    
    
    //
    // Create an empty table with nBuckets buckets
    //
    @SuppressWarnings("unchecked")
	public StringTable(int nBuckets)
    {
    	this.nBuckets = nBuckets;
    	buckets = new LinkedList[nBuckets];
    	
    	// TODO - fill in the rest of this method to initialize your table
    	
    	////////////////////////////////////////////////////////////////
    	
    	// 1. Use for-loop to allocate the linked list for each hash bucket
    	for (int i = 0; i < this.nBuckets; i++) {
    		buckets[i] = new LinkedList<Record>(); // allocate linked list
    	}
    	
    	////////////////////////////////////////////////////////////////
    	
    }
    
    
    /**
     * insert - inserts a record to the StringTable
     *
     * @param r
     * @return true if the insertion was successful, or false if a
     *         record with the same key is already present in the table.
     */
    public boolean insert(Record r) 
    {  
    	// TODO - implement this method
    	
    	////////////////////////////////////////////////////////////////
    	
    	// My thought process: front end --> back end --> analyze 3 cases
    	
    	// 1. Front end: Initialize Record r's key and corresponding hashcode c
    	String r_key = r.key;
    	
    	int r_hashcode_c = stringToHashCode(r_key);
    	
    	// 2. Back end: Find the bucket_index that r's hashcode maps to
    	int bucket_index_j = toIndex(r_hashcode_c);

    	LinkedList<Record> bucket_j = buckets[bucket_index_j]; // locate the current bucket
    	
    	// 3. Case 1 (The Base Case): current bucket is empty, simply add r to it
    	if (bucket_j.isEmpty() == true) {
    		bucket_j.add(r); // add Record r to the current empty bucket
    		
    		size ++; // adjust the number of records currently stored in table
    		
    		return true; // insertion success!
    	}
    	
    	else {
        	
    		// 4. Case 2: current bucket has a record with the same key, insertion failed
        	ListIterator<Record> list_iterator = bucket_j.listIterator();
        	
        	while (list_iterator.hasNext() == true) { // iterate among records in the current bucket
        		Record record_in_the_bucket = list_iterator.next();
        		
        		if (r_key.equals(record_in_the_bucket.key)) { // current bucket has a record with the same key
        			return false; // insertion failed
        		}
        	}
        	
        	// 5. Case 3: current bucket is not empty, but does not have a record with the same key, insertion success! 
        	bucket_j.addLast(r); // append Record r at the end of the bucket
        	
        	size++; // adjust the number of records currently stored in table
        	
        	return true;
    	}

    	////////////////////////////////////////////////////////////////
    }
    
    
    /**
     * find - finds the record with a key matching the input.
     *
     * @param key
     * @return the record matching this key, or null if it does not exist.
     */
    public Record find(String key) 
    {
    	// TODO - implement this method
    	
    	////////////////////////////////////////////////////////////////
    	// I think this method is very similar to the previous insert method.
    	
    	// My thought process: front end --> back end --> analyze 3 cases
    	
    	// 1. Front end: Initialize key's corresponding hashcode c
    	int key_hashcode_c = stringToHashCode(key);
    	
    	// 2. Back end: Find the bucket_index that key's hashcode maps to
    	int bucket_index_j = toIndex(key_hashcode_c);
    	
    	LinkedList<Record> bucket_j = buckets[bucket_index_j]; // locate the current bucket
    	
    	// 3. Case 1 (The Base Case): current bucket is empty, return null
    	if (bucket_j.isEmpty() == true) {
    		
    		return null;
    	}
    	
    	else {
    		
    		// 4. Case 2: current bucket has a record with the same key, found it!
        	ListIterator<Record> list_iterator = bucket_j.listIterator();
        	
        	while (list_iterator.hasNext() == true) { // iterate among records in the current bucket
        		Record record_in_the_bucket = list_iterator.next();
        		
        		if (key.equals(record_in_the_bucket.key)) { // current bucket has a record with the same key
        			return record_in_the_bucket;
        		}
        	}
        	
        	// 5. Case 3: current bucket is not empty, but does not have a record with the same key
        	return null;
    	}
    	
    	////////////////////////////////////////////////////////////////
    	
    }
    
    
    /**
     * remove - finds a record in the StringTable with the given key
     * and removes the record if it exists.
     *
     * @param key
     */
    public void remove(String key) 
    {
    	// TODO - implement this method
    	
    	////////////////////////////////////////////////////////////////
    	
    	// My thought process: front end --> back end --> analyze 1 case
    	
    	// 1. Front end: Initialize key's corresponding hashcode c
    	int key_hashcode_c = stringToHashCode(key);
    	
    	// 2. Back end: Find the bucket_index that key's hashcode maps to
    	int bucket_index_j = toIndex(key_hashcode_c);
    	
    	LinkedList<Record> bucket_j = buckets[bucket_index_j]; // locate the current bucket
    	
    	// 3. Only consider the case where the current bucket is not empty
    	if (bucket_j.isEmpty() == false) {
    		
    		ListIterator<Record> list_iterator = bucket_j.listIterator();
    		
    		while (list_iterator.hasNext() == true) { // iterate among records in the current bucket
    			Record record_in_the_bucket = list_iterator.next();
    			
    			if (key.equals(record_in_the_bucket.key)) { // current bucket has a record with the same key
    				bucket_j.remove(record_in_the_bucket);
    				
    				size --; // adjust the number of records currently stored in table
    				break;
    			}
    		}
    	}
    	////////////////////////////////////////////////////////////////
    	
    	
    }
    

    /**
     * toIndex - convert a string's hashcode to a table index
     *
     * As part of your hashing computation, you need to convert the
     * hashcode of a key string (computed using the provided function
     * stringToHashCode) to a bucket index in the hash table.
     *
     * You should use a multiplicative hashing strategy to convert
     * hashcodes to indices.  If you want to use the fixed-point
     * computation with bit shifts, you may assume that nBuckets is a
     * power of 2 and compute its log at construction time.
     * Otherwise, you can use the floating-point computation.
     */
    private int toIndex(int hashcode)
    {
    	// Fill in your own hash function here
    	
    	////////////////////////////////////////////////////////////////
    	
    	// 1. Pick Professor Knuth's irrational A from [0.5, 1)
    	double knuth_A = (Math.sqrt(5) - 1) / 2;
    	
    	// 2. Apply the multiplication hashing function: j = b(c) = | ((c*A)%1.0) * m |
    	int index_j = Math.abs((int)(((hashcode * knuth_A) % 1.0) * nBuckets));
    	
    	// 3. Return the converted indices
    	return index_j;
    	
    	////////////////////////////////////////////////////////////////
    	
    }
    
    
    /**
     * stringToHashCode
     * Converts a String key into an integer that serves as input to
     * hash functions.  This mapping is based on the idea of integer
     * multiplicative hashing, where we do multiplies for successive
     * characters of the key (adding in the position to distinguish
     * permutations of the key from each other).
     *
     * @param string to hash
     * @returns hashcode
     */
    int stringToHashCode(String key)
    {
    	int A = 1952786893;
	
    	int v = A;
    	for (int j = 0; j < key.length(); j++)
	    {
    		char c = key.charAt(j);
    		v = A * (v + (int) c + j) >> 16; // right shifts the result by 16 bits
	    }
	
    	return v;
    }

    /**
     * Use this function to print out your table for debugging
     * purposes.
     */
    public String toString() 
    {
    	StringBuilder sb = new StringBuilder();
	
    	for(int i = 0; i < nBuckets; i++) 
	    {
    		sb.append(i+ "  ");
    		if (buckets[i] == null) 
		    {
    			sb.append("\n");
    			continue;
		    }
    		for (Record r : buckets[i]) 
		    {
    			sb.append(r.key + "  ");
		    }
    		sb.append("\n");
	    }
    	return sb.toString();
    }
}
