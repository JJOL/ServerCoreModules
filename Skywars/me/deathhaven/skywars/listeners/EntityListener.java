package me.deathhaven.skywars.listeners;

import me.deathhaven.skywars.SkyWars;
import me.deathhaven.skywars.controllers.PlayerController;
import me.deathhaven.skywars.game.Game;
import me.deathhaven.skywars.game.GameState;
import me.deathhaven.skywars.player.GamePlayer;
import me.deathhaven.skywars.utilities.DebbugUtils;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;

public class EntityListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) {
            return;
        }

        Player player = (Player) event.getEntity();
        GamePlayer gamePlayer = PlayerController.get().get(player);
        
        if(gamePlayer.isSpectating()) {
        	gamePlayer.getGame().spawnSpectator(gamePlayer); 
        	event.setCancelled(true);
        	player.sendMessage("Damage Detected!");
        	//DebbugUtils.get().sendDebbugMC("Spectator was damaged: "+player.getName());
        	return;
        }
        
        if (event.getCause() == DamageCause.FIRE_TICK && gamePlayer.shouldSkipFireTicks()) {
            player.setFireTicks(0);
            event.setCancelled(true);
            gamePlayer.setSkipFireTicks(false);
        }

        if (!gamePlayer.isPlaying()) {
            return;
        }

        Game game = gamePlayer.getGame();

        if (game.getState() == GameState.WAITING) {
            event.setCancelled(true);
        } else if (event.getCause() == EntityDamageEvent.DamageCause.FALL && gamePlayer.shouldSkipFallDamage()) {
            gamePlayer.setSkipFallDamage(false);
            event.setCancelled(true);
        } else if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
            player.setFallDistance(0F);
            event.setCancelled(true);
            gamePlayer.getGame().onPlayerDeath(gamePlayer, null);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDeath(final PlayerDeathEvent event) {
        Player player = event.getEntity();
        final GamePlayer gamePlayer = PlayerController.get().get(player);

        if(gamePlayer.isSpectating()) {
        	//DebbugUtils.get().sendDebbugMC("Spectator was safed: "+player.getName());
        	return;
        }
        
        if (!gamePlayer.isPlaying()) {
            return;
        }

        DamageCause damageCause = player.getLastDamageCause().getCause();
        if (player.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
            Bukkit.getScheduler().runTaskLater(SkyWars.get(), new Runnable() {
                @Override
                public void run() {
                    gamePlayer.getGame().onPlayerDeath(gamePlayer, event);
                }
            }, 1L);
        } else if (damageCause == DamageCause.LAVA || damageCause == DamageCause.FIRE || damageCause == DamageCause.FIRE_TICK) {
            gamePlayer.setSkipFireTicks(true);
            gamePlayer.getGame().onPlayerDeath(gamePlayer, event);
        } else {
            gamePlayer.getGame().onPlayerDeath(gamePlayer, event);
        }
    }
}
