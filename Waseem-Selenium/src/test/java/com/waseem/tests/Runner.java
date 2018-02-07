package com.waseem.tests;
//
import java.util.ArrayList;
import org.openqa.selenium.Platform;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.util.Strings;
import org.testng.xml.XmlTest;

import com.waseem.framework.PlatformType;

public class Runner {

	protected int numOfChrome = 3;
	protected int numOfFirefox = 3;
	protected int numOfIE = 3;
	protected int numOfIOS = 3;
	protected int numOfAndroid = 3;

	protected String versionOfChrome = "62";
	protected String versionOfFirefox = "57.0.2";
	protected String versionOfIE = "11";

	protected Platform platform = Platform.WIN8;

	protected ArrayList<Thread> threadList = new ArrayList<Thread>();

	public static void main(String[] args) throws InterruptedException {

		Runner runner = new Runner();
		runner.testBrowsers();
	}

	@Test
	public void testBrowsers() throws InterruptedException {
		testBrowser(numOfChrome, PlatformType.CHROME, versionOfChrome, platform);
		testBrowser(numOfFirefox, PlatformType.FIREFOX, versionOfFirefox, platform);
		testBrowser(numOfIE, PlatformType.IE, versionOfIE, platform);
		testBrowser(numOfIOS, PlatformType.IOS, "", null);
		testBrowser(numOfAndroid, PlatformType.ANDROID, "", null);

		for (int i = 0; i < threadList.size(); i++) {
			threadList.get(i).join();
		}
	}

	protected void testBrowser(int runCount, PlatformType platformType, String browserVersion, Platform platform) {
		WebTest test = null;
		Thread thread = null;
		for (int i = 1; i <= runCount; i++) {
			test = new WebTest("Waseem " + platformType + " test #" + i, platformType, browserVersion, platform);
			thread = new Thread(test, test.getTestName());
			thread.start();
			threadList.add(thread);
		}
	}

	@BeforeTest
	public void getParameters(ITestContext context) {
		if (context == null) {
			return;
		}
		XmlTest xmlTest = context.getCurrentXmlTest();
		if (xmlTest == null) {
			return;
		}
		numOfChrome = getIntParameter(xmlTest, "numOfChrome", numOfChrome);
		numOfFirefox = getIntParameter(xmlTest, "numOfFirefox", numOfFirefox);
		numOfIE = getIntParameter(xmlTest, "numOfIE", numOfIE);
		numOfIOS = getIntParameter(xmlTest, "numOfIOS", numOfIOS);
		numOfAndroid = getIntParameter(xmlTest, "numOfAndroid", numOfAndroid);

		versionOfChrome = getStringParameter(xmlTest, "versionOfChrome", versionOfChrome);
		versionOfFirefox = getStringParameter(xmlTest, "versionOfFirefox", versionOfFirefox);
		versionOfIE = getStringParameter(xmlTest, "versionOfIE", versionOfIE);
		platform = Platform.valueOf(getStringParameter(xmlTest, "platform",platform.toString()));
	}

	protected int getIntParameter(XmlTest xmlTest, String parameter, int defaultValue) {
		String found = null;
		int value = defaultValue;
		if (xmlTest != null) {
			found = xmlTest.getParameter(parameter);
			if (Strings.isNotNullAndNotEmpty(found)) {
				value = Integer.parseInt(found);
			}
		}
		System.out.println(parameter + "(" + found + ",default=" + defaultValue + ") > " + value);
		return value;
	}

	protected String getStringParameter(XmlTest xmlTest, String parameter, String defaultValue) {
		String found = null;
		String value = defaultValue;
		if (xmlTest != null) {
			found = xmlTest.getParameter(parameter);
			if (Strings.isNotNullAndNotEmpty(found)) {
				value = found;
			}
		}
		System.out.println(parameter + "(" + found + ",default=" + defaultValue + ") > " + value);
		return value;
	}
}
