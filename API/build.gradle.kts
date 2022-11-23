plugins {
    id("java")
}

group = "com.desticube.core"
version = "1.0-SNAPSHOT"
description = "DestiCube's main core"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://libraries.minecraft.net")

}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.2-R0.1-SNAPSHOT")
    compileOnly("com.electronwill.night-config:toml:3.6.5")
    compileOnly("mysql:mysql-connector-java:8.0.30")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("me.clip:placeholderapi:2.11.2")
    compileOnly("net.luckperms:api:5.4")
    compileOnly("com.github.DestiCube:DestiPlaceholders:8933681736")

    compileOnly("com.github.GamerDuck123:DuckCommons:8aa89c6040")

}