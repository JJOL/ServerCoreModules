package me.deathhaven.skywars.commands;

import java.util.ArrayList;
import java.util.List;

import me.deathhaven.skywars.SkyWars;
import me.deathhaven.skywars.controllers.CustomController;
import me.deathhaven.skywars.controllers.GameController;
import me.deathhaven.skywars.controllers.PlayerController;
import me.deathhaven.skywars.game.Game;
import me.deathhaven.skywars.game.GameInfo;
import me.deathhaven.skywars.player.GamePlayer;
import me.deathhaven.skywars.utilities.Messaging;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandPermissions("skywars.command.vote")
@CommandDescription("Lets you Join a Game as a Spectator")
public class SpectateCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		
		//sender.sendMessage("This is the Spectate command that will open spectate options");
		
		if(args.length < 2) {
			sender.sendMessage("Usage: /sw spectate <#gameid|player|toggle>");
			return true;
		}
		sender.sendMessage("ARGS:" + args.length);
		
		List<Game> games = (ArrayList<Game>)GameController.get().getAll();
		if(games.size()==0) {
			sender.sendMessage("§c§lNo Active Games!");
			return true;
		}
		Game game;
		GameInfo info;
		Player player = (Player)sender;
		if (CustomController.checkValidInt(args[1])) {
			
			int gId = Integer.parseInt(args[1]);
			
			info = CustomController.getAllGameInfo(gId);
			
			if(gId >= 0 && gId < games.size()) {	
				
			}
		} else if (SkyWars.get().getServer().getPlayer(args[1]) != null) {
			Player target = SkyWars.get().getServer().getPlayer(args[1]);
			info = CustomController.getGameInfo(PlayerController.get().get(target) );
			if(info.gId == -1) {
				sender.sendMessage("Player ["+target.getName()+"] is in lobby!");
				return true;
			}
		
		} else if(args[1].equalsIgnoreCase("toggle") && player.hasPermission("skywars.command.spectate.toggle")) {
			PlayerController.get().get(player).toggleSpectatingAccess();
			sender.sendMessage("Command Access Toggled!");
			return true;
		} else {
			sender.sendMessage("§4§l[ERROR] §f§lNot Valid Identifier ("
					+ args[2] + ")");
			return true;
		}
		game = games.get(info.gId);
		game.onSpectatorJoin(PlayerController.get().get(player));
		sender.sendMessage(new Messaging.MessageFormatter().withPrefix()
				.format("Spectating Game... #"+info.gId));
		
		
		
		return true;
	}
	
	

}
