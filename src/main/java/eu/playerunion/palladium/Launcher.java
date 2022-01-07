package eu.playerunion.palladium;

import java.io.File;
import java.nio.file.Files;

import org.json.JSONObject;

public class Launcher {
	
	/**
	 * Csak teszt erejéig
	 * @param args
	 * @throws Exception
	 */
	
	public static void main(String[] args) throws Exception {
		File versionFile = new File(IOUtil.getWorkingDirectory(), "version.json");
		StringBuilder rawJsonData = new StringBuilder("");
		JSONObject version = new JSONObject();
		
		Files.lines(versionFile.toPath()).forEach(line -> rawJsonData.append(line));
		
		version = new JSONObject(rawJsonData.toString()).getJSONObject("versions");
		
		new PalladiumWrapper("45857243895723895732895", "szviktor", "0000000000000000", "1.12.2-Forge", 1024, version).startClient();
	}

}
