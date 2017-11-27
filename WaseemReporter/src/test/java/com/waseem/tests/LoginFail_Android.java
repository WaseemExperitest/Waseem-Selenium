package com.waseem.tests;

import com.waseem.framework.BaseTest;

import org.openqa.selenium.By;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;

public class LoginFail_Android extends BaseTest {

	protected AndroidDriver<AndroidElement> driver = null;

	@BeforeMethod
	@Parameters({ "build", "deviceQuery" })
	public void setUp(@Optional("0") String build, @Optional("@os='android'") String deviceQuery) throws Exception {

		// Init application / device capabilities
		init(build, deviceQuery);

		dc.setCapability(MobileCapabilityType.APP, "cloud:com.experitest.ExperiBank/.LoginActivity");
		dc.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "com.experitest.ExperiBank");
		dc.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ".LoginActivity");
		dc.setCapability(MobileCapabilityType.NO_RESET, true);
		dc.setCapability("instrumentApp", true);

		driver = new AndroidDriver<AndroidElement>(url, dc);
		if (!driver.isAppInstalled("com.experitest.ExperiBank")) {
			driver.installApp("cloud:com.experitest.ExperiBank/.LoginActivity");
		}
	}

	@Test
	public void test() {
		// Enter the test code
		driver.context("NATIVE_APP");
		driver.executeScript("client:client.deviceAction(\"Unlock\")");
		driver.executeScript("client:client.launch(\"com.experitest.ExperiBank/.LoginActivity\")");

		driver.findElement(By.xpath("//*[@id='usernameTextField']")).sendKeys("company");
		driver.findElement(By.xpath("//*[@id='passwordTextField']")).sendKeys("Wrong");
		driver.findElement(By.xpath("//*[@id='loginButton']")).click();

		driver.findElement(By.xpath("//*[@text='Invalid username or password!']"));
		driver.findElement(By.xpath("//*[@text='Close']")).click();
	}

	@AfterMethod
	public void tearDown() {
		driver.quit();
	}
}
