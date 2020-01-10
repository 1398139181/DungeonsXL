/*
 * Copyright (C) 2012-2019 Frank Baumann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.erethon.dungeonsxl.api.dungeon;

import de.erethon.dungeonsxl.api.DungeonsAPI;
import de.erethon.dungeonsxl.api.world.ResourceWorld;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A dungeon consists of floors and settings including its game rules.
 *
 * @author Daniel Saukel
 */
public class Dungeon {

    private DungeonsAPI api;

    private String name;
    private DungeonConfig config;
    private ResourceWorld map;

    /**
     * Instantiates a dungeon from a dungeon config file.
     *
     * @param api  the plugin instance
     * @param file the file to load from
     */
    public Dungeon(DungeonsAPI api, File file) {
        this.api = api;

        name = file.getName().replaceAll(".yml", "");
        config = new DungeonConfig(plugin, file);
        map = config.getStartFloor();
    }

    /**
     * Instantiates a dungeon without a dungeon config file.
     * <p>
     * Internally, each {@link ResourceWorld} gets one single floor dungeon.
     *
     * @param api      the plugin instance
     * @param resource the only resource world
     */
    public Dungeon(DungeonsAPI api, ResourceWorld resource) {
        this.api = api;

        name = resource.getName();
        map = resource;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the config
     */
    public DungeonConfig getConfig() {
        return config;
    }

    /**
     * @return if this dungeon has multiple floors
     */
    public boolean isMultiFloor() {
        return config != null;
    }

    /**
     * @return the floors of the dungeon
     */
    public List<DResourceWorld> getFloors() {
        if (isMultiFloor()) {
            return config.getFloors();
        } else {
            return new ArrayList<>(Arrays.asList(map));
        }
    }

    /**
     * @return the SFD map / start floor
     */
    public DResourceWorld getMap() {
        return map;
    }

    /**
     * @param map the SFD map / start floor to set
     */
    public void setMap(DResourceWorld map) {
        this.map = map;
    }

    /**
     * @return false if there are setup errors
     */
    public boolean isSetupCorrect() {
        for (DResourceWorld resource : plugin.getDWorldCache().getResources()) {
            if (resource.getName().equals(name)) {
                return false;
            }
        }
        return config.getStartFloor() != null && config.getEndFloor() != null;
    }

    /* Statics */
    /**
     * @param name the name of the dungeon
     * @return the file. Might not exist
     */
    public static File getFileFromName(String name) {
        return new File(DungeonsAPI.DUNGEONS, name + ".yml");
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{name=" + name + "; multiFloor=" + isMultiFloor() + "}";
    }

}
