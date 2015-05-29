/*  1:   */ package me.deathhaven.skywars.build;
/*  2:   */ 
/*  3:   */ import com.sk89q.worldedit.Vector;
/*  4:   */ import com.sk89q.worldedit.blocks.BaseBlock;
/*  5:   */ 
/*  6:   */ public class BlockBuilderEntry
/*  7:   */ {
/*  8:   */   private final Vector location;
/*  9:   */   private final BaseBlock block;
/* 10:   */   
/* 11:   */   public BlockBuilderEntry(Vector location, BaseBlock block)
/* 12:   */   {
/* 13:12 */     this.location = location;
/* 14:13 */     this.block = block;
/* 15:   */   }
/* 16:   */   
/* 17:   */   public Vector getLocation()
/* 18:   */   {
/* 19:17 */     return this.location;
/* 20:   */   }
/* 21:   */   
/* 22:   */   public BaseBlock getBlock()
/* 23:   */   {
/* 24:21 */     return this.block;
/* 25:   */   }
/* 26:   */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.build.BlockBuilderEntry
 * JD-Core Version:    0.7.0.1
 */