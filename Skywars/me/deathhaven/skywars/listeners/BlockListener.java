package me.deathhaven.skywars.listeners;

import me.deathhaven.skywars.controllers.PlayerController;
import me.deathhaven.skywars.game.GameState;
import me.deathhaven.skywars.player.GamePlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = PlayerController.get().get(player);

        if (gamePlayer.isPlaying() && gamePlayer.getGame().getState() == GameState.WAITING) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = PlayerController.get().get(player);

        if (gamePlayer.isPlaying() && gamePlayer.getGame().getState() == GameState.WAITING) {
            event.setCancelled(true);
        }
    }
}
