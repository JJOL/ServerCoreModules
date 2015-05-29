/*  1:   */ package me.deathhaven.skywars.utilities;
/*  2:   */ 
/*  3:   */ import com.google.common.collect.Lists;
/*  4:   */ import com.sk89q.worldedit.CuboidClipboard;
/*  5:   */ import com.sk89q.worldedit.EditSession;
/*  6:   */ import com.sk89q.worldedit.MaxChangedBlocksException;
/*  7:   */ import com.sk89q.worldedit.Vector;
/*  8:   */ import com.sk89q.worldedit.blocks.BaseBlock;
/*  9:   */ import com.sk89q.worldedit.bukkit.BukkitWorld;
/* 10:   */ import java.util.Collections;
/* 11:   */ import java.util.Comparator;
/* 12:   */ import java.util.List;
/* 13:   */ import me.deathhaven.skywars.SkyWars;
/* 14:   */ import me.deathhaven.skywars.build.BlockBuilder;
/* 15:   */ import me.deathhaven.skywars.build.BlockBuilder.BuildFinishedHandler;
/* 16:   */ import me.deathhaven.skywars.build.BlockBuilderEntry;
/* 17:   */ import me.deathhaven.skywars.config.PluginConfig;
/* 18:   */ import me.deathhaven.skywars.game.Game;
/* 19:   */ import org.bukkit.Bukkit;
/* 20:   */ import org.bukkit.Location;
/* 21:   */ import org.bukkit.scheduler.BukkitScheduler;
/* 22:   */ 
/* 23:   */ public class WEUtils
/* 24:   */ {
/* 25:   */   public static boolean pasteSchematic(Location origin, CuboidClipboard schematic)
/* 26:   */   {
/* 27:27 */     EditSession editSession = new EditSession(new BukkitWorld(origin.getWorld()), 2147483647);
/* 28:28 */     editSession.setFastMode(true);
/* 29:   */     try
/* 30:   */     {
/* 31:31 */       schematic.paste(editSession, new Vector(origin.getBlockX(), origin.getBlockY(), origin.getBlockZ()), PluginConfig.ignoreAir());
/* 32:   */     }
/* 33:   */     catch (MaxChangedBlocksException ignored)
/* 34:   */     {
/* 35:33 */       return false;
/* 36:   */     }
/* 37:36 */     return true;
/* 38:   */   }
/* 39:   */   
/* 40:   */   public static void buildSchematic(final Game game, Location origin, final CuboidClipboard schematic)
/* 41:   */   {
/* 42:40 */     Bukkit.getScheduler().runTaskAsynchronously(SkyWars.get(), new Runnable()
/* 43:   */     {
/* 44:   */       public void run()
/* 45:   */       {
/* 46:43 */         Vector pasteLocation = new Vector(WEUtils.this.getBlockX(), WEUtils.this.getBlockY(), WEUtils.this.getBlockZ());
/* 47:   */         
/* 48:   */ 
/* 49:   */ 
/* 50:47 */         List<BlockBuilderEntry> blockQueue = Lists.newLinkedList();
/* 51:48 */         List<BlockBuilderEntry> delayedQueue = Lists.newLinkedList();
/* 52:50 */         for (int xxx = 0; xxx < schematic.getSize().getBlockX(); xxx++) {
/* 53:51 */           for (int yyy = 0; yyy < schematic.getSize().getBlockY(); yyy++) {
/* 54:52 */             for (int zzz = 0; zzz < schematic.getSize().getBlockZ(); zzz++)
/* 55:   */             {
/* 56:53 */               Vector currentPoint = new Vector(xxx, yyy, zzz);
/* 57:54 */               BaseBlock currentBlock = schematic.getPoint(currentPoint);
/* 58:56 */               if (!currentBlock.isAir())
/* 59:   */               {
/* 60:60 */                 currentPoint = currentPoint.add(pasteLocation).add(schematic.getOffset());
/* 61:62 */                 switch (currentBlock.getType())
/* 62:   */                 {
/* 63:   */                 case 8: 
/* 64:   */                 case 9: 
/* 65:   */                 case 10: 
/* 66:   */                 case 11: 
/* 67:   */                 case 23: 
/* 68:   */                 case 25: 
/* 69:   */                 case 52: 
/* 70:   */                 case 54: 
/* 71:   */                 case 61: 
/* 72:   */                 case 63: 
/* 73:   */                 case 68: 
/* 74:   */                 case 84: 
/* 75:   */                 case 114: 
/* 76:   */                 case 116: 
/* 77:   */                 case 117: 
/* 78:   */                 case 119: 
/* 79:   */                 case 120: 
/* 80:   */                 case 137: 
/* 81:   */                 case 138: 
/* 82:   */                 case 154: 
/* 83:   */                 case 158: 
/* 84:85 */                   delayedQueue.add(new BlockBuilderEntry(currentPoint, currentBlock));
/* 85:86 */                   break;
/* 86:   */                 default: 
/* 87:89 */                   blockQueue.add(new BlockBuilderEntry(currentPoint, currentBlock));
/* 88:   */                 }
/* 89:   */               }
/* 90:   */             }
/* 91:   */           }
/* 92:   */         }
/* 93:95 */         Collections.sort(blockQueue, new Comparator()
/* 94:   */         {
/* 95:   */           public int compare(BlockBuilderEntry o1, BlockBuilderEntry o2)
/* 96:   */           {
/* 97:98 */             return Integer.compare(o1.getLocation().getBlockY(), o2.getLocation().getBlockY());
/* 98:   */           }
/* 99::1 */         });
/* :0::2 */         int blockCount = blockQueue.size() + delayedQueue.size();
/* :1::3 */         int ticksRequired = blockCount / PluginConfig.blocksPerTick();
/* :2::4 */         int time = (int)(ticksRequired * (PluginConfig.buildInterval() * 50L));
/* :3::5 */         game.setTimer((int)(time / 1000L));
/* :4:   */         
/* :5::7 */         EditSession editSession = new EditSession(new BukkitWorld(WEUtils.this.getWorld()), 2147483647);
/* :6::8 */         editSession.setFastMode(true);
/* :7:   */         
/* :8:;0 */         BlockBuilder blockBuilder = new BlockBuilder(editSession, blockQueue, delayedQueue, PluginConfig.blocksPerTick(), new BlockBuilder.BuildFinishedHandler()
/* :9:   */         {
/* ;0:   */           public void onBuildFinish()
/* ;1:   */           {
/* ;2:;3 */             this.val$game.setBuilt(true);
/* ;3:;5 */             if (this.val$game.isFull()) {
/* ;4:;6 */               this.val$game.onGameStart();
/* ;5:   */             } else {
/* ;6:;8 */               this.val$game.setTimer(1);
/* ;7:   */             }
/* ;8:   */           }
/* ;9:<1 */         });
/* <0:<2 */         blockBuilder.start(40L, PluginConfig.buildInterval());
/* <1:   */       }
/* <2:   */     });
/* <3:   */   }
/* <4:   */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.utilities.WEUtils
 * JD-Core Version:    0.7.0.1
 */