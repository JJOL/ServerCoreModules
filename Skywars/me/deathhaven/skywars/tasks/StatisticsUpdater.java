package me.deathhaven.skywars.tasks;

import me.deathhaven.skywars.SkyWars;
import me.deathhaven.skywars.config.PluginConfig;
import me.deathhaven.skywars.controllers.StatisticsController;
import me.deathhaven.skywars.database.Database;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class StatisticsUpdater extends BukkitRunnable {

    public StatisticsUpdater() {
        runTaskTimerAsynchronously(SkyWars.get(), 20L, PluginConfig.getStatisticsUpdateInterval());
    }

    @Override
    public void run() {
        final StatisticsController statisticsController = StatisticsController.get();

        try {
            Database database = new Database(SkyWars.get().getConfig().getConfigurationSection("database"));
            statisticsController.setTopList(database.getTopScore(PluginConfig.getStatisticsTop()));
            database.close();

        } catch (Exception ignored) {
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(SkyWars.get(), new Runnable() {
            @Override
            public void run() {
                statisticsController.update();
            }
        });
    }
}
