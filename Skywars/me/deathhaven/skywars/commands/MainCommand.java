/*  1:   */ package me.deathhaven.skywars.commands;
/*  2:   */ 
/*  3:   */ import com.google.common.collect.Maps;
/*  4:   */ import java.util.Map;
/*  5:   */ import java.util.Map.Entry;
/*  6:   */ import me.deathhaven.skywars.config.PluginConfig;
/*  7:   */ import me.deathhaven.skywars.utilities.Messaging.MessageFormatter;
/*  8:   */ import org.bukkit.command.Command;
/*  9:   */ import org.bukkit.command.CommandExecutor;
/* 10:   */ import org.bukkit.command.CommandSender;
/* 11:   */ import org.bukkit.entity.Player;
/* 12:   */ 
/* 13:   */ public class MainCommand
/* 14:   */   implements CommandExecutor
/* 15:   */ {
/* 16:17 */   private Map<String, CommandExecutor> subCommandMap = Maps.newHashMap();
/* 17:   */   
/* 18:   */   public MainCommand()
/* 19:   */   {
/* 20:20 */     this.subCommandMap.put("reload", new ReloadCommand());
/* 21:21 */     this.subCommandMap.put("setlobby", new SetLobbyCommand());
/* 22:22 */     this.subCommandMap.put("start", new StartCommand());
/* 23:23 */     this.subCommandMap.put("leave", new LeaveCommand());
/* 24:24 */     this.subCommandMap.put("score", new ScoreCommand());
/* 25:25 */     this.subCommandMap.put("vote", new VoteCommand());
/* 26:26 */     this.subCommandMap.put("join", new JoinCommand());
/* 27:27 */     this.subCommandMap.put("spectate", new SpectateCommand());
/* 28:28 */     this.subCommandMap.put("info", new InfoCommand());
/* 29:29 */     if (!PluginConfig.disableKits()) {
/* 30:30 */       this.subCommandMap.put("kit", new KitCommand());
/* 31:   */     }
/* 32:   */   }
/* 33:   */   
/* 34:   */   public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
/* 35:   */   {
/* 36:36 */     if (!(sender instanceof Player))
/* 37:   */     {
/* 38:37 */       sender.sendMessage(new Messaging.MessageFormatter().format("error.player-only"));
/* 39:38 */       return true;
/* 40:   */     }
/* 41:41 */     Player player = (Player)sender;
/* 42:42 */     if (args.length == 0)
/* 43:   */     {
/* 44:43 */       printHelp(player, label);
/* 45:44 */       return true;
/* 46:   */     }
/* 47:47 */     String subCommandName = args[0].toLowerCase();
/* 48:48 */     if (!this.subCommandMap.containsKey(subCommandName))
/* 49:   */     {
/* 50:49 */       printHelp(player, label);
/* 51:50 */       return true;
/* 52:   */     }
/* 53:53 */     CommandExecutor subCommand = (CommandExecutor)this.subCommandMap.get(subCommandName);
/* 54:54 */     if (!hasPermission(player, subCommand))
/* 55:   */     {
/* 56:55 */       player.sendMessage(new Messaging.MessageFormatter().format("error.insufficient-permissions"));
/* 57:56 */       return true;
/* 58:   */     }
/* 59:59 */     return subCommand.onCommand(sender, command, label, args);
/* 60:   */   }
/* 61:   */   
/* 62:   */   private boolean hasPermission(Player bukkitPlayer, CommandExecutor cmd)
/* 63:   */   {
/* 64:63 */     CommandPermissions permissions = (CommandPermissions)cmd.getClass().getAnnotation(CommandPermissions.class);
/* 65:64 */     if (permissions == null) {
/* 66:65 */       return true;
/* 67:   */     }
/* 68:68 */     for (String permission : permissions.value()) {
/* 69:69 */       if (bukkitPlayer.hasPermission(permission)) {
/* 70:70 */         return true;
/* 71:   */       }
/* 72:   */     }
/* 73:74 */     return false;
/* 74:   */   }
/* 75:   */   
/* 76:   */   private void printHelp(Player bukkitPlayer, String label)
/* 77:   */   {
/* 78:78 */     bukkitPlayer.sendMessage(new Messaging.MessageFormatter().withPrefix().format("cmd.available-commands"));
/* 79:80 */     for (Map.Entry<String, CommandExecutor> commandEntry : this.subCommandMap.entrySet()) {
/* 80:81 */       if (hasPermission(bukkitPlayer, (CommandExecutor)commandEntry.getValue()))
/* 81:   */       {
/* 82:82 */         String description = "No description available.";
/* 83:   */         
/* 84:84 */         CommandDescription cmdDescription = (CommandDescription)((CommandExecutor)commandEntry.getValue()).getClass().getAnnotation(CommandDescription.class);
/* 85:85 */         if (cmdDescription != null) {
/* 86:86 */           description = cmdDescription.value();
/* 87:   */         }
/* 88:89 */         bukkitPlayer.sendMessage("§7/" + label + " " + (String)commandEntry.getKey() + " §f-§e " + description);
/* 89:   */       }
/* 90:   */     }
/* 91:   */   }
/* 92:   */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.commands.MainCommand
 * JD-Core Version:    0.7.0.1
 */