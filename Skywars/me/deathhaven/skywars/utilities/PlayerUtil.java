/*  1:   */ package me.deathhaven.skywars.utilities;
/*  2:   */ 
/*  3:   */ import javax.annotation.Nonnull;
/*  4:   */ import org.bukkit.entity.Player;
/*  5:   */ import org.bukkit.inventory.PlayerInventory;
/*  6:   */ import org.bukkit.potion.PotionEffect;
/*  7:   */ 
/*  8:   */ public class PlayerUtil
/*  9:   */ {
/* 10:   */   public static void refreshPlayer(@Nonnull Player player)
/* 11:   */   {
/* 12:12 */     if (!player.isDead())
/* 13:   */     {
/* 14:13 */       player.setHealth(20.0D);
/* 15:14 */       player.setFoodLevel(20);
/* 16:15 */       player.setSaturation(20.0F);
/* 17:   */     }
/* 18:19 */     player.setFireTicks(0);
/* 19:20 */     player.setExp(0.0F);
/* 20:21 */     player.setLevel(0);
/* 21:   */     
/* 22:23 */     removePotionEffects(player);
/* 23:   */   }
/* 24:   */   
/* 25:   */   public static void clearInventory(@Nonnull Player pPlayer)
/* 26:   */   {
/* 27:28 */     pPlayer.closeInventory();
/* 28:   */     
/* 29:30 */     PlayerInventory inventory = pPlayer.getInventory();
/* 30:31 */     inventory.setArmorContents(null);
/* 31:32 */     inventory.clear();
/* 32:34 */     for (int iii = 0; iii < inventory.getSize(); iii++) {
/* 33:35 */       inventory.clear(iii);
/* 34:   */     }
/* 35:38 */     pPlayer.updateInventory();
/* 36:   */   }
/* 37:   */   
/* 38:   */   public static void removePotionEffects(@Nonnull Player pPlayer)
/* 39:   */   {
/* 40:42 */     for (PotionEffect potionEffect : pPlayer.getActivePotionEffects()) {
/* 41:43 */       pPlayer.removePotionEffect(potionEffect.getType());
/* 42:   */     }
/* 43:   */   }
/* 44:   */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.utilities.PlayerUtil
 * JD-Core Version:    0.7.0.1
 */