package com.waseem.tests;

//import java.util.ArrayList;
//import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import com.waseem.framework.WebBaseTest;

public class ExperitestCustomers extends WebBaseTest {
	private static int customersPrintCount = 10;

	@Test
	public void testWebTest() {
		System.out.println(browserOS + "@Test:test()");
		WebElement customer = null;
		for (int i = 1; i <= customersPrintCount; i++) {
			customer = driver.findElement(By.xpath("((//*[contains(@class,'img_name')]/../..)/*/*)[" + i + "]"));
			System.out.println(browserOS + "[" + i + "] src=" + customer.getAttribute("src"));
		}
//		if (!isMobile) {
//			ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
//			driver.switchTo().window(tabs.get(1));
//		}
//
//		List<WebElement> customers = driver.findElements(By.xpath("//*[contains(@class,'img_name')]"));
//		System.out.println("Customers size = " + customers.size());
//
//		for (int i = 0; i < customersPrintCount; i++) {
//			System.out.println(customers.get(i).getAttribute("class"));
//		}
	}
}
