package me.pcrunn.data.user.listener;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.pcrunn.data.DataPlugin;
import me.pcrunn.data.user.User;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class UserListener implements Listener {

    @NonNull
    private DataPlugin plugin;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        User user = this.plugin.getUserService().find(event.getPlayer().getUniqueId());

        user.setJoined(System.currentTimeMillis());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        User user = this.plugin.getUserService().find(event.getPlayer().getUniqueId());
        user.setPlaytime(user.getPlaytime() + (System.currentTimeMillis() - user.getJoined()));

        this.plugin.getUserService().save(user);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        User user = this.plugin.getUserService().find(event.getPlayer().getUniqueId());

        event.setFormat(user.getRank().getColor() + user.getName() + ChatColor.WHITE + ": " + event.getMessage().replace("%", "%%"));
    }

}
