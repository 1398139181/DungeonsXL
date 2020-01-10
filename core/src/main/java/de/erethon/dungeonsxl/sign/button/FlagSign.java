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

import de.erethon.commons.misc.NumberUtil;
import de.erethon.dungeonsxl.DungeonsXL;
import de.erethon.dungeonsxl.api.sign.Button;
import de.erethon.dungeonsxl.api.sign.DungeonSignType;
import de.erethon.dungeonsxl.sign.DSignTypeDefault;
import de.erethon.dungeonsxl.world.DGameWorld;
import de.erethon.dungeonsxl.world.block.TeamFlag;
import org.bukkit.block.Sign;

/**
 * @author Daniel Saukel
 */
public class FlagSign extends Button {

    private int team;

    public FlagSign(DungeonsXL plugin, Sign sign, String[] lines, DGameWorld gameWorld) {
        super(plugin, sign, lines, gameWorld);
    }

    /* Getters and setters */
    @Override
    public DungeonSignType getType() {
        return DSignTypeDefault.FLAG;
    }

    /* Actions */
    @Override
    public boolean validate() {
        return NumberUtil.parseInt(lines[1], -1) != -1;
    }

    @Override
    public void initialize() {
        this.team = NumberUtil.parseInt(lines[1]);
        if (getGame().getDGroups().size() > team) {
            getGameWorld().addGameBlock(new TeamFlag(plugin, getSign().getBlock(), getGame().getDGroups().get(team)));
        }
    }

}
