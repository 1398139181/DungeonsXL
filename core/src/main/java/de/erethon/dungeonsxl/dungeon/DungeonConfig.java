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

import de.erethon.commons.config.DREConfig;
import de.erethon.dungeonsxl.DungeonsXL;
import de.erethon.dungeonsxl.api.world.ResourceWorld;
import de.erethon.dungeonsxl.world.WorldConfig;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Represents a dungeon script. See {@link de.erethon.dungeonsxl.dungeon.Dungeon}.
 *
 * @author Daniel Saukel
 */
public class DungeonConfig extends DREConfig {

    private DungeonsXL plugin;

    public static final int CONFIG_VERSION = 1;

    private ResourceWorld startFloor;
    private ResourceWorld endFloor;
    private List<ResourceWorld> floors = new ArrayList<>();
    private int floorCount;
    private boolean removeWhenPlayed;
    private WorldConfig overrideValues;
    private WorldConfig defaultValues;

    public DungeonConfig(DungeonsXL plugin, File file) {
        super(file, CONFIG_VERSION);

        this.plugin = plugin;

        if (initialize) {
            initialize();
        }
        load();
    }

    /**
     * @return the startFloor
     */
    public ResourceWorld getStartFloor() {
        return startFloor;
    }

    /**
     * @param startFloor the startFloor to set
     */
    public void setStartFloor(ResourceWorld startFloor) {
        this.startFloor = startFloor;
    }

    /**
     * @return the endFloor
     */
    public ResourceWorld getEndFloor() {
        return endFloor;
    }

    /**
     * @param endFloor the endFloor to set
     */
    public void setEndFloor(ResourceWorld endFloor) {
        this.endFloor = endFloor;
    }

    /**
     * @return the floors
     */
    public List<ResourceWorld> getFloors() {
        return floors;
    }

    /**
     * @param resource the resource to add
     */
    public void addFloor(ResourceWorld resource) {
        floors.add(resource);
    }

    /**
     * @param resource the resource to remove
     */
    public void removeFloor(ResourceWorld resource) {
        floors.remove(resource);
    }

    /**
     * @return the floorCount
     */
    public int getFloorCount() {
        return floorCount;
    }

    /**
     * @param floorCount the floorCount to set
     */
    public void setFloorCount(int floorCount) {
        this.floorCount = floorCount;
    }

    /**
     * @return the removeWhenPlayed
     */
    public boolean getRemoveWhenPlayed() {
        return removeWhenPlayed;
    }

    /**
     * @param removeWhenPlayed the removeWhenPlayed to set
     */
    public void setRemoveWhenPlayed(boolean removeWhenPlayed) {
        this.removeWhenPlayed = removeWhenPlayed;
    }

    /**
     * The values from this WorldConfig will override all values of the WorldConfigs of inherited maps.
     *
     * @return the override values
     */
    public WorldConfig getOverrideValues() {
        return overrideValues;
    }

    /**
     * @param worldConfig the WorldConfig to set
     */
    public void setOverrideValues(WorldConfig worldConfig) {
        overrideValues = worldConfig;
    }

    /**
     * The values from this WorldConfig will get overriden by values of the WorldConfigs of inherited maps. They will still override the values from the main
     * config, though.
     *
     * @return the default values
     */
    public WorldConfig getDefaultValues() {
        return defaultValues;
    }

    /**
     * @param worldConfig the WorldConfig to set
     */
    public void setDefaultValues(WorldConfig worldConfig) {
        defaultValues = worldConfig;
    }

    /**
     * @param resource the DResourceWorld to check
     * @return true if the floor is either in the list or the start / end floor.
     */
    public boolean containsFloor(ResourceWorld resource) {
        return floors.contains(resource) || startFloor.equals(resource) || endFloor.equals(resource);
    }

    /**
     * @param mapName the name of the map to check
     * @return true if the floor is either in the list or the start / end floor.
     */
    public boolean containsFloor(String mapName) {
        return containsFloor(plugin.getMapRegistry().get(mapName));
    }

    @Override
    public void load() {
        for (String floor : config.getStringList("floors")) {
            ResourceWorld resource = plugin.getMapRegistry().get(floor);
            if (resource != null) {
                floors.add(resource);
            }
        }
        startFloor = plugin.getMapRegistry().get(config.getString("startFloor"));
        endFloor = plugin.getMapRegistry().get(config.getString("endFloor"));
        floorCount = config.getInt("floorCount", floors.size() + 2);
        removeWhenPlayed = config.getBoolean("removeWhenPlayed", removeWhenPlayed);

        ConfigurationSection overrideSection = config.getConfigurationSection("overrideValues");
        if (overrideSection != null) {
            overrideValues = new WorldConfig(plugin, overrideSection);
        }
        ConfigurationSection defaultSection = config.getConfigurationSection("defaultValues");
        if (defaultValues != null) {
            defaultValues = new WorldConfig(plugin, defaultSection);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{file=" + file.getPath() + "}";
    }

}
