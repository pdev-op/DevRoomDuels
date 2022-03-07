package com.pdev.drduels.managers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pdev.drduels.Main;
import com.pdev.drduels.config.Config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public abstract class SQLManager {
    protected Main plugin;
    protected String prefix;

    private Config config;
    private HikariConfig hConfig;
    private HikariDataSource dataSource;

    public SQLManager(Main plugin) throws SQLException {
        // Init
        this.plugin = plugin;
        hConfig = new HikariConfig();
        config = plugin.getConfigFile();

        // DB values
        String user = config.getSQLUsername();
        String password = config.getSQLPassword();
        prefix = config.getSQLPrefix();

        if (!prefix.matches("^[_A-Za-z0-9]+$")) {
            plugin.getLogger().warning("Table prefix " + prefix + " is not alphanumeric. Using drduels...");
            prefix = "drduels";
        }

        String url = "jdbc:mysql://" + config.getSQLHost() + ":" + config.getSQLPort() + "/" + config.getSQLDatabase();

        // Pooling config & datasource
        hConfig.setPoolName("drduelsPool");
        hConfig.setUsername(user);
        hConfig.setPassword(password);
        hConfig.setJdbcUrl(url);
        hConfig.addDataSourceProperty("cachePrepStmts", "true");
        hConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hConfig.setMaximumPoolSize(20);
        dataSource = new HikariDataSource(hConfig);

        // Get it going
        getConnection();
        createTable();
    }

    public abstract void createTable();

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    protected synchronized ResultSet executeQuery(PreparedStatement statement) throws SQLException {
        return statement.executeQuery();
    }

    protected synchronized void executeUpdate(PreparedStatement statement) throws SQLException {
        statement.executeUpdate();
    }

    protected synchronized PreparedStatement prepareStatement(Connection connection, String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }
}
