import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Map.Entry;
import java.util.Scanner;


public class LogisticRegression extends Classifier{

	public static double epsilon = 0.01; 
	public static double eta = 0.0001; 
	public static double lamda = 5;
	
	private int featureNum;
	private int labelNum;
	private double[][] weight;

	public LogisticRegression(DataSet ds){
		
		featureNum = ds.getFeatureNum();
		this.labelNum = Config.labelNum;
		weight = new double[labelNum - 1][featureNum + 1];
		updateProb(ds);
		double l = getCondLikelihood(ds);
		double oldl;
	//	int i = 0;
		do{
			oldl = l;
			updateWeight(ds);
			updateProb(ds);
			l = getCondLikelihood(ds);
		//	i++;
		//	System.out.println(l - oldl);
		}while(Math.abs(l - oldl) > epsilon);
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
	
	public int getLabel(DataTuple dt){
		double[] probList = getExpList(dt);
		double max = 0;
		int index = -1;
		for(int i = 0; i < labelNum; i++){
			if(max < probList[i]){
				max = probList[i];
				index = i;
			}
		}
		return index;
	}
	
	private void updateWeight(DataSet ds){
		double[][] penalty = penaltyPrepare();
		for(DataTuple dt : ds.getData()){
			double[] probList = dt.getProbList();
			for(int i = 0; i < labelNum - 1; i++){
				double diff = -1 * probList[i];
				diff += (dt.getLabel() == i)? 1 : 0;
				for(Entry<Integer, Integer> e : dt.getEntry()){
					if(e.getKey() > featureNum) {continue;}
					weight[i][e.getKey()] += eta * e.getValue() * diff;
				}
				weight[i][0] += eta * diff;
			}
		}
		penaltyExecute(penalty);
	}
	
	private double[][] penaltyPrepare(){
		if(lamda == 0){return null;}
		double[][] p = new double[labelNum - 1][featureNum + 1];
		for(int i = 0; i < labelNum - 1; i++){
			for(int j = 0; j <= featureNum; j++){
				p[i][j] = eta * lamda * weight[i][j];
			}
		}
		return p;
	}
	
	private void penaltyExecute(double[][] p){
		if(lamda == 0){return ;}
		for(int i = 0; i < labelNum - 1; i++){
			for(int j = 0; j <= featureNum; j++){
				weight[i][j] -= p[i][j];
			}
		}
	}
	
	private void updateProb(DataSet ds){
		for(DataTuple dt : ds.getData()){
			dt.setProb(getCondProbList(dt));
		}
	}
	
	private double[] getExpList(DataTuple dt){
		double[] probList = new double[labelNum];
		for(int i = 0; i < labelNum - 1; i++){
			double p = weight[i][0];
			for(Entry<Integer, Integer> e : dt.getEntry()){
				if(e.getKey() > featureNum) {continue;}
				p += weight[i][e.getKey()] * e.getValue();
			}
			probList[i] = Math.exp(p);
		}
		probList[labelNum - 1] = 1;
		return probList;
	}
	
	private double[] getCondProbList(DataTuple dt){
		double[] probList = getExpList(dt);
		double sum = 0;
		for(double d : probList){
			sum += d;
		}
		double[] res = new double[labelNum];
		for(int i = 0; i < labelNum; i++){
			res[i] = probList[i] / sum;
		}
		return res;
	}
	
	private double getCondLikelihood(DataSet ds){
		double sum = 0;
		for(DataTuple dt : ds.getData()){
			sum += Math.log(dt.getLabelProb());
		}
		return sum - getRegularization();
	}
	
	private double getRegularization(){
		return lamda / 2 * getSqMag();
	}
	
	private double getSqMag(){
		double sum = 0;
		for(double[] darray : weight){
			for(double d : darray){
				sum += d * d;
			}
		}
		return sum;
	}
	
	public void write(String dir) throws IOException{
		PrintStream output = new PrintStream(new File(dir));
		output.println(featureNum);
		output.println(labelNum);
		for(int i = 0; i < labelNum - 1; i++){
			for(int j = 0; j <= featureNum; j++){
				output.println(weight[i][j]);
			}
		}
	}
	
	public LogisticRegression(String dir) throws IOException {
		Scanner scan = new Scanner(new File(dir));
		featureNum = scan.nextInt();
		labelNum = scan.nextInt();
		weight = new double[labelNum - 1][featureNum + 1];
		for(int i = 0; i < labelNum - 1; i++){
			for(int j = 0; j <= featureNum; j++){
				weight[i][j] = scan.nextDouble();
			}
		}		
	}
}
