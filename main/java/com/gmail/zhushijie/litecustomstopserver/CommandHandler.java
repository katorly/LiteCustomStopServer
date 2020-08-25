package com.gmail.zhushijie.litecustomstopserver;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Objects;

public class CommandHandler implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("litecustomstopserver")) {
            FileConfiguration config = LiteCustomStopServer.INSTANCE.getConfig();
            FileConfiguration mconfig = LiteCustomStopServer.Messagesconfig.getConfig();
            if (args.length < 1) { //若只输入了个/lcss则执行/lcss stop
                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"litecustomstopserver stop");
            } else if (Objects.equals(args[0], "stop")) {
                if (LiteCustomStopServer.stopalready) {
                    sender.sendMessage(mconfig.getString("plugin-prefix").replace("&","§") + mconfig.getString("countdown-already").replace("&","§"));
                } else {
                    LiteCustomStopServer.stopalready = true;
                    Bukkit.broadcastMessage(mconfig.getString("announcement-prefix").replace("&","§") + mconfig.getString("shutdown-message").replace("&","§").replace("<seconds>",config.getString("seconds")));
                    if (Objects.equals(config.getString("title-true-or-false"),"true")) { //关服公告发送的同时是否显示标题文字
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.sendTitle(mconfig.getString("announcement-prefix").replace("&","§").replace(" ",""),mconfig.getString("shutdown-message").replace("&","§").replace("<seconds>",config.getString("seconds")), 10, 70, 20);
                        }
                    }
                    new BukkitRunnable() {
                        int countdown = config.getInt("seconds");
                        final List<Integer> counter = config.getIntegerList("countdown");
                        @Override
                        public void run() {
                            if (!LiteCustomStopServer.cancelled) {
                                countdown--;
                                counter.forEach(value -> {
                                    if (countdown == value) {
                                        if (Objects.equals(config.getString("title-true-or-false"),"true")) { //关服公告发送的同时是否显示标题文字
                                            for (Player player : Bukkit.getOnlinePlayers()) {
                                                player.sendTitle(mconfig.getString("announcement-prefix").replace("&","§").replace(" ",""),mconfig.getString("shutdown-message").replace("&","§").replace("<seconds>",String.valueOf(countdown)), 10, 70, 20);
                                            }
                                        }
                                        Bukkit.broadcastMessage(mconfig.getString("announcement-prefix").replace("&","§") + mconfig.getString("shutdown-message").replace("&","§").replace("<seconds>",String.valueOf(countdown)));
                                    }
                                });
                                if (countdown == 0) {
                                    Bukkit.broadcastMessage(mconfig.getString("prefix") + mconfig.getString("message-shutdown"));
                                    Bukkit.getServer().shutdown();
                                }
                            } else {
                                LiteCustomStopServer.stopalready = false;
                                LiteCustomStopServer.cancelled = false;
                                if (Objects.equals(config.getString("title-true-or-false"),"true")) { //关服已取消公告发送的同时是否显示标题文字
                                    for (Player player : Bukkit.getOnlinePlayers()) {
                                        player.sendTitle(mconfig.getString("announcement-prefix").replace("&","§").replace(" ",""),mconfig.getString("cancel-announce").replace("&","§"), 10, 70, 20);
                                    }
                                }
                                Bukkit.broadcastMessage(mconfig.getString("announcement-prefix").replace("&","§") + mconfig.getString("cancel-announce").replace("&","§"));
                                cancel();
                            }
                        }
                    }.runTaskTimer(LiteCustomStopServer.INSTANCE,0L,20L);
                }
            } else if (Objects.equals(args[0], "cancel")) {
                if (LiteCustomStopServer.cancelled) {
                    sender.sendMessage(mconfig.getString("plugin-prefix").replace("&","§") + mconfig.getString("already-cancelled").replace("&","§"));
                } else if (!LiteCustomStopServer.stopalready) {
                    sender.sendMessage(mconfig.getString("plugin-prefix").replace("&","§") + mconfig.getString("already-cancelled").replace("&","§"));
                } else {
                    LiteCustomStopServer.cancelled = true;
                }
            } else if (Objects.equals(args[0], "reload")) {
                LiteCustomStopServer.INSTANCE.saveDefaultConfig();
                LiteCustomStopServer.INSTANCE.reloadConfig();
                LiteCustomStopServer.INSTANCE.saveConfig();
                LiteCustomStopServer.Messagesconfig.saveDefaultConfig();
                LiteCustomStopServer.Messagesconfig.reloadConfig();
                LiteCustomStopServer.Messagesconfig.saveConfig();
                sender.sendMessage(mconfig.getString("plugin-prefix").replace("&","§") + mconfig.getString("reload-success").replace("&","§"));
            } else if (Objects.equals(args[0], "help")) { //查看插件帮助的指令 lcss help
                List<String> helpmessage = mconfig.getStringList("help-message");
                int i = 0;
                for (int length = helpmessage.size(); i < length; i++) {
                    sender.sendMessage(helpmessage.get(i).replace("&","§"));
                }
            } else { //若指令输入错误
                if (!(sender instanceof Player)) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"litecustomstopserver help");
                } else {
                    Player player = (Player) sender;
                    player.performCommand("litecustomstopserver help");
                }
            }
        }
        return true;
    }
}
