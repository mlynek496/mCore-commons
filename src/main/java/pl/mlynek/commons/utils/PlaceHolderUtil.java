package pl.mlynek.commons.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: mlyn3kk_
 * @Website: https://discord.gg/swircode
 * @Date: 25.05.2026
 * @Project: krytmc-anabox-addon
 * @Description: szkidbi eszkere gigachad
 */
public class PlaceHolderUtil {

    public static ItemStack applyPlaceholders(Player player, ItemStack item) {
        if (item == null) {
            return null;
        }
        ItemStack newItem = item.clone();
        ItemMeta meta = newItem.getItemMeta();
        if (meta == null) {
            return newItem;
        }
        if (meta.hasDisplayName()) {
            String displayName = meta.getDisplayName();
            displayName = PlaceholderAPI.setPlaceholders(player, displayName);
            meta.displayName(AdventureUtil.miniMessage(displayName, null));
        }
        if (meta.hasLore()) {
            List<String> lore = meta.getLore();
            if (lore != null && !lore.isEmpty()) {
                List<Component> fixedLore = new ArrayList<>();
                for (String line : lore) {
                    line = PlaceholderAPI.setPlaceholders(player, line);
                    fixedLore.add(AdventureUtil.miniMessage(line, null));
                }
                meta.lore(fixedLore);
            }
        }
        newItem.setItemMeta(meta);
        return newItem;
    }

    public static String apply(Player player, String string, Object... objectArray) {
        return apply(player, string, process(objectArray));
    }

    public static String apply(String string, Object... objectArray) {
        return apply(string, process(objectArray));
    }

    public static String apply(Player player, String string, Map<String, String> map) {
        if (string == null) {
            return null;
        }
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String placeholder = "{" + entry.getKey() + "}";
                string = string.replace(placeholder, entry.getValue());
            }
        }
        return PlaceholderAPI.setPlaceholders(player, string);
    }

    private static String apply(String string, Map<String, String> map) {
        if (string == null) {
            return null;
        }
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String placeholder = "{" + entry.getKey() + "}";
                string = string.replace(placeholder, entry.getValue());
            }
        }
        return string;
    }

    public static String apply(Player player, String string) {
        if (string == null) {
            return null;
        }
        return PlaceholderAPI.setPlaceholders(player, string);
    }

    public static Map<String, String> process(Object... objectArray) {
        if (objectArray.length % 2 != 0) {
            throw new IllegalArgumentException("Placeholders must be in the format: key, value.");
        }
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < objectArray.length; i += 2) {
            String key = objectArray[i].toString();
            String value = objectArray[i + 1].toString();
            map.put(key, value);
        }
        return map;
    }
}