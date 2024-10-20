package test_scenarios;

import java.io.IOException;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import functions.AppointmentPortalFunctions;
import io.github.bonigarcia.wdm.WebDriverManager;
import utilities.ReportUtilities;

public class TestMethods {

	WebDriver driver;	
	ReportUtilities reports = new ReportUtilities(driver);
	String URL="http://localhost:8080/";
	
	@BeforeTest
	public void beforeTest() throws InterruptedException {
		WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-dev-shm-usage");
		driver = new ChromeDriver(options);
		driver.manage().window().maximize();				
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
		driver.get(URL);
	}
	
	@Test
	//Acceptance Criteria 1: User can see his appointment data on screen
	public void VerifyAppointmentDataOnCustomerDashboard() throws Exception{
		try {
			reports.StartTest("Verify Appointment Data");	
			AppointmentPortalFunctions appointment = new AppointmentPortalFunctions(driver);
			appointment.VerifyAppointmentData();
		}
		catch(Exception message) {
			message.printStackTrace();
			throw message;
		}		
	}
	
	@Test
	//Acceptance Criteria 2: User can see his rescheduled appointment data on screen
	public void VerifyRescheduledAppointmentDataOnCustomerDashboard() throws IOException{
		try {
			Thread.sleep(2000);
			reports.StartTest("Verify Rescheduled Appointment Data");	
			AppointmentPortalFunctions appointment = new AppointmentPortalFunctions(driver);
			appointment.VerifyRescheduledAppointmentData();
		}
		catch(Exception message) {
			ReportUtilities.extentTest.log(Status.FAIL, message.getMessage());
		}		
	}
		
	@AfterTest
	public void afterTest() {
		driver.quit();
		reports.CompleteTest();
	}	
}
