package com.waseem.tests;

import com.waseem.framework.BaseTest;

import org.openqa.selenium.By;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;

public class EriBank_Android extends BaseTest {
	protected AndroidDriver<AndroidElement> driver = null;

	@BeforeMethod
	// @Parameters({ "build", "deviceQuery" })
	// public void setUp(@Optional("0") String build, @Optional("@os='android'")
	// String deviceQuery) throws Exception {
	public void setUp(ITestContext context) throws Exception {

		String build = getParameter(context, "build", "0");
		String deviceQuery = getParameter(context, "EriBank_Android_DeviceQuery", "@os='android'");
		// Init application / device capabilities
		init(build, deviceQuery);

		dc.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "cloud:com.experitest.ExperiBank");
		dc.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ".LoginActivity");

		// dc.setCapability(MobileCapabilityType.APP,
		// "cloud:com.experitest.ExperiBank/.LoginActivity");
		// dc.setCapability(AndroidMobileCapabilityType.APP_PACKAGE,
		// "com.experitest.ExperiBank");
		// dc.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ".LoginActivity");
		// dc.setCapability(MobileCapabilityType.NO_RESET, true);
		// dc.setCapability("instrumentApp", true);
		//
		driver = new AndroidDriver<AndroidElement>(url, dc);
		// if (!driver.isAppInstalled("com.experitest.ExperiBank")) {
		// driver.installApp("cloud:com.experitest.ExperiBank/.LoginActivity");
		// }
		System.out.println();
		System.out.println("---");
		System.out.println(getClass().getName() + " > " + dc);
		System.out.println("---");
		System.out.println();

	}

	@Test
	public void test() {
		// Enter the test code
		driver.context("NATIVE_APP");
		driver.unlockDevice();
		driver.startActivity("com.experitest.ExperiBank", ".LoginActivity");

		// driver.executeScript("client:client.deviceAction(\"Unlock\")");
		// driver.executeScript("client:client.launch(\"com.experitest.ExperiBank/.LoginActivity\")");

		driver.findElement(By.xpath("//*[@id='usernameTextField']")).sendKeys("company");
		driver.findElement(By.xpath("//*[@id='passwordTextField']")).sendKeys("company");
		driver.findElement(By.xpath("//*[@id='loginButton']")).click();
		driver.findElement(By.xpath("//*[@text='Make Payment']")).click();
		driver.findElement(By.xpath("//*[@id='phoneTextField']")).sendKeys("032-545306454");
		driver.findElement(By.xpath("//*[@id='nameTextField']")).sendKeys("Waseem");
		driver.findElement(By.xpath("//*[@id='amountTextField' or @id='amount']")).sendKeys("11");
		driver.findElement(By.xpath("//*[@id='countryButton']")).click();
		driver.findElement(By.xpath("//*[@text='Iceland']")).click();
		driver.findElement(By.xpath("//*[@id='sendPaymentButton']")).click();
		driver.findElement(By.xpath("//*[@text='Yes']")).click();
		driver.findElement(By.xpath("//*[@id='logoutButton']")).click();
	}

	@AfterMethod
	public void tearDown() {
		driver.quit();
	}
}
