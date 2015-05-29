package me.deathhaven.skywars.commands;

import me.deathhaven.skywars.controllers.PlayerController;
import me.deathhaven.skywars.game.GameState;
import me.deathhaven.skywars.player.GamePlayer;
import me.deathhaven.skywars.utilities.Messaging;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandDescription("Votes for a game to start faster.")
@CommandPermissions("skywars.command.vote")
public class VoteCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if (sender instanceof Player) {
			Player p = (Player) sender;
			GamePlayer gp = PlayerController.get().get(p);
			if (gp.isPlaying()) {
				if (gp.getGame().getState() == GameState.WAITING && !(gp.getGame().hasReachedMinimumPlayers() || gp.getGame().canStart())) {
					if (gp.getGame().addVote(p.getName())) {
						p.sendMessage(new Messaging.MessageFormatter().format(
										"game.voted"));
					} else {
						p.sendMessage(new Messaging.MessageFormatter().format(
										"error.already-voted"));
					}
				} else {
					p.sendMessage(new Messaging.MessageFormatter().format("error.no-valid-vote"));
				}
			} else {
				p.sendMessage(new Messaging.MessageFormatter()
						.format("error.not-in-game"));
			}
		} else {
			sender.sendMessage("Only players can vote!");
		}

		return true;
	}

}
