package main;

public class StringAlignment {
	
	public static int insert = 1;
	public static int delete = 1;
	public static int substitute(char a, char b) {
		if (a == b) {
			return -2;
		} else {
			return 1;
		}
	}
	
	public static int alignment (String a, String b) {
		int n = a.length();
		int m = b.length();
		int[][] result = new int[n + 1][m + 1];
		for (int j = 0; j <= m; j ++) {
			result[0][j] = j * insert;
		}
		
		for (int i = 1; i <= n; i ++) {
			result[i][0] = i * delete;
			for (int j = 1; j <= m; j ++) {
				result[i][j] = Math.min(result[i - 1][j - 1] + substitute(a.charAt(i - 1), b.charAt(j - 1)),
										Math.min(result[i][j - 1] + insert, result[i - 1][j] + delete));
			}
		}
		
		return result[n][m];
	}
}
