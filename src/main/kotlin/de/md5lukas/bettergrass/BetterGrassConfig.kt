package de.md5lukas.bettergrass

import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.util.Vector
import java.lang.IllegalArgumentException
import java.util.logging.Level

class BetterGrassConfig(private val main: BetterGrass) {

    var seeds: MutableList<Material> = mutableListOf()
        private set
    var blocks: MutableList<Material> = mutableListOf()
        private set
    var soundData: SoundData? = null
        private set
    var particleData: ParticleData? = null
        private set

    fun load() {
        val cfg = main.config

        seeds.clear()
        cfg.getStringList("seeds").map(Material::matchMaterial).forEach {
            if (it != null) {
                seeds.add(it)
            }
        }

        blocks.clear()
        cfg.getStringList("blocks").map(Material::matchMaterial).forEach {
            if (it != null) {
                blocks.add(it)
            }
        }

        loadSoundData(cfg.getConfigurationSection("sound"))
        loadParticleData(cfg.getConfigurationSection("particle"))
    }

    private fun loadSoundData(cfg: ConfigurationSection?) {
        if (cfg == null) {
            soundData = null
            main.logger.log(Level.WARNING, "There is no sound configuration present")
            return
        }

        if(!cfg.getBoolean("enabled", false)) {
            soundData = null
            return
        }

        val name = cfg.getString("sound")
        if (name == null) {
            soundData = null
            main.logger.log(Level.WARNING, "There is no sound name provided")
            return
        }

        val sound: Sound?
        try {
            sound = Sound.valueOf(name)
        } catch (e: IllegalArgumentException) {
            soundData = null
            main.logger.log(Level.WARNING, "Could not find a proper sound with name name $name")
            return
        }

        soundData = SoundData(
            sound,
            cfg.getDouble("volume").toFloat(),
            cfg.getDouble("pitch").toFloat(),
        )
    }
    private fun loadParticleData(cfg: ConfigurationSection?) {
        if (cfg == null) {
            particleData = null
            main.logger.log(Level.WARNING, "There is no particle configuration present")
            return
        }

        if (!cfg.getBoolean("enabled", false)) {
            particleData = null
            return
        }

        val name = cfg.getString("name")
        if (name == null) {
            particleData = null
            main.logger.log(Level.WARNING, "There is no particle name provided")
            return
        }

        val particle: Particle?
        try {
            particle = Particle.valueOf(name)
        } catch (e: IllegalArgumentException) {
            particleData = null
            main.logger.log(Level.WARNING, "Could not find a proper particle with name $name")
            return
        }

        particleData = ParticleData(
            particle,
            cfg.getInt("amount", 20),
            Vector(
                cfg.getDouble("particleCenter.x"),
                cfg.getDouble("particleCenter.y"),
                cfg.getDouble("particleCenter.z")
            ),
            Vector(
                cfg.getDouble("particleArea.x"),
                cfg.getDouble("particleArea.y"),
                cfg.getDouble("particleArea.z")
            )
        )
    }

    class SoundData(val sound: Sound, val volume: Float, val pitch: Float)
    class ParticleData(val particle: Particle, val amount: Int, val particleCenter: Vector, val particleArea: Vector)
}
