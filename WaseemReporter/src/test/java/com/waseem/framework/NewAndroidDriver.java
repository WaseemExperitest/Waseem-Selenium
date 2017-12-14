package com.waseem.framework;

import io.appium.java_client.android.AndroidDriver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.SessionId;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("unchecked")
public class NewAndroidDriver<T extends WebElement> extends AndroidDriver<T> {

	private final String deviceID;
	protected String deviceName = "";

	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

	public NewAndroidDriver(URL remoteAddress, Capabilities desiredCapabilities) {

		super(remoteAddress, desiredCapabilities);

		this.deviceID = (String) desiredCapabilities.getCapability("device.serialNumber");
		try {
			this.deviceName = ((String) desiredCapabilities.getCapability("device.name")).replace(" ", "_")
					.replace("'", "-").trim();
		} catch (Exception e) {
			this.deviceName = getDeviceID();
		}

	}

	@Override
	protected void log(SessionId sessionId, String commandName, Object toLog, When when) {
		if (commandName.equals("newSession"))
			sdf = new SimpleDateFormat("HH:mm:ss");
		super.log(sessionId, commandName, toLog, when);

		System.out.println(toLog);
		//System.out.println(sdf.format(new Date(System.currentTimeMillis())) + ": " + getDeviceID() + " - " + when + ": " + commandName + " toLog:" + toLog);
		if (deviceName != null) {
			System.out.println(deviceName + sdf.format(new Date(System.currentTimeMillis())) + when + ": " + commandName
					+ " toLog:" + toLog);

		}
	}

	public String getDeviceID() {
		return deviceID;
	}

	public String getDeviceName() {
		return deviceName;
	}

}
