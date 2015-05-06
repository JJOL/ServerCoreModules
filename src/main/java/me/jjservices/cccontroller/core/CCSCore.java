package me.jjservices.cccontroller.core;

import org.bukkit.plugin.java.JavaPlugin;

public class CCSCore extends JavaPlugin {
	
	public static CCSCore INSTANCE = null; 
	
	@Override
	public void onEnable() {
		CCSCore.INSTANCE = this;
	}
	
	@Override
	public void onDisable() {
	}

}
