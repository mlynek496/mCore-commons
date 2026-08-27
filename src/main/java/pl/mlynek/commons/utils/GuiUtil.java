package pl.mlynek.commons.utils;

import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.GuiItem;
import pl.mlynek.commons.adapter.MenuItemAdapter;

import java.util.List;

/**
 * @Author: mlyn3kk_
 * @Website: https://discord.gg/swircode
 * @Date: 24.08.2026
 * @Project: mCore-server
 * @Description: szkidbi eszkere gigachad
 */
public class GuiUtil {

    public static void fillGuiWithGlass(BaseGui gui, List<MenuItemAdapter> decorations) {
        decorations.forEach(decoration -> {
            ItemBuilder itemBuilder = ItemBuilder.of(decoration.getItemStack());
            GuiItem guiItem = new GuiItem(itemBuilder.asItemStack());
            decoration.getSlots().forEach(slot -> gui.setItem(slot, guiItem));
        });
    }
}
