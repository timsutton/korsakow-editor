package org.korsakow.ide.util;

import java.util.Arrays;
import java.util.List;

public class Platform
{
	public static enum Arch
	{
		X86("x86", Arrays.asList("x86","x86_64","i386")),
		POWERPC("PowerPC", Arrays.asList("PowerPC", "ppc")),
		UNKNOWN("unknown", Arrays.asList(""))
		;
		
		public static Arch findArch(String name)
		{
			for (Arch arch : values())
				if (arch.isArch(name))
					return arch;
			return Arch.UNKNOWN;
		}
		
		private final String canonicalName;
		private final List<String> aliases;
		private Arch(String canonicalName, List<String> aliases)
		{
			this.canonicalName = canonicalName;
			this.aliases = aliases;
		}
		public String getCanonicalName()
		{
			return canonicalName;
		}
		public boolean isArch(String name)
		{
			if (canonicalName.equalsIgnoreCase(name))
				return true;
			for (String alias : aliases)
				if (alias.equalsIgnoreCase(name))
					return true;
			return false;
		}
	}
	public static enum OS
	{
		MAC("mac"),
		WIN("windows"),
		NIX("linux"),
		UNKNOWN("unknown")
		;
		
		private final String canonicalName;
		private OS(String canonicalName)
		{
			this.canonicalName = canonicalName;
		}
		public String getCanonicalName()
		{
			return canonicalName;
		}
		public String getVersion()
		{
			return System.getProperty("os.version");
		}
	}
	public static OS getOS()
	{
		if (isMacOS())
			return OS.MAC;
		if (isWindowsOS())
			return OS.WIN;
		if (isLinuxOS())
			return OS.NIX;
		return OS.UNKNOWN;
	}
	public static String getArchString() {
		return System.getProperty("os.arch");
	}
	public static String getOSString() {
		return System.getProperty("os.name") + " " + System.getProperty("os.version");
	}
	public static Arch getArch()
	{
		return Arch.findArch(System.getProperty("os.arch"));
	}
	public static boolean isLinuxOS()
	{
		final String osName =  System.getProperty("os.name", "unknown").toLowerCase();
		return osName.startsWith("linux");
	}
	public static boolean isWindowsOS()
	{
		final String osName =  System.getProperty("os.name", "unknown").toLowerCase();
		return osName.startsWith("windows");
	}
	public static boolean isMacOS()
	{
		final String osName =  System.getProperty("os.name", "unknown").toLowerCase();
		return osName.startsWith("mac") || osName.contains("darwin");
	}
}
