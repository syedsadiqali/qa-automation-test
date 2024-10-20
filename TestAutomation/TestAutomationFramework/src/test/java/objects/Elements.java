package objects;

import org.openqa.selenium.By;

public class Elements {

	//Xpaths of web elements
	public static By aufgabe = By.xpath("//div[@class='appointment-details']/p[2]");
	public static By tag = By.xpath("//div[@class='appointment-details']/h3");
	public static By startZeit = By.xpath("//div[@class='appointment-details']/p[1]");
	public static By endZeit = By.xpath("//div[@class='appointment-details']/p[1]");
	public static By technikerName = By.xpath("//div[@class='technician-details']/p[1]");
	public static By technikerAlter = By.xpath("//div[@class='technician-details']/p[2]");
	public static By technikerGeschlecht = By.xpath("//div[@class='technician-details']/p[3]");
	public static By reschduleButton = By.xpath("//button[@class='btn-reschedule']");
	public static By rescheduleOptions = By.xpath("//div[@class='reschedule-options']/ul/li");
	public static By newAppointment = By.xpath("(//div[@class='reschedule-options']/ul/li)[3]");
	public static By initialAppointment = By.xpath("(//div[@class='reschedule-options']/ul/li)[1]");
}
