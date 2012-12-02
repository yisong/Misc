package test;

import java.util.List;

import main.Pair;
import main.Silhouette;
import main.Triple;

public class Test {

	public static void main(String[] args){
		Triple[] input = new Triple[]{new Triple(1,2,1), new Triple(10,12,2), new Triple(2,4,1), new Triple(5,8,3), new Triple(3,6,4)};
		List<Pair> result = Silhouette.silhouette(input);
		System.out.println(result);
	}
}
