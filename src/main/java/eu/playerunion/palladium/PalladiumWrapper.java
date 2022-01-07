package eu.playerunion.palladium;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.rmi.CORBA.Util;

import org.json.JSONObject;

public class PalladiumWrapper {
	
	private String uuid;
	private String username;
	private String accessToken;
	private String version;
	private int maxMemory;
	private JSONObject launchConfiguration;
	
	private String workDir = IOUtil.getWorkingDirectory().toString();
	
	/**
	 * Egy új PalladiumWrapper meghívása.
	 * 
	 * @param uuid a játékos egyedi azonosítója.
	 * @param username a játékos neve.
	 * @param accessToken a játékos hozzáférési kulcsa.
	 * @param maxMemory a játéknak megadott maximális memória.
	 * @param launchConfiguration az elindítás konfigurácója.
	 */
	
	public PalladiumWrapper(String uuid, String username, String accessToken, String version, int maxMemory, JSONObject launchConfiguration) {
		this.uuid = uuid;
		this.username = username;
		this.accessToken = accessToken;
		this.version = version;
		this.maxMemory = maxMemory;
		this.launchConfiguration = launchConfiguration;
	}
	
	/**
	 * Kliens elindítása a konstruktorban már megadott adatok segítségével.
	 */
	
	public void startClient() {
		System.out.println("[ DEBUG ] A kliens indítása elkezdődött...");
		
		ArrayList<String> params = new ArrayList<String>();
		
		String cp = IOUtil.getPlatform() == OS.Windows ? ";" : ":";
		String java = IOUtil.getPlatform() == OS.Windows ? "javaw" : "java";
		String libs = "";
		String libNatives = "";
		
		JSONObject step1 = this.launchConfiguration.getJSONObject(this.version);
		JSONObject step2 = step1.getJSONObject("libraries");
		JSONObject step3 = step1.getJSONObject("natives").getJSONObject(IOUtil.getPlatform().name().toLowerCase());
		
		String assetIndex = step1.getString("assetIndex");
		String version = step1.getString("version");
		String versionType = step1.getString("versionType");
		String mainClass = step1.getString("mainClass");
		String tweakClass = step1.getString("tweakClass");
		String nativesDir = step1.getString("nativesDir").replaceAll("%gameDir%", this.workDir.toString()).replace('/', File.separatorChar);
		String logConfiguration = step1.getString("logConfiguration").replaceAll("%gameDir%", this.workDir.toString()).replace('/', File.separatorChar);
		String clientJar = step1.getJSONObject("clientJar").getString("file").replaceAll("%gameDir%", this.workDir.toString()).replace('/', File.separatorChar);
		
		Iterator<String> libIterator = step2.keys();
		Iterator<String> nativesIterator = step3.keys();
		
		while(libIterator.hasNext())
			libs += this.workDir + File.separator + "libraries" + File.separator + libIterator.next() + cp;
		
		while(nativesIterator.hasNext())
			libNatives += this.workDir + File.separator + "platform_natives" + File.separator + nativesIterator.next() + cp;
		
		libs += libNatives + clientJar;
		
		params.add(java);
		params.add("-Dfile.encoding=UTF-8");
		params.add("Djava.library.path=" + nativesDir);
		params.add("-Dlog4j.configurationFile=" + logConfiguration);
		params.add("-Dfml.ignoreInvalidMinecraftCertificates=true");
		params.addAll(Arrays.asList("-XX:+UnlockExperimentalVMOptions", "-XX:+UseG1GC", "-XX:G1NewSizePercent=20", "-XX:G1ReservePercent=20", "-XX:MaxGCPauseMillis=50", "-XX:G1HeapRegionSize=16M"));
		
		if(IOUtil.getPlatform() == OS.Windows)
			params.add("-XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump");
		
		params.add("-cp");
		params.add(libs);
		params.add(mainClass);
		
		params.add("-assetsIndex");
		params.add(assetIndex);
		params.add("--version");
		params.add(version);
		params.add("--username");
		params.add(this.username);
		params.add("--accessToken");
		params.add(this.accessToken);
		params.add("--tweakClass");
		params.add(tweakClass);
		params.add("--versionType");
		params.add(versionType);
		params.add("--userType");
		params.add("mojang");
		
		System.out.println("[ DEBUG ] START!");
		
		// LAUNCH;
	}

}
