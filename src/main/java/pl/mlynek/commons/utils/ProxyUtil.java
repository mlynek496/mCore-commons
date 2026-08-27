package pl.mlynek.commons.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * @Author: mlyn3kk_
 * @Website: https://discord.gg/swircode
 * @Date: 22.08.2026
 * @Project: mCore-server
 * @Description: szkidbi eszkere gigachad
 */
public final class ProxyUtil {

    private ProxyUtil() {
    }

    public static void sendPlayerToServer(Plugin plugin, Player player, String server) {
        if (plugin == null || player == null || server == null || server.isEmpty()) {
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }

    public static void sendAllPlayersFromServerToServer(Plugin plugin, String fromServer, String targetServer) {
        if (plugin == null || fromServer == null || fromServer.isEmpty() || targetServer == null || targetServer.isEmpty()) {
            return;
        }
        Player messenger = Bukkit.getOnlinePlayers().stream().findFirst().orElse(null);
        if (messenger == null) {
            return;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("ExecuteCommand");
        out.writeUTF("send " + fromServer + " " + targetServer);
        messenger.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }
}
