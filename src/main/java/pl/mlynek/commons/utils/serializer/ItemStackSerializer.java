package pl.mlynek.commons.utils.serializer;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.io.*;
import java.util.Base64;

/**
 * @Author: mlyn3kk_
 * @Website: https://discord.gg/swircode
 * @Date: 11.06.2026
 * @Project: mCore-anarchia
 * @Description: szkidbi eszkere gigachad
 */

/**
 * @Author: mlyn3kk_
 * @Website: https://discord.gg/swircode
 * @Date: 11.06.2026
 * @Project: mCore-anarchia
 * @Description: szkidbi eszkere gigachad
 */
@SuppressWarnings("unused")
public class ItemStackSerializer {

    public static String itemStackToBase64(ItemStack item) {
        if (item == null || item.getType().isAir()) {
            return "";
        }
        return Base64.getEncoder().encodeToString(item.serializeAsBytes());
    }

    public static ItemStack itemStackFromBase64(String data) {
        if (data == null || data.isEmpty()) {
            return null;
        }
        return ItemStack.deserializeBytes(Base64.getDecoder().decode(data));
    }

    public static String itemStackArrayToBase64(ItemStack[] items) throws IllegalStateException {
        if (items == null) {
            return "";
        }
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             DataOutputStream dataOutput = new DataOutputStream(outputStream)) {
            dataOutput.writeInt(items.length);
            for (ItemStack item : items) {
                if (item == null || item.getType().isAir()) {
                    dataOutput.writeInt(0);
                } else {
                    byte[] bytes = item.serializeAsBytes();
                    dataOutput.writeInt(bytes.length);
                    dataOutput.write(bytes);
                }
            }
            return Base64.getEncoder().encodeToString(outputStream.toByteArray());
        } catch (IOException e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }

    public static ItemStack[] itemStackArrayFromBase64(String data) throws IOException {
        if (data == null || data.isEmpty()) {
            return new ItemStack[0];
        }
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(data));
             DataInputStream dataInput = new DataInputStream(inputStream)) {
            int length = dataInput.readInt();
            ItemStack[] items = new ItemStack[length];
            for (int i = 0; i < length; i++) {
                int byteLength = dataInput.readInt();
                if (byteLength > 0) {
                    byte[] itemBytes = new byte[byteLength];
                    dataInput.readFully(itemBytes);
                    items[i] = ItemStack.deserializeBytes(itemBytes);
                }
            }
            return items;
        }
    }

    public static String toBase64(Inventory inventory) throws IllegalStateException {
        if (inventory == null) {
            return "";
        }
        return itemStackArrayToBase64(inventory.getContents());
    }

    public static Inventory fromBase64(String base64Data) throws IOException {
        if (base64Data == null || base64Data.isEmpty()) {
            return null;
        }
        ItemStack[] items = itemStackArrayFromBase64(base64Data);
        Inventory inventory = Bukkit.createInventory(null, items.length);
        inventory.setContents(items);
        return inventory;
    }

    public static String[] playerInventoryToBase64(PlayerInventory playerInventory) throws IllegalStateException {
        if (playerInventory == null) {
            return new String[]{"", ""};
        }
        String content = itemStackArrayToBase64(playerInventory.getStorageContents());
        String armor = itemStackArrayToBase64(playerInventory.getArmorContents());
        return new String[]{content, armor};
    }

    public static ItemStack[] inventoryFromBase64(String base64Data) {
        if (base64Data == null || base64Data.isEmpty()) {
            return new ItemStack[0];
        }
        try {
            return itemStackArrayFromBase64(base64Data);
        } catch (Exception e) {
            return new ItemStack[0];
        }
    }
}