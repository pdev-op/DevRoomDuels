package com.pdev.drduels.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.pdev.drduels.Main;
import com.pdev.drduels.api.player.QuitPlayer;
import com.pdev.drduels.api.player.StatsPlayer;

import org.bukkit.Bukkit;

public class PlayerManager extends SQLManager {
    private Map<UUID, QuitPlayer> quitPlayers;

    public PlayerManager(Main plugin) throws SQLException {
        super(plugin);

        this.quitPlayers = new HashMap<>();

        // Logging
        plugin.getLogger().info("PlayerManager initialized");
    }

    // NOTE
    // - This is a simple implementation to prevent those who log out during a duel
    // to get moved when they log back in.
    // I understand that this isn't perfect and storing a last used location &
    // inventoryin the db would be better, but
    // I am sticking to the provided data model
    public Map<UUID, QuitPlayer> getQuitPlayers() {
        return quitPlayers;
    }

    public void removeQuitPlayer(UUID uuid) {
        quitPlayers.remove(uuid);
    }

    public void addQuitPlayer(UUID uuid, QuitPlayer quitPlayer) {
        quitPlayers.put(uuid, quitPlayer);
    }

    @Override
    public void createTable() {
        try (Connection connection = getConnection()) {
            PreparedStatement create = prepareStatement(connection,
                    "CREATE TABLE IF NOT EXISTS " + prefix + "_players " +
                            "(id INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                            "uuid VARCHAR(256) NOT NULL, " +
                            "name VARCHAR(256) NOT NULL, " +
                            "wins INTEGER NOT NULL, " +
                            "kills INTEGER NOT NULL, " +
                            "deaths INTEGER NOT NULL, " +
                            "win_streak INTEGER NOT NULL, " +
                            "losses INTEGER NOT NULL)");
            executeUpdate(create);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public StatsPlayer getPlayer(UUID uuid) {
        CompletableFuture<StatsPlayer> drPlayer = CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection()) {
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM " + prefix + "_players WHERE uuid = '" + uuid.toString() + "';");
                ResultSet results = statement.executeQuery();

                while (results.next()) {
                    StatsPlayer dPlayer = new StatsPlayer(uuid);

                    dPlayer.setWins(results.getInt("wins"));
                    dPlayer.setLosses(results.getInt("losses"));
                    dPlayer.setKills(results.getInt("kills"));
                    dPlayer.setDeaths(results.getInt("deaths"));
                    dPlayer.setWinStreak(results.getInt("win_streak"));

                    return dPlayer;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        });

        try {
            return drPlayer.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void createNewPlayer(UUID uuid, String name) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (Connection connection = getConnection()) {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO " + prefix
                        + "_players (uuid, name, wins, kills, deaths, win_streak, losses) VALUES (?, ?, 0, 0, 0, 0, 0);");

                statement.setString(1, uuid.toString());
                statement.setString(2, name);

                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void updateName(UUID uuid, String name) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (Connection connection = getConnection()) {
                PreparedStatement statement = connection.prepareStatement("UPDATE " + prefix + "_players SET name = '"
                        + name + "' WHERE uuid = '" + uuid.toString() + "';");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public int getKills(UUID uuid) {
        CompletableFuture<Integer> kills = CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection()) {
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT uuid,kills FROM " + prefix + "_players WHERE uuid = '" + uuid.toString() + "';");
                ResultSet results = statement.executeQuery();

                while (results.next()) {
                    return results.getInt("kills");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return -1;
        });

        try {
            return kills.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public void setKills(UUID uuid, int kills) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (Connection connection = getConnection()) {
                PreparedStatement statement = connection.prepareStatement("UPDATE " + prefix + "_players SET kills = "
                        + kills + " WHERE uuid = '" + uuid.toString() + "';");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public int getDeaths(UUID uuid) {
        CompletableFuture<Integer> kills = CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection()) {
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT uuid,deaths FROM " + prefix + "_players WHERE uuid = '" + uuid.toString() + "';");
                ResultSet results = statement.executeQuery();

                while (results.next()) {
                    return results.getInt("deaths");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return -1;
        });

        try {
            return kills.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public void setDeaths(UUID uuid, int deaths) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (Connection connection = getConnection()) {
                PreparedStatement statement = connection.prepareStatement("UPDATE " + prefix + "_players SET deaths = "
                        + deaths + " WHERE uuid = '" + uuid.toString() + "';");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public int getWins(UUID uuid) {
        CompletableFuture<Integer> kills = CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection()) {
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT uuid,wins FROM " + prefix + "_players WHERE uuid = '" + uuid.toString() + "';");
                ResultSet results = statement.executeQuery();

                while (results.next()) {
                    return results.getInt("wins");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return -1;
        });

        try {
            return kills.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public void setWins(UUID uuid, int wins) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (Connection connection = getConnection()) {
                PreparedStatement statement = connection.prepareStatement("UPDATE " + prefix + "_players SET wins = "
                        + wins + " WHERE uuid = '" + uuid.toString() + "';");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public int getLosses(UUID uuid) {
        CompletableFuture<Integer> kills = CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection()) {
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT uuid,losses FROM " + prefix + "_players WHERE uuid = '" + uuid.toString() + "';");
                ResultSet results = statement.executeQuery();

                while (results.next()) {
                    return results.getInt("losses");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return -1;
        });

        try {
            return kills.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public void setLosses(UUID uuid, int losses) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (Connection connection = getConnection()) {
                PreparedStatement statement = connection.prepareStatement("UPDATE " + prefix + "_players SET losses = "
                        + losses + " WHERE uuid = '" + uuid.toString() + "';");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public int getWinStreak(UUID uuid) {
        CompletableFuture<Integer> kills = CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection()) {
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT uuid,win_streak FROM " + prefix + "_players WHERE uuid = '" + uuid.toString() + "';");
                ResultSet results = statement.executeQuery();

                while (results.next()) {
                    return results.getInt("win_streak");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return -1;
        });

        try {
            return kills.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public void setWinStreak(UUID uuid, int streak) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (Connection connection = getConnection()) {
                PreparedStatement statement = connection.prepareStatement("UPDATE " + prefix
                        + "_players SET win_streak = " + streak + " WHERE uuid = '" + uuid.toString() + "';");
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
