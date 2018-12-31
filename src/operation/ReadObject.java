package operation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ReadObject {
	
 Properties p = new Properties();
	
	public Properties getObjectRepository() throws IOException{
		File file = new File(System.getProperty("user.dir")+"\\src\\Objects.properties");
		FileInputStream inputstream = new FileInputStream(file);
		p.load(inputstream);
		return p;
	}
	
}


