import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JsonParser {

	public static void main(String[] args) throws IOException, JSONException{
		for (String s  : Config.hashtags){
			File dataDir = new File("data/json/" + s + "/");
			PrintStream output = new PrintStream(new File("data/text/" + s));
			for(File f : dataDir.listFiles()) {
				Scanner scan = new Scanner(f);
				JSONObject json = new JSONObject(scan.nextLine());
				JSONArray result = json.getJSONArray("results");
				for (int j = 0; j < result.length(); j++) {
					output.println(parseString(result.getJSONObject(j).get("text").toString(), s));
				}
			}
		}
	}
	
	public static String parseString(String line, String keyword){
		line = line.replaceAll("\n", " ");
		StringBuilder sb = new StringBuilder();
		Scanner scan = new Scanner(line);
		while (scan.hasNext()){
			String token = scan.next();
			parseToken(token.toLowerCase(), sb, keyword);
		};
		return sb.toString();
	}
	
	private static void parseToken(String token, StringBuilder sb, String keyword){
		if (token.length() == 0) { return; }
		
		if (token.endsWith("...")){
			parseToken(token.substring(0, token.length() - 3), sb, keyword);
			sb.append("... ");
		} else if (token.endsWith(",")|token.endsWith(".")|token.endsWith("?")
					|token.endsWith(":")|token.endsWith(";")|token.endsWith("!")
					|token.endsWith("'")|token.endsWith("\"")){
			String c = token.substring(token.length()-1);
			parseToken(token.substring(0, token.length() - 1), sb, keyword);
			sb.append(c + " ");
		} else if (token.startsWith("http:")){
			sb.append("http://url ");
		}else if (token.startsWith("@")) {
			sb.append("@ ");
			parseToken(token.substring(1), sb, keyword);
		} else {
			if (token.charAt(0) == '#'){
				if (token.substring(1).equals(keyword)){
					sb.append("<#hashtag> ");
					return;
				} 
			}
			
			sb.append(token + " ");
		}
	}
}
