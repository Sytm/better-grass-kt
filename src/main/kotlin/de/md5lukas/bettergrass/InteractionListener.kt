package de.md5lukas.bettergrass

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class InteractionListener(private val main: BetterGrass) : Listener {

    @EventHandler
    fun onInteract(e: PlayerInteractEvent) {
        val item = e.item
        val block = e.clickedBlock

        if (item != null && block != null
            && e.action == Action.RIGHT_CLICK_BLOCK
            && main.bgConfig.seeds.contains(item.type)
            && main.bgConfig.blocks.contains(block.type)) {

            item.amount--
            block.type = Material.GRASS_BLOCK

            val world = block.world
            val loc = block.location

            val particleData = main.bgConfig.particleData
            if (particleData != null) {
                world.spawnParticle(
                    particleData.particle,
                    loc.add(particleData.particleCenter),
                    particleData.amount,
                    particleData.particleArea.x,
                    particleData.particleArea.y,
                    particleData.particleArea.z
                )
            }

            val soundData = main.bgConfig.soundData
            if (soundData != null) {
                world.playSound(loc, soundData.sound, soundData.volume, soundData.pitch)
            }
        }
    }
}