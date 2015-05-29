package me.deathhaven.skywars.commands;

import me.deathhaven.skywars.config.PluginConfig;
import me.deathhaven.skywars.controllers.PlayerController;
import me.deathhaven.skywars.game.GameState;
import me.deathhaven.skywars.player.GamePlayer;
import me.deathhaven.skywars.utilities.Messaging;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandDescription("Starts a SkyWars game")
@CommandPermissions("skywars.command.start")
public class StartCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        GamePlayer gamePlayer = PlayerController.get().get((Player) sender);

        if (!gamePlayer.isPlaying()) {
            sender.sendMessage(new Messaging.MessageFormatter().format("error.not-in-game"));
        } else if (gamePlayer.getGame().getState() != GameState.WAITING) {
            sender.sendMessage(new Messaging.MessageFormatter().format("error.already-started"));
        } else if (gamePlayer.getGame().getPlayerCount() < 2) {
            sender.sendMessage(new Messaging.MessageFormatter().format("error.not-enough-players"));
        } else if (PluginConfig.buildSchematic() && !gamePlayer.getGame().isBuilt()) {
            sender.sendMessage(new Messaging.MessageFormatter().format("error.arena-under-construction"));
        } else {
            gamePlayer.getGame().onGameStart();
        }

        return true;
    }
}
