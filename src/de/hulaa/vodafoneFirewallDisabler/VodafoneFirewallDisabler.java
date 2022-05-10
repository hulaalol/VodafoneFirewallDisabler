package de.hulaa.vodafoneFirewallDisabler;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.v101.console.Console;
import org.openqa.selenium.support.ui.Select;

public class VodafoneFirewallDisabler {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(Utils.getOS());

		if(args.length < 3) {
			System.out.println("Not enough input parameters --> call like this: java -jar vfd.jar http://192.168.0.1 admin password");
		}else {
			try {
				disableFirewall(args[0], args[1], args[2]);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

	}

	public static void disableFirewall(String routerURL, String user, String password) throws InterruptedException {
		// TODO: move to args
		String routerlogin = routerURL+"/#/login";
		System.setProperty("webdriver.chrome.driver", "src/de/hulaa/vodafoneFirewallDisabler/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.get(routerlogin);
		Thread.sleep(3000); // Let the user actually see something!

		WebElement userElem = driver.findElement(By.id("username"));
		WebElement pwElem = driver.findElement(By.id("password"));
		WebElement loginButton = driver.findElement(By.id("login-button"));

		userElem.sendKeys(user);
		pwElem.sendKeys(password);
		Thread.sleep(1000);
		loginButton.click();

		Thread.sleep(1000);
		// TODO: use wrapper function to get element
		List<WebElement> terminateSessionButton = driver.findElements(By.id("terminateSessionBtn"));
		if (terminateSessionButton.size() > 0) {
			terminateSessionButton.get(0).click();
		}

		Thread.sleep(1000);
		List<WebElement> changeStdPassword = driver.findElements(By.id("CancelCdpButton"));
		if (changeStdPassword.size() > 0) {
			changeStdPassword.get(0).click();
		}

		Thread.sleep(5000);
		// TODO: use base url and only refs to #login / #internet
		driver.get(routerURL+"/#/internet");
		Thread.sleep(5000);
		List<WebElement> firewallContainer = driver.findElements(By.id("page_firewall_content_container"));
		if (firewallContainer.size() > 0) {
			System.out.println("found firewallContainer...");

			List<WebElement> firewallSwitch = firewallContainer.get(0).findElements(By.tagName("label"));
			if (firewallSwitch.size() > 0) {
				System.out.println("found firewallSwitch...");

				boolean firewallActive = firewallContainer.get(0).findElements(By.id("page_firewall_ipv4_switch")).get(0).isSelected();
				
				if(firewallActive) {
					System.out.println("Firewall is active.");
				}else {
					System.out.println("Firewall is already disabled.");
				}
				
				
				if(firewallActive) {
					// second label element
					firewallSwitch.get(1).click();
					Thread.sleep(1000);
				}
				//TODO: check if switch is disabled, try again if not
			}

			List<WebElement> applyButton = driver.findElements(By.id("save_btn"));
			if (applyButton.size() > 0) {
				applyButton.get(0).click();
			}
			
			Thread.sleep(1000);
			List<WebElement> acceptWarningButton = driver.findElements(By.id("applyWarningButton"));
			if(acceptWarningButton.size() > 0) {
				acceptWarningButton.get(0).click();
			}

			

		}

		Thread.sleep(10000); // Let the user actually see something!
		
		//logout
		driver.get(routerURL+"/#/logout");
		Thread.sleep(5000);
		driver.quit();

	}

	public static void testGoogleSearch() throws InterruptedException {
		// TODO: change to Resource folder --> make OS dynamix
		System.setProperty("webdriver.chrome.driver", "src/de/hulaa/vodafoneFirewallDisabler/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.get("http://www.google.com/");
		Thread.sleep(5000); // Let the user actually see something!
		WebElement searchBox = driver.findElement(By.name("q"));
		searchBox.sendKeys("ChromeDriver");
		searchBox.submit();
		Thread.sleep(5000); // Let the user actually see something!
		driver.quit();
	}

}
