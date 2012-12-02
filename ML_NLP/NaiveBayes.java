import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;


public class NaiveBayes extends Classifier{

	private int[] classWordCount;
	private Map<Integer, int[]> wordCount;
	private int[] groupCount;
	private int totalCount = 0;

	public NaiveBayes(DataSet ds) {
		
		classWordCount = new int[Config.labelNum];
		groupCount = new int[Config.labelNum];
		wordCount = new HashMap<Integer, int[]>();
		List<List<DataTuple>> group = ds.group();
		for(int i = 0; i < Config.labelNum; i++){
			groupCount[i] = group.get(i).size();
			totalCount += groupCount[i];
			for(DataTuple  dt: group.get(i)){
				for(Map.Entry<Integer, Integer>  entry : dt.getEntry()){
					classWordCount[i]++;
					int[] count = wordCount.get(entry.getKey());
					if (count == null) { count = new int[Config.labelNum]; }
					count[i] += entry.getValue();
					wordCount.put(entry.getKey(), count);					
				}
			}
		}
	}
	
	public NaiveBayes(String dir)throws IOException{
		classWordCount = new int[Config.labelNum];
		groupCount = new int[Config.labelNum];
		wordCount = new HashMap<Integer, int[]>();
		Scanner scan = new Scanner(new File(dir));
		for(int i = 0; i < Config.labelNum; i++){
			classWordCount[i] = scan.nextInt();
		}
		for(int i = 0; i < Config.labelNum; i++){
			groupCount[i] = scan.nextInt();
		}
		totalCount = scan.nextInt();
		while(scan.hasNext()){
			int key = scan.nextInt();
			int[] v = new int[Config.labelNum];
			for(int i = 0; i < Config.labelNum; i++){
				v[i] = scan.nextInt();
			}
			wordCount.put(key,v);
		}
	}
	
	public void write(String dir) throws IOException {
		PrintStream output = new PrintStream(new File(dir));
		for(int i = 0; i < Config.labelNum; i++){
			output.println(classWordCount[i]);
		}
		for(int i = 0; i < Config.labelNum; i++){
			output.println(groupCount[i]);
		}
		output.println(totalCount);
		for (Entry<Integer, int[]> entry : wordCount.entrySet()) {
			output.println(entry.getKey());
			int[] v = entry.getValue();
			for(int i = 0; i < Config.labelNum; i++){
				output.println(v[i]);
			}
		}
	}

	public double[][] test(DataSet ds){
		int[] base = new int[Config.labelNum];
		int[] count = new int[Config.labelNum];
		int[] hit = new int[Config.labelNum];
		
		for(DataTuple dt : ds.getData()){
			int guess = getLabel(dt);
			int real = dt.getLabel();
			base[real] ++;
			count[guess] ++;
			if(guess == real){
				hit[real]++;
			}

		}
		
		double[][] score = new double[Config.labelNum][2];
		for (int i = 0; i < Config.labelNum; i++) {
			score[i][0] = (double) hit[i] / base[i];
			score[i][1] = (double) hit[i] / count[i];
		}
		return score;
	}
	
	public int getLabel(DataTuple dt) {
		double[] scores = new double[Config.labelNum];
		for(int i = 0; i < Config.labelNum; i++){
			scores[i] = Math.log((double) groupCount[i] / totalCount);
		}
		int wordNum = wordCount.size();
		for(Map.Entry<Integer, Integer>  entry : dt.getEntry()){
			if(!wordCount.containsKey(entry.getKey())){continue;}
			int[] counts = wordCount.get(entry.getKey());
			for(int i = 0; i < Config.labelNum; i++){
				double k = 1;
				double prob = 1.0 * (counts[i] + k) / (classWordCount[i] + k * wordNum);
				scores[i] += Math.log(prob) * entry.getValue();
			}
		}	
		double max = Integer.MIN_VALUE;
		int res = -1;
		for(int i = 0; i < Config.labelNum; i++){
			if (max < scores[i]){
				max = scores[i];
				res = i;
			}
		}
		return res;
	}


}
