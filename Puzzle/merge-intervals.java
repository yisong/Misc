/*

Given a collection of intervals, merge all overlapping intervals.

For example,
Given [1,3],[2,6],[8,10],[15,18],
return [1,6],[8,10],[15,18].

*/

/**
 * Definition for an interval.
 * public class Interval {
 *     int start;
 *     int end;
 *     Interval() { start = 0; end = 0; }
 *     Interval(int s, int e) { start = s; end = e; }
 * }
 */
public class Solution {
    public List<Interval> merge(List<Interval> intervals) {
      Collections.sort(intervals, new Comparator<Interval>() {
               @Override
                public int compare(Interval i1, Interval i2) {
                    return i1.start - i2.start;
                }
            }
        );
        
        List<Interval> result = new ArrayList<Interval>();
        Interval tmp = null;
        for (Interval i : intervals) {
            if (tmp == null) {
                tmp = i;
            } else if (i.start <= tmp.end) {
                tmp = new Interval(tmp.start, Math.max(tmp.end, i.end));
            } else {
                result.add(tmp);
                tmp = i;
            }
        }
        
        if (tmp != null) {
            result.add(tmp);
        }
       
        return result;
    }
    
    
}
