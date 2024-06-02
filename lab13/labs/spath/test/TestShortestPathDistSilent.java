package spath.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import spath.ShortestPaths;
import spath.graphs.DirectedGraph;

public class TestShortestPathDistSilent {
	private GraphGenerator gg;

	@Test
	public void test() {
		int[] shortestPathLengths = { 3,  4,  5,  6,  8, 10,  50, 100,  300};
		int[] totalVertices       = { 5,  8, 10, 12, 16, 20,  75, 200, 1000};
		int[] totalEdges          = {10, 16, 20, 24, 32, 50, 300, 400, 5000};

		for(int i = 0; i < totalEdges.length; i++) {
			gg = new GraphGenerator(totalVertices[i], totalEdges[i], shortestPathLengths[i]);
			gg.genShortestPath();

			genGraphAndTest();

			while(gg.getNumVerts() < totalVertices[i]) {
				gg.addEdgeWithVertex();
				genGraphAndTest();
			}

			while(gg.getNumEdges() < totalEdges[i]) {
				gg.addEdge();
				genGraphAndTest();
			}
		}
	}

	private void genGraphAndTest() {
		DirectedGraph g = gg.getGraph();	
		ShortestPaths sp = new ShortestPaths(g, gg.weights(), gg.start());
		sp.run();
		
		assertEquals("Total weight of the shortest path was incorrect.", gg.getShortestPathValue(), 
				sp.returnLengthDirect(gg.end()));
	}

}
