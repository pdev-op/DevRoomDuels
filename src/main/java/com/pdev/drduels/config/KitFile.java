package com.pdev.drduels.config;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.pdev.drduels.Main;
import com.pdev.drduels.api.Kit;
import com.pdev.drduels.data.KitsData;

public class KitFile {
    private File file;
    private Main plugin;

    public KitFile(Main plugin) {
        this.plugin = plugin;

        file = new File(plugin.getDataFolder(), "kit.json");

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }

        save();
    }

    public void save() {
        if (!file.exists()) {
            plugin.saveResource("kit.json", false);
        }
    }

    public Map<String, Map<String, Kit>> getKits() {
        Gson gson = new Gson();

        try (Reader reader = new FileReader(file)) {
            KitsData kd = gson.fromJson(reader, KitsData.class);

            Map<String, Map<String, Kit>> data = new HashMap<>();

            data.put(kd.getDefaultKit(), kd.getKits());

            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
