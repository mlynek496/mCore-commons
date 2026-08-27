package pl.mlynek.commons.utils.message;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.util.Ticks;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.mlynek.commons.utils.AdventureUtil;

import java.util.List;
import java.util.Map;

/**
 * @Author: mlyn3kk_
 * @Website: https://discord.gg/swircode
 * @Date: 22.08.2026
 * @Project: mCore-server
 * @Description: szkidbi eszkere gigachad
 */
public class PlayerMessage {
    public static void message(CommandSender sender, MessageType type, String message, Map<String, String> placeholders) {
        PlayerMessage.sendMessageToSender(sender, type, message, placeholders);
    }

    public static void message(CommandSender sender, MessageType type, String message) {
        PlayerMessage.message(sender, type, message, null);
    }

    public static void message(MessageType type, String message) {
        Bukkit.getOnlinePlayers().forEach(online -> PlayerMessage.message(online, type, message));
    }

    public static void broadcast(MessageType type, String message) {
        Bukkit.getOnlinePlayers().forEach(p -> PlayerMessage.message(p, type, message));
    }

    public static void message(MessageType type, String message, String permission) {
        Bukkit.getOnlinePlayers().forEach(online -> {
            if (online.hasPermission(permission)) {
                PlayerMessage.message(online, type, message);
            }
        });
    }

    public static void message(List<Component> components, String permission) {
        Bukkit.getOnlinePlayers().forEach(online -> {
            if (online.hasPermission(permission)) {
                PlayerMessage.sendMessagesToSender(online, components);
            }
        });
    }

    public static void message(CommandSender sender, List<Component> components) {
        PlayerMessage.sendMessagesToSender(sender, components);
    }

    private static void sendMessageToSender(CommandSender sender, MessageType type, String rawMessage, Map<String, String> placeholders) {
        if (sender instanceof Player player) {
            switch (type) {
                case TITLE: {
                    Component title = AdventureUtil.miniMessage(rawMessage, placeholders);
                    player.showTitle(Title.title(title, Component.empty(), Title.Times.times(Ticks.duration(10L), Ticks.duration(60L), Ticks.duration(20L))));
                    break;
                }
                case TITLE_SUBTITLE: {
                    String[] parts = rawMessage.split("\n", 2);
                    Component title = AdventureUtil.miniMessage(parts[0], placeholders);
                    TextComponent subtitle = parts.length > 1 ? (TextComponent)AdventureUtil.miniMessage(parts[1], placeholders) : Component.empty();
                    player.showTitle(Title.title(title, subtitle, Title.Times.times(Ticks.duration(10L), Ticks.duration(60L), Ticks.duration(20L))));
                    break;
                }
                case SUBTITLE: {
                    Component subtitle = AdventureUtil.miniMessage(rawMessage, placeholders);
                    player.showTitle(Title.title(Component.empty(), subtitle, Title.Times.times(Ticks.duration(10L), Ticks.duration(60L), Ticks.duration(20L))));
                    break;
                }
                case ACTIONBAR: {
                    Component actionbar = AdventureUtil.miniMessage(rawMessage, placeholders);
                    player.sendActionBar(actionbar);
                    break;
                }
                case CHAT: {
                    Component chat = AdventureUtil.miniMessage(rawMessage, placeholders);
                    player.sendMessage(chat);
                }
            }
        } else {
            Component comp = AdventureUtil.miniMessage(rawMessage, placeholders);
            sender.sendMessage(comp);
        }
    }

    private static void sendMessagesToSender(CommandSender sender, List<Component> components) {
        if (sender instanceof Player p) {
            components.forEach(p::sendMessage);
        } else {
            components.forEach(sender::sendMessage);
        }
    }
}

