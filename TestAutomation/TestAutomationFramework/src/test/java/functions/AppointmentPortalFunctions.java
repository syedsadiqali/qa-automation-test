package functions;

import objects.Elements;
import utilities.ExcelUtilities;
import utilities.ReportUtilities;

import java.io.File;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.aventstack.extentreports.Status;

public class AppointmentPortalFunctions {

	//Variable declaration
	
	WebDriver driver;
	String dataFilePath = new File("TestData" + File.separator + "TerminTestData.xlsx").getAbsolutePath();
	String sheetName = "TerminDetails";
	
	String actualAufgabe;
	String actualTag;
	String actualStartzeit;
	String actualEndzeit;
	String actualTechnikername;
	String actualTechnikeralter;
	String actualTechnikergeschlecht;
	
	String expectedAufgabe; 
	String expectedTag; 
	String expectedStartzeit; 
	String expectedEndzeit; 
	String expectedTechnikername; 
	String expectedTechnikeralter; 
	String expectedTechnikergeschlecht;
	
	public AppointmentPortalFunctions(WebDriver driver) {
		this.driver = driver;
	}
	
	public void VerifyAppointmentData() throws Exception {
		try {
			//Finding actual data visible on screen
			FindActualData();		
			
			//Fetching expected data from data file
			ExcelUtilities excelUtilities = new ExcelUtilities(dataFilePath, sheetName);
			expectedAufgabe = excelUtilities.getCellData(1, 1); 
			expectedTag = excelUtilities.getCellData(1, 2); 
			expectedStartzeit = excelUtilities.getCellData(1, 3); 
			expectedEndzeit = excelUtilities.getCellData(1, 4); 
			expectedTechnikername = excelUtilities.getCellData(1, 5); 
			expectedTechnikeralter = excelUtilities.getCellData(1, 6); 
			expectedTechnikergeschlecht= excelUtilities.getCellData(1, 7); 
			excelUtilities.closeWorkbook();
			
			//Comparing actual and expected data
			AssertData();
		}
		catch(Exception e) {
			ReportUtilities.extentTest.log(Status.FAIL, e.getMessage());
			throw e;
		}		
	}
	
	public void VerifyRescheduledAppointmentData() throws Exception {
		try {
			ExcelUtilities excelUtilities = new ExcelUtilities(dataFilePath, sheetName);
			
			ReportUtilities.extentTest.log(Status.INFO, "Clicking on Reschedule Appointment button.");
			driver.findElement(Elements.reschduleButton).click();
			
			//Checking if 3 options are visible for rescheduling appointment
			List<WebElement> rescheduleOPtions = driver.findElements(Elements.rescheduleOptions);
			
			JavascriptExecutor js = (JavascriptExecutor) driver;
	        js.executeScript("window.scrollBy(0, 1000)");
	        
			if(rescheduleOPtions.size() > 3 || rescheduleOPtions.size() < 3) {
				throw new Exception("Number of options for rescheduling appointment are incorrect.");
			}
			else {
				ReportUtilities.extentTest.log(Status.INFO, "User can see exactly 3 options to select another appointment.");
				ReportUtilities.extentTest.addScreenCaptureFromPath(ReportUtilities.CaptureScreenshot(driver));
			}
			
			//Rescheduling appointment and fetching the expected data to be verified further
			String buttonText = driver.findElement(Elements.newAppointment).getText();
			driver.findElement(Elements.newAppointment).click();
			
	        String[] buttonTextArray = buttonText.split(" ");
	        expectedAufgabe = excelUtilities.getCellData(1, 1); 
	        excelUtilities.closeWorkbook();
			expectedTag = buttonTextArray[0]; 
			expectedStartzeit = buttonTextArray[1]; 
			expectedEndzeit = buttonTextArray[3].split("\n")[0]; 
			expectedTechnikername = buttonTextArray[4] + " " + buttonTextArray[5]; 
			expectedTechnikeralter = buttonTextArray[6].replace("(", ""); 
			expectedTechnikergeschlecht= buttonTextArray[9].replace(")", "");
	       
			//Finding actual data on dashboard after rescheduling appointment
			FindActualData();
			
			//Comparing actual and expected data
			AssertData();
			
			System.out.println(buttonTextArray);
			
			//Deliberately selecting the intial appointment again to avoid failure of any further dependent testcases
			Thread.sleep(1000);
			driver.findElement(Elements.reschduleButton).click();
			Thread.sleep(1000);
			driver.findElement(Elements.initialAppointment).click();
		}
		catch(Exception e) {
			ReportUtilities.extentTest.log(Status.FAIL, e.getMessage());
			throw e;
		}		
	}
	
	private void FindActualData() {
		try {
			actualAufgabe = driver.findElement(Elements.aufgabe).getText().split(":")[1].replaceFirst("^\\s*", "");
			actualTag = driver.findElement(Elements.tag).getText();
			actualStartzeit = driver.findElement(Elements.startZeit).getText().substring(10, 15);
			actualEndzeit = driver.findElement(Elements.endZeit).getText().substring(18, 23);
			actualTechnikername = driver.findElement(Elements.technikerName).getText().split(":")[1].replaceFirst("^\\s*", "");
			actualTechnikeralter = driver.findElement(Elements.technikerAlter).getText().split(":")[1].replaceFirst("^\\s*", "");
			actualTechnikergeschlecht = driver.findElement(Elements.technikerGeschlecht).getText().split(":")[1].replaceFirst("^\\s*", "");
		}
		catch(Exception e) {
			ReportUtilities.extentTest.log(Status.FAIL, e.getMessage());
			throw e;
		}		
	}
	
	private void AssertData() throws Exception {
		ReportUtilities reports = new ReportUtilities(driver);
		if(actualAufgabe.equals(expectedAufgabe) && actualTag.equals(expectedTag) && actualStartzeit.equals(expectedStartzeit)
				&& actualEndzeit.equals(expectedEndzeit) && actualTechnikername.equals(expectedTechnikername) 
				&& actualTechnikeralter.equals(expectedTechnikeralter)
				&& actualTechnikergeschlecht.equals(expectedTechnikergeschlecht)) {
			reports.AddScreenshotForPassOrFailStatus("PASS", "All the data is as expected and visible on customer portal dashboard");
		}
		else {
			reports.AddScreenshotForPassOrFailStatus("FAIL", "Data not correct.");
			throw new Exception("Appointment Data not Visible");
		}
	}
}
