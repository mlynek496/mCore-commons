package pl.mlynek.commons.adapter;

import eu.okaeri.configs.OkaeriConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @Author: mlyn3kk_
 * @Website: https://discord.gg/swircode
 * @Date: 24.08.2026
 * @Project: mCore-server
 * @Description: szkidbi eszkere gigachad
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemAdapter extends OkaeriConfig {
    private List<Integer> slots;
    private int cmd;
    private ItemStack itemStack;
}

