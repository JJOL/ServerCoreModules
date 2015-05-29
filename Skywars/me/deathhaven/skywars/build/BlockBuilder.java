/*  1:   */ package me.deathhaven.skywars.build;
/*  2:   */ 
/*  3:   */ import com.sk89q.worldedit.EditSession;
/*  4:   */ import com.sk89q.worldedit.MaxChangedBlocksException;
/*  5:   */ import java.util.List;
/*  6:   */ import me.deathhaven.skywars.SkyWars;
/*  7:   */ import org.bukkit.scheduler.BukkitRunnable;
/*  8:   */ 
/*  9:   */ public class BlockBuilder
/* 10:   */   extends BukkitRunnable
/* 11:   */ {
/* 12:   */   private EditSession editSession;
/* 13:   */   private List<BlockBuilderEntry> vectorList;
/* 14:   */   private List<BlockBuilderEntry> delayedList;
/* 15:   */   private int blocksPerTick;
/* 16:   */   private BuildFinishedHandler buildFinishedHandler;
/* 17:   */   
/* 18:   */   public BlockBuilder(EditSession editSession, List<BlockBuilderEntry> vectorList, List<BlockBuilderEntry> delayedList, int blocksPerTick, BuildFinishedHandler buildFinishedHandler)
/* 19:   */   {
/* 20:22 */     this.editSession = editSession;
/* 21:23 */     this.vectorList = vectorList;
/* 22:24 */     this.delayedList = delayedList;
/* 23:25 */     this.blocksPerTick = blocksPerTick;
/* 24:26 */     this.buildFinishedHandler = buildFinishedHandler;
/* 25:   */   }
/* 26:   */   
/* 27:   */   public void start(long delay, long period)
/* 28:   */   {
/* 29:30 */     runTaskTimer(SkyWars.get(), delay, period);
/* 30:   */   }
/* 31:   */   
/* 32:   */   public void run()
/* 33:   */   {
/* 34:   */     try
/* 35:   */     {
/* 36:36 */       for (int iii = 0; iii < this.blocksPerTick; iii++) {
/* 37:37 */         if (!this.vectorList.isEmpty())
/* 38:   */         {
/* 39:38 */           place((BlockBuilderEntry)this.vectorList.remove(0));
/* 40:   */         }
/* 41:40 */         else if (!this.delayedList.isEmpty())
/* 42:   */         {
/* 43:41 */           place((BlockBuilderEntry)this.delayedList.remove(0));
/* 44:   */         }
/* 45:   */         else
/* 46:   */         {
/* 47:44 */           cancel();
/* 48:45 */           this.buildFinishedHandler.onBuildFinish();
/* 49:   */           
/* 50:47 */           break;
/* 51:   */         }
/* 52:   */       }
/* 53:   */     }
/* 54:   */     catch (MaxChangedBlocksException ex)
/* 55:   */     {
/* 56:51 */       cancel();
/* 57:52 */       this.buildFinishedHandler.onBuildFinish();
/* 58:   */     }
/* 59:   */   }
/* 60:   */   
/* 61:   */   private void place(BlockBuilderEntry entry)
/* 62:   */     throws MaxChangedBlocksException
/* 63:   */   {
/* 64:57 */     this.editSession.setBlock(entry.getLocation(), entry.getBlock());
/* 65:   */   }
/* 66:   */   
/* 67:   */   public static abstract interface BuildFinishedHandler
/* 68:   */   {
/* 69:   */     public abstract void onBuildFinish();
/* 70:   */   }
/* 71:   */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.build.BlockBuilder
 * JD-Core Version:    0.7.0.1
 */