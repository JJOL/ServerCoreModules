/*  1:   */ package me.deathhaven.skywars.utilities;
/*  2:   */ 
/*  3:   */ import java.util.Random;
/*  4:   */ import org.bukkit.Location;
/*  5:   */ import org.bukkit.World;
/*  6:   */ import org.bukkit.generator.ChunkGenerator;
/*  7:   */ 
/*  8:   */ public class WorldGenerator
/*  9:   */   extends ChunkGenerator
/* 10:   */ {
/* 11:   */   public byte[] generate(World world, Random random, int cx, int cz)
/* 12:   */   {
/* 13:11 */     return new byte[65536];
/* 14:   */   }
/* 15:   */   
/* 16:   */   public Location getFixedSpawnLocation(World world, Random random)
/* 17:   */   {
/* 18:16 */     if (!world.isChunkLoaded(0, 0)) {
/* 19:17 */       world.loadChunk(0, 0);
/* 20:   */     }
/* 21:19 */     return new Location(world, 0.0D, 64.0D, 0.0D);
/* 22:   */   }
/* 23:   */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.utilities.WorldGenerator
 * JD-Core Version:    0.7.0.1
 */