import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;


public class JsonParser2 {

	public static void main(String[] args) throws IOException, JSONException{
		for (String s  : Config.hashtags){
			File f = new File("data/json/" + s + ".data");
			List<String> lines = new ArrayList<String>();
			Scanner scan = new Scanner(f);
			while(scan.hasNextLine()){
				String line = scan.nextLine();
				if (line.isEmpty()){
					continue;
				}
				JSONObject json = new JSONObject(line);
				lines.add(JsonParser.parseString(json.get("text").toString(), s));
			}
			PrintStream train = new PrintStream(new File("data/train/" + s));
			PrintStream test = new PrintStream(new File("data/test/" + s));
			
			int a = (int) Math.round(lines.size() * Config.p);
			int n = lines.size();
			
			for (String line : lines) {
				if (Math.random() < (double) a / n){
					train.println(line);
					a --;
				} else {
					test.println(line);
				}
				n--;
			}
		}
	}
}
