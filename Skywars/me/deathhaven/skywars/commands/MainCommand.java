package me.deathhaven.skywars.commands;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.deathhaven.skywars.config.PluginConfig;
import me.deathhaven.skywars.utilities.Messaging;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class MainCommand implements CommandExecutor, TabCompleter{

    private Map<String, CommandExecutor> subCommandMap = Maps.newHashMap();

    public MainCommand() {
        //subCommandMap.put("reload", new ReloadCommand());
        //subCommandMap.put("setlobby", new SetLobbyCommand());
        subCommandMap.put("start", new StartCommand());
        subCommandMap.put("leave", new LeaveCommand());
        //subCommandMap.put("score", new ScoreCommand());
        subCommandMap.put("vote", new VoteCommand());
        subCommandMap.put("join", new JoinCommand());
        //subCommandMap.put("spectate", new SpectateCommand());
        //subCommandMap.put("info", new InfoCommand());
        //subCommandMap.put("create", new CreateCommand());
        //subCommandMap.put("manage", new GameManagerCommand());
        if (!PluginConfig.disableKits()) {
            subCommandMap.put("kit", new KitCommand());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(new Messaging.MessageFormatter().format("error.player-only"));
            return true;
        }

        Player player = (Player) sender;
        if (args.length == 0) {
            printHelp(player, label);
            return true;
        }

        String subCommandName = args[0].toLowerCase();
        if (!subCommandMap.containsKey(subCommandName)) {
            printHelp(player, label);
            return true;
        }

        CommandExecutor subCommand = subCommandMap.get(subCommandName);
        if (!hasPermission(player, subCommand)) {
            player.sendMessage(new Messaging.MessageFormatter().format("error.insufficient-permissions"));
            return true;
        }

        return subCommand.onCommand(sender, command, label, args);
    }

    private boolean hasPermission(Player bukkitPlayer, CommandExecutor cmd) {
        CommandPermissions permissions = cmd.getClass().getAnnotation(CommandPermissions.class);
        if (permissions == null) {
            return true;
        }

        for (String permission : permissions.value()) {
            if (bukkitPlayer.hasPermission(permission)) {
                return true;
            }
        }

        return false;
    }

    private void printHelp(Player bukkitPlayer, String label) {
        bukkitPlayer.sendMessage(new Messaging.MessageFormatter().withPrefix().format("cmd.available-commands"));

        for (Map.Entry<String, CommandExecutor> commandEntry : subCommandMap.entrySet()) {
            if (hasPermission(bukkitPlayer, commandEntry.getValue())) {
                String description = "No description available.";

                CommandDescription cmdDescription = commandEntry.getValue().getClass().getAnnotation(CommandDescription.class);
                if (cmdDescription != null) {
                    description = cmdDescription.value();
                }

                bukkitPlayer.sendMessage("\2477/" + label + " " + commandEntry.getKey() + " \247f-\247e " + description);
            }
        }
    }

	@Override
	public List<String> onTabComplete(CommandSender sender, Command arg1,
			String arg2, String[] args) {
		
		if (args.length <= 1) {
			// List The Commands
			Set<String> matches = new HashSet<>();
			
			String search = args[0].toLowerCase();
			for (String name : subCommandMap.keySet()) {
				if(name.startsWith( search )) {
					matches.add(name);
				}
			}
			return Lists.newArrayList(matches);
		}
		
		if(!subCommandMap.containsKey(args[0])) 
			return null;
		try {
			TabExecutor exe = (TabExecutor)subCommandMap.get(args[0]);
			return exe.onTabComplete(sender, arg1, args[0], args);
		} catch(Exception ex) {
			// Command Doesnt Have TabExecutor
			return null;
		}
		
	}

	/*public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] args) {
		StringBuilder builder = new StringBuilder();
		for(String arg : args) {
			builder.append(arg);
			builder.append(" ");
		}
		arg0.sendMessage(new String(builder));
		return null;
	}*/
}
