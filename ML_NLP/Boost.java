import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;


public class Boost extends Classifier{

	public static int m = 100;
	
	private Classifier[] models;
	private double[] b;

	public Boost (DataSet ds, int base) {
		int n = ds.size();
		models = new Classifier[m];
		double[] w = new double[n];
		b = new double[m];
		for(int i = 0; i < n; i++){ w[i] = 1.0 / n; }
		for(int r = 0; r < m; r++){
			double[] p = new double[n];
			double sum = 0;
			for(int i = 0; i < n; i++){ sum += w[i]; }
			for(int i = 0; i < n; i++){ p[i] = w[i] / sum; }
			DataSet l = ds.sampleByWeight(p);
			models[r] = ClassifierFactory.construct(base, l);
			double e = 0;
			for(int i = 0; i < n; i++){ 
				if(ds.getTuple(i).getLabel() != models[r].getLabel(ds.getTuple(i))) {
					e += p[i];
				}
			}
			if(e == 0){
				m = r ;
				System.out.println("Break!!  " + r + " " + e);
				break;
			}

			b[r] = e / (1.0 - e);
			for(int i = 0; i < n; i++){ 
				if (ds.getTuple(i).getLabel() == models[r].getLabel(ds.getTuple(i))) {
					w[i] *= b[r];
				}
			}			
		}
	}
	
	public Boost(String dir, int base) throws IOException{
		models = new Classifier[m];
		for (int i = 0; i < m; i++) {
			models[i] = ClassifierFactory.construct(base, dir + "/base-" + i);
		}
		b = new double[m];
		Scanner scan = new Scanner(new File(dir + "/info"));
		for (int i = 0; i < m; i++) {
			b[i] = scan.nextDouble();
		}
	}
	
	public void write(String dir) throws IOException {
		for (int i = 0; i < m; i++) {
			models[i].write(dir + "/base-" + i);
		}
		PrintStream output = new PrintStream(new File(dir + "/info"));
		for (int i = 0; i < m; i++) {
			output.println(b[i]);
		}
	}
	
	public int getLabel (DataTuple dt) {
		double[] score = new double[Config.labelNum];
		for(int r = 0; r < m; r++){
			int label = models[r].getLabel(dt);
			double c = Math.log(1.0 / b[r]);
			score[label] += c;
		}
		double max = 0;
		int label = 0;
		for (int i = 0; i < Config.labelNum; i++) {
			if (max < score[i]) {
				max = score[i];
				label = i;
			}
		}
		return label;
	}
}
