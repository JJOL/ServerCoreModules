package me.deathhaven.skywars.commands;

import me.deathhaven.skywars.controllers.IconMenuController;
import me.deathhaven.skywars.controllers.KitController;
import me.deathhaven.skywars.controllers.PlayerController;
import me.deathhaven.skywars.game.GameState;
import me.deathhaven.skywars.player.GamePlayer;
import me.deathhaven.skywars.utilities.Messaging;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandDescription("Allows a player to pick kits")
public class KitCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        GamePlayer gamePlayer = PlayerController.get().get(player);

        if (!gamePlayer.isPlaying()) {
            sender.sendMessage(new Messaging.MessageFormatter().format("error.not-in-game"));
        } else if (gamePlayer.hasChosenKit()) {
            sender.sendMessage(new Messaging.MessageFormatter().format("error.already-has-kit"));
        } else if (gamePlayer.getGame().getState() != GameState.WAITING) {
            sender.sendMessage(new Messaging.MessageFormatter().format("error.can-not-pick-kit"));
        } else if (!IconMenuController.get().has(player)) {
            KitController.get().openKitMenu(gamePlayer);
        }

        return true;
    }
}