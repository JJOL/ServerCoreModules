package me.jjol.servercmds;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.md_5.bungee.api.plugin.Plugin;

import org.yaml.snakeyaml.Yaml;

public class Main extends Plugin {
	 
	private static final String msgKey = "tpmsg";       // YAML Node for the Teleport Message String
	private static final String serversKey = "servers"; // YAML Node for the array of Server Names
	
	/* For Quick Debugging Purposed ONLY!!
	public static void main(String[] args) {
		{
			File config = new File(System.getProperty("user.dir")+"/config.yml");
			try {
				if(config.createNewFile()) {
					FileWriter fw = new FileWriter(config.getAbsoluteFile());
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(msgKey+": Connecting to Server {name}");
					bw.write("\n");
					bw.write(serversKey+": [hub]");
					bw.close();
					System.out.println("File Written! in path" + System.getProperty("user.dir"));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
		// LOADING Config.yml
		String message = null;
		List<String> serverList = new ArrayList<String>();
		
		{
			File config = new File(System.getProperty("user.dir")+"/config.yml");
			InputStream stream;
			
			Yaml yaml = new Yaml();
			try {
				stream = new FileInputStream(config);
				@SuppressWarnings("unchecked")
				Map<String, Object> data = (Map<String, Object>)yaml.load(stream);
				message = data.get(msgKey).toString();
				serverList = (List<String>)data.get(serversKey);
				
			} catch (FileNotFoundException e) {				
				e.printStackTrace();
			}		
			
		} 
		System.out.println("TPMSG: "+message);
		System.out.println("Servers Registered: \n");
		for(String name : serverList) {
			System.out.println("  - "+name);
		}
	}*/
	
	@SuppressWarnings("unchecked")
	@Override
	public void onEnable() {
		getLogger().info("[Simple-Server-Commands] Enabled!");
		
		
		// SAVING Config.yml
		{
			File config = new File(getDataFolder()+"/config.yml");
			try {
				if(config.createNewFile()) {
					FileWriter fw = new FileWriter(config.getAbsoluteFile());
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(msgKey+": Connecting to Server {name}");
					bw.write("\n");
					bw.write(serversKey+": [hub]");
					bw.close();
				}
			} catch (IOException e) {
				printError("Error Encountered Saving the Default config.yml File!");
			}
		}
		
		// LOADING Config.yml
		String message = null;
		List<String> serverList = new ArrayList<String>();
		
		{
			File config = new File(getDataFolder()+"/config.yml");
			InputStream stream;
			
			Yaml yaml = new Yaml();
			try {
				stream = new FileInputStream(config);
				Map<String, Object> data = (Map<String, Object>)yaml.load(stream);
				message = data.get(msgKey).toString();
				serverList = (List<String>)data.get(serversKey);
				
			} catch (FileNotFoundException e) {
				printError("Error Encountered Loading Content from config.yml File!");
				e.printStackTrace();
			}
			
			
			
		} 
		
		// Settingp up Commands
		for(String server : serverList) {
			getProxy().getPluginManager().registerCommand(this, new ServerCommand(server, this, message));
		}
		
		/* Old Setup   In Config.yml Add "servers: [hub,survival,skyblock,skywars]"   to load the same server commands
		getProxy().getPluginManager().registerCommand(this, new ServerCommand("hub", this, message));
		getProxy().getPluginManager().registerCommand(this, new ServerCommand("survival", this, message));
		getProxy().getPluginManager().registerCommand(this, new ServerCommand("skyblock", this, message));
		getProxy().getPluginManager().registerCommand(this, new ServerCommand("skywars", this, message));
		*/
		getProxy().getPluginManager().registerCommand(this, new IntrusorCommander(this));
		
	}
	
	public void printError(String error) {
		getLogger().info("[ServerCMDS] ----------------------------------------------------------------");
		getLogger().info("[ServerCMDS] "+error);
		getLogger().info("[ServerCMDS] ----------------------------------------------------------------");
	}
	
	@Override
	public void onDisable() {
		getLogger().info("[Simple-Server-Commands] Disabled!");
	}

}
