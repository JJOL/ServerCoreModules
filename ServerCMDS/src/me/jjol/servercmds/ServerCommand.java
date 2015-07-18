package me.jjol.servercmds;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ServerCommand extends Command{

	private final String server;
	private final Main core;
	private final String msg;
	private final static String defaultMsg = "Connecting to... {name}";
	
	
	
	public ServerCommand(String server, Main core, String msg) {
		super(server);
		this.server = server;
		this.core = core;
		if(msg==null || msg.isEmpty()) {
			this.msg = defaultMsg;
		} else {
			this.msg = msg;
		}
		
	}
	
	public void execute(CommandSender sender, String[] strings) {
		
		sender.sendMessage(new ComponentBuilder(msg.replaceAll("{name}", server)).color(ChatColor.AQUA).create());
		ProxiedPlayer p = (ProxiedPlayer)sender;
		p.connect(core.getProxy().getServerInfo(server));		
		
	}
	
	
	
	
}
