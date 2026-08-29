package pl.mlynek.commons.adapter;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import pl.mlynek.commons.utils.adventure.AdventureUtil;

/**
 * @Author: mlyn3kk_
 * @Website: https://discord.gg/swircode
 * @Date: 24.08.2026
 * @Project: mCore-server
 * @Description: szkidbi eszkere gigachad
 */
public class MenuAdapter {

    public static Gui createGUI(int rows, String title, boolean disableAllInteractions) {
        Gui gui = Gui.gui().title(AdventureUtil.translate(title)).rows(rows).create();
        if (disableAllInteractions) {
            gui.disableAllInteractions();
        }
        return gui;
    }

    public static PaginatedGui createPaginatedGUI(int rows, int pageSize, String title, boolean disableAllInteractions) {
        PaginatedGui gui = Gui.paginated().title(AdventureUtil.translate(title)).rows(rows).pageSize(pageSize).create();
        if (disableAllInteractions) {
            gui.disableAllInteractions();
        }
        return gui;
    }
}
