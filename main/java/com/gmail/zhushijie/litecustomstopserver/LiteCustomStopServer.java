package com.gmail.zhushijie.litecustomstopserver;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Objects;

public final class LiteCustomStopServer extends JavaPlugin {
    public static LiteCustomStopServer INSTANCE;
    public LiteCustomStopServer() {
        INSTANCE = this;
    }

    public static boolean cancelled = false;
    public static boolean stopalready = false;

    public void timing() {
        new BukkitRunnable() { //定时关服功能
            @Override
            public void run() {
                FileConfiguration config = LiteCustomStopServer.INSTANCE.getConfig();
                String stoptime = config.getString("auto-stop-time");
                if (!Objects.equals(config.getString("auto-stop-time"), "none")) {
                    long millisnow = System.currentTimeMillis();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH-mm");
                    String timenow = dateFormat.format(millisnow);
                    if (Objects.equals(timenow,stoptime)) {
                        getLogger().info(config.getString("time-for-shutdown").replace("&","§"));
                        Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"litecustomstopserver stop");
                        cancel();
                    }
                }
            }
        }.runTaskTimer(this,200L,200L);
    }

    public void pluginupdater() {
        String currentversion = "1.2";
        getLogger().info("正在检查更新......");
        try {
            URL url = new URL("https://cdn.jsdelivr.net/gh/main-world/litecustom@update/LiteCustomStopServer/version.txt");
            InputStream is = url.openStream();
            InputStreamReader ir = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(ir);
            String version = br.readLine();
            if (version.equals(currentversion)) {
                getLogger().info("插件已是最新版本!");
            } else {
                getLogger().info("检查到插件有新版本!");
                getLogger().info("请前往相应网页下载更新!");
            }
        } catch (Throwable t) {
            getLogger().info("更新检查失败!");
        }
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();
        getLogger().info("LiteCustomStopServer已成功加载!");
        getLogger().info("作者:主世界");
        getLogger().info("本插件已免费发布并在Gitee上开源");
        pluginupdater();
        LiteCustomStopServer.INSTANCE.getCommand("litecustomstopserver").setExecutor(new CommandHandler());
        getServer().getPluginManager().registerEvents(new PlayerJoin(),this);
        timing();
    }

    @Override
    public void onDisable() {
        reloadConfig();
        LiteCustomStopServer.INSTANCE.saveConfig();
        HandlerList.unregisterAll(this);
        getLogger().info("LiteCustomStopServer已成功卸载!");
    }
}
