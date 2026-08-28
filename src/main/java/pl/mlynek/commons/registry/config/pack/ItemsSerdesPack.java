package pl.mlynek.commons.registry.config.pack;

import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.configs.serdes.SerdesRegistry;
import eu.okaeri.configs.yaml.bukkit.serdes.itemstack.ItemStackFailsafe;
import eu.okaeri.configs.yaml.bukkit.serdes.serializer.ItemStackSerializer;

/**
 * @Author: mlyn3kk_
 * @Website: https://discord.gg/swircode
 * @Date: 29.08.2026
 * @Project: mCore-commons
 * @Description: szkidbi eszkere gigachad
 */
public class ItemsSerdesPack implements OkaeriSerdesPack {
    @Override
    public void register(SerdesRegistry registry) {
        registry.register(new ItemStackSerializer(ItemStackFailsafe.NONE));
        registry.register(new MiniMessageItemMetaSerializer());
    }
}