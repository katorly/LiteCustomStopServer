package com.gmail.zhushijie.litecustomstopserver;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

public class PlayerJoin implements Listener {
    @EventHandler
    public void onLogin(PlayerJoinEvent e) {
        if (LiteCustomStopServer.stopalready) { //服务器进入关服倒计时后若仍有玩家进入则提醒他们服务器即将关闭
            FileConfiguration config = LiteCustomStopServer.INSTANCE.getConfig();
            Player player = e.getPlayer();
            player.sendMessage(config.getString("announcement-prefix").replace("&","§") + config.getString("shutdown-inseconds").replace("&","§"));
            if (Objects.equals(config.getString("title-true-or-false"),"true")) { //关服提醒发送的同时是否显示标题文字
                player.sendTitle(config.getString("announcement-prefix").replace("&","§").replace(" ",""),config.getString("shutdown-inseconds").replace("&","§"), 10, 70, 20);
            }
        }
    }
}
