package heaps.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import org.junit.Rule;
import org.junit.Test;

import heaps.Decreaser;
import heaps.HeapSort;
import heaps.MinHeap;
import heaps.util.HeapToStrings;
import heaps.validate.MinHeapValidator;
import timing.InputSpec;
import timing.Ticker;
import timing.utils.IntArrayGenerator;

/**
 * 
 * @author roncytron and timhuber
 *      updated by stevecole and leahrivkin
 *
 */
public class testMinHeap extends TestUtil {

	@Test
	public void testEmptyHeap() {
		MinHeap<Integer> pq = genHeap(0).pq;	
		assertTrue("The heap after construction should be empty, but was not:  check your isEmpty() method", pq.isEmpty());
		assertEquals("The heap after construction should have 0 elements, but did not: check your size() method", 0, pq.size());
		verifySizeCapacity(pq);
	}

	@Test
	public void testInsert() {
		MinHeapValidator<Integer> mhv = genHeap(200);
		MinHeap<Integer> pq = mhv.pq;	
		int num = r.nextInt(100) + 1;
		for (int i=0; i < num; ++i) {
			verifySize("before insert", pq, i);
			verifySizeCapacity(pq);
			mhv.check();
			pq.insert(i);
			mhv.check();
			verifySize("after insert", pq, i+1);
			assertTrue("Heap should not be empty now: check your isEmpty() method", !pq.isEmpty());
		}
	}
	
	@Test
	public void testExtractMin() {
		MinHeapValidator<Integer> mhv = genHeap(200);
		MinHeap<Integer> pq = mhv.pq;	
		int n = r.nextInt(1000);
		int num = r.nextInt(100) + 1;
		for (int i=0; i < num; ++i) {
			mhv.check();
			pq.insert(n);
			mhv.check();
		}
		for (int i=0; i < num; ++i) {
			verifySizeCapacity(pq);
			verifySize("before extractMin", pq, num-i);
			mhv.check();
			int frompq = pq.extractMin();
			assertEquals("Should only see value " + n + " but saw value " + frompq,
					n, frompq);
			verifySize("after extractMin", pq,num-i-1);
			verifySizeCapacity(pq);
			mhv.check();
		}
		assertTrue("Heap should be empty now", pq.isEmpty());
	}
	
	@Test
	public void testUsingHeapSort() {
		HeapSort heaper = new HeapSort();
		heaper.loadInput(new IntArrayGenerator().genInput(InputSpec.gen(1000000)));
		heaper.reset(new Ticker());
		heaper.run();
	}
	
	//
	//Test the insert and extract min methods of the priority queue by generating 
	//an unsorted array of integers, inserting that array into a priority queue, and
	//then comparing the output of extractMin() with the respective value in the
	//sorted starting array
	//
	@Test
	public void testViaSorting() {
		for (int i=0; i < 10; ++i) {
			System.out.println("Testing unique size " + i);
			sortTest(genUniqueInts(i), false);
			System.out.println("done");
		}
		for (int i=0; i < 10; ++i) {
			System.out.println("Testing with duplicates size " + i);
			sortTest(genDupInts(i), false);
			System.out.println("done");
		}
	}
	
	@Test
	public void testLocUpdatedDecrease() {
		//
		// Test that decrease updates the .loc field
		//
		MinHeapValidator<Integer> mhv = genHeap(5);
		MinHeap<Integer> pq = mhv.pq;
		mhv.check();
		Decreaser<Integer> d131 = pq.insert(131);
		mhv.check();
		String before = d131.toString();
		mhv.check();
		pq.insert(1); // should cause 131 to change locations
		mhv.check();
		String after = d131.toString();
		assertTrue("You did not update the .loc field of affected heap elements in decrease",
				!before.equals(after));
	}
	
	@Test
	public void testLocUpdatedHeapify() {
		//
		// Test that decrease updates the .loc field
		//
		MinHeapValidator<Integer> mhv = genHeap(10);
		MinHeap<Integer> pq = mhv.pq;
		for (int i=1; i <= 6; ++i) {
			mhv.check();
			pq.insert(i);
			mhv.check();
		}
		Decreaser<Integer> d131 = pq.insert(131);
		String before = d131.toString();
		mhv.check();
		pq.extractMin(); // should cause 131 to change locations
		mhv.check();
		String after = d131.toString();
		assertTrue("You did not update the .loc field of affected heap elements in heapify",
				!before.equals(after));
	}

	@Test
	public void testAndPrint() {
		System.out.println("Displaying a correct heap of 50 unique integers:");
		sortTest(genUniqueInts(50), true);
	}

}
