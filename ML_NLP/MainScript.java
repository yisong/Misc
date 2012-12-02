
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;



public class MainScript {

/*	public static String[] hashtags={"lol", "wtf"};
	
	public static double p = 0.9;
	public static int gramlength = 2;
	
	public static int labelNum = hashtags.length;
	*/
	public static Map<List<String>, Integer> dict = new HashMap<List<String>, Integer>();
	

	public static void main (String[] args) throws IOException{
		System.out.println("Setting up data set");
		DataSet ds = setupProblem();
		ds.reportGroupInfo();
		System.out.println();
		
		DataSet[] dss = ds.split(Config.p);
		DataSet train = dss[0];
		DataSet test = dss[1];

		for (int j = 0; j < 8; j++) {
			System.out.println(ClassifierFactory.type(j));
			Classifier b = ClassifierFactory.construct(j, train);	
			double[][] score = b.test(test);
			for (int i = 0; i < Config.labelNum; i++) {
				System.out.printf("%s :\t%.2f\t%.2f\n", Config.hashtags[i], score[i][0], score[i][1]);
			}
			System.out.println();
		}

	}

	private static DataSet setupProblem() throws IOException {
		DataSet totalDs = new DataSet();
		for (int i = 0; i < Config.labelNum; i++) {
			Scanner input = new Scanner(new File("data/text/" + Config.hashtags[i]));
		    
		    while (input.hasNextLine()) {
		    	Map<Integer, Integer> attriMap = new HashMap<Integer, Integer>();   	
		    	List<String> tokens = parseLine(input.nextLine());
		    	List<List<String>> ngrams = ngrams(tokens);
		    	
		    	for (List<String> sequence : ngrams) {
		    		addgrams(sequence,attriMap);
		    	}
		    		
		    	totalDs.addTuple(new DataTuple(i, attriMap));
		    }
		}
	    return totalDs;
	}
	
	private static List<String> parseLine(String line){
		Scanner s = new Scanner(line);
		List<String> tokenList = new ArrayList<String>();
		while(s.hasNext()){
			tokenList.add(s.next());
		}
		return tokenList;
	}
	
	
	private static List<List<String>> ngrams (List<String> tokens) {
		List<List<String>> ngrams = new ArrayList<List<String>>();
		int n = tokens.size();
		for (int i = 1; i <= Config.gramlength; i ++) {
			for (int j = 0; j < n - i + 1; j ++) {
				List<String> sequence = new ArrayList<String>(i);
				for (int k = 0; k < i; k++) {
					sequence.add(tokens.get(j + k));
				}
				ngrams.add(sequence);
			}
		}
		return ngrams;
	}
	
	private static void addgrams(List<String> grams, Map<Integer, Integer> attriMap){
		
		Integer index = dict.get(grams);
		if (index == null) {
			index = dict.size();
			dict.put(grams, index);

		} 
		Integer old = attriMap.get(index);
		attriMap.put(index, old == null? 1 : old + 1);		
	}
	
	
}
