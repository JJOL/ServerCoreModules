package me.deathhaven.skywars.utilities;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

public class PlayerUtil {

    public static void refreshPlayer(@Nonnull Player player) {
        if (!player.isDead()) {
            player.setHealth(20);
            player.setFoodLevel(20);
            player.setSaturation(20f);
        }

        
        player.setFireTicks(0);
        player.setExp(0.0F);
        player.setLevel(0);

        removePotionEffects(player);
    }

    @SuppressWarnings("deprecation")
    public static void clearInventory(@Nonnull Player pPlayer) {
        pPlayer.closeInventory();

        PlayerInventory inventory = pPlayer.getInventory();
        inventory.setArmorContents(null);
        inventory.clear();

        for (int iii = 0; iii < inventory.getSize(); iii++) {
            inventory.clear(iii);
        }

        pPlayer.updateInventory();
    }

    public static void removePotionEffects(@Nonnull Player pPlayer) {
        for (PotionEffect potionEffect : pPlayer.getActivePotionEffects()) {
            pPlayer.removePotionEffect(potionEffect.getType());
        }
    }
}