package com.waseem.tests;

import com.waseem.framework.BaseTest;
import com.waseem.framework.NewIOSDriver;

import org.openqa.selenium.By;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.remote.IOSMobileCapabilityType;

public class EriBank_iOS extends BaseTest {
	
	protected NewIOSDriver<IOSElement> driver = null;

	@BeforeMethod
	// @Parameters({ "build", "deviceQuery" })
	// public void setUp(@Optional("0") String build, @Optional("@os='ios'") String
	// deviceQuery) throws Exception {
	public void setUp(ITestContext context) throws Exception {

		String build = getParameter(context, "build", "0");
		String deviceQuery = getParameter(context, "deviceQuery", "@os='ios' and contains(@model, 'iphone')");
//		String deviceQuery = getParameter(context, "EriBank_iOS_DeviceQuery", "@os='ios' and contains(@model, 'iphone')");

		// Init application / device capabilities
		init(build, deviceQuery);

//		dc.setCapability(MobileCapabilityType.APP, "cloud:com.experitest.ExperiBank");
//		dc.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "com.experitest.ExperiBank");
//		dc.setCapability(MobileCapabilityType.NO_RESET, true);
//		dc.setCapability("instrumentApp", true);

		dc.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "com.experitest.ExperiBank");
		driver = new NewIOSDriver<IOSElement>(url, dc);

//		if (!driver.isAppInstalled("com.experitest.ExperiBank")) {
//			driver.installApp("cloud:com.experitest.ExperiBank");
//		}

		System.out.println();
		System.out.println("---   " + getClass().getName() + " > " + dc);
		System.out.println();
	}

	@Test(groups = { "seetest" })
	public void test() {
		  driver.context("NATIVE_APP");
		  //driver.executeScript("client:client.deviceAction(\"Unlock\")");
		  //driver.executeScript("client:client.launch(\"com.experitest.ExperiBank\")");
		  driver.findElement(By.xpath("//*[@accessibilityLabel='usernameTextField']")).sendKeys("company");
		  driver.findElement(By.xpath("//*[@accessibilityLabel='passwordTextField']")).sendKeys("company");
		  driver.findElement(By.xpath("//*[@accessibilityLabel='loginButton' or @value='loginButton']")).click();
		  driver.findElement(By.xpath("//*[@accessibilityLabel='Make Payment' or @value='makePaymentButton']")).click();
		  driver.findElement(By.xpath("//*[@accessibilityLabel='Phone' or @value='Phone']")).sendKeys("032-325306454");
		  driver.findElement(By.xpath("//*[@accessibilityLabel='Name' or @value='Name']")).sendKeys("Waseem");
		  driver.findElement(By.xpath("//*[@accessibilityLabel='Amount' or @value='Amount']")).sendKeys("11");
		  driver.findElement(By.xpath("//*[@accessibilityLabel='Select' or @text='countryButton']")).click();
		  driver.findElement(By.xpath("//*[@text='Iceland']")).click();
		  driver.findElement(By.xpath("//*[@accessibilityLabel='Send Payment' or @text='sendPaymentButton']")).click();
		  driver.findElement(By.xpath("//*[@text='Yes']")).click();
		  driver.findElement(By.xpath("//*[@accessibilityLabel='logoutButton' or @value='logoutButton']")).click();
		 }

	@AfterMethod
	public void tearDown() {
		System.out.println(" >> " + getClass().getSimpleName() + ": @AfterMethod: driver.quit()");
		// driver.close();
		driver.quit();
	}
}
