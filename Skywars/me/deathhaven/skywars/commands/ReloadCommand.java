/*  1:   */ package me.deathhaven.skywars.commands;
/*  2:   */ 
/*  3:   */ import me.deathhaven.skywars.SkyWars;
/*  4:   */ import me.deathhaven.skywars.controllers.ChestController;
/*  5:   */ import me.deathhaven.skywars.controllers.KitController;
/*  6:   */ import me.deathhaven.skywars.utilities.Messaging;
/*  7:   */ import me.deathhaven.skywars.utilities.Messaging.MessageFormatter;
/*  8:   */ import org.bukkit.command.Command;
/*  9:   */ import org.bukkit.command.CommandExecutor;
/* 10:   */ import org.bukkit.command.CommandSender;
/* 11:   */ 
/* 12:   */ @CommandDescription("Reloads the chests, kits and the plugin.yml")
/* 13:   */ @CommandPermissions({"skywars.command.reload"})
/* 14:   */ public class ReloadCommand
/* 15:   */   implements CommandExecutor
/* 16:   */ {
/* 17:   */   public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
/* 18:   */   {
/* 19:18 */     ChestController.get().load();
/* 20:19 */     KitController.get().load();
/* 21:20 */     SkyWars.get().reloadConfig();
/* 22:21 */     new Messaging(SkyWars.get());
/* 23:   */     
/* 24:23 */     sender.sendMessage(new Messaging.MessageFormatter().format("success.reload"));
/* 25:24 */     return true;
/* 26:   */   }
/* 27:   */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.commands.ReloadCommand
 * JD-Core Version:    0.7.0.1
 */