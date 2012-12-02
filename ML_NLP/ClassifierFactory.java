import java.io.IOException;


public class ClassifierFactory {

	public static String type(int i){
		if (i == 0) {
			return "Random classifier";
		} else if (i == 1) {
			return "Naive Bayes";
		} else if (i == 2) {
			return "Logistic Regression";
		} else if (i == 3) {
			return "Decision Tree";
		} else if (i == 4) {
			return "Bagging with Naive Bayes";
		} else if (i == 5) {
			return "Bagging with Decision Tree";
		} else if (i == 6) {
			return "Boost with Naive Bayes";
		} else  {
			return "Boost with Decision Tree";
		}
	}
	
	public static Classifier construct(int i, String s) throws IOException{
		if (i == 0) {
			return new RandomClassifier(s);
		} else if (i == 1) {
			return new NaiveBayes(s);
		} else if (i == 2) {
			return new LogisticRegression(s);
		} else if (i == 3) {
			return new DecisionTree(s);
		} else if (i == 4) {
			return new Bagging(s, 1);
		} else if (i == 5) {
			DecisionTree.prun = -1;
			DecisionTree.height = 2;
			return new Bagging(s, 3);
		} else if (i == 6) {
			return new Boost(s, 1);
		} else  {
			DecisionTree.prun = -1;
			DecisionTree.height = 2;
			return new Boost(s, 3);
		}
	}
	
	public static Classifier construct(int i, DataSet ds){
		if (i == 0) {
			return new RandomClassifier(ds);
		} else if (i == 1) {
			return new NaiveBayes(ds);
		} else if (i == 2) {
			return new LogisticRegression(ds);
		} else if (i == 3) {
			return new DecisionTree(ds);
		} else if (i == 4) {
			return new Bagging(ds, 1);
		} else if (i == 5) {
			DecisionTree.prun = -1;
			DecisionTree.height = 2;
			return new Bagging(ds, 3);
		} else if (i == 6) {
			return new Boost(ds, 1);
		} else  {
			DecisionTree.prun = -1;
			DecisionTree.height = 2;
			return new Boost(ds, 3);
		}
	}
}
