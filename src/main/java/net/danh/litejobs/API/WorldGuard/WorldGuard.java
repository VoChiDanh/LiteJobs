package net.danh.litejobs.API.WorldGuard;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.codemc.worldguardwrapper.WorldGuardWrapper;
import org.codemc.worldguardwrapper.flag.IWrappedFlag;
import org.codemc.worldguardwrapper.flag.WrappedState;

import java.util.Optional;
import java.util.logging.Level;

public class WorldGuard {

    public static void register(final JavaPlugin plugin) {
        Plugin worldGuard = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
        if (worldGuard != null) {
            WorldGuardWrapper wrapper = WorldGuardWrapper.getInstance();
            Optional<IWrappedFlag<WrappedState>> miningFlag = wrapper.registerFlag("litejobs-block-break", WrappedState.class, WrappedState.ALLOW);
            miningFlag.ifPresent(wrappedStateIWrappedFlag -> plugin.getLogger().log(Level.INFO, "Registered flag " + wrappedStateIWrappedFlag.getName()));
        }
    }

    public static boolean handleForLocation(Player player, Location loc, Cancellable e, String flag_name) {
        IWrappedFlag<WrappedState> flag = getStateFlag(flag_name);
        if (flag == null) {
            return true;
        }

        WrappedState state = WorldGuardWrapper.getInstance().queryFlag(player, loc, flag).orElse(WrappedState.ALLOW);
        if (state == WrappedState.DENY) {
            e.setCancelled(true);
            return false;
        }
        return true;
    }

    public static IWrappedFlag<WrappedState> getStateFlag(String flagName) {
        Optional<IWrappedFlag<WrappedState>> flagOptional = WorldGuardWrapper.getInstance().getFlag(flagName, WrappedState.class);
        return flagOptional.orElse(null);
    }
}
