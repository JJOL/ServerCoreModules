package me.deathhaven.skywars.commands;

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

@CommandDescription("You can use this command to get nodes of information")
@CommandPermissions("skywars.command.getinfo")
public class InfoCommand implements CommandExecutor {

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

					sender.sendMessage("Active Games: ");
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

					if (CustomController.checkValidInt(args[2])) {
						gId = Integer.valueOf(args[2]);

						if (gId < 0 || gId > activeGames.size()) {
							sender.sendMessage( new Messaging.MessageFormatter()
								.setVariable("gameid", args[2])
								.format("error.not-valid-gameid")
							  );
							return true;
						}
						info = CustomController.getAllGameInfo(gId);

					} else if (SkyWars.get().getServer().getPlayer(args[2]) != null) {
						Player target = SkyWars.get().getServer().getPlayer(args[2]);
						info = CustomController.getAllGameInfo(PlayerController.get().get(target));
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


	

}
