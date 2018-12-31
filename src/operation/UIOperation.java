package operation;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class UIOperation {
	WebDriver driver;
	String status;
	public UIOperation(WebDriver driver){
		this.driver = driver;
	}
	
	private Logger logger = Logger.getLogger("flightReservation");
	public String perform(Properties p, String operation, String objectType, String objectName, String value){
		status = "Pass";
		try{
		switch(operation.toUpperCase()){
		
		case "GOTOURL":
			logger.info("Invoking Application Under Test : "+value);
			driver.get(value);
			driver.manage().window().maximize();
			logger.info("Invoked Application Under Test : "+value);
			break;
		case "CLICK":
			logger.info("Clicking on '"+objectName+"'");
			WebElement elemntToBeClicked = driver.findElement(this.getObject(p,objectType,objectName));
			highlightElement(elemntToBeClicked);
			elemntToBeClicked.click();
			logger.info("Clicked on '"+objectName+"'");
			break;
		case "SET":
			logger.info("Entering '"+value+"' in the '"+objectName+"' edit field");
			WebElement elemntToBeSet = driver.findElement(this.getObject(p,objectType,objectName));
			highlightElement(elemntToBeSet);
			elemntToBeSet.sendKeys(value);
			logger.info("Entered '"+value+"' in the '"+objectName+"' edit field");
			break;
		case "SELECT":
			logger.info("Selecting '"+value+"' from the '"+objectName+"' drop down");
			WebElement elemntToBeSelected = driver.findElement(this.getObject(p,objectType,objectName));
			highlightElement(elemntToBeSelected);
			Select select = new Select(elemntToBeSelected);
			select.selectByVisibleText(value);
			logger.info("Selected '"+value+"' from the '"+objectName+"' drop down");
			break;
			
		case "VERIFYTITLE":
			
			logger.info("Verifying Page Title : "+value);
			if(!driver.getTitle().equals(value)){
				status = "Fail";
				logger.error("Status -- Failed | #Expected Title : "+value+" | #Actual Title : "+driver.getTitle());
			}
			logger.info("Verified Page Title : "+value);
			break;
			
		case "VERIFYELEMENTPRESENT":
			
			logger.info("Verifying the presence of '"+objectName+"' on web page");
			
			if(!driver.findElement(this.getObject(p, objectType, objectName)).isDisplayed()){
				status = "Fail";
				logger.info("Status -- Failed | '"+objectName+"' is not displayed on web page");
			}else{
				highlightElement(driver.findElement(this.getObject(p, objectType, objectName)));
			}
			logger.info("Verified the presence of '"+objectName+"' on web page");
			break;
		case "VERIFYTEXT":
			logger.info("Verifying the presence of '"+value+"' on web page");
			if(!driver.getPageSource().contains(value)){
				status = "Fail";
				logger.info("Status -- Failed | '"+value+"' is not displayed on web page");
			}
			logger.info("Verified the presence of '"+value+"' on web page");
			break;
			
		}
		
			
		}catch(Exception ex){
			status = "Fail";
			logger.error("Exception occurred : "+ex.getMessage());
		}
		
		return status;
		
		}
		
		
	

	private By getObject(Properties p, String objectType, String objectName) {
		if(objectType.equals("ID")){
			return By.id(p.getProperty(objectName));
		}else if(objectType.equals("NAME")){
			return By.name(p.getProperty(objectName));
		}else if(objectType.equals("XPATH")){
			return By.xpath(p.getProperty(objectName));
		}else if(objectType.equals("CSS")){
			return By.cssSelector(p.getProperty(objectName));
			
		}else if(objectType.equals("LINKTEXT")){
			return By.linkText(p.getProperty(objectName));
		}else if(objectType.equals("TAGNAME")){
			return By.tagName(p.getProperty(objectName));
		}else if(objectType.equals("CLASSNAME")){
			return By.className(p.getProperty(objectName));
		}else{
			return By.partialLinkText(objectName);
		}
	}
	
	
	private void highlightElement(WebElement element){
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].setAttribute('Style','background: yellow; border: 2px solid red;');", element);
	}

}
