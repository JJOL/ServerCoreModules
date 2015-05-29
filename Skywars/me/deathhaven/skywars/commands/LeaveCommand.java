package me.deathhaven.skywars.commands;

import me.deathhaven.skywars.controllers.PlayerController;
import me.deathhaven.skywars.player.GamePlayer;
import me.deathhaven.skywars.utilities.Messaging;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandDescription("Leaves a SkyWars game")
public class LeaveCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        GamePlayer gamePlayer = PlayerController.get().get((Player) sender);

        if (!gamePlayer.isPlaying()) {
            sender.sendMessage(new Messaging.MessageFormatter().format("error.not-in-game"));
        } else {
            gamePlayer.getGame().onPlayerLeave(gamePlayer);
        }

        return true;
    }
}
