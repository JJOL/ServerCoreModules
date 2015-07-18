package me.jjol.servercmds;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import net.md_5.bungee.api.plugin.Plugin;

import org.yaml.snakeyaml.Yaml;

public class Main extends Plugin {
	 
	private static final String fileKey = "tpmsg";
	
	@Override
	public void onEnable() {
		getLogger().info("[Simple-Server-Commands] Enabled!");
		
		
		// SAVING Config.yml
		{
			File config = new File(getDataFolder()+"config.yml");
			try {
				if(config.createNewFile()) {
					FileWriter fw = new FileWriter(config.getAbsoluteFile());
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(fileKey+": Connecting to Server {name}");
					bw.close();
				}
			} catch (IOException e) {
				printError("Error Encountered Saving the Default config.yml File!");
			}
		}
		
		// LOADING Config.yml
		String message = null;
		{
			File config = new File(getDataFolder()+"config.yml");
			InputStream stream;
			Yaml yaml = new Yaml();
			try {
				stream = new FileInputStream(config);
				@SuppressWarnings("unchecked")
				Map<String, String> data = (Map<String, String>)yaml.load(stream);
				message = data.get("tpmsg");
				
			} catch (FileNotFoundException e) {
				printError("Error Encountered Loading Content from config.yml File!");
				e.printStackTrace();
			}
			
			
			
		} 
		
		// Settingp up Commands
		
		getProxy().getPluginManager().registerCommand(this, new ServerCommand("hub", this, message));
		getProxy().getPluginManager().registerCommand(this, new ServerCommand("survival", this, message));
		getProxy().getPluginManager().registerCommand(this, new ServerCommand("skyblock", this, message));
		getProxy().getPluginManager().registerCommand(this, new ServerCommand("skywars", this, message));
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
