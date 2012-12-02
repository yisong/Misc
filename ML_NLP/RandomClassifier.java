import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;


public class RandomClassifier extends Classifier{

	double[] groupCount;
	
	public RandomClassifier(DataSet ds){
		groupCount = new double[Config.labelNum];
		List<List<DataTuple>> group = ds.group();
		for(int i = 0; i < Config.labelNum; i++){
			groupCount[i] = (double)group.get(i).size() / ds.size();
		}
	}
	
	public RandomClassifier(String dir) throws IOException {
		groupCount = new double[Config.labelNum];
		Scanner scan = new Scanner(new File(dir));
		for(int i = 0; i < Config.labelNum; i++){
			groupCount[i] = scan.nextDouble();
		}
	}
	
	public void write(String dir) throws IOException{
		PrintStream output = new PrintStream(new File(dir));
		for(int i = 0; i < Config.labelNum; i++){
			output.println(groupCount[i]);
		}
	}
	
	public int getLabel(DataTuple dt){
		double r = Math.random();
		double sum = 0;
		for (int i = 0; i < Config.labelNum; i++) {
			sum += groupCount[i];
			if (r < sum) { return i;}
		}
		return 0;
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
}
