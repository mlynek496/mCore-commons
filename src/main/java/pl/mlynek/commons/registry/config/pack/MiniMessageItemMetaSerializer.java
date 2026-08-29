package pl.mlynek.commons.registry.config.pack;

import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Author: mlyn3kk_
 * @Website: https://discord.gg/swircode
 * @Date: 29.08.2026
 * @Project: mCore-commons
 * @Description: szkidbi eszkere gigachad
 */

public class MiniMessageItemMetaSerializer implements ObjectSerializer<ItemMeta> {
    private static final MiniMessage MM = MiniMessage.miniMessage();

    @Override
    public boolean supports(@NotNull Class<?> type) {
        return ItemMeta.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(ItemMeta itemMeta, @NotNull SerializationData data, @NotNull GenericsDeclaration generics) {
        if (itemMeta.hasDisplayName()) {
            Component name = itemMeta.displayName();
            if (name != null) {
                data.set("display", GradientCollapser.collapse(MM.serialize(stripItalicFalse(name))));
            }
        }
        if (itemMeta.hasLore()) {
            List<Component> lore = itemMeta.lore();
            if (lore != null) {
                data.setCollection("lore", lore.stream().map(line -> GradientCollapser.collapse(MM.serialize(stripItalicFalse(line)))).toList(), String.class);
            }
        }
        if (!itemMeta.getEnchants().isEmpty()) {
            data.setMap("enchantments", itemMeta.getEnchants(), Enchantment.class, Integer.class);
        }
        if (!itemMeta.getItemFlags().isEmpty()) {
            data.setCollection("flags", itemMeta.getItemFlags(), ItemFlag.class);
        }
        if (itemMeta.hasCustomModelData()) {
            data.set("custom-model-data", itemMeta.getCustomModelData());
        }
    }

    @Override
    public ItemMeta deserialize(DeserializationData data, @NotNull GenericsDeclaration generics) {
        String display = data.get("display", String.class);
        if (display == null) {
            display = data.get("display-name", String.class);
        }
        List<String> lore = data.containsKey("lore") ? data.getAsList("lore", String.class) : Collections.emptyList();
        Map<Enchantment, Integer> enchantments = data.containsKey("enchantments") ? data.getAsMap("enchantments", Enchantment.class, Integer.class) : Collections.emptyMap();
        List<ItemFlag> itemFlags = new ArrayList<>(data.containsKey("flags") ? data.getAsList("flags", ItemFlag.class) : Collections.emptyList());
        ItemMeta itemMeta = new ItemStack(Material.COBBLESTONE).getItemMeta();
        if (itemMeta == null) {
            throw new IllegalStateException("Cannot extract empty ItemMeta from COBBLESTONE");
        }
        if (display != null) {
            itemMeta.displayName(applyDefaultItalic(display));
        }
        if (!lore.isEmpty()) {
            itemMeta.lore(lore.stream().map(this::applyDefaultItalic).toList());
        }
        enchantments.forEach((enchantment, level) -> itemMeta.addEnchant(enchantment, level, true));
        itemMeta.addItemFlags(itemFlags.toArray(new ItemFlag[0]));
        if (data.containsKey("custom-model-data")) {
            itemMeta.setCustomModelData(data.get("custom-model-data", Integer.class));
        }
        return itemMeta;
    }

    private Component applyDefaultItalic(String text) {
        String cleanText = text.replace("<!italic>", "").replace("<!i>", "").replace("<italic:false>", "").replace("<i:false>", "").trim();
        Component component = MM.deserialize(cleanText);
        return component.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    private Component stripItalicFalse(Component component) {
        if (component.decoration(TextDecoration.ITALIC) == TextDecoration.State.FALSE) {
            component = component.decoration(TextDecoration.ITALIC, TextDecoration.State.NOT_SET);
        }
        return component.children(component.children().stream().map(this::stripItalicFalse).toList());
    }
}