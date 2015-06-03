package me.deathhaven.skywars.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.deathhaven.skywars.SkyWars;
import me.deathhaven.skywars.controllers.DataController;
import me.deathhaven.skywars.controllers.GameController;
import me.deathhaven.skywars.controllers.PlayerController;
import me.deathhaven.skywars.game.Game;
import me.deathhaven.skywars.game.GameInfo;
import me.deathhaven.skywars.player.GamePlayer;
import me.deathhaven.skywars.utilities.Debugger;
import me.deathhaven.skywars.utilities.Messaging;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

@CommandPermissions("skywars.command.spectate")
@CommandDescription("Lets you Join a Game as a Spectator")
public class SpectateCommand implements CommandExecutor, TabExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		
		/*
		 * Number of Arguments = 2
		 * args[0] = spectate
		 * args[1] = argument
		 * */
		

		
		if(args.length < 2) {
			sender.sendMessage("Usage: /sw spectate <#gameid|player|toggle>");
			return true;
		}
		
		List<Game> games = (ArrayList<Game>)GameController.get().getAll();
		if(games.size()==0) {
			sender.sendMessage(new Messaging.MessageFormatter().
					format("error.no-active-games"));
			return true;
		}
		
		Game game;
		GameInfo info;
		Player player = (Player)sender;
		
		if (DataController.checkValidInt(args[1])) {
			
			Debugger.get().sendDebbugMC("[SpectateCMD] Argument is A Number : "+args[1]);
			
			int gId = Integer.parseInt(args[1]);
			
			if(gId < 0 || gId > games.size()-1) {
				Debugger.get().sendDebbugMC("[SpectateCMD] ID didnt pass the test");
				sender.sendMessage(new Messaging.MessageFormatter()
									.setVariable("gameid", args[1])
									.format("error.not-valid-gameid"));	
				return true;
			}
			Debugger.get().sendDebbugMC("[SpectateCMD] ID Passed the Test!");
			info = DataController.getGameInfo(gId);
			
		} 
		else if (SkyWars.get().getServer().getPlayer(args[1]) != null) {
			Player target = SkyWars.get().getServer().getPlayer(args[1]);
			info = DataController.getGameInfo(PlayerController.get().get(target) );
			if(info.gId == -1) {
				sender.sendMessage(new Messaging.MessageFormatter()
										.format("cmd.player-in-lobby"));
				return true;
			}
		
		} else if(args[1].equalsIgnoreCase("toggle") && player.hasPermission("skywars.command.spectate.toggle")) {
			
			GamePlayer gPlayer = PlayerController.get().get(player);
			gPlayer.toggleSpectatingAccess();
			if(gPlayer.hasSpectatingAccess()) {
				sender.sendMessage(new Messaging.MessageFormatter()
										.format("cmd.spectate-toggle-on"));
			} else {
				sender.sendMessage(new Messaging.MessageFormatter()
										.format("cmd.spectate-toggle-off"));
			}
			return true;
		} else {
			sender.sendMessage(new Messaging.MessageFormatter()
					.setVariable("player", args[1])
					.format("error.not-valid-playerid"));
			return true;
		}
		game = games.get(info.gId);
		game.onSpectatorJoin(PlayerController.get().get(player));
		sender.sendMessage(new Messaging.MessageFormatter()
							.setVariable("gameid", String.valueOf(info.gId))
							.format("game.spectator-join")
						);
		
		
		
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command arg1,
			String arg2, String[] args) {
		
		Set<String> matches = new HashSet<>();
		
		String search = args[1].toLowerCase();
		for (String name : new String[]{"0", "toggle"}) {
			if(name.startsWith( search )) {
				matches.add(name);
			}
		}
		return Lists.newArrayList(matches);
	}

}
