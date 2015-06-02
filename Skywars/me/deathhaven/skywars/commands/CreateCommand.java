package me.deathhaven.skywars.commands;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.deathhaven.skywars.controllers.CustomController;
import me.deathhaven.skywars.controllers.GameController;
import me.deathhaven.skywars.controllers.SchematicController;
import me.deathhaven.skywars.game.GameConfig;
import me.deathhaven.skywars.utilities.Messaging;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import com.google.common.collect.Lists;

@CommandDescription("Lets you create personalized Games")
@CommandPermissions("skywars.command.create")
public class CreateCommand implements CommandExecutor, TabExecutor {

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
			sender.sendMessage("Usage /sw create");
			sender.sendMessage(" - map mapname");
			return true;
		}
		
		String mapname = args[2];
		
		
		
		
		if(CustomController.validMap(mapname)) {
			GameConfig config = new GameConfig(
										"CustomGame", 
										SchematicController.get().getArena(mapname)
									);
			
			GameController.get().createSpecial(config);
			
			sender.sendMessage(new Messaging.MessageFormatter()
									.setVariable("name", mapname)
									.format("cmd.create-game")
								);
			
			
		} else {
			sender.sendMessage(new Messaging.MessageFormatter()
									.setVariable("name", mapname)
									.format("error.no-existant-map")
								);
		}
		
		return true;
	}






	@Override
	public List<String> onTabComplete(CommandSender sender, Command arg1,
			String arg2, String[] args) {
		
		Set<String> matches = new HashSet<>();
		if(args.length <= 2) {
			
			
			String search = args[1].toLowerCase();
			for (String name : new String[]{"map"}) {
				if(name.startsWith( search )) {
					matches.add(name);
				}
			}
			return Lists.newArrayList(matches);
		}
		
		// Return All Maps
		
		String search = args[2];
		
		for (String name : Lists.newArrayList(SchematicController.get().getAllMapNames())) {
			if(name.startsWith( search )) {
				matches.add(name);
			}
		}
		return Lists.newArrayList(matches);
		
		
		

	}
	
}
