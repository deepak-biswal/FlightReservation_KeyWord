package utility;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ReportGenerator {
	
	public void generateReport(String content) throws IOException{
		
		FileWriter writer = new FileWriter(Constant.textReportPath,true);
		PrintWriter write = new PrintWriter(writer);
		write.println(content);
		write.close();
		
	}

}
