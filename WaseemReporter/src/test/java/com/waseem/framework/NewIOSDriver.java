package com.waseem.framework;

import io.appium.java_client.ios.IOSDriver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.SessionId;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewIOSDriver<T extends WebElement> extends IOSDriver<WebElement> {

	protected String deviceID = null;
	protected String deviceName = null;

	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

	public NewIOSDriver(URL remoteAddress, Capabilities desiredCapabilities) {
		super(remoteAddress, desiredCapabilities);
		try {
			this.deviceID = (String) desiredCapabilities.getCapability("device.udid");
			this.deviceName = ((String) desiredCapabilities.getCapability("device.name")).replace(" ", "_")
					.replace("'", "-").trim();

		} catch (Exception e) {
			System.out.println("No Id or Name");
		}

	}

	@Override
	protected void log(SessionId sessionId, String commandName, Object toLog, When when) {
		if (commandName.equals("newSession"))
			sdf = new SimpleDateFormat("HH:mm:ss");

		System.out.println(toLog);
		// System.out.println(sdf.format(new Date(System.currentTimeMillis())) + ": " +
		// deviceID + " - " + when + ": " + commandName + " toLog:" + toLog);
		super.log(sessionId, commandName, toLog, when);
		if (deviceName != null) {
			System.out.println(deviceName + " - " + sdf.format(new Date(System.currentTimeMillis())) + when + ": "
					+ commandName + " toLog:" + toLog);
		}
	}

	public String getDeviceID() {
		return deviceID;
	}

	public String getDeviceName() {
		return deviceName;
	}

}
