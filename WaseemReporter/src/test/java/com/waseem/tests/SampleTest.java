package com.waseem.tests;

import org.testng.annotations.Test;
import org.testng.annotations.Parameters;

public class SampleTest {
	@Test
	@Parameters("job")
	public void testMethod(String job) {
		System.err.println("job that was passed in via Jenkins job " + job);
	}
}
