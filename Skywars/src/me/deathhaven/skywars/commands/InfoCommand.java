package me.deathhaven.skywars.commands;

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
import me.deathhaven.skywars.utilities.Messaging;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

@CommandDescription("You can use this command to get nodes of information")
@CommandPermissions("skywars.command.getinfo")
public class InfoCommand implements CommandExecutor, TabExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if (sender instanceof Player) {

			if (args.length == 1) {
				sender.sendMessage("Usage of the aviable options /sw info ");
				sender.sendMessage(" - games");
				sender.sendMessage(" - game <#id>");
				sender.sendMessage(" - game <player>");
				sender.sendMessage(" - player IMPLEMENT");

				// Open Inventory
			} else {
				// Get Information With Arguments

				if (args[1].equalsIgnoreCase("games")) {

					sender.sendMessage(new Messaging.MessageFormatter()
										.setVariable("size", String.valueOf(getGames().size()))
										.format("cmd.active-games"));
					for (int i = 0; i < getGames().size(); i++) {
						sender.sendMessage("Game #" + i);
					}

				} else if (args[1].equalsIgnoreCase("game")) {

					List<Game> activeGames = getGames();
					if(activeGames.size()==0) {
						sender.sendMessage(new Messaging.MessageFormatter().
								format("error.no-active-games"));
						return true;
					}

					if (args.length != 3) {
						sender.sendMessage("Usage: /sw info game <id> <players|map>");
						return true;
					}

					GameInfo info;

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
						info = DataController.getAllGameInfo(gId);

					} else if (SkyWars.get().getServer().getPlayer(args[2]) != null) {
						Player target = SkyWars.get().getServer().getPlayer(args[2]);
						info = DataController.getAllGameInfo(PlayerController.get().get(target));
						if(info.gId == -1) {
							sender.sendMessage(new Messaging.MessageFormatter()
												.format("cmd.player-in-lobby"));
							return true;
						}
					} else {
						sender.sendMessage(new Messaging.MessageFormatter()
							.setVariable("player", args[2])
							.format("error.not-valid-playerid")
						  );
						return true;
					}

					List<GamePlayer> gPlayers = info.players;
					List<GamePlayer> gSpectators = info.spectators;

					sender.sendMessage("§3Game #" + info.gId + "  §3- Information:");
					sender.sendMessage("§4§lState:         §f"
							+ info.state.toString());
					sender.sendMessage("§4§lMap  :         §f" + info.mapName);
					sender.sendMessage("§4§lPlayer Count:  §f"
							+ info.playerCount);

					for (GamePlayer player : gPlayers) {

						sender.sendMessage("   §f§l--   §6"
								+ player.getBukkitPlayer().getName());

					}

					sender.sendMessage("§4§lSpectator Count:  §f"
							+ info.spectatorCount);
					for (GamePlayer player : gSpectators) {

						sender.sendMessage("   §f§l--   §6"
								+ player.getBukkitPlayer().getName());

					}

				} else {

				}

			}

		}

		return true;
	}

	private List<Game> getGames() {
		return (List<Game>) GameController.get().getAll();
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command arg1,
			String arg2, String[] args) {
		
		Set<String> matches = new HashSet<>();
		
		String search = args[1].toLowerCase();
		for (String name : new String[]{"games", "game", "player"}) {
			if(name.startsWith( search )) {
				matches.add(name);
			}
		}
		return Lists.newArrayList(matches);
	}


	

}
