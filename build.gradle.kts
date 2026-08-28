plugins {
    `java-library`
    `maven-publish`
}

group = "com.github.mlynek496"
version = "1.0.5"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://maven.enginehub.org/repo/")
    maven("https://repo.panda-lang.org/releases")
    maven("https://repo.okaeri.cloud/releases")
    maven("https://storehouse.okaeri.eu/repository/maven-public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    implementation("net.kyori:adventure-api:5.2.0")
    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")
    compileOnly("dev.rollczi:litecommands-bukkit:3.10.9")
    compileOnly("dev.morphia.morphia:morphia-core:2.5.3")
    compileOnly("dev.triumphteam:triumph-gui:3.1.13")
    compileOnly("eu.okaeri:okaeri-configs-yaml-bukkit:5.0.13")
    compileOnly("eu.okaeri:okaeri-configs-serdes-bukkit:5.0.13")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.13")
    compileOnly("com.mojang:authlib:1.5.25")
    compileOnly("me.clip:placeholderapi:2.12.2")
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

configurations.all {
    resolutionStrategy {
        force("com.google.code.gson:gson:2.11.0")
    }
}