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
package de.erethon.dungeonsxl.sign.button;

import de.erethon.caliburn.item.VanillaItem;
import de.erethon.dungeonsxl.DungeonsXL;
import de.erethon.dungeonsxl.api.sign.Button;
import de.erethon.dungeonsxl.api.sign.DungeonSignType;
import de.erethon.dungeonsxl.player.DClass;
import de.erethon.dungeonsxl.player.DGamePlayer;
import de.erethon.dungeonsxl.sign.DSignTypeDefault;
import de.erethon.dungeonsxl.trigger.InteractTrigger;
import de.erethon.dungeonsxl.world.DGameWorld;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

/**
 * @author Frank Baumann, Daniel Saukel
 */
public class ClassesSign extends Button {

    private DClass dClass;

    public ClassesSign(DungeonsXL plugin, Sign sign, String[] lines, DGameWorld gameWorld) {
        super(plugin, sign, lines, gameWorld);
        dClass = plugin.getDClassCache().getByName(sign.getLine(1));
    }

    /* Getters and setters */
    /**
     * @return the DClass of the sign
     */
    public DClass getDClass() {
        return dClass;
    }

    /**
     * @param dClass the DClass to set
     */
    public void setDClass(DClass dClass) {
        this.dClass = dClass;
    }

    /* Actions */
    @Override
    public boolean validate() {
        return plugin.getDClassCache().getByName(lines[1]) != null;
    }

    @Override
    public void initialize() {
        if (dClass == null) {
            markAsErroneous("No such class");
            return;
        }

        if (!getTriggers().isEmpty()) {
            getSign().getBlock().setType(VanillaItem.AIR.getMaterial());
            return;
        }

        InteractTrigger trigger = InteractTrigger.getOrCreate(0, getSign().getBlock(), getGameWorld());
        if (trigger != null) {
            trigger.addListener(this);
            addTrigger(trigger);
        }

        getSign().setLine(0, ChatColor.DARK_BLUE + "############");
        getSign().setLine(1, ChatColor.DARK_GREEN + dClass.getName());
        getSign().setLine(2, "");
        getSign().setLine(3, ChatColor.DARK_BLUE + "############");
        getSign().update();

        getGame().setClasses(true);
    }

    @Override
    public boolean push(Player player) {
        DGamePlayer.getByPlayer(player).setDClass(dClass);
        return true;
    }

    @Override
    public DungeonSignType getType() {
        return DSignTypeDefault.CLASSES;
    }

}
