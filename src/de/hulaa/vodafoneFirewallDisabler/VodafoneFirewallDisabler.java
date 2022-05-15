package de.hulaa.vodafoneFirewallDisabler;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.v101.console.Console;
import org.openqa.selenium.support.ui.Select;

public class VodafoneFirewallDisabler {

	static WebDriver driver;
	static int stdDelay = 3000;

	public static void main(String[] args) {

		if (args.length < 3) {
			System.out.println(
					"Not enough input parameters --> call like this: java -jar vfd.jar http://192.168.0.1 admin password");
		} else {
			//init
			driver = init();
			int MINUTES = 20;
			
			//loop
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
			  @Override
			  public void run() {
					try {
						disableFirewall(args[0], args[1], args[2]);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			  }
			}, 5000, 1000*60*MINUTES);//wait 0 ms before doing the action and do it evry 1000ms (1second)
		}
	}

	public static ChromeDriver init() {
		String os = Utils.getOS();
		String driverFile;
		if (os.toLowerCase().contains("windows")) {
			driverFile = "chromedriver.exe";
		} else {
			driverFile = "chromedriver";
		}
		System.setProperty("webdriver.chrome.driver", ("src/de/hulaa/vodafoneFirewallDisabler/" + driverFile));
		return new ChromeDriver();
	}

	public static void disableFirewall(String routerURL, String user, String password) throws InterruptedException {

		String routerlogin = routerURL + "/#/login";

		driver.get(routerlogin);
		Thread.sleep(stdDelay); // Let the user actually see something!

		WebElement userElem = driver.findElement(By.id("username"));
		WebElement pwElem = driver.findElement(By.id("password"));
		WebElement loginButton = driver.findElement(By.id("login-button"));

		userElem.sendKeys(user);
		pwElem.sendKeys(password);
		Thread.sleep(stdDelay);
		loginButton.click();

		Thread.sleep(stdDelay);

		WebElement terminateSessionButton = getElementById("terminateSessionBtn");
		if(terminateSessionButton != null) {
			terminateSessionButton.click();
			Thread.sleep(stdDelay);
		}

		WebElement changeStdPassword = getElementById("CancelCdpButton");
		changeStdPassword.click();
		Thread.sleep(2*stdDelay);

		driver.get(routerURL + "/#/internet");
		Thread.sleep(2*stdDelay);

		WebElement firewallContainer = getElementById("page_firewall_content_container");
		List<WebElement> firewallSwitch = firewallContainer.findElements(By.tagName("label"));
		
		boolean firewallActive;
		if (firewallSwitch.size() > 0) {
			System.out.println("found firewallSwitch...");

			firewallActive = firewallContainer.findElements(By.id("page_firewall_ipv4_switch")).get(0).isSelected();

			if (firewallActive) {
				System.out.println("Firewall is active.");
				// second label element
				firewallSwitch.get(1).click();
				Thread.sleep(stdDelay);
				System.out.println("Disabled the firewall switch...");
				
				WebElement applyButton = getElementById("save_btn");
				applyButton.click();
				Thread.sleep(stdDelay);

				WebElement acceptWarningButton = getElementById("applyWarningButton");
				acceptWarningButton.click();
				Thread.sleep(4*stdDelay);
			} else {
				System.out.println("Firewall is already disabled.");
			}
		}

		// logout
		driver.get(routerURL + "/#/logout");
	}

	public static WebElement getElementById(String id) {
		List<WebElement> elements = driver.findElements(By.id(id));
		if (elements.size() > 0) {
			return elements.get(0);
		} else {
			return null;
		}
	}

}
