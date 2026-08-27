plugins {
    `java-library`
    `maven-publish`
}

group = "com.github.mlynek496"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://maven.enginehub.org/repo/")
    maven("https://repo.helpch.at/releases")
    maven("https://repo.panda-lang.org/releases")
    maven("https://repo.okaeri.cloud/releases")
    maven("https://storehouse.okaeri.eu/repository/maven-public/")
    maven("https://projectlombok.org/edge-releases")
    maven("https://repo.eternalcode.pl/releases")
    maven("https://repo.triumphteam.dev/snapshots/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    implementation("net.kyori:adventure-api:5.2.0")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.13")
    compileOnly("com.mojang:authlib:1.5.25")
    compileOnly("me.clip:placeholderapi:2.12.2")
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
    implementation("dev.rollczi:litecommands-bukkit:3.10.9")
    implementation("dev.morphia.morphia:morphia-core:2.5.3")
    implementation("dev.triumphteam:triumph-gui:3.1.13")
    implementation("eu.okaeri:okaeri-configs-yaml-bukkit:5.0.13")
    implementation("eu.okaeri:okaeri-configs-serdes-bukkit:5.0.13")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}

tasks.test {
    useJUnitPlatform()
}