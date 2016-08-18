public class Solution {
    /**
     * @param numbers : Give an array numbers of n integer
     * @return : Find all unique triplets in the array which gives the sum of zero.
     */
    public ArrayList<ArrayList<Integer>> threeSum(int[] numbers) {
        // write your code here
        Arrays.sort(numbers);
        
        ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
        Map<Integer, Integer> m = new HashMap<Integer, Integer>();
        for (int i = 0; i < numbers.length; i++) {
            m.put(numbers[i], i);
        }
        
        for (int i = 0; i < numbers.length; i++) {
            if (i > 0 && numbers[i] == numbers[i - 1]) {
                continue;
            }
            for (int j = i + 1; j < numbers.length; j++) {
                if (j > i + 1 && numbers[j] == numbers[j - 1]) {
                    continue;
                }
                Integer k = m.get(-1*(numbers[i] + numbers[j]));
                if (k != null && k > j) {
                    ArrayList<Integer> aL = new ArrayList<Integer>();
                    aL.add(numbers[i]);
                    aL.add(numbers[j]);
                    aL.add(numbers[k]);
                    result.add(aL);
                }
            }
        }
        return result;

    }
}
