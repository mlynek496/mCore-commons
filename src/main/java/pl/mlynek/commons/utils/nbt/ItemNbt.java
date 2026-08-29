package pl.mlynek.commons.utils.nbt;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

/**
 * @Author: mlyn3kk_
 * @Website: https://discord.gg/swircode
 * @Date: 04.07.2026
 * @Project: mCore-anarchia
 * @Description: szkidbi eszkere gigachad
 */
public final class ItemNbt {
    private static Plugin plugin;

    public static boolean hasCustomData(ItemStack itemStack, String string, PersistentDataType persistentDataType) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return false;
        }
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        return persistentDataContainer.has(new NamespacedKey(plugin, string), persistentDataType);
    }

    public static Object getCustomData(ItemStack itemStack, String string, PersistentDataType persistentDataType) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return null;
        }
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        return persistentDataContainer.get(new NamespacedKey(plugin, string), persistentDataType);
    }

    public static ItemStack withCustomData(ItemStack itemStack, String string, Object object, PersistentDataType persistentDataType) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
            persistentDataContainer.set(new NamespacedKey(plugin, string), persistentDataType, object);
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;
    }

    public static void setPlugin(Plugin plugin) {
        ItemNbt.plugin = plugin;
    }

    private ItemNbt() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
