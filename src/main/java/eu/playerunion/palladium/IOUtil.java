package eu.playerunion.palladium;

import java.io.File;

public class IOUtil {
	
	private static File workDir;
	
	public static OS getPlatform() {
		String osName = System.getProperty("os.name").toLowerCase();
		
		if(osName.contains("windows"))
			return OS.Windows;
		
		if(osName.contains("linux"))
			return OS.Linux;
		
		if(osName.contains("mac"))
			return OS.MacOS;
		
		return osName.contains("unix") ? OS.Linux : OS.Unknown;
	}
	
	public static File getWorkingDirectory() {
	    if (workDir == null)
	    	workDir = getWorkingDirectory("playerunion");

	    return workDir;
	}
	
	public static File getWorkingDirectory(String appName) {
		String userHome = System.getProperty("user.home", ".");
		File workingDirectory = null;
		
		switch(getPlatform()) {
			case Linux:
				workingDirectory = new File(userHome, '.' + appName + '/');
			
				break;
	    
			case Windows:
				String appData = System.getenv("APPDATA");
			
				if(appData != null)
					workingDirectory = new File(appData, "." + appName + '/');
				else
					workingDirectory = new File(userHome, '.' + appName + '/');
			
				break;
			
			case MacOS:
				workingDirectory = new File(userHome, "Library/Application Support/" + appName);
			
				break;
		}
		
		if (!workingDirectory.exists() && !workingDirectory.mkdirs()) 
	         throw new RuntimeException("[ HIBA ] Nem sikerült létrehozni a játék gyökérkönyvtárát: " + workingDirectory);
		
		System.out.println("[ DEBUG ] A kliens mappája: " + workingDirectory);
		
		return workingDirectory;
	}

}
