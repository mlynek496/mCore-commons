package pl.mlynek.commons.utils;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ItemBuilder {
    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }

    private ItemBuilder(Material material, int amount) {
        this.itemStack = new ItemStack(material, amount);
        this.itemMeta = this.itemStack.getItemMeta();
    }

    public static ItemBuilder of(Material material) {
        return new ItemBuilder(material, 1);
    }

    public static ItemBuilder of(ItemStack itemStack) {
        return new ItemBuilder(itemStack.clone());
    }

    public ItemBuilder name(String name) {
        if (this.itemMeta != null) {
            this.itemMeta.displayName(AdventureUtil.miniMessage(name, null));
            this.refreshMeta();
        }
        return this;
    }

    public ItemBuilder lore(List<String> strings) {
        if (this.itemMeta != null) {
            this.itemMeta.lore(AdventureUtil.miniMessage(strings, null));
            this.refreshMeta();
        }
        return this;
    }

    public ItemBuilder lore(String... strings) {
        return this.lore(Arrays.asList(strings));
    }

    public ItemBuilder appendLore(String... lines) {
        return this.appendLore(Arrays.asList(lines));
    }

    public ItemBuilder appendLore(List<String> strings) {
        if (this.itemMeta != null) {
            List<Component> currentLore = this.itemMeta.lore();
            List<Component> lore = (this.itemMeta.hasLore() && currentLore != null) ? new ArrayList<>(currentLore) : new ArrayList<>();
            lore.addAll(AdventureUtil.miniMessage(strings, null));
            this.itemMeta.lore(lore);
            this.refreshMeta();
        }
        return this;
    }

    public ItemBuilder glow() {
        if (this.itemMeta != null) {
            this.itemMeta.addEnchant(Enchantment.UNBREAKING, 1, true);
            this.itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            this.refreshMeta();
        }
        return this;
    }

    public ItemBuilder amount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder flag(ItemFlag... itemFlags) {
        if (this.itemMeta != null) {
            this.itemMeta.addItemFlags(itemFlags);
            this.refreshMeta();
        }
        return this;
    }

    public ItemBuilder enchantment(Enchantment enchantment, int level) {
        if (this.itemMeta != null) {
            this.itemMeta.addEnchant(enchantment, level, true);
            this.refreshMeta();
        }
        return this;
    }

    @SuppressWarnings("deprecation")
    public ItemBuilder setCustomModelData(int customModelData) {
        if (this.itemMeta != null) {
            this.itemMeta.setCustomModelData(customModelData);
            this.refreshMeta();
        }
        return this;
    }

    public ItemBuilder texture(String texture) {
        if (this.itemStack.getType() != Material.PLAYER_HEAD) {
            return this;
        }
        if (!(this.itemMeta instanceof SkullMeta skullMeta)) {
            return this;
        }
        this.setSkullTexture(skullMeta, texture);
        this.refreshMeta();
        return this;
    }

    public ItemBuilder head(Player player) {
        if (this.itemStack.getType().equals(Material.PLAYER_HEAD) && this.itemMeta instanceof SkullMeta skullMeta) {
            skullMeta.setOwningPlayer(player);
            this.refreshMeta();
        }
        return this;
    }

    public void setSkullTexture(SkullMeta meta, String textureValue) {
        try {
            PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
            profile.setProperty(new ProfileProperty("textures", textureValue));
            meta.setPlayerProfile(profile);
        } catch (Exception paperException) {
            try {
                org.bukkit.profile.PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID());
                profile.getTextures().setSkin(new java.net.URL("https://textures.minecraft.net/texture/" + textureValue));
                meta.setOwnerProfile(profile);
            } catch (Exception bukkitException) {
                try {
                    GameProfile profile = new GameProfile(UUID.randomUUID(), "");
                    profile.getProperties().put("textures", new Property("textures", textureValue));
                    Field profileField = meta.getClass().getDeclaredField("profile");
                    profileField.setAccessible(true);
                    profileField.set(meta, profile);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ItemMeta getMeta() {
        return this.itemMeta;
    }

    public void refreshMeta() {
        if (this.itemMeta != null) {
            this.itemStack.setItemMeta(this.itemMeta);
        }
    }

    public ItemStack asItemStack() {
        this.refreshMeta();
        return this.itemStack;
    }
}