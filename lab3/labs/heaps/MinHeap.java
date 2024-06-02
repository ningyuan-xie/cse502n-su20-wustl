package heaps;

import java.util.Random;
import java.util.UUID;

import javax.swing.JOptionPane;

import heaps.util.HeapToStrings;
import heaps.validate.MinHeapValidator;
import timing.Ticker;

public class MinHeap<T extends Comparable<T>> implements PriorityQueue<T> {

	private Decreaser<T>[] array;
	private int size;
	private final Ticker ticker;

	/**
	 * I've implemented this for you.  We create an array
	 *   with sufficient space to accommodate maxSize elements.
	 *   Remember that we are not using element 0, so the array has
	 *   to be one larger than usual.
	 * @param maxSize
	 */
	@SuppressWarnings("unchecked")
	public MinHeap(int maxSize, Ticker ticker) {
		this.array = new Decreaser[maxSize+1];
		this.size = 0;
		this.ticker = ticker;
	}

	//
	// Here begin the methods described in lecture
	//
	
	/**
	 * Insert a new thing into the heap.  As discussed in lecture, it
	 *   belongs at the end of objects already in the array.  You can avoid
	 *   doing work in this method by observing, as in lecture, that
	 *   inserting into the heap is reducible to calling decrease on the
	 *   newly inserted element.
	 *   
	 *   This method returns a Decreaser instance, which for the inserted
	 *   thing, tracks the thing itself, the location where the thing lives
	 *   in the heap array, and a reference back to MinHeap so it can call
	 *   decrease(int loc) when necessary.
	 */
	public Decreaser<T> insert(T thing) {
		//
		// Below we create the "handle" through which the value of
		//    the contained item can be decreased.
		// VERY IMPORTANT!
		//    The Decreaser object contains the current location
		//    of the item in the heap array.  Initially it's ++size,
		//    as shown below.  The size is increased by 1, and that's
		//    where you should store ans in the heap array.
		//
		//    If and when the element there changes location in the heap
		//    array, the .loc field of the Decreaser must change to reflect
		//    that.
		//
        // NOTE: when swapping elements, make sure you do not create any new
        //       Decreasers with the "new" keyword!  This may work for this lab
        //       but will cause hard-to-track errors in a future lab that makes
        //       use of the MinHeap.
	    //   
		Decreaser<T> ans = new Decreaser<T>(thing, this, ++size);
		ticker.tick(); // tick once
		//
		// You have to now put ans into the heap array
		//   Recall in class we reduced insert to decrease
		//
		// FIXME
		
		// 1. Put ans into the heap array
		array[this.size] = ans;
		ticker.tick(); // tick once
		
		// 2. Update ans's location
		array[this.size].loc = size;
		ticker.tick(); // tick once
		
		// 3. Swapping elements using the "decrease" method from below
		decrease(size); // recursion's input is the inserted value's location
		ticker.tick(); // tick once
		
		// 
		return ans;
	}

	/**
	 * This method responds to an element in the heap decreasing in
	 * value.   As described in lecture, that element might have to swap
	 * its way up the tree so that the heap property is maintained.
	 * 
	 * This method can be called from within this class, in response
	 *   to an insert.  Or it can be called from a Decreaser.
	 *   The information needed to call this method is the current location
	 *   of the heap element (index into the array) whose value has decreased.
	 *   
	 * Really important!   If this method changes the location of elements in
	 *   the array, then the loc field within those elements must be modified 
	 *   too.  For example, if a Decreaser d is currently at location 100,
	 *   then d.loc == 100.  If this method moves that element d to
	 *   location 50, then this method must set d.loc = 50.
	 *   
	 * In my solution, I made sure the above happens by writing a method
	 *    moveItem(int from, int to)
	 * which moves the Decreaser from index "from" to index "to" and, when
	 * done, sets array[to].loc = to
     *
     * NOTE: when swapping elements, make sure you do not create any new
     *       Decreasers with the "new" keyword!  This may work for this lab
     *       but will cause hard-to-track errors in a future lab that makes
     *       use of the MinHeap.
	 *   
	 * This method is missing the "public" keyword so that it
	 *   is only callable within this package.
	 * @param loc position in the array where the element has been
	 *     decreased in value
	 */
	void decrease(int loc) {
		//
		// As described in lecture
		//
		
		// 1. Set base case
		if (loc == 1) {    // when the input is at the root
			ticker.tick(); // tick once
			return;   	   // stop the recursion
		}
		
		// 2. Swap child with parent
		Decreaser<T> temp; // define "temp" for swap
		temp = array[loc]; // "temp" stores child
		ticker.tick(); // tick once
		
		if (array[loc].getValue().compareTo(array[loc/2].getValue()) < 0) {
			ticker.tick(); // tick once
			
			array[loc] = array[loc/2]; // child swap with parent
			ticker.tick(); // tick once
			
			array[loc/2] = temp; 	   // parent swap with child
			ticker.tick(); // tick once
			
			// 3. Update location
			array[loc/2].loc = loc/2;  // update the new parent's location
			ticker.tick(); // tick once
			
			array[loc].loc = loc;	   // update the new child's location
			ticker.tick(); // tick once
			
			// 4. Recursion
			decrease(loc/2);	       // Recursion "UP" to the root
		}
		
		
	}
	
	/**
	 * Described in lecture, this method will return a minimum element from
	 *    the heap.  The hole that is created is handled as described in
	 *    lecture.
	 *    This method should call heapify to make sure the heap property is
	 *    maintained at the root node (index 1 into the array).
	 */
	public T extractMin() {
		T ans = array[1].getValue();
		ticker.tick(); // tick once
		//
		// There is effectively a hole at the root, at location 1 now.
		//    Fix up the heap as described in lecture.
		//    Be sure to store null in an array slot if it is no longer
		//      part of the active heap
		//
		// FIXME
		
		// 1. Fill the hole at the root with the last value
		array[1] = array[size];
		ticker.tick(); // tick once
		
		// 2. Update the root's value location
		array[1].loc = 1;
		ticker.tick(); // tick once
		
		// 3. Update the empty child node and size
		array[size] = null;
		this.size -= 1;
		ticker.tick(2); // tick twice
		
		// 4. Recursion using heapify written below 
		if (this.size > 1) {
			ticker.tick(); // tick once
			
			heapify(1); // recurse from the root all the way down to the bottom to check heap property.
			ticker.tick(); // tick once
		}

		//
		return ans;
	}

	/**
	 * As described in lecture, this method looks at a parent and its two 
	 *   children, imposing the heap property on them by perhaps swapping
	 *   the parent with the lesser of the two children.  The child thus
	 *   affected must be heapified itself by a recursive call.
	 * @param where the index into the array where the parent lives
	 */
	
	// Create a helper method for heapify
	public void helpSwap(int p, int c) { // p = parent's location; c = child's location
		
		// 1. Define "temp" for swap
		Decreaser<T> temp; 		// Define temp for swap
		temp = array[c];   		// "temp" stores child
		ticker.tick(); // tick once
		
		// 2. Swap child with parent
		array[c] = array[p];	// Child swap with parent
		array[p] = temp; 		// Parent swap with child
		ticker.tick(2); // tick twice
		
		// 3. Update location
		array[c].loc = c; 		// Update the new parent's location
		array[p].loc = p; 		// Update the new child's location
		ticker.tick(2); // tick twice
		
	}
	
	private void heapify(int where) { // where = parent's location; 
									  // where * 2 is left child's location; where * 2 + 1 is right child's location.
		//
		// As described in lecture
		//  FIXME
		
		///////////////////////// Section 1: rule out all the base case //////////////////////////////
		
		// Situation 1: parent is larger than size (base case)
		if (where > size || array[where] == null){
			ticker.tick(2); // tick twice
			
			return; // stop the recursion
		}
		
		// Situation 2: left child is larger than size (base case)
		if (where * 2 > size || array[where * 2] == null){
			ticker.tick(2); // tick twice
			
			return; // stop the recursion
		}
		
		// Situation 3: right child is larger than size and left child < parent, swap left child with parent
		if (where * 2 + 1 > size || array[where * 2 + 1] == null) {
			if ((array[where * 2].getValue().compareTo((array[where]).getValue())<0)){ // left child < parent
				ticker.tick(2); // tick twice
				
				helpSwap(where, where * 2);											   // swap left child with parent
				ticker.tick();  // tick once
			}
			
			// Situation 4: right child is larger than size and left child >= parent (base case)
			return; // stop the recursion
		}
		///////////////////////// Section 1 ends here ////////////////////////////////////////////////
		
		
		///////////////////////// Section 2: enumurate all permutations //////////////////////////////
		
		// Here is a brief summery of the 6 situations I found:
		// (The test is truly comprehensive! I only found all situations through fixing errors.)
		
		// Situation 1: parent < left child <= right child, stop the recurtion
		// Situation 2: parent < right child <= left child, stop the recurtion
		
		// Situation 3: left child < parent <= right child, swap left child with parent
		// Situation 4: left child <= right child < parent, swap left child with parent
		
		// Situation 5: right child <= left child < parent, swap right child with parent
		// Situation 6: right child < parent <= left child, swap right child with parent
		
		// Here are all the 6 detailed situations with codes:
		
		// Situation 1: set base case: parent < left child <= right child
		if (array[where].getValue().compareTo(array[where * 2].getValue()) < 0			   // parent < left child
		 && array[where * 2].getValue().compareTo(array[where * 2 + 1].getValue()) <= 0) { // left child <= right child
			ticker.tick(2); // tick twice
			
			return;																		   // stop the recursion
		}
		
		// Situation 2: set base case: parent < right child <= left child
		if (array[where].getValue().compareTo(array[where * 2 + 1].getValue()) < 0		   // parent < right child
		 && array[where * 2 + 1].getValue().compareTo(array[where * 2].getValue()) <= 0) { // right child <= left child
			ticker.tick(2); // tick twice
			
			return;																		   // stop the recursion
		}
		
		// Situation 3: left child < parent < right child
		if (array[where * 2].getValue().compareTo(array[where].getValue()) < 0		      // left child < parent            
		 && array[where].getValue().compareTo(array[where * 2 + 1].getValue()) <= 0) {    // parent <= right child
			ticker.tick(2); // tick twice	
				
				helpSwap(where, where * 2);												  // swap left child with parent
				ticker.tick(); // tick once
				
				heapify(where * 2);														  // recursion "DOWN" the tree to left child's location    
		}
		
		// Situation 4: left child <= right child < parent
		if (array[where * 2].getValue().compareTo(array[where * 2 + 1].getValue()) <= 0	  // left child <= right child            
		 && array[where * 2 + 1].getValue().compareTo(array[where].getValue()) < 0) {     // right child < parent
			ticker.tick(2); // tick twice
			
				helpSwap(where, where * 2);												  // swap left child with parent
				ticker.tick(); // tick once
				
				heapify(where * 2);														  // recursion "DOWN" the tree to left child's location    
		}		
		
		// Situation 5: right child <= left child < parent
		if (array[where * 2 + 1].getValue().compareTo(array[where * 2].getValue()) <= 0   // right child <= left child
		 && array[where * 2].getValue().compareTo(array[where].getValue()) < 0) { 		  // left child < parent
			ticker.tick(2); // tick twice
				
				helpSwap(where, where * 2 + 1);											  // swap right child with parent
				ticker.tick(); // tick once
				
				heapify(where * 2 + 1);													  // recursion "DOWN" the tree to right child's location
		}
		
		// Situation 6: right child < parent < left child
		if (array[where * 2 + 1].getValue().compareTo(array[where].getValue()) < 0        // right child < parent
		 && array[where].getValue().compareTo(array[where * 2].getValue()) <= 0) { 		  // parent <= left child
			ticker.tick(2); // tick twice	
				
				helpSwap(where, where * 2 + 1);											  // swap right child with parent
				ticker.tick(); // tick once
				
				heapify(where * 2 + 1);													  // recursion "DOWN" the tree to right child's location
		}
		///////////////////////// Section 2 ends here ////////////////////////////////////////////////
		
		//
	}
	
	/**
	 * Does the heap contain anything currently?
	 * I implemented this for you.  Really, no need to thank me!
	 */
	public boolean isEmpty() {
		return size == 0;
	}
	
	//
	// End of methods described in lecture
	//
	
	//
	// The methods that follow are necessary for the debugging
	//   infrastructure.
	//
	/**
	 * This method would normally not be present, but it allows
	 *   our consistency checkers to see if your heap is in good shape.
	 * @param loc the location
	 * @return the value currently stored at the location
	 */
	public T peek(int loc) {
		if (array[loc] == null)
			return null;
		else return array[loc].getValue();
	}

	/**
	 * Return the loc information from the Decreaser stored at loc.  They
	 *   should agree.  This method is used by the heap validator.
	 * @param loc
	 * @return the Decreaser's view of where it is stored
	 */
	public int getLoc(int loc) {
		return array[loc].loc;
	}

	public int size() {
		return this.size;
	}
	
	public int capacity() {
		return this.array.length-1;
	}
	

	/**
	 * The commented out code shows you the contents of the array,
	 *   but the call to HeapToStrings.toTree(this) makes a much nicer
	 *   output.
	 */
	public String toString() {
//		String ans = "";
//		for (int i=1; i <= size; ++i) {
//			ans = ans + i + " " + array[i] + "\n";
//		}
//		return ans;
		return HeapToStrings.toTree(this);
	}

	/**
	 * This is not the unit test, but you can run this as a Java Application
	 * and it will insert and extract 100 elements into the heap, printing
	 * the heap each time it inserts.
	 * @param args
	 */
	public static void main(String[] args) {
		JOptionPane.showMessageDialog(null, "You are welcome to run this, but be sure also to run the TestMinHeap JUnit test");
		MinHeap<Integer> h = new MinHeap<Integer>(500, new Ticker());
		MinHeapValidator<Integer> v = new MinHeapValidator<Integer>(h);
		Random r = new Random();
		for (int i=0; i < 100; ++i) {
			v.check();
			h.insert(r.nextInt(1000));
			v.check();
			System.out.println(HeapToStrings.toTree(h));
			//System.out.println("heap is " + h);
		}
		while (!h.isEmpty()) {
			int next = h.extractMin();
			System.out.println("Got " + next);
		}
	}


}
