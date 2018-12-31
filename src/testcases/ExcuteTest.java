package testcases;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import excelInputAndOutput.ExcelInteraction;
import operation.ReadObject;
import operation.UIOperation;
import utility.Constant;
import utility.ReportGenerator;

public class ExcuteTest {
	WebDriver driver;
	UIOperation operation;
	ReadObject object;
	ExcelInteraction excel;
	ReportGenerator report;
	String status;
	Properties allObjects;
	@Parameters({"browser"})
	@BeforeTest
	public void setUp(String browser) throws IOException{
		if(browser.equals("chrome")){
			System.setProperty("webdriver.chrome.driver",Constant.chromeDriverPath);
			driver = new ChromeDriver();
		}else if(browser.equals("firefox")){
			System.setProperty("webdriver.gecko.driver", Constant.geckoDriverPath);
			driver = new FirefoxDriver();
		}
		
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		
		operation = new UIOperation(driver);
		object = new ReadObject();
		allObjects = object.getObjectRepository();
		report = new ReportGenerator();
		excel = new ExcelInteraction();
		
		File txtfile = new File(Constant.textReportPath);
		if(txtfile.exists()){
			txtfile.delete();
		}
		
		
		report.generateReport("Project Name : Flight Reservation(KeyWord)");
		report.generateReport("Browser : "+browser);
		report.generateReport("Java Version : "+System.getProperty("java.version"));
		report.generateReport("OS : "+System.getProperty("os.name"));
		report.generateReport("User : "+System.getProperty("user.name"));
		InetAddress myHost = InetAddress.getLocalHost();
		report.generateReport("Host Name : "+myHost.getHostName());
		report.generateReport("**************************************************");
		
	}
	
	
	
	@Test
	public void executeTest() throws IOException{
		
		Sheet sheet = excel.getTestDataSheet(Constant.filePath, Constant.fileName);
		int rowCount = sheet.getLastRowNum()-sheet.getFirstRowNum();

		for(int i=1;i<=rowCount;i++){
			Row row = sheet.getRow(i);
			Cell cell = row.getCell(0);
			if(cell.getStringCellValue().length() == 0){
				status = operation.perform(allObjects, row.getCell(1).getStringCellValue(), row.getCell(2).getStringCellValue(), row.getCell(3).getStringCellValue(), row.getCell(4).getStringCellValue());
				report.generateReport(row.getCell(1).getStringCellValue()+"---"+row.getCell(2).getStringCellValue()+"---"+row.getCell(3).getStringCellValue()+"---"+row.getCell(4).getStringCellValue()+"---"+status);
				Cell cell1 = row.createCell(5);
				cell1.setCellValue(status);
				CellStyle style = excel.getCellStyle(status);
				cell1.setCellStyle(style);
				
			}else{
				report.generateReport("===================================================================");
				report.generateReport("New TestCase : "+row.getCell(0).getStringCellValue());
				report.generateReport("===================================================================");
			}
			
		}
		excel.closeInputStream();
		excel.generateReport(Constant.excelReportPath);
		
	}
	
	@AfterTest
	public void closeDriver(){
		driver.quit();
	}

}
