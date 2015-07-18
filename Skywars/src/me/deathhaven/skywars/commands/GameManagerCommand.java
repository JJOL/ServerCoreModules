package me.deathhaven.skywars.commands;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.deathhaven.skywars.controllers.DataController;
import me.deathhaven.skywars.controllers.GameController;
import me.deathhaven.skywars.game.Game;
import me.deathhaven.skywars.utilities.Messaging;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import com.google.common.collect.Lists;

@CommandDescription("Game Managing Tools")
@CommandPermissions("skywars.command.manager")
public class GameManagerCommand implements CommandExecutor, TabExecutor {

	/*
	 *  sw game new "Name"
	 *  sw game map "MapName"
	 *  sw game push
	 *  sw game end
	 *  
	 *  sw create "MapName"
	 * */
	
	public boolean onCommand(CommandSender sender, Command cmd, String lable,
			String[] args) {
		
		if(args.length < 2) {
			sender.sendMessage("Usage /sw manage unload <gameid>");
			return true;
		}
		
		if(args[1].equalsIgnoreCase("unload")) {
			
			List<Game> activeGames = DataController.getGames();
			int gId;

			if (DataController.checkValidInt(args[2])) {
				gId = Integer.valueOf(args[2]);

				if (gId < 0 || gId > activeGames.size()-1) {
					sender.sendMessage( new Messaging.MessageFormatter()
						.setVariable("gameid", args[2])
						.format("error.not-valid-gameid")
					  );
					return true;
				}
				activeGames.get(gId).onGameEnd();
				sender.sendMessage("Game #"+gId+" has been Stopped and Deleted!");
			}
			else if(args[2].equalsIgnoreCase("all")) {
				GameController.get().shutdown();
				sender.sendMessage("§3All Games Have Been Stopped and Deleted!");
			} else {
				sender.sendMessage("&cParameter "+args[2] + " not valid!");
			}
			
		} else {
			sender.sendMessage("Usage /sw manage unload <gameid>");
		}
		
		
		
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command arg1,
			String arg2, String[] args) {
		
		Set<String> matches = new HashSet<>();
		if(args.length <= 2) {
			
			
			String search = args[1].toLowerCase();
			for (String name : new String[]{"unload"}) {
				if(name.startsWith( search )) {
					matches.add(name);
				}
			}
			return Lists.newArrayList(matches);
		}
		
		return null;
		
		
		

	}
	
}



