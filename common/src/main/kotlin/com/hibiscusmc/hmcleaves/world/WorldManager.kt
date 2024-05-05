package com.hibiscusmc.hmcleaves.world

import com.hibiscusmc.hmcleaves.block.BlockData
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class WorldManager(
    private val worlds: MutableMap<UUID, LeavesWorld> = ConcurrentHashMap()
) {

    operator fun get(world: UUID): LeavesWorld? {
        return this.worlds[world]
    }

    fun getOrAdd(world: UUID, supplier: (key: UUID) -> LeavesWorld = { LeavesWorld(it) }): LeavesWorld {
        return this.worlds.computeIfAbsent(world, supplier)
    }

    operator fun get(position: Position): BlockData? {
        return this.worlds[position.world]
            ?.get(position.getChunkPosition())
            ?.get(position.toPositionInChunk())
    }

}