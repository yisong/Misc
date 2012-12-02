import java.util.Map;
import java.util.Set;


public class DataTuple {

	private int label;
	private Map<Integer, Integer> attriMap;
	private double[] probs;
	
	public DataTuple(int l, Map<Integer, Integer> map){
		label = l;
		attriMap = map;
	}
	
	public boolean contains(int key){
		return attriMap.containsKey(key);
	}
	
	public Set<Map.Entry<Integer, Integer>> getEntry(){
		return attriMap.entrySet();
	}
	
	public void print(){
		System.out.print(label + ": ");
		for(Map.Entry<Integer, Integer> entry: attriMap.entrySet()){
			System.out.print(" " + entry.toString());
		}
		System.out.println();
	}
	
	public void setProb(double[] d){
		probs = d;
	}

	public double getLabelProb(){
		return probs[label];
	}
	
	public double[] getProbList(){
		return probs;
	}
	
	public int getLabel(){
		return label;
	}
	
	public boolean isSameAs(DataTuple dt){
		return attriMap.equals(dt.attriMap);
	}
}
