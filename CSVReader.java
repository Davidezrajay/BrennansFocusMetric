package greenStand;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class CSVReader {
 
 
    BufferedReader reader = null;
    String path;
	public CSVReader(String path) {
		
		this.path = path;
		
		
	};
	
    public String[] readLine() {
    	try {
    	if (this.reader == null) {
    		this.reader = new BufferedReader(new FileReader(path));
    	}
    	
    	String line = reader.readLine();
    	if (line != null) {
    		return line.split(",");
    	}
    	return null;
    	}
    	catch (IOException ex)
    	{
    		return null;
    	}
    }

}