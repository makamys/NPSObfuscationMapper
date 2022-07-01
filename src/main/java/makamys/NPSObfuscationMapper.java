package makamys;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.graalvm.visualvm.lib.jfluid.results.cpu.CPUResultsSnapshot;
import org.graalvm.visualvm.lib.profiler.LoadedSnapshot;

class NPSObfuscationMapper {
	
	public static void main(String[] args){
		if(args.length != 2) {
			System.out.println("Usage: java -jar " + getJarName() + " MCPDIR NPS\n\nRemaps the obfuscated method names in NPS to deobfuscated ones using the mappings in MCPDIR (which should contain methods.csv).\nThe output file will have the same name as NPS, with the '-deobf' suffix added.");
			System.exit(1);
		}
		File mcpDir = new File(args[0]);
		File npsFile = new File(args[1]);
		remapFile(loadMCPMap(new File(mcpDir, "methods.csv")), npsFile);
	}
	
	private static Map<String, String> loadMCPMap(File file){
		HashMap<String, String> map = new HashMap<>();
		try {
			Files.lines(file.toPath(), StandardCharsets.UTF_8).skip(1).forEach(s -> {
				String[] fields = s.split(",");
				map.put(fields[0], fields[1]);
			});
		} catch(Exception e) {
			System.err.println("Failed to load MCP CSV file " + file);
			e.printStackTrace();
		}
		return map;
	}
	
	static void remapFile(Map<String, String> deobfMap, File npsFile) {
		try {
			LoadedSnapshot ss = LoadedSnapshot.loadSnapshot(new DataInputStream(new BufferedInputStream(new FileInputStream(npsFile))));
			
			CPUResultsSnapshot crs = (CPUResultsSnapshot)ss.getSnapshot();
			String[] methodNames = crs.getInstrMethodNames();
			for(int i = 0; i < methodNames.length; i++) {
				String deobfName = deobfMap.get(methodNames[i]);
				if(deobfName != null) {
					methodNames[i] = deobfName;
				}
			}
			File outFile = concatenateBeforeExtension(npsFile, "-deobf");
			try(DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outFile)))){
				ss.save(dos);	
			}
		} catch (IOException e) {
			System.err.println("Error opening snapshot " + npsFile);
			e.printStackTrace();
		}
	}
	
	private static File concatenateBeforeExtension(File file, String suffix) {
		String newName = file.getName();
		int commaIdx = newName.lastIndexOf('.');
		newName = commaIdx != -1 ? newName.substring(0, commaIdx) + suffix + newName.substring(commaIdx) : newName + suffix;
		return new File(file.getParentFile(), newName);
	}
	
	private static String getJarName() {
		return new java.io.File(NPSObfuscationMapper.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getName();
	}
}