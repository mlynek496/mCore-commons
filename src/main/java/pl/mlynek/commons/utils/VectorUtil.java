package pl.mlynek.commons.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * @Author: mlyn3kk_
 * @Website: https://discord.gg/swircode
 * @Date: 08.08.2026
 * @Project: mCore-anarchia-recode
 * @Description: szkidbi eszkere gigachad
 */
public class VectorUtil {
    public static void push(Player player, double pushStrength) {
        if (pushStrength == 0.0) {
            pushStrength = 1.0;
        }
        Vector forwardDirection = player.getLocation().getDirection().normalize();
        Vector pushVector = forwardDirection.multiply(pushStrength);
        player.setVelocity(player.getVelocity().add(pushVector));
    }

    public static void knockBack(Player player, Location location) {
        Vector vector = location.toVector().subtract(player.getLocation().toVector()).normalize().multiply(1.4).setY(0.1);
        if (player.isInsideVehicle()) {
            player.leaveVehicle();
        }
        player.setVelocity(vector);
    }
}