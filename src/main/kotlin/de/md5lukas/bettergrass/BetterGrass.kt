package de.md5lukas.bettergrass

import org.bukkit.plugin.java.JavaPlugin

class BetterGrass : JavaPlugin() {

    lateinit var bgConfig: BetterGrassConfig
        private set

    override fun onEnable() {
        bgConfig = BetterGrassConfig(this)
        server.pluginManager.registerEvents(InteractionListener(this), this)
    }
}