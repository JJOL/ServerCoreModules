package me.deathhaven.skywars.commands;

import me.deathhaven.skywars.SkyWars;
import me.deathhaven.skywars.controllers.ChestController;
import me.deathhaven.skywars.controllers.KitController;
import me.deathhaven.skywars.utilities.Messaging;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@CommandDescription("Reloads the chests, kits and the plugin.yml")
@CommandPermissions("skywars.command.reload")
public class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ChestController.get().load();
        KitController.get().load();
        SkyWars.get().reloadConfig();
        new Messaging(SkyWars.get());

        sender.sendMessage(new Messaging.MessageFormatter().format("success.reload"));
        return true;
    }
}
