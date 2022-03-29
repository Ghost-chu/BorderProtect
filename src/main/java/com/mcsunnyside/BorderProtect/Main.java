package com.mcsunnyside.BorderProtect;

import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public class Main extends JavaPlugin implements Listener {
    private final Set<Chunk> cancelledChunk = new HashSet<>();
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler(ignoreCancelled = true,priority = EventPriority.HIGHEST)
    public void onTeleport(PlayerTeleportEvent e){
        if(e.getTo().getWorld().getWorldBorder().isInside(e.getTo()))
            return;
        if(e.getPlayer().getGameMode() != GameMode.SURVIVAL && e.getPlayer().getGameMode() != GameMode.ADVENTURE)
            return;
        e.setCancelled(true);
        e.getPlayer().sendMessage(ChatColor.RED+"警告：你正在尝试传送到边界外，这会导致你立即死亡，因此传送已被取消");
        cancelledChunk.add(e.getTo().getChunk());
    }
    @EventHandler(ignoreCancelled = true,priority = EventPriority.HIGHEST)
    public void onTeleport(PlayerPortalEvent e){
        if(e.getTo().getWorld().getWorldBorder().isInside(e.getTo()))
            return;
        if(e.getPlayer().getGameMode() != GameMode.SURVIVAL && e.getPlayer().getGameMode() != GameMode.ADVENTURE)
            return;
        e.setCancelled(true);
        e.getPlayer().sendMessage(ChatColor.RED+"警告：你正在尝试通过地狱门传送到边界外，这会导致你立即死亡，因此传送已被取消，建议您将此传送门拆除");
        cancelledChunk.add(e.getTo().getChunk());
    }
    @EventHandler(ignoreCancelled = true,priority = EventPriority.HIGHEST)
    public void onTeleport(PlayerJoinEvent e){
        if(e.getPlayer().getWorld().getWorldBorder().isInside(e.getPlayer().getLocation()))
            return;
        if(e.getPlayer().getGameMode() != GameMode.SURVIVAL && e.getPlayer().getGameMode() != GameMode.ADVENTURE)
            return;
        e.getPlayer().teleport(e.getPlayer().getWorld().getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        e.getPlayer().sendMessage(ChatColor.RED+"警告：你在边界外尝试登陆服务器，这会导致你立即死亡，您已被传送至默认出生点");
    }
    @EventHandler(ignoreCancelled = true,priority = EventPriority.HIGHEST)
    public void onTeleport(PlayerRespawnEvent e){
        if(e.getPlayer().getWorld().getWorldBorder().isInside(e.getPlayer().getLocation()))
            return;
        if(e.getPlayer().getGameMode() != GameMode.SURVIVAL && e.getPlayer().getGameMode() != GameMode.ADVENTURE)
            return;
        e.getPlayer().teleport(e.getPlayer().getWorld().getSpawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
        e.getPlayer().sendMessage(ChatColor.RED+"警告：你正在尝试复活在边界外的位置，这会导致你立即死亡，你已被传送至默认出生点");
        e.setRespawnLocation(e.getPlayer().getWorld().getSpawnLocation());
    }
    @EventHandler(ignoreCancelled = true,priority = EventPriority.MONITOR)
    public void onTeleport(ChunkUnloadEvent e){
        if(!cancelledChunk.contains(e.getChunk()))
            return;
        e.setSaveChunk(false);
        getLogger().info("取消区块 "+e.getChunk().getWorld().getName()+"#"+e.getChunk().getX()+"#"+e.getChunk().getZ()+" 的保存，因为此区块是玩家意外传送到边界外而错误加载的");
    }
}
