/*  1:   */ package me.deathhaven.skywars.utilities;
/*  2:   */ 
/*  3:   */ import org.bukkit.Location;
/*  4:   */ import org.bukkit.World;
/*  5:   */ 
/*  6:   */ public class LocationUtil
/*  7:   */ {
/*  8:   */   public static Location getLocation(World world, String coordinates)
/*  9:   */   {
/* 10: 9 */     String[] chunks = coordinates.split(" ");
/* 11:   */     
/* 12:11 */     double posX = Double.parseDouble(chunks[0]);
/* 13:12 */     double posY = Double.parseDouble(chunks[1]);
/* 14:13 */     double posZ = Double.parseDouble(chunks[2]);
/* 15:   */     
/* 16:15 */     float yaw = 0.0F;
/* 17:16 */     float pitch = 0.0F;
/* 18:18 */     if (chunks.length == 5)
/* 19:   */     {
/* 20:19 */       yaw = (Float.parseFloat(chunks[3]) + 180.0F + 360.0F) % 360.0F;
/* 21:20 */       pitch = Float.parseFloat(chunks[4]);
/* 22:   */     }
/* 23:23 */     return chunks.length == 5 ? new Location(world, posX, posY, posZ, yaw, pitch) : new Location(world, posX, posY, posZ);
/* 24:   */   }
/* 25:   */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.utilities.LocationUtil
 * JD-Core Version:    0.7.0.1
 */