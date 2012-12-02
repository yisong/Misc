import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;


public class DecisionTree extends Classifier{

	public static double threadhold = 0.9;
	public static double prun = -1;
	public static int featureNum = 100;
	public static int height = 100;
	
	private class DecisionTreeNode {
		
		private int label;
		private int leaf;;
		private DecisionTreeNode left; 
		private DecisionTreeNode right;
		
		public DecisionTreeNode (DataSet ds, Set<Integer> attri, int height){
			
			List<List<DataTuple>> dtList = ds.group();
			int[] count = new int[Config.labelNum];
			for(int i = 0; i < Config.labelNum; i++){
				count[i] = dtList.get(i).size();
			}
			label = max(count);
			if ((double) count[label] / ds.size() > threadhold || height == 0 || attri.isEmpty()||ds.isAllSame()) {
				leaf = -1;
				return;
			}
			
			double max = -1.0 * Double.MAX_VALUE;
			
			for(int i : attri){
				int[] zero = new int[Config.labelNum];
				int[] positive = new int[Config.labelNum];
				for(DataTuple dt : ds.getData()){
					if (dt.contains(i)){
						positive[dt.getLabel()] += 1;
					} else {
						zero[dt.getLabel()] += 1;
					}
				}
				
				double value = entropy(zero) + entropy(positive);
				
				if (value > max) {
					max = value;
					leaf = i;
				}
			}
			attri.remove(leaf);
			DataSet[] dss = ds.splitByAttri(leaf);
			left = new DecisionTreeNode(dss[0], attri, height - 1);
			right = new DecisionTreeNode(dss[1], attri, height - 1);
			attri.add(leaf);
		}
		
		public DecisionTreeNode (Scanner scan){
			label = scan.nextInt();
			leaf = scan.nextInt();
			String annotation = scan.next();
			if (annotation.equals("$")) {
				left = null;
				right = null;
			} else {
				left = new DecisionTreeNode(scan);
				right = new DecisionTreeNode(scan);
			}
			
		}
		
		public int getLabel(DataTuple dt){
			if (leaf < 0) {
				return label;
			} else if (dt.contains(leaf)){
				return right.getLabel(dt);
			} else {
				return left.getLabel(dt);
			}
		}
		
		public void write(PrintStream ps) {
			ps.println(label);
			ps.println(leaf);
			if (left == null) {
				ps.println("$");
			} else {
				ps.println("@");
				left.write(ps);
				right.write(ps);

			}
		}
	}
	
	private DecisionTreeNode root;
	
	public DecisionTree(DataSet ds){
		Set<Integer> attriSet = ds.selectFeature(featureNum);
		if (prun < 0) {
			root = new DecisionTreeNode(ds, attriSet, height);
		} else {
			DataSet[] dss = ds.split(prun);
			root = new DecisionTreeNode(dss[0], attriSet, height);
			prun(dss[1]);
		}
	}
	
	public DecisionTree(String s) throws IOException{
		Scanner scan = new Scanner(new File(s));
		root = new DecisionTreeNode(scan);
	}
	
	public void prun(DataSet ds){
		while(prunSingleNode(ds));
	}
	
	public void write(String dir) throws IOException {
		PrintStream output = new PrintStream(new File(dir));
		root.write(output);
	}
	
	
	private boolean prunSingleNode (DataSet ds){
		double max = accuracy(ds);
		DecisionTreeNode res = null;

		for(DecisionTreeNode n : getAllNodes()){
			if(n.leaf < 0){continue;}
			int tmp = n.leaf;
			n.leaf = -1;
			double value = accuracy(ds);
			n.leaf = tmp;
			if(value >= max){
				max = value;
				res = n;
			}
		}

		if(res == null){ 
			return false;
		} else {
			res.leaf = -1;
			res.left = null;
			res.right = null;
			return true;
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
	
	private List<DecisionTreeNode> getAllNodes(){
		List<DecisionTreeNode> nodeList = new ArrayList<DecisionTreeNode>();
		nodeList.add(root);
		for(int i = 0; i < nodeList.size(); i++){
			DecisionTreeNode n = nodeList.get(i);
			if(n.leaf > 0){
				nodeList.add(n.left);
				nodeList.add(n.right);
			}
		}
		return nodeList;
	}
	
	public int getLabel(DataTuple dt){
		return root.getLabel(dt);
	}
	
	private double accuracy(DataSet ds){
		int hitCount = 0;
		for(DataTuple tuple : ds.getData()){
			int t = getLabel(tuple);
			if (tuple.getLabel() == t){
				hitCount ++;
			}
		}
		return 1.0 * hitCount / ds.size();
	}
	
	public static double entropy(int[] array) {
		int total = sum(array);
		if (total == 0) {
			return -0.1 * Double.MAX_VALUE;
		}
		double res = 0;
		for(int i = 0; i < array.length; i++) {
			double ratio = (array[i] == 0)? Double.MIN_NORMAL : (double) array[i] / total;
			res += ratio* Math.log(ratio);
		}
		return res * total;
	}
	
	public static int max (int[] array) {
		int index = -1;
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < array.length; i++) {
			if (max < array[i]) {
				max = array[i];
				index = i;
			}
		}
		return index;
	}
	
	public static int sum (int[] array) {
		int sum = 0;
		for (int i = 0; i < array.length; i++) {
			sum += array[i];
		}
		return sum;
	}
}
