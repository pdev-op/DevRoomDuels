package com.pdev.drduels.managers;

import java.util.Map;
import java.util.Set;

import com.pdev.drduels.Main;
import com.pdev.drduels.api.Kit;

public class KitManager {
    private Map<String, Kit> kits;
    private String defaultKit;

    public KitManager(Main plugin) {
        Map<String, Map<String, Kit>> data = plugin.getKitFile().getKits();

        data.forEach((defaultKit, kits) -> {
            this.defaultKit = defaultKit;
            this.kits = kits;
        });
    }

    public Kit getKit(String kitName) {
        // Account for case being off
        for (String s : kits.keySet()) {
            if (kitName.equalsIgnoreCase(s)) {
                kitName = s;
            }
        }

        return kits.get(kitName);
    }

    public Set<String> getKitNames() {
        return kits.keySet();
    }

    public Kit getDefaultKit() {
        return getKit(defaultKit);
    }
}
