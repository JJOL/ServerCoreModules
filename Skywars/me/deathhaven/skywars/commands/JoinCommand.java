package me.deathhaven.skywars.commands;

import me.deathhaven.skywars.config.PluginConfig;
import me.deathhaven.skywars.controllers.GameController;
import me.deathhaven.skywars.controllers.PlayerController;
import me.deathhaven.skywars.controllers.SchematicController;
import me.deathhaven.skywars.game.Game;
import me.deathhaven.skywars.player.GamePlayer;
import me.deathhaven.skywars.utilities.Messaging;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandDescription("Joins a Game.")
public class JoinCommand implements CommandExecutor{
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
        Player player = (Player)sender;
		GamePlayer gamePlayer = PlayerController.get().get(player);		
		
        if (!gamePlayer.isPlaying() && player.getLocation().getWorld().equals(PluginConfig.getLobbySpawn().getWorld())) {
        	if (SchematicController.get().size() == 0) {
                player.sendMessage(new Messaging.MessageFormatter().format("error.no-schematics"));
                return true;
            }
        	Game game = GameController.get().findEmpty();
            game.onPlayerJoin(gamePlayer);
            
        } else {
        	player.sendMessage(new Messaging.MessageFormatter().format("error.in-game"));
        }
        return true;
    }
}
