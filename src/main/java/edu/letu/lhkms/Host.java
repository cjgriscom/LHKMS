package edu.letu.lhkms;

import static edu.letu.lhkms.Host.HostPlatform.*;

import java.io.File;

public class Host {
	private static final String OS = System.getProperty("os.name").toLowerCase();
	private static final HostPlatform PLATFORM = getHostPlatform();

	public static enum HostPlatform {
		Windows, Linux, MacOSX, Unknown
	}

	public static HostPlatform getHostPlatform() {
		// Determine platform
		if (isWindows()) {
			return Windows;
		} else if (isMac()) {
			return MacOSX;
		} else if (isLinux()) {
			return Linux;
		}
		return Unknown;
	}
	
	public static File getConfigurationDirectory() {
		switch (PLATFORM) {
		case Windows:
			return new File(System.getenv("APPDATA") + "\\" + "LVKMS");
		case MacOSX:
			return new File(System.getProperty("user.home") + "/Library/Application Support/LVKMS");
		case Linux:
			return new File(System.getProperty("user.home") + "/.config/LVKMS");
		default:
			throw ISE();
		}
	}

	private static IllegalStateException ISE() {
		return new IllegalStateException("Unknown platform: " + PLATFORM);
	}


	public static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}

	public static boolean isMac() {
		return (OS.indexOf("mac") >= 0);
	}

	public static boolean isLinux() {
		return (OS.indexOf("linux") >= 0);
	}
}
