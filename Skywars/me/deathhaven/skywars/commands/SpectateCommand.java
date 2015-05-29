package me.deathhaven.skywars.commands;

import java.util.ArrayList;
import java.util.List;

import me.deathhaven.skywars.controllers.GameController;
import me.deathhaven.skywars.controllers.PlayerController;
import me.deathhaven.skywars.game.Game;
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
		
		
		
		
		if (isInt(args[1])) {
			
			int gId = Integer.parseInt(args[1]);
			
			List<Game> games = (ArrayList<Game>)GameController.get().getAll();
			
			if(gId >= 0 && gId < games.size()) {
				Game game = games.get(gId);
				
				game.onSpectatorJoin(PlayerController.get().get((Player)sender));
				sender.sendMessage(new Messaging.MessageFormatter().withPrefix()
						.format("Joining Game... #"+gId));
				
			}
		}
		
		return true;
	}
	
	private boolean isInt(String s) {
		try {
			int i = Integer.parseInt(s);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

}
