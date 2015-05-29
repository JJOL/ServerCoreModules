package me.deathhaven.skywars.commands;

import me.deathhaven.skywars.config.PluginConfig;
import me.deathhaven.skywars.utilities.Messaging;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandDescription("Set the lobby")
@CommandPermissions("skywars.command.setlobby")
public class SetLobbyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        PluginConfig.setLobbySpawn(((Player) sender).getLocation());
        sender.sendMessage(new Messaging.MessageFormatter().format("success.lobby-set"));
        return true;
    }
}
