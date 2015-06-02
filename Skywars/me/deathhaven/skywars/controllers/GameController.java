package me.deathhaven.skywars.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

import javax.annotation.Nonnull;

import me.deathhaven.skywars.SkyWars;
import me.deathhaven.skywars.game.Game;
import me.deathhaven.skywars.game.GameConfig;
import me.deathhaven.skywars.game.GameState;

import com.google.common.collect.Lists;
import com.sk89q.worldedit.CuboidClipboard;

public class GameController {

    private static GameController instance;
    
    private List<Game> gameList = Lists.newArrayList();
    

    public Game findEmpty() {
        for (Game game : gameList) {
            if (game.getState() != GameState.PLAYING && !game.isFull()) {
                return game;
            }
        }

        return create();
    }

    public Game create() {
        CuboidClipboard schematic = SchematicController.get().getRandom();
        Game game = new Game(schematic);

        while (!game.isReady()) {
            String schematicName = SchematicController.get().getName(schematic);
            SkyWars.get().getLogger().log(Level.SEVERE, String.format("Schematic '%s' does not have any spawns set!", schematicName));
            SchematicController.get().remove(schematicName);

            schematic = SchematicController.get().getRandom();
            game = new Game(schematic);
        }

        gameList.add(game);
        return game;
    }
    
    public Game createSpecial(GameConfig config) {
    	CuboidClipboard schematic = config.getSchematic();
    	Game game = new Game(schematic);

        while (!game.isReady()) {
            String schematicName = SchematicController.get().getName(schematic);
            SkyWars.get().getLogger().log(Level.SEVERE, String.format("Schematic '%s' does not have any spawns set!", schematicName));
            SchematicController.get().remove(schematicName);

            schematic = SchematicController.get().getRandom();
            game = new Game(schematic);
        }

        gameList.add(game);
        return game;
    }

    public void remove(@Nonnull Game game) {
        gameList.remove(game);
    }

    public void shutdown() {
        for (Game game : new ArrayList<Game>(gameList)) {
            game.onGameEnd();
        }
    }

    public Collection<Game> getAll() {
        return gameList;
    }
    
    public int getGameId(Game game) {
    	for(int i=0; i < gameList.size(); i++) {
    		if (gameList.get(i) == game) {
    			return i;
    		}
    	}
    	return -1;
    }

    public static GameController get() {
        if (instance == null) {
            return instance = new GameController();
        }

        return instance;
    }
}
