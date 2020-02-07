/*
 * Copyright (C) 2012-2020 Frank Baumann
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
package de.erethon.dungeonsxl.dungeon;

import de.erethon.dungeonsxl.DungeonsXL;
import de.erethon.dungeonsxl.api.dungeon.Dungeon;
import de.erethon.dungeonsxl.api.world.ResourceWorld;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a dungeon. While multi floor dungeon scripts are represented by {@link de.erethon.dungeonsxl.config.DungeonConfig}, single floor dungeons also get
 * a dungeon object without a config file as a placeholder.
 *
 * @author Daniel Saukel
 */
public class XLDungeon implements Dungeon {

    private DungeonsXL plugin;

    private String name;
    private DungeonConfig config;
    private ResourceWorld map;

    /**
     * Real dungeon
     *
     * @param plugin the plugin instance
     * @param file   the file to load from
     */
    public XLDungeon(DungeonsXL plugin, File file) {
        this.plugin = plugin;

        name = file.getName().replaceAll(".yml", "");
        config = new DungeonConfig(plugin, file);
        map = config.getStartFloor();
    }

    /**
     * Artificial dungeon
     *
     * @param plugin   the plugin instance
     * @param resource the only resource world
     */
    public XLDungeon(DungeonsXL plugin, ResourceWorld resource) {
        this.plugin = plugin;

        name = resource.getName();
        map = resource;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public DungeonConfig getConfig() {
        return config;
    }

    @Override
    public boolean isMultiFloor() {
        return config != null;
    }

    @Override
    public List<ResourceWorld> getFloors() {
        if (isMultiFloor()) {
            return config.getFloors();
        } else {
            return new ArrayList<>(Arrays.asList(map));
        }
    }

    @Override
    public ResourceWorld getMap() {
        return map;
    }

    /**
     * @param map the SFD map / start floor to set
     */
    public void setMap(ResourceWorld map) {
        this.map = map;
    }

    @Override
    public boolean isSetupCorrect() {
        for (ResourceWorld resource : plugin.getMapRegistry()) {
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
        return new File(DungeonsXL.DUNGEONS, name + ".yml");
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{name=" + name + "; multiFloor=" + isMultiFloor() + "}";
    }

}
