package pl.mlynek.commons.registry.config;

import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.Plugin;
import pl.mlynek.commons.registry.config.configure.DoubleQuotedYamlConfigurer;
import pl.mlynek.commons.registry.config.pack.ItemsSerdesPack;

import java.io.File;
import java.util.HashSet;
import java.util.Set;


/**
 * @Author: mlyn3kk_
 * @Website: https://discord.gg/swircode
 * @Date: 24.08.2026
 * @Project: mCore-server
 * @Description: szkidbi eszkere gigachad
 */
@RequiredArgsConstructor
public class ConfigRegistry {
    private final Set<OkaeriConfig> configs = new HashSet<>();

    public void reload() {
        this.configs.forEach(OkaeriConfig::load);
    }

    public <T extends OkaeriConfig> T create(Class<T> clazz, File file) {
        T configFile = ConfigManager.create(clazz, it -> {
            it.configure(opt -> {
                opt.configurer(new DoubleQuotedYamlConfigurer(), new SerdesBukkit(), new ItemsSerdesPack());
                opt.bindFile(file);
                opt.removeOrphans(true);
            });
            it.saveDefaults();
            it.load(true);
        });
        this.configs.add(configFile);
        return configFile;
    }

    public <T extends OkaeriConfig> T register(Class<T> clazz, Plugin plugin, String fileName) {
        return this.create(clazz, new File(plugin.getDataFolder(), fileName));
    }
}