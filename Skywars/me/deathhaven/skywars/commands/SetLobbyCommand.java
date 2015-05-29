/*  1:   */ package me.deathhaven.skywars.commands;
/*  2:   */ 
/*  3:   */ import me.deathhaven.skywars.config.PluginConfig;
/*  4:   */ import me.deathhaven.skywars.utilities.Messaging.MessageFormatter;
/*  5:   */ import org.bukkit.command.Command;
/*  6:   */ import org.bukkit.command.CommandExecutor;
/*  7:   */ import org.bukkit.command.CommandSender;
/*  8:   */ import org.bukkit.entity.Player;
/*  9:   */ 
/* 10:   */ @CommandDescription("Set the lobby")
/* 11:   */ @CommandPermissions({"skywars.command.setlobby"})
/* 12:   */ public class SetLobbyCommand
/* 13:   */   implements CommandExecutor
/* 14:   */ {
/* 15:   */   public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
/* 16:   */   {
/* 17:17 */     PluginConfig.setLobbySpawn(((Player)sender).getLocation());
/* 18:18 */     sender.sendMessage(new Messaging.MessageFormatter().format("success.lobby-set"));
/* 19:19 */     return true;
/* 20:   */   }
/* 21:   */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.commands.SetLobbyCommand
 * JD-Core Version:    0.7.0.1
 */