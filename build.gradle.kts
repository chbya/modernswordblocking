import xyz.jpenilla.resourcefactory.bukkit.BukkitPluginYaml

plugins {
    `java-library`
    id("io.papermc.paperweight.userdev") version "1.7.3"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("xyz.jpenilla.resource-factory-bukkit-convention") version "1.2.0"
}

group = "de.chbya.modernswordblocking"
version = "1.0.0"
description = "allows you to block like in older minecraft versions"

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION

dependencies {
    paperweight.paperDevBundle("1.21.3-R0.1-SNAPSHOT")
}

tasks {
    compileJava {
        options.release = 21
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    runServer {
    }
}

bukkitPluginYaml {
    main = "net.player005.modernswordblocking.ModernSwordBlockingPaper"
    load = BukkitPluginYaml.PluginLoadOrder.STARTUP
    authors.add("chbya")
    apiVersion = "1.21"
}
