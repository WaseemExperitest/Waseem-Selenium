package com.waseem.tests;

import org.testng.annotations.Test;
import org.testng.annotations.Parameters;

public class SampleTest {
	@Test
	@Parameters({ "build", "accessKey" })
	public void testMethod(String build,String accessKey) {
		System.err.println("job that was passed in via Jenkins job " + build);
		System.out.println("accessKey=" + accessKey);
	}
}
