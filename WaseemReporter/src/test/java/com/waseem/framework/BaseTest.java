package com.waseem.framework;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestContext;
import org.testng.util.Strings;

public class BaseTest {

	protected URL url;
	protected DesiredCapabilities dc = new DesiredCapabilities();
	protected Properties cloudProperties = new Properties();

	public BaseTest()
	{
		try {
			initCloudProperties();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void init(String build, String deviceQuery) throws Exception {
		// initCloudProperties();
		String query = adhocDevice(deviceQuery);
		System.out.println("query=" + query);
		dc.setCapability("deviceQuery", query);
		// dc.setCapability(MobileCapabilityType.UDID, "5127b6db");

		dc.setCapability("reportDirectory", "reports");
		dc.setCapability("reportFormat", "xml");

		String username = getProperty("username", cloudProperties);
		if (username != null && username.length() > 0) {
			dc.setCapability("user", username);
		}
		String password = getProperty("password", cloudProperties);
		if (password != null && password.length() > 0) {
			dc.setCapability("password", password);
		}
		String accessKey = getProperty("accessKey", cloudProperties);
		if (accessKey != null && accessKey.length() > 0) {
			dc.setCapability("accessKey", accessKey);
		}
		// In case your user is assign to a single project leave empty,
		// otherwise please specify the project name
		System.out.println("project=" + getProperty("project", cloudProperties));
		dc.setCapability("project", getProperty("project", cloudProperties));

		dc.setCapability("stream", getProperty("stream", cloudProperties));
		dc.setCapability("build", build);
		System.out.println("build=" + build);

		dc.setCapability("testName", getClass().getName());
		String port = getProperty("port", cloudProperties);
		if (port == null || port.length() <= 0) {
			port = "443";
		}
		url = new URL(getProperty("url", cloudProperties) + ":" + port + "/wd/hub");
		System.out.println("url=" + url);
		System.out.println(getClass().getName() + " > " + dc);
	}

	protected String getParameter(ITestContext context, String key, String defaultValue) throws FileNotFoundException, IOException {
		String parameter = context.getCurrentXmlTest().getParameter(key);
		System.out.println("getParameter(" + context.getName() + "," + key + ")=" + parameter);
		if (Strings.isNullOrEmpty(parameter)) {
			parameter = getProperty(key, cloudProperties);
		}
		if (Strings.isNullOrEmpty(parameter)) {
			System.out.println("getParameter(" + context.getName() + "," + key + ") > using default value:" + defaultValue);
			parameter = defaultValue;
		}
		return parameter;
	}

	protected String getProperty(String property) throws FileNotFoundException, IOException {
		String value = getProperty(property, cloudProperties);
		return value;
	}

	protected String getProperty(String property, Properties props) throws FileNotFoundException, IOException {

		String value = System.getProperty(property);
		if (value != null) {
			System.out.println("System.getProperty(" + property + ")=" + value);
			return value;
		} else if (System.getenv().containsKey(property)) {
			value = System.getenv(property);
			System.out.println("System.getenv(" + property + ")=" + value);
			return value;
		} else if (props != null) {
			value = props.getProperty(property);
			System.out.println("getProperty(" + property + ")=" + value);
			return value;
		}
		System.out.println("property '" + property + "' was not found!");
		return null;
	}

	private void initCloudProperties() throws FileNotFoundException, IOException {
		FileReader fr = new FileReader("cloud.properties");
		cloudProperties.load(fr);
		fr.close();
	}

	private static synchronized String adhocDevice(String deviceQuery) {
		try {
			File jarLocation = (System.getProperty("os.name").toUpperCase().contains("WIN"))
					? new File(System.getenv("APPDATA"), ".mobiledata")
					: new File(System.getProperty("user.home") + "/Library/Application " + "Support", ".mobiledata");
			File adhocProperties = new File(jarLocation, "adhoc.properties");
			if (adhocProperties.exists()) {
				Properties prop = new Properties();
				FileReader reader = new FileReader(adhocProperties);
				try {
					prop.load(reader);
				} finally {
					reader.close();
				}
				adhocProperties.delete();
				return "@serialnumber='" + prop.getProperty("serial") + "'";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return deviceQuery;
	}

}
