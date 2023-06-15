/*
 *
 *  *     HMCLeaves
 *  *     Copyright (C) 2022  Hibiscus Creative Studios
 *  *
 *  *     This program is free software: you can redistribute it and/or modify
 *  *     it under the terms of the GNU General Public License as published by
 *  *     the Free Software Foundation, either version 3 of the License, or
 *  *     (at your option) any later version.
 *  *
 *  *     This program is distributed in the hope that it will be useful,
 *  *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  *     GNU General Public License for more details.
 *  *
 *  *     You should have received a copy of the GNU General Public License
 *  *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package io.github.fisher2911.hmcleaves.hook.oraxen;

import io.github.fisher2911.hmcleaves.HMCLeaves;
import io.github.fisher2911.hmcleaves.config.LeavesConfig;
import io.github.fisher2911.hmcleaves.hook.ItemHook;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import io.th0rgal.oraxen.api.OraxenItems;
import io.th0rgal.oraxen.api.events.OraxenNoteBlockBreakEvent;
import io.th0rgal.oraxen.api.events.OraxenNoteBlockPlaceEvent;
import io.th0rgal.oraxen.items.ItemBuilder;
import io.th0rgal.oraxen.mechanics.MechanicFactory;
import io.th0rgal.oraxen.mechanics.MechanicsManager;
import io.th0rgal.oraxen.mechanics.provided.gameplay.noteblock.NoteBlockMechanic;
import io.th0rgal.oraxen.mechanics.provided.gameplay.noteblock.NoteBlockMechanicFactory;
import org.bukkit.Bukkit;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class OraxenHook implements ItemHook {

    private final HMCLeaves plugin;
    private final LeavesConfig config;

    public OraxenHook(HMCLeaves plugin) {
        this.plugin = plugin;
        this.config = plugin.getLeavesConfig();
    }

    @Override
    @Nullable
    public String getId(ItemStack itemStack) {
        return OraxenItems.getIdByItem(itemStack);
    }

    @Override
    public @Nullable ItemStack getItem(String id) {
        final ItemBuilder builder = OraxenItems.getItemById(id);
        if (builder == null) return null;
        return builder.build();
    }

    @Override
    @Nullable
    public Integer getBlockId(String id) {
        final MechanicFactory mechanicFactory = MechanicsManager.getMechanicFactory("noteblock");
        if (!(mechanicFactory instanceof final NoteBlockMechanicFactory noteBlockMechanicFactory)) {
            return null;
        }
        final NoteBlockMechanic mechanic = (NoteBlockMechanic) noteBlockMechanicFactory.getMechanic(id);
        if (mechanic == null) {
            return null;
        }
        final NoteBlock noteBlock = noteBlockMechanicFactory.createNoteBlockData(id);
        return SpigotConversionUtil.fromBukkitBlockData(noteBlock).getGlobalId();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onNoteblockPlace(OraxenNoteBlockPlaceEvent event) {
        final String id = event.getMechanic().getItemID();
        if (this.config.getItem(id) != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onNoteblockRemove(OraxenNoteBlockBreakEvent event) {
        final String id = event.getMechanic().getItemID();
        if (this.config.getItem(id) != null) {
            event.setCancelled(true);
        }
    }

}
