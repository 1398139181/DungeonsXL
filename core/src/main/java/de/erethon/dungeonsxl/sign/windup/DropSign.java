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
package de.erethon.dungeonsxl.sign.windup;

import de.erethon.caliburn.item.ExItem;
import de.erethon.commons.misc.NumberUtil;
import de.erethon.dungeonsxl.DungeonsXL;
import de.erethon.dungeonsxl.api.sign.DungeonSignType;
import de.erethon.dungeonsxl.api.sign.Windup;
import de.erethon.dungeonsxl.sign.DSignTypeDefault;
import de.erethon.dungeonsxl.world.DGameWorld;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Frank Baumann, Milan Albrecht, Daniel Saukel
 */
public class DropSign extends Windup {

    private ItemStack item;

    public DropSign(DungeonsXL plugin, Sign sign, String[] lines, DGameWorld gameWorld) {
        super(plugin, sign, lines, gameWorld);
    }

    /* Getters and setters */
    /**
     * @return the item
     */
    public ItemStack getItem() {
        return item;
    }

    /**
     * @param item the item to set
     */
    public void setItem(ItemStack item) {
        this.item = item;
    }

    /* Actions */
    @Override
    public boolean validate() {
        return plugin.getCaliburn().getExItem(lines[1]) != null;
    }

    @Override
    public void initialize() {
        ExItem item = plugin.getCaliburn().getExItem(lines[1]);

        String[] attributes = lines[2].split(",");
        if (attributes.length >= 1) {
            this.item = item.toItemStack(NumberUtil.parseInt(attributes[0], 1));
        }
        if (attributes.length == 2) {
            interval = NumberUtil.parseDouble(attributes[1]);
        }
    }

    @Override
    public void activate() {
        final Location spawnLocation = getSign().getLocation().add(0.5, 0, 0.5);
        if (interval < 0) {
            getSign().getWorld().dropItem(spawnLocation, item);

        } else {

            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        spawnLocation.getWorld().dropItem(spawnLocation, item);
                    } catch (NullPointerException exception) {
                        cancel();
                    }
                }
            }.runTaskTimer(plugin, getIntervalTicks(), getIntervalTicks());
        }
    }

    @Override
    public DungeonSignType getType() {
        return DSignTypeDefault.DROP;
    }

}
