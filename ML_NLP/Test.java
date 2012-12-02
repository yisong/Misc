import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


public class Test {

	
	public static void main(String[] args) throws IOException {
		Config.gramlength = Integer.valueOf(args[0]);
		Train.dict = new HashMap<List<String>, Integer>();
		readDict(args[0]);
		System.out.println("Reading testing data\n");
		DataSet test = Train.setupProblem("data/test/", false);
		for (int j = 0; j < 8; j++) {
			System.out.println(ClassifierFactory.type(j));
			Classifier c = ClassifierFactory.construct(j, "model-" + args[0] +"/" + ClassifierFactory.type(j));
			double[][] score = c.test(test);
			double finalscore = 0;
			for (int i = 0; i < Config.labelNum; i++) {
			    double f = (2 * score[i][0] * score[i][1]) / (score[i][0] + score[i][1]);   
			    finalscore += f;
			    System.out.printf("%s :\t%.2f\t%.2f\t%.2f\n", Config.hashtags[i], score[i][0], score[i][1], f);
			}
			System.out.printf("final score: %.2f\n", finalscore / Config.labelNum);
			System.out.println();
		}
	}
	
	private static void readDict(String n) throws IOException{
		Scanner scan = new Scanner(new File("model-" + n +"/dict"));
		while(scan.hasNext()){
			String line = scan.nextLine();
			Scanner lineScanner = new Scanner(line);
			List<String> list = new ArrayList<String>();
			while(lineScanner.hasNext()){
				list.add(lineScanner.next());
			}
			int value = Integer.valueOf(scan.nextLine());
			Train.dict.put(list, value);
		}
	}
}
