/*   1:    */ package me.deathhaven.skywars.utilities;
/*   2:    */ 
/*   3:    */ import com.google.common.collect.Maps;
/*   4:    */ import java.io.File;
/*   5:    */ import java.io.IOException;
/*   6:    */ import java.util.Map;
/*   7:    */ import java.util.Map.Entry;
/*   8:    */ import java.util.logging.Level;
/*   9:    */ import java.util.regex.Matcher;
/*  10:    */ import java.util.regex.Pattern;
/*  11:    */ import org.bukkit.ChatColor;
/*  12:    */ import org.bukkit.configuration.file.FileConfiguration;
/*  13:    */ import org.bukkit.configuration.file.YamlConfiguration;
/*  14:    */ import org.bukkit.plugin.Plugin;
/*  15:    */ 
/*  16:    */ public final class Messaging
/*  17:    */ {
/*  18:    */   private static Messaging instance;
/*  19: 19 */   private static final Pattern COLOR_PATTERN = Pattern.compile("(?i)(&|" + String.valueOf('§') + ")[0-9A-FK-OR]");
/*  20:    */   private final FileConfiguration storage;
/*  21:    */   private final File storageFile;
/*  22:    */   
/*  23:    */   public Messaging(Plugin plugin)
/*  24:    */   {
/*  25: 24 */     this.storageFile = new File(plugin.getDataFolder(), "messages.yml");
/*  26: 26 */     if (!this.storageFile.exists()) {
/*  27: 27 */       plugin.saveResource("messages.yml", false);
/*  28:    */     }
/*  29: 30 */     this.storage = YamlConfiguration.loadConfiguration(this.storageFile);
/*  30: 31 */     addMessages();
/*  31: 32 */     instance = this;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public static String stripColor(String input)
/*  35:    */   {
/*  36: 36 */     if (input == null) {
/*  37: 37 */       return "";
/*  38:    */     }
/*  39: 40 */     return COLOR_PATTERN.matcher(input).replaceAll("");
/*  40:    */   }
/*  41:    */   
/*  42:    */   public String getPrefix()
/*  43:    */   {
/*  44: 44 */     return this.storage.getString("prefix", "");
/*  45:    */   }
/*  46:    */   
/*  47:    */   public String getMessage(String format)
/*  48:    */   {
/*  49: 48 */     if (this.storage.contains(format)) {
/*  50: 49 */       return this.storage.getString(format);
/*  51:    */     }
/*  52: 52 */     return null;
/*  53:    */   }
/*  54:    */   
/*  55:    */   public static class MessageFormatter
/*  56:    */   {
/*  57: 57 */     private static final Pattern PATTERN = Pattern.compile("(?i)(\\{[a-z0-9_]+\\})");
/*  58: 58 */     private final Map<String, String> variableMap = Maps.newHashMap();
/*  59:    */     private boolean prefix;
/*  60:    */     
/*  61:    */     public MessageFormatter withPrefix()
/*  62:    */     {
/*  63: 62 */       this.prefix = true;
/*  64: 63 */       return this;
/*  65:    */     }
/*  66:    */     
/*  67:    */     public MessageFormatter setVariable(String format, String value)
/*  68:    */     {
/*  69: 67 */       if ((format != null) && (!format.isEmpty())) {
/*  70: 68 */         if (value == null) {
/*  71: 69 */           this.variableMap.remove(format);
/*  72:    */         } else {
/*  73: 71 */           this.variableMap.put(format, value);
/*  74:    */         }
/*  75:    */       }
/*  76: 74 */       return this;
/*  77:    */     }
/*  78:    */     
/*  79:    */     public String format(String message)
/*  80:    */     {
/*  81: 78 */       if ((message == null) || (message.isEmpty())) {
/*  82: 79 */         return "";
/*  83:    */       }
/*  84: 82 */       if (Messaging.getInstance().getMessage(message) != null) {
/*  85: 83 */         message = Messaging.getInstance().getMessage(message);
/*  86:    */       }
/*  87: 86 */       Matcher matcher = PATTERN.matcher(message);
/*  88: 88 */       while (matcher.find())
/*  89:    */       {
/*  90: 89 */         String variable = matcher.group();
/*  91: 90 */         variable = variable.substring(1, variable.length() - 1);
/*  92:    */         
/*  93: 92 */         String value = (String)this.variableMap.get(variable);
/*  94: 93 */         if (value == null) {
/*  95: 94 */           value = "";
/*  96:    */         }
/*  97: 97 */         message = message.replaceFirst(Pattern.quote(matcher.group()), Matcher.quoteReplacement(value));
/*  98:    */       }
/*  99:100 */       if (this.prefix) {
/* 100:101 */         message = Messaging.getInstance().getPrefix() + message;
/* 101:    */       }
/* 102:104 */       return ChatColor.translateAlternateColorCodes('&', message);
/* 103:    */     }
/* 104:    */   }
/* 105:    */   
/* 106:    */   public static Messaging getInstance()
/* 107:    */   {
/* 108:109 */     return instance;
/* 109:    */   }
/* 110:    */   
/* 111:    */   public void addMessages()
/* 112:    */   {
/* 113:113 */     boolean update = false;
/* 114:114 */     Map<String, String> newVariables = Maps.newHashMap();
/* 115:115 */     newVariables.put("cmd.score", "&a{player}'s score is: &6{value}");
/* 116:116 */     newVariables.put("error.no-valid-score", "&cNot a valid score!");
/* 117:117 */     newVariables.put("error.no-valid-player", "&cNot a valid player!");
/* 118:118 */     newVariables.put("error.not-enough-arguments", "&cNot enough arguments. &f{example}");
/* 119:119 */     newVariables.put("success.score-set", "&6{player}&a's score has been set to &6{value}");
/* 120:120 */     newVariables.put("success.score-give", "&6{value} &ahas been added to &6{player}&a's score");
/* 121:121 */     newVariables.put("success.score-take", "&6{value} &ahas been removed from &6{player}&a's score");
/* 122:122 */     newVariables.put("error.invalid-cmd", "&eInvalid command");
/* 123:124 */     for (Map.Entry<String, String> variable : newVariables.entrySet()) {
/* 124:125 */       if (!this.storage.isSet((String)variable.getKey()))
/* 125:    */       {
/* 126:126 */         this.storage.set((String)variable.getKey(), variable.getValue());
/* 127:127 */         update = true;
/* 128:    */       }
/* 129:    */     }
/* 130:130 */     if (update) {
/* 131:    */       try
/* 132:    */       {
/* 133:132 */         this.storage.save(this.storageFile);
/* 134:    */       }
/* 135:    */       catch (IOException ex)
/* 136:    */       {
/* 137:134 */         LogUtils.log(Level.SEVERE, "Unable to add messages: " + ex.getMessage(), new Object[0]);
/* 138:    */       }
/* 139:    */     }
/* 140:    */   }
/* 141:    */ }


/* Location:           C:\Users\JJOL\Desktop\SavesB\DHMaySkywars\plugins\DHSkyWarsDummy.jar
 * Qualified Name:     me.deathhaven.skywars.utilities.Messaging
 * JD-Core Version:    0.7.0.1
 */