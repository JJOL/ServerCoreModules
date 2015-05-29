package me.deathhaven.skywars.commands;

import java.util.List;

import me.deathhaven.skywars.controllers.GameController;
import me.deathhaven.skywars.game.Game;
import me.deathhaven.skywars.player.GamePlayer;
import me.deathhaven.skywars.utilities.Messaging;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


@CommandDescription("You can use this command to get nodes of information")
@CommandPermissions("skywars.command.getinfo")
public class InfoCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		
		if (sender instanceof Player) {
			sender.sendMessage(new Messaging.MessageFormatter().withPrefix()
			.format("&lComing Soon!  &3V. 0.1"));
		}
		/*if(sender instanceof Player) {
			Player p = (Player)sender;
			if (args.length==0) {
				sender.sendMessage("Usage of the aviable options /sw info ");
				sender.sendMessage(" - games");
				sender.sendMessage(" - <gamename> players");
				sender.sendMessage(" - <gamename> map");
				
				// Open Inventory
			} else {
				// Get Information With Arguments
				
				if(args[0].equalsIgnoreCase("list")) {
					
					sender.sendMessage("Active Games: ");
					for(int i=0; i < getGames().size(); i++) {
						sender.sendMessage("Game #" + i);
					}
					
				} else if (args[0].equalsIgnoreCase("game")) {
					if (args.length==2) {
						
					}
					/*if(args.length==2 && checkValidInt(args[1])) {
						int gId = Integer.parseInt(args[1]);
						Game game = ((List<Game>)GameController.get().getAll()).get(gId);
						sender.sendMessage("Game #"+gId+"  - Information:");
						sender.sendMessage("Map: " );
						sender.sendMessage("State: ");
						sender.sendMessage("Player Count: ");
						sender.sendMessage("Vote Count: ");
						String pNames = "";
						for (game.getPlayers())
						sender.sendMessage("Players: [");
					} else {
						
					}
				} else {
					
				}
				
				
			}
			
			sender.sendMessage("This is the Information Command where you get Information from The Current Games");
			
			// Create the Information Menu
			//IconMenu menu = new IconMenu("InfoMenu", 3 );
			/*IconMenu menu = new IconMenu("", 3, new IconMenu.OptionClickEventHandler() {
				
				@Override
				public void onOptionClick(OptionClickEvent event) {
					
				}
			}, SkyWars.get());
			
			
		}*/
		
		
		return true;
	}
	
	private List<Game> getGames() {
		return (List<Game>)GameController.get().getAll(); 
	}
	
	private String[] getPlayers(Game g) {
		String[] players = new String[g.getPlayers().size()] ;
		
		for(int i=0; i < g.getPlayers().size(); i++) {
			players[i] = ((List<GamePlayer>)g.getPlayers()).get(i).getName();
		}
		
		return players;
	}
	
	private final boolean checkValidInt(String s) {
		try {
			Integer.parseInt(s);
		} catch(Exception e) {
			return false;
		}
		return true;
	}

}
