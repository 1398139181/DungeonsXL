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
package de.erethon.dungeonsxl.sign.script;

import de.erethon.dungeonsxl.DungeonsXL;
import de.erethon.dungeonsxl.api.sign.AbstractDSign;
import de.erethon.dungeonsxl.api.sign.DungeonSignType;
import de.erethon.dungeonsxl.api.sign.Passive;
import de.erethon.dungeonsxl.sign.DSignTypeDefault;
import de.erethon.dungeonsxl.world.DGameWorld;
import org.bukkit.block.Sign;

/**
 * @author Daniel Saukel
 */
public class ScriptSign extends Passive {

    private SignScript script;

    public ScriptSign(DungeonsXL plugin, Sign sign, String[] lines, DGameWorld gameWorld) {
        super(plugin, sign, lines, gameWorld);
    }

    /**
     * @return the script
     */
    public SignScript getScript() {
        return script;
    }

    @Override
    public boolean validate() {
        script = plugin.getSignScriptRegistry().get(lines[1]);
        return script != null;
    }

    @Override
    public void initialize() {
        for (String[] lines : script.getSigns()) {
            AbstractDSign dSign = AbstractDSign.create(plugin, getSign(), lines, getGameWorld());
            if (dSign.isErroneous()) {
                continue;
            }
            getGameWorld().getDSigns().add(dSign);

            dSign.initialize();
            if (!dSign.hasTriggers()) {
                dSign.activate();
            }
        }
    }

    @Override
    public DungeonSignType getType() {
        return DSignTypeDefault.SCRIPT;
    }

}
