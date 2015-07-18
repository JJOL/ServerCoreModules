package me.jjol.servercmds;

import java.util.HashSet;
import java.util.Set;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

public class IntrusorCommander extends Command implements TabExecutor{

	private Main core;
	
	public IntrusorCommander(Main core) {
		super("fakeplayer");
		this.core = core;
		
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		((ProxiedPlayer)sender).hasPermission("bungeecmds.dev.debbug");
		ProxiedPlayer target = core.getProxy().getPlayer(args[0]);
		if(target == null) {
			sender.sendMessage(new TextComponent("Player "+args[0]+"doesnt exist or is not online!"));
			return;
		}			
		String msg;
		StringBuilder builder = new StringBuilder();
		for(int i= 1; i < args.length; i++) {
			builder.append(args[i]);
			builder.append(" ");
		}
		msg = new String(builder);
		target.chat(msg);
	}

	@Override
	public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
		
		Set<String> matches = new HashSet<>();
		
		String search = args[0].toLowerCase();
		for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
			if(player.getName().toLowerCase().startsWith( search )) {
				matches.add(player.getName());
			}
		}
		return matches;
	}

}
