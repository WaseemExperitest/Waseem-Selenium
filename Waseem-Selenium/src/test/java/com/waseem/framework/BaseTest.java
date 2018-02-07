package com.waseem.framework;
//
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;

import com.google.common.base.Strings;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public abstract class BaseTest implements Runnable {

	protected String testName;
	protected PlatformType platformType;
	protected String browserVersion;
	protected Platform platform;
	protected String url;

	protected DesiredCapabilities dc = new DesiredCapabilities();
	protected Properties cloudProperties = new Properties();
	protected WebDriver driver;
	protected boolean isMobile = false;

	public BaseTest(String testName, PlatformType platformType, String browserVersion, Platform platform) {
		this.testName = testName;
		this.platformType = platformType;
		this.browserVersion = browserVersion;
		this.platform = platform;

		System.out.println(this);

		this.initProperties();
		this.setDesiredCapabilities();
	}

	@Override
	public String toString() {
		return super.toString() + "(" + testName + "," + platformType + "," + browserVersion + "," + platform + ")";
	}

	abstract public void runTest() throws Exception;

	public void run() {
		try {
			System.out.println("run(" + this + ")");

			RemoteWebDriver remoteWebDriver = new RemoteWebDriver(new URL(url), dc);
			remoteWebDriver.setLogLevel(Level.ALL);

			driver = remoteWebDriver;
			//
			// Actual test
			//
			this.runTest();
			
			this.tearDown();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void tearDown() {
		driver.quit();
	}

	protected void setDesiredCapabilities() {

		isMobile = false;
		switch (platformType) {
		case CHROME:
			dc.setCapability(CapabilityType.BROWSER_NAME, BrowserType.CHROME);
			break;
		case FIREFOX:
			dc.setCapability(CapabilityType.BROWSER_NAME, BrowserType.FIREFOX);
			break;
		case IE:
			dc.setCapability(CapabilityType.BROWSER_NAME, BrowserType.IE);
			break;
		case ANDROID:
			dc = DesiredCapabilities.chrome();
			dc.setCapability("platformName", "Android");
			isMobile = true;
			break;
		case IOS:
			dc = DesiredCapabilities.safari();
			dc.setCapability("platformName", "ios");
			isMobile = true;
			break;
		default:
			dc = new DesiredCapabilities();
			break;
		}
		if (!isMobile) {
			dc.setCapability(CapabilityType.VERSION, browserVersion);
			dc.setCapability(CapabilityType.PLATFORM, platform);
			
			dc.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
			dc.setCapability("newCommandTimeout", 60);
			dc.setCapability("newSessionWaitTimeout", 60);			
		}

		dc.setCapability("testName", this.getTestName());
		dc.setCapability("platformType", this.platformType);
		dc.setCapability("reportDirectory", "reports");
		dc.setCapability("reportFormat", "xml");
		dc.setCapability("generateReport", true);
		dc.setCapability("stream", "Waseem-Selenium");
		dc.setCapability("build.number", System.getenv("BUILD_NUMBER"));

		this.initProperties();
		String accessKey = getProperty("accessKey", cloudProperties);
		if (Strings.isNullOrEmpty(accessKey)) {
			dc.setCapability("user", getProperty("username", cloudProperties));
			dc.setCapability("password", getProperty("password", cloudProperties));
		} else {
			dc.setCapability("accessKey", accessKey);
		}
		String project = getProperty("project", cloudProperties);
		if (Strings.isNullOrEmpty(project)) {
			project = "Default";
		}
		dc.setCapability("project", project);

		String port = getProperty("port", cloudProperties);
		if (Strings.isNullOrEmpty(port)) {
			port = "443";
			System.out.println("default port " + port);
		}		
		url = getProperty("url", cloudProperties);
		if (!url.contains("/wd/hub")) {
			if (!url.contains(":")) {
				// case url=sales.experitest.com
				url += ":" + port;
			} else {
				if (url.contains("http")) {
					int i = url.lastIndexOf(':');
					if (i < 6) { // 012345
						// case url=https://sales.experitest.com
						url += ":" + port;
					}
				}
			}
			url += "/wd/hub";
		}
		// https://sales.experitest.com:443/wd/hub
		System.out.println();
		System.out.println("--- " + getClass().getName() + " > DesiredCapabilities:");
		System.out.println(dc);
		System.out.println("--- url: " + url);
	}

	protected String getProperty(String property, Properties props) {
		if (System.getProperty(property) != null) {
			return System.getProperty(property);
		} else if (System.getenv().containsKey(property)) {
			return System.getenv(property);
		} else if (props != null) {
			return props.getProperty(property);
		}
		return null;
	}

	protected void initProperties() {
		boolean propertiesLoaded = false;
		File cloudPropertiesFile = new File("cloud.properties");
		if (cloudPropertiesFile.exists()) {
			FileReader fr = null;
			try {
				fr = new FileReader("cloud.properties");
				cloudProperties.load(fr);
				propertiesLoaded = true;

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (fr != null) {
					try {
						fr.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		if (!propertiesLoaded) {
			cloudProperties.setProperty("accessKey", System.getenv("accessKey"));
			cloudProperties.setProperty("url", System.getenv("url"));
		}
	}

	public String getTestName() {
		return testName;
	}

	public PlatformType getPlatformType() {
		return platformType;
	}

	public String getBrowserVersion() {
		return browserVersion;
	}

	public Platform getPlatform() {
		return platform;
	}

}
