package test;

import org.junit.Test;
import static org.junit.Assert.*;

import main.OrderedAdditionCombination;
import main.UnorderedAdditionCombination;
import main.UnorderedNonduplicateAdditionCombination;

public class AdditionCombinationTest {

	public static int[] numbers = {2, 3, 5, 7};
	public static int[] tests = {5, 6, 7, 8, 9, 10, 15};
	
	@Test
	public void UnorderedAdditionCombinationTest(){
		int[] result = new int[]{2, 2, 3, 3, 4, 5, 10};
		
		for(int i = 0; i < 7; i ++) {
			assertEquals(UnorderedAdditionCombination.compute(tests[i]), result[i]);
		}
	}
	
	@Test
	public void OrderedAdditionCombinationTest(){
		int[] result = new int[]{3, 2, 6, 6, 10, 16, 100};
		
		for(int i = 0; i < 7; i ++) {
			assertEquals(OrderedAdditionCombination.compute(tests[i]), result[i]);
		}
	}
		
	@Test
	public void OrderedAdditionCombinationTest2(){
		int[] result = new int[]{3, 2, 6, 6, 10, 16, 100};
		
		for(int i = 0; i < 7; i ++) {
			assertEquals(OrderedAdditionCombination.compute2(tests[i]), result[i]);
		}
	}
	
	@Test
	public void OrderedNonduplicateAdditionCombinationTest(){
		int[] result = new int[]{2, 0, 2, 1, 1, 2, 1};
		
		for(int i = 0; i < 7; i ++) {
			assertEquals(UnorderedNonduplicateAdditionCombination.compute(tests[i]), result[i]);
		}
	}
}