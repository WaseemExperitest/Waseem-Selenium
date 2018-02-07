package com.waseem.tests;
//
import org.openqa.selenium.Platform;

import com.waseem.framework.BaseTest;
import com.waseem.framework.PlatformType;

public class WebTest extends BaseTest {

	public WebTest(String testName, PlatformType platformType, String browserVersion, Platform platform) {
		super(testName, platformType, browserVersion, platform);
	}

	@Override
	public void runTest() throws Exception {
		Thread.sleep(8000);
		
		System.out.println("runTest(" + this + ") > google.com");

		driver.get("http://www.ktm.com/");

		System.out.println("The page url is: " + driver.getCurrentUrl());
		System.out.println("The page title is: " + driver.getTitle());
		Thread.sleep(8000);
	}
}