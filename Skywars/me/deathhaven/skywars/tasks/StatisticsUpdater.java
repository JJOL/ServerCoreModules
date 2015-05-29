/*  1:   */ package me.deathhaven.skywars.tasks;
/*  2:   */ 
/*  3:   */ import me.deathhaven.skywars.SkyWars;
/*  4:   */ import me.deathhaven.skywars.config.PluginConfig;
/*  5:   */ import me.deathhaven.skywars.controllers.StatisticsController;
/*  6:   */ import me.deathhaven.skywars.database.Database;
/*  7:   */ import org.bukkit.Bukkit;
/*  8:   */ import org.bukkit.configuration.file.FileConfiguration;
/*  9:   */ import org.bukkit.scheduler.BukkitRunnable;
/* 10:   */ import org.bukkit.scheduler.BukkitScheduler;
/* 11:   */ 
/* 12:   */ public class StatisticsUpdater
/* 13:   */   extends BukkitRunnable
/* 14:   */ {
/* 15:   */   public StatisticsUpdater()
/* 16:   */   {
/* 17:14 */     runTaskTimerAsynchronously(SkyWars.get(), 20L, PluginConfig.getStatisticsUpdateInterval());
/* 18:   */   }
/* 19:   */   
/* 20:   */   public void run()
/* 21:   */   {
/* 22:19 */     final StatisticsController statisticsController = StatisticsController.get();
/* 23:   */     try
/* 24:   */     {
/* 25:22 */       Database database = new Database(SkyWars.get().getConfig().getConfigurationSection("database"));
/* 26:23 */       statisticsController.setTopList(database.getTopScore(PluginConfig.getStatisticsTop()));
/* 27:24 */       database.close();
/* 28:   */     }
/* 29:   */     catch (Exception localException) {}
/* 30:29 */     Bukkit.getScheduler().scheduleSyncDelayedTask(SkyWars.get(), new Runnable()
/* 31:   */     {
/* 32:   */       public void run()
/* 33:   */       {
/* 34:32 */         statisticsController.update();
/* 35:   */       }
/* 36:   */     });
/* 37:   */   }
/* 38:   */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.tasks.StatisticsUpdater
 * JD-Core Version:    0.7.0.1
 */