package com.waseem.tests;

import com.waseem.framework.BaseTest;

import org.openqa.selenium.By;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;

public class EriBank_iOS extends BaseTest {
	protected IOSDriver<IOSElement> driver = null;

	@BeforeMethod
	@Parameters({ "build", "deviceQuery" })
	public void setUp(@Optional("0") String build, @Optional("@os='ios'") String deviceQuery) throws Exception {

		// Init application / device capabilities
		init(build, deviceQuery);

		dc.setCapability(MobileCapabilityType.APP, "cloud:com.experitest.ExperiBank");
		dc.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "com.experitest.ExperiBank");
		dc.setCapability(MobileCapabilityType.NO_RESET, true);
		dc.setCapability("instrumentApp", true);

		driver = new IOSDriver<IOSElement>(url, dc);

		if (!driver.isAppInstalled("com.experitest.ExperiBank")) {
			driver.installApp("cloud:com.experitest.ExperiBank");
		}
	}

	@Test
	public void test() {
		// Enter the test code
		driver.context("NATIVE_APP");
		driver.executeScript("client:client.deviceAction(\"Unlock\")");
		driver.executeScript("client:client.launch(\"com.experitest.ExperiBank\")");

		driver.findElement(By.xpath("//*[@accessibilityLabel='usernameTextField']")).sendKeys("company");
		driver.findElement(By.xpath("//*[@accessibilityLabel='passwordTextField']")).sendKeys("company");
		driver.findElement(By.xpath("//*[@value='loginButton']")).click();
		driver.findElement(By.xpath("//*[@value='makePaymentButton']")).click();
		driver.findElement(By.xpath("//*[@value='Phone']")).sendKeys("032-545306454");
		driver.findElement(By.xpath("//*[@value='Name']")).sendKeys("Waseem");
		driver.findElement(By.xpath("//*[@value='Amount']")).sendKeys("11");
		driver.findElement(By.xpath("//*[@text='countryButton']")).click();
		driver.findElement(By.xpath("//*[@text='Iceland']")).click();
		driver.findElement(By.xpath("//*[@text='sendPaymentButton']")).click();
		driver.findElement(By.xpath("//*[@text='Yes']")).click();
	}

	@AfterMethod
	public void tearDown() {
		driver.quit();
	}
}
