import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;


public class Train {

	public static Map<List<String>, Integer> dict ;
	
	public static void main(String[] args) throws IOException {
		dict = new HashMap<List<String>, Integer>();
		System.out.println("Reading training data\n");
		DataSet train = setupProblem("data/train/", true);
		train.setFeatureNum(dict.size());
		DataSet test = setupProblem("data/test/", false);

		writeDict();
		for (int j = 0; j < 8; j++) {
			System.out.println(ClassifierFactory.type(j));
			Classifier b = ClassifierFactory.construct(j, train);
			b.write("model-" + Config.gramlength +"/" + ClassifierFactory.type(j));
			double[][] score = b.test(test);
			for (int i = 0; i < Config.labelNum; i++) {
				System.out.printf("%s :\t%.2f\t%.2f\n", Config.hashtags[i], score[i][0], score[i][1]);
			}
			System.out.println();
		}
	}
	
	private static void writeDict() throws IOException{
		PrintStream output = new PrintStream(new File("model-" + Config.gramlength +"/dict"));
		for (Entry<List<String>, Integer>  entry: dict.entrySet()) {
			for (String s : entry.getKey()) {
				output.print(s + " ");
			}
			output.println();
			output.println(entry.getValue());
		}
	}
	
	static DataSet setupProblem(String dir, boolean addDict) throws IOException {
		DataSet totalDs = new DataSet();
		for (int i = 0; i < Config.labelNum; i++) {
			Scanner input = new Scanner(new File(dir + Config.hashtags[i]));
		    
		    while (input.hasNextLine()) {
		    	Map<Integer, Integer> attriMap = new HashMap<Integer, Integer>();   	
		    	List<String> tokens = parseLine(input.nextLine());
		    	List<List<String>> ngrams = ngrams(tokens);
		    	
		    	for (List<String> sequence : ngrams) {
		    		addgrams(sequence,attriMap, addDict);
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
	
	private static void addgrams(List<String> grams, Map<Integer, Integer> attriMap, boolean addDict){
		
		Integer index = dict.get(grams);
		if (index == null) {
			if (addDict){
				index = dict.size();
				dict.put(grams, index);
			} else {
				return;
			}

		} 
		Integer old = attriMap.get(index);
		attriMap.put(index, old == null? 1 : old + 1);		
	}
}
