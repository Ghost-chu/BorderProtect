package com.ghostchu.borderprotect;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public class Main extends JavaPlugin implements Listener {
    private final Set<Chunk> cancelledChunk = new HashSet<>();
    @Override
    public void onEnable() {
        saveDefaultConfig();
        Util.parseColours(getConfig());
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    // World unload clean up
    @EventHandler(ignoreCancelled = true,priority = EventPriority.MONITOR)
    public void worldCleanup(WorldUnloadEvent event){
        cancelledChunk.removeIf(c->c.getWorld().equals(event.getWorld()));
    }

    @EventHandler(ignoreCancelled = true,priority = EventPriority.HIGHEST)
    public void onTeleport(PlayerTeleportEvent e){
        if(!getConfig().getBoolean("management.teleport",false))
            return;
        if(e.getTo() == null)
            return;
        if(e.getTo().getWorld() == null)
            return;
        if(e.getTo().getWorld().getWorldBorder().isInside(e.getTo()))
            return;
        if(e.getPlayer().getGameMode() != GameMode.SURVIVAL && e.getPlayer().getGameMode() != GameMode.ADVENTURE)
            return;
        e.setCancelled(true);
        e.getPlayer().sendMessage(getConfig().getString("message.warning-teleport"));
        e.getTo().getChunk().unload(false);
    }
    @EventHandler(ignoreCancelled = true,priority = EventPriority.HIGHEST)
    public void onTeleport(PlayerPortalEvent e){
        if(!getConfig().getBoolean("management.portal",false))
            return;
        if(e.getTo() == null)
            return;
        if(e.getTo().getWorld() == null)
            return;
        if(e.getTo().getWorld().getWorldBorder().isInside(e.getTo()))
            return;
        if(e.getPlayer().getGameMode() != GameMode.SURVIVAL && e.getPlayer().getGameMode() != GameMode.ADVENTURE)
            return;
        e.setCancelled(true);
        e.getPlayer().sendMessage(getConfig().getString("message.warning-portal"));
        e.getTo().getChunk().unload(false);
    }
    @EventHandler(ignoreCancelled = true,priority = EventPriority.HIGHEST)
    public void onTeleport(PlayerJoinEvent e){
        if(!getConfig().getBoolean("management.join",false))
            return;
        if(e.getPlayer().getWorld().getWorldBorder().isInside(e.getPlayer().getLocation()))
            return;
        if(e.getPlayer().getGameMode() != GameMode.SURVIVAL && e.getPlayer().getGameMode() != GameMode.ADVENTURE)
            return;
        e.getPlayer().teleport(e.getPlayer().getWorld().getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        e.getPlayer().sendMessage(getConfig().getString("message.warning-join"));
    }
    @EventHandler(ignoreCancelled = true,priority = EventPriority.HIGHEST)
    public void onTeleport(PlayerRespawnEvent e){
        if(!getConfig().getBoolean("management.respawn",false))
            return;
        if(e.getPlayer().getWorld().getWorldBorder().isInside(e.getPlayer().getLocation()))
            return;
        if(e.getPlayer().getGameMode() != GameMode.SURVIVAL && e.getPlayer().getGameMode() != GameMode.ADVENTURE)
            return;
        e.getPlayer().teleport(e.getPlayer().getWorld().getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        e.getPlayer().sendMessage(getConfig().getString("message.warning-respawn"));
        e.setRespawnLocation(e.getPlayer().getWorld().getSpawnLocation());
    }
    @EventHandler(ignoreCancelled = true,priority = EventPriority.MONITOR)
    public void onTeleport(ChunkUnloadEvent e){
        if(!cancelledChunk.contains(e.getChunk()))
            return;
        e.setSaveChunk(false);
        getLogger().info("Chunk save at "+e.getChunk().getWorld().getName()+"#"+e.getChunk().getX()+"#"+e.getChunk().getZ()+" has been cancelled, because it is created/loaded by teleport to a position that out of side of the border");
    }
}
