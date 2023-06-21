package icu.moondrinkwind.moonisland.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import icu.moondrinkwind.moonisland.entity.Island;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService {
    private final HikariDataSource sqlConnectionPool;
    private static List<Island> islands = new ArrayList<>();
    public final static String ISLAND_TABLE = "CREATE TABLE IF NOT EXISTS ISLAND(" +
            "ID INT AUTO_INCREMENT," +
            "NAME VARCHAR(255), " +
            "START_X INT," +
            "START_Z INT," +
            "END_X INT," +
            "END_Z INT," +
            "CENTER_X INT," +
            "CENTER_Z INT," +
            "OWNER VARCHAR(255)," +
            "PRIMARY KEY(ID))";
    public final static String PLAYER_TABLE = "CREATE TABLE IF NOT EXISTS PLAYER(" +
            "UUID VARCHAR(255)," +
            "ISLAND INT" +
            ")";

    public DatabaseService(){
        YamlConfiguration config = (YamlConfiguration) Bukkit.getPluginManager().getPlugin("MoonIsland").getConfig();
        ConfigurationSection database = config.createSection("database");
        String username = database.getString("username");
        String password = database.getString("password");
        String host = database.getString("host");
        int port = database.getInt("port");
        String databaseName = database.getString("database-name");
        ConfigurationSection hikariCP = config.createSection("hikariCP");
        int maxiIdle = hikariCP.getInt("maxiIdle");
        int miniIdle = hikariCP.getInt("miniIdle");
        String URL = "jdbc:mysql://"
                +host + ":"
                +port + "/"
                +databaseName
                +"?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=UTF-8&useSSL=false";
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.jdbc.Driver");
        hikariConfig.setMinimumIdle(maxiIdle);
        hikariConfig.setMinimumIdle(miniIdle);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setJdbcUrl(URL);
        hikariConfig.setAutoCommit(true);
        sqlConnectionPool = new HikariDataSource(hikariConfig);
    }

    public Connection getConnection() {
        Connection connection = null;
        try{
            connection = sqlConnectionPool.getConnection();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return connection;
    }

    public static List<Island> getIslands(){
        return islands;
    }
}
