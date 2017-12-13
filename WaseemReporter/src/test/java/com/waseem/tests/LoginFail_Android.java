package com.waseem.tests;

import com.waseem.framework.BaseTest;
import com.waseem.framework.NewAndroidDriver;

import org.openqa.selenium.By;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidElement;

public class LoginFail_Android extends BaseTest {

	protected NewAndroidDriver<AndroidElement> driver = null;

	@BeforeMethod
	// @Parameters({ "build", "deviceQuery" })
	// public void setUp(@Optional("0") String build, @Optional("@os='android'")
	// String deviceQuery) throws Exception {
	public void setUp(ITestContext context) throws Exception {

		String build = getParameter(context, "build", "0");
		String deviceQuery = getParameter(context, "deviceQuery", "@os='android'");
		// String deviceQuery = getParameter(context, "EriBank_Android_DeviceQuery",
		// "@os='android'");
		// Init application / device capabilities
		init(build, deviceQuery);

		// dc.setCapability(AndroidMobileCapabilityType.APP_PACKAGE,
		// "cloud:com.experitest.ExperiBank");
		// dc.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ".LoginActivity");

		// dc.setCapability(MobileCapabilityType.APP,
		// "cloud:com.experitest.ExperiBank/.LoginActivity");
		// dc.setCapability(AndroidMobileCapabilityType.APP_PACKAGE,
		// "com.experitest.ExperiBank");
		// dc.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ".LoginActivity");
		// dc.setCapability(MobileCapabilityType.NO_RESET, true);
		// dc.setCapability("instrumentApp", true);
		//
		driver = new NewAndroidDriver<AndroidElement>(url, dc);
		// if (!driver.isAppInstalled("com.experitest.ExperiBank")) {
		// driver.installApp("cloud:com.experitest.ExperiBank/.LoginActivity");
		// }
		System.out.println();
		System.out.println("---   " + getClass().getName() + " > " + dc);
		System.out.println();
	}

	@Test(groups = { "seetest" })
	public void test() {
		driver.context("NATIVE_APP");
		driver.unlockDevice();
		driver.startActivity("com.experitest.ExperiBank", ".LoginActivity");

		driver.findElement(By.xpath("//*[@id='usernameTextField']")).sendKeys("Wrong-User");
		driver.findElement(By.xpath("//*[@id='passwordTextField']")).sendKeys("company");
		driver.findElement(By.xpath("//*[@id='loginButton']")).click();

		driver.findElement(By.xpath("//*[@text='Invalid username or password!']"));
		driver.findElement(By.xpath("//*[@text='Close']")).click();
	}

	@AfterMethod
	public void tearDown() {
		System.out.println(" >> " + driver.getDeviceName() + " ," + getClass().getSimpleName() + ": @AfterMethod: driver.quit()");
		// driver.close();
		driver.quit();
	}
}
