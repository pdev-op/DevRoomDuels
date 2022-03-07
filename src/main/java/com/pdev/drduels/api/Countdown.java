package com.pdev.drduels.api;

import java.util.function.Consumer;

import com.pdev.drduels.Main;

import org.bukkit.Bukkit;

public class Countdown implements Runnable {
    private Main plugin;
    private Integer id;
    private int seconds;
    private int secondsLeft;
    private Consumer<Countdown> every;
    private Runnable after;

    public Countdown(Main plugin, int seconds, Runnable after, Consumer<Countdown> every) {
        this.plugin = plugin;
        this.seconds = seconds;
        this.secondsLeft = seconds;
        this.after = after;
        this.every = every;
    }

    @Override
    public void run() {
        if (secondsLeft < 1) {
            after.run();

            if (id != null) {
                Bukkit.getScheduler().cancelTask(id);
            }

            return;
        }

        every.accept(this);
        secondsLeft--;
    }

    public int getDuration() {
        return seconds;
    }

    public int getSecondsLeft() {
        return secondsLeft;
    }

    public void schedule() {
        this.id = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 0L, 20L);
    }
}
