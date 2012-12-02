import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;


public class DataSet {
	
	private List<DataTuple> dtList;
	private int featureNum;
	
	public DataSet(){
		dtList = new ArrayList<DataTuple>();
	}
	
	public DataSet(int n){
		dtList = new ArrayList<DataTuple>(n);
	}
	
	public void addTuple(DataTuple dt){
		dtList.add(dt);
	}

	public List<DataTuple> getData(){
		return dtList;
	}
	
	public List<List<DataTuple>> group(){
		List<List<DataTuple>> groupList = new ArrayList<List<DataTuple>>(Config.labelNum);
		for (int i = 0; i < Config.labelNum; i++){
			groupList.add(new ArrayList<DataTuple>());
		}
		for(DataTuple dt : dtList) {
			groupList.get(dt.getLabel()).add(dt);
		}
		return groupList;
	}
	
	public void reportGroupInfo(){
		List<List<DataTuple>> groupList = group();
		for (int i = 0; i < groupList.size(); i++) {
			System.out.print(Config.hashtags[i] + ": \t" + groupList.get(i).size() + "\t");
		}
		System.out.println();
	}

	public DataSet[] splitByAttri(int attri){
		DataSet zero = new DataSet();
		DataSet positive = new DataSet();
		for(DataTuple dt : getData()){
			if (dt.contains(attri)){
				positive.addTuple(dt);
			} else {
				zero.addTuple(dt);
			}
		}
		return new DataSet[] {zero, positive};
	}
	
	public DataSet[] split (double p) {
		int a = (int) Math.round(size() * p);
		int b = size() - a;
		int n = size();
		
		DataSet dsA = new DataSet(a + Config.labelNum);
		DataSet dsB = new DataSet(b + + Config.labelNum);

		List<List<DataTuple>>groupList = group();
		for (List<DataTuple> l : groupList) {
			n = l.size();
			a = (int) Math.round(n * p);
			b = n - a;
			
			for (DataTuple dt : l) {
				if (Math.random() < (double) a / n){
					dsA.addTuple(dt);
					a --;
				} else {
					dsB.addTuple(dt);
				}
			
				n--;
			}
		}
		
		return new DataSet[]{dsA, dsB};
	}
	
	public void print(){
		for(DataTuple dt: dtList){
			dt.print();
		}
	}
	
	public int size(){
		return dtList.size();
	}
	
	public DataTuple getTuple (int i) {
		return dtList.get(i);
	}
	
	public DataSet sampleByUniform () {
		int n = this.size();
		Random r = new Random();
		DataSet resampled = new DataSet(n);
		for (int i = 0; i < n; i++) {
			resampled.addTuple(this.getTuple(r.nextInt(n)));
		}
		return resampled;
	}
	
	public DataSet sampleByWeight (double[] p) {
		int n = this.size();
		double[] c = new double[n];
		c[0] = p[0];
		DataSet resampled = new DataSet(n);
		for(int i = 1; i < n; i++){
			c[i] = c[i - 1] + p[i];
		}
		for(int i = 0; i < n; i++){
			double r = Math.random();
			for(int j = 0; j < n; j++){
				if(r < c[j]){
					resampled.addTuple(this.getTuple(j));
					break;
				}
			}
		}
		return resampled;
	}
	
	public Set<Integer> selectFeature(int size){
		class PQEntry implements Comparable<PQEntry>{
			Integer s;
			int n;

			private PQEntry(Integer i, int num){
				this.s = i;
				n = num;
			}

			public int compareTo(PQEntry o){
				return o.n - n;
			}
		}
		
		Map<Integer, Integer> featureCount = new HashMap<Integer, Integer>();
		
		for(DataTuple dt : dtList){
			for (Map.Entry<Integer, Integer> entry : dt.getEntry()){
				Integer i = featureCount.get(entry.getKey());
				if (i == null) {
					featureCount.put(entry.getKey(), entry.getValue());
				} else {
					featureCount.put(entry.getKey(), entry.getValue() + i);
				}
			}
		}
		PriorityQueue<PQEntry> pq = new PriorityQueue<PQEntry>(featureCount.size());	
		for (Map.Entry<Integer, Integer> entry : featureCount.entrySet()) {
			pq.add(new PQEntry(entry.getKey(), entry.getValue()));
		}

		Set<Integer> features = new HashSet<Integer>();
		for (int i = 0; i < size; i++){
			features.add(pq.poll().s);
		}
		
		return features;
	}
	
	public boolean isAllSame(){
		boolean start = true;
		DataTuple sample = null;
		for(DataTuple dt : dtList){
			if (start) {
				sample = dt;
				start = false;
			} else if(!dt.isSameAs(sample)){
				return false;
			}
		}
		return true;
	}
	
	public void setFeatureNum(int f){
		featureNum = f;
	}
	
	public int getFeatureNum(){
		return featureNum;
	}
}
