package test;

import main.Majority;

public class Test {

	public static void main(String[] args){
		System.out.println(Majority.findMajority(new int[]{1, 2, 2}));
		System.out.println(Majority.findMajority(new int[]{1, 2, 4, 1, 6, 1, 7, 1, 1}));
		System.out.println(Majority.findMajority(new int[]{1, 2, 2, 1, 2, 1, 7, 2, 2, 2}));
	}
}
