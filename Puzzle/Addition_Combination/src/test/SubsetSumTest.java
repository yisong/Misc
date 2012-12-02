package test;

import main.KSubsetSum;
import main.SubsetSum;

import org.junit.Test;
import static org.junit.Assert.*;

public class SubsetSumTest {

	public static int[] numbers = {-6, -2, 3, 7};
	
	@Test
	public void subsetSumTest(){
		assertTrue(SubsetSum.compute(0));
		assertTrue(SubsetSum.compute(1));
		assertTrue(SubsetSum.compute(-1));
		assertTrue(SubsetSum.compute(4));
		assertTrue(SubsetSum.compute(5));
		assertFalse(SubsetSum.compute(6));
		assertFalse(SubsetSum.compute(12));
		assertTrue(SubsetSum.compute(-3));
		assertTrue(SubsetSum.compute(-5));
	}
	
	@Test
	public void kSubsetSumTest(){
		assertTrue(KSubsetSum.compute(-2, 1));
		assertTrue(KSubsetSum.compute(1, 2));
		assertTrue(KSubsetSum.compute(10, 2));
		assertTrue(KSubsetSum.compute(-5, 3));
		assertFalse(KSubsetSum.compute(-5, 2));
		assertTrue(KSubsetSum.compute(2, 4));
		assertFalse(KSubsetSum.compute(2, 3));
		assertFalse(KSubsetSum.compute(0, 1));
	}
}
