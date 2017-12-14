package com.waseem.framework;

import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.util.Strings;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;

import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class WebBaseTest {

	// Client and Device
	protected boolean isMobile = true;
	protected BrowserOS browserOS = BrowserOS.ANDROID;
	protected RemoteWebDriver driver = null;
	protected Properties cloudProperties = new Properties();

	private String reportDirectory = "reports";
	private String reportFormat = "xml";

	private void initCloudProperties() throws FileNotFoundException, IOException {
		FileReader fr = new FileReader("cloud.properties");
		cloudProperties.load(fr);
		fr.close();
	}

	private static String getParameter(ITestContext context, String key) {
		String parameter = context.getCurrentXmlTest().getParameter(key);
		System.out.println("getParameter(" + context.getName() + "," + key + ")=" + parameter);
		return parameter;
	}

	protected String getProperty(String property, Properties props) throws FileNotFoundException, IOException {

		String value = System.getProperty(property);
		if (value != null) {
			System.out.println("System.getProperty(" + property + ")=" + value);
			return value;
		} else if (System.getenv().containsKey(property)) {
			value = System.getenv(property);
			System.out.println("System.getenv(" + property + ")=" + value);
			return value;
		} else if (props != null) {
			value = props.getProperty(property);
			System.out.println("getProperty(" + property + ")=" + value);
			return value;
		}
		System.out.println("property '" + property + "' was not found!");
		return null;
	}

	private String addGridCapabilities(ITestContext context, DesiredCapabilities dc)
			throws FileNotFoundException, IOException {
		dc.setCapability("reporter.suite", context.getSuite().getName());
		dc.setCapability("deviceQuery", getParameter(context, "deviceQuery"));

		String username = getProperty("username", cloudProperties);
		if (username != null && username.length() > 0) {
			dc.setCapability("user", username);
		}
		String password = getProperty("password", cloudProperties);
		if (password != null && password.length() > 0) {
			dc.setCapability("password", password);
		}
		String accessKey = getProperty("accessKey", cloudProperties);
		if (accessKey != null && accessKey.length() > 0) {
			dc.setCapability("accessKey", accessKey);
		}
		// In case your user is assign to a single project leave empty,
		// otherwise please specify the project name
		System.out.println("project=" + getProperty("project", cloudProperties));
		dc.setCapability("project", getProperty("project", cloudProperties));

		dc.setCapability("stream", getProperty("stream", cloudProperties));
		String build = getParameter(context, "build");
		dc.setCapability("build", build);
		System.out.println("build=" + build);

		dc.setCapability("testName", getClass().getName());
		String port = getProperty("port", cloudProperties);
		if (port == null || port.length() <= 0) {
			port = "443";
		}
		String url = getProperty("url", cloudProperties) + ":" + port + "/wd/hub";
		System.out.println("url=" + url);

		return url;
	}

	@BeforeSuite
	public void beforeSuite(ITestContext context) {
		System.out.println("@BeforeSuite(" + context.getName() + ")");
	}

	/*
	 * private String getQueryFromUdid(String udid) { String query =
	 * "@serialnumber='" + udid + "'"; System.out.println("getQueryFromUdid(" + udid
	 * + ")=" + query); return query; }
	 */

	@BeforeClass
	public void beforeClass(ITestContext context) throws FileNotFoundException, IOException {
		initCloudProperties();
		browserOS = BrowserOS.valueOf(getParameter(context, "browser.os").toUpperCase());
		System.out.println("@BeforeClass(" + context.getName() + ") > browserOS=" + browserOS);

		DesiredCapabilities dc = new DesiredCapabilities();
		dc.setCapability("reportDirectory", reportDirectory);
		dc.setCapability("reportFormat", reportFormat);
		dc.setCapability("generateReport", true);
		dc.setCapability("testName", this.getClass().getName());

		String seeTestserver = null;
		switch (browserOS) {
		case ANDROID:
			isMobile = true;
			seeTestserver = addGridCapabilities(context, dc);
			dc.setCapability(MobileCapabilityType.BROWSER_NAME, BrowserType.CHROME);
			System.out.println();
			System.out.println("---");
			System.out.println(getClass().getName() + " ANDROID > " + dc);
			System.out.println("---");
			System.out.println();
			AndroidDriver<WebElement> androidDriver = new AndroidDriver<WebElement>(new URL(seeTestserver), dc);
			androidDriver.unlockDevice();
			androidDriver.rotate(ScreenOrientation.PORTRAIT);
			driver = androidDriver;
			break;

		case IOS:
			isMobile = true;
			seeTestserver = addGridCapabilities(context, dc);
			dc.setCapability(MobileCapabilityType.BROWSER_NAME, BrowserType.SAFARI);
			System.out.println();
			System.out.println("---");
			System.out.println(getClass().getName() + " IOS > " + dc);
			System.out.println("---");
			System.out.println();
			IOSDriver<WebElement> iosDriver = new IOSDriver<WebElement>(new URL(seeTestserver), dc);
			iosDriver.executeScript("client:client.deviceAction(\"Unlock\")");
			iosDriver.rotate(ScreenOrientation.PORTRAIT);
			driver = iosDriver;
			break;

		case CHROME:
			isMobile = false;

			System.out.println(browserOS + ", getClass()=" + getClass());
			System.out.println(browserOS + ", driver=" + System.getProperty("webdriver.chrome.driver"));

			URL resource = getClass().getClassLoader().getResource("resources/drivers/experi_chromedriver.exe");
			System.out.println(browserOS + ", getResource=" + resource);
			System.setProperty("webdriver.chrome.driver", resource.getPath());
			System.out.println(browserOS + ", driver=" + System.getProperty("webdriver.chrome.driver"));
			dc.setCapability(MobileCapabilityType.BROWSER_NAME, BrowserType.CHROME);
			System.out.println();
			System.out.println("---");
			System.out.println(getClass().getName() + " CHROME > " + dc);
			System.out.println("---");
			System.out.println();
			driver = new ChromeDriver(dc);
			break;
		case FIREFOX:
			isMobile = false;
			dc.setCapability(MobileCapabilityType.BROWSER_NAME, BrowserType.FIREFOX);
			System.out.println();
			System.out.println("---");
			System.out.println(getClass().getName() + " FIREFOX > " + dc);
			System.out.println("---");
			System.out.println();
			driver = new FirefoxDriver(dc);
			break;
		default:
			break;
		}
		// driver.setLogLevel(Level.INFO);
		driver.setLogLevel(Level.ALL);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		if (!isMobile) {
			System.out.println(browserOS + ", isMobile=" + isMobile + " > window().maximize");
			driver.manage().window().maximize();
		} else {
			driver.executeScript("client:client.deviceAction(\"Unlock\")");
		}

		String launshUrl = getParameter(context, "launsh.url");
		if (Strings.isNullOrEmpty(launshUrl)) {
			launshUrl = "https://experitest.com/customers";
		}

		driver.get(launshUrl);
		try {
			driver.wait(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@AfterClass
	public void afterClass() {
		System.out.println(" >> " + getClass().getSimpleName() + "," + browserOS + ": @AfterClass: driver.quit() "
				+ driver.getCapabilities());
		// driver.close();
		driver.quit();
	}
}