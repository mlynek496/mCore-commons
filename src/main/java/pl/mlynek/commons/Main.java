package pl.mlynek.commons;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @Author: mlyn3kk_
 * @Website: https://discord.gg/swircode
 * @Date: 28.08.2026
 * @Project: Default (Template) Project
 * @Description: szkidbi eszkere gigachad
 */
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main extends JavaPlugin {
    @Getter
    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
    }
}