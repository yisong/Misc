package test;

import main.SubString;

public class Test {

	public static void main(String[] args){
		System.out.println(SubString.isSubString("banana", "ana"));
		System.out.println(SubString.isSubString("banana", "ba"));
		System.out.println(SubString.isSubString("banana", "a"));
		System.out.println(SubString.isSubString("banana", "bb"));
	}
}
