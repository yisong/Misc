import java.io.IOException;


public abstract class Classifier {

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
	
	public abstract int getLabel(DataTuple dt);
	
	public abstract void write(String dir) throws IOException;
}
