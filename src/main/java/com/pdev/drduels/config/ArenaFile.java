package com.pdev.drduels.config;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;

import com.google.gson.Gson;
import com.pdev.drduels.Main;
import com.pdev.drduels.api.Arena;
import com.pdev.drduels.data.ArenaData;

public class ArenaFile {
    private File file;
    private Main plugin;

    public ArenaFile(Main plugin) {
        this.plugin = plugin;

        file = new File(plugin.getDataFolder(), "arena.json");

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }

        save();
    }

    public void save() {
        if (!file.exists()) {
            plugin.saveResource("arena.json", false);
        }
    }

    public Arena getArena() {
        Gson gson = new Gson();

        try (Reader reader = new FileReader(file)) {
            ArenaData ad = gson.fromJson(reader, ArenaData.class);

            return ad.getArena();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
