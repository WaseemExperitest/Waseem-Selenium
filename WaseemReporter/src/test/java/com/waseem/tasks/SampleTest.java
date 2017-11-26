package com.waseem.tasks;

import org.testng.annotations.Test;
import org.testng.annotations.Parameters;

public class SampleTest {
	@Test
	@Parameters("name")
	public void testMethod(String name) {
		System.err.println("Name that was passed in via Jenkins job " + name);
	}
}
