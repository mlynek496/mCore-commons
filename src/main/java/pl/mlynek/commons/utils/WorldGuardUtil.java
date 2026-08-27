package pl.mlynek.commons.utils;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class WorldGuardUtil {
    public static boolean isDeniedFlag(Location location, Player player, StateFlag stateFlag) {
        LocalPlayer localPlayer = player != null ? WorldGuardPlugin.inst().wrapPlayer(player) : null;
        RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery regionQuery = regionContainer.createQuery();
        com.sk89q.worldedit.util.Location location2 = BukkitAdapter.adapt(location);
        if (WorldGuardUtil.isInRegion(location2)) {
            return !regionQuery.testState(location2, localPlayer);
        }
        boolean bl = WorldGuardUtil.canUseInGlobal(BukkitAdapter.adapt(location.getWorld()), stateFlag);
        return location.getWorld() != null && !bl;
    }

    public static boolean isInRegion(Player player) {
        return WorldGuardUtil.isInRegion(BukkitAdapter.adapt(player.getLocation()));
    }

    public static boolean isInRegion(com.sk89q.worldedit.util.Location location) {
        RegionQuery regionQuery = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery();
        ApplicableRegionSet applicableRegionSet = regionQuery.getApplicableRegions(location);
        return !applicableRegionSet.getRegions().isEmpty();
    }

    private static boolean canUseInGlobal(World world, StateFlag stateFlag) {
        RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = regionContainer.get(world);
        if (regionManager != null) {
            ProtectedRegion protectedRegion = regionManager.getRegion("__global__");
            return protectedRegion != null && protectedRegion.getFlag(stateFlag) != StateFlag.State.DENY;
        }
        return true;
    }

    public static boolean isInSpecificRegion(Player p, String string) {
        com.sk89q.worldedit.util.Location location2 = BukkitAdapter.adapt(p.getLocation());
        RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery regionQuery = regionContainer.createQuery();
        ApplicableRegionSet applicableRegionSet = regionQuery.getApplicableRegions(location2);
        for (ProtectedRegion protectedRegion : applicableRegionSet) {
            if (!protectedRegion.getId().equalsIgnoreCase(string)) continue;
            return true;
        }
        return false;
    }

    public static boolean isPlayerInAnyRegion(Player player, List<String> regions) {
        return regions.stream().anyMatch(region -> WorldGuardUtil.isInSpecificRegion(player, region));
    }
}
