import java.io.IOException;


public class Bagging extends Classifier{

	public static int m = 100;
	
	private Classifier[] models;
	
	public Bagging (DataSet ds, int base) {
		
		models = new Classifier[m];
		for (int i = 0; i < m; i++) {
			models[i] = ClassifierFactory.construct(base, ds.sampleByUniform());
		}
		
	}
	
	public void write(String dir) throws IOException {
		for (int i = 0; i < m; i++) {
			models[i].write(dir + "/base-" + i);
		}
	}
	
	public Bagging(String dir, int base) throws IOException{
		models = new Classifier[m];
		for (int i = 0; i < m; i++) {
			models[i] = ClassifierFactory.construct(base, dir + "/base-" + i);
		}
	}
	
	public int getLabel (DataTuple dt) {
		int[] score = new int[Config.labelNum];
		for (Classifier lr : models) {
			score[lr.getLabel(dt)] += 1;
		}
		
		int max = 0;
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
