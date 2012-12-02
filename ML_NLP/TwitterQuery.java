import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class TwitterQuery {

	public static int page = 13;
	
	public static void main (String[] args) throws IOException{
		HttpURLConnection connection = null;
		BufferedReader rd  = null;
		URL serverAddress = null;
		PrintStream output = null;
		
		for (String s : Config.hashtags) {
			for (int i = 1; i <= page; i++) {
				serverAddress = new URL("http://search.twitter.com/search.json?q=%23" + s +"&rpp=100&lang=en&page=" + i);
				output = new PrintStream(new File("data/json/"+ s + "/" + i));
				connection = null;
	        
				connection = (HttpURLConnection)serverAddress.openConnection();
				connection.setRequestMethod("GET");
				connection.setDoOutput(true);
				connection.setReadTimeout(10000);
		                    
				connection.connect();
		        
				rd  = new BufferedReader(new InputStreamReader(connection.getInputStream()));

				output.println(rd.readLine());
	                    
				connection.disconnect();
				rd = null;
				connection = null;
				output = null;
				System.out.println(s + "-" + i + " complete");
	      }
	  }
	}
}

