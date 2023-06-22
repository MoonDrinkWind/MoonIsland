package icu.moondrinkwind.moonisland.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import icu.moondrinkwind.moonisland.entity.Island;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.util.mysql.MySQLDSL;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService {
    private final HikariDataSource sqlConnectionPool;
    private static final List<Island> islands = new ArrayList<>();
    public final static String ISLAND_TABLE = "CREATE TABLE IF NOT EXISTS ISLAND(" +
            "ID LONG," +
            "NAME VARCHAR(255), " +
            "START_X INT," +
            "START_Z INT," +
            "END_X INT," +
            "END_Z INT," +
            "CENTER_X INT," +
            "CENTER_Z INT," +
            "OWNER VARCHAR(255))";
    public final static String PLAYER_TABLE = "CREATE TABLE IF NOT EXISTS PLAYER(" +
            "UUID VARCHAR(255)," +
            "ISLAND VARCHAR(255)" +
            ")";

    public final static String DELETE_ISLAND = "DELETE FROM ISLAND WHERE ID = '%d'";
    public final static String INSERT_ISLAND = "INSERT INTO ISLAND(ID, NAME, START_X, START_Z, CENTER_X, CENTER_Z, END_X, END_Z, OWNER) VALUES" +
            "('%s', '%s', %d, %d, %d, %d, %d, %d, '%s')";

    public final static String INSERT_PLAYER = "INSERT INTO PLAYER(UUID, ISLAND) VALUES ('%s', '%s')";
    public final static String DELETE_PLAYER_BY_UUID = "DELETE FROM PLAYER WHERE UUID = '%s'";
    public final static String DELETE_PLAYER_BY_ISLAND = "DELETE FROM PLAYER WHERE ISLAND = '%s'";

    public final static String GET_ISLAND_ID = "SELECT ISLAND FROM PLAYER WHERE UUID = '%s'";

    public final static String GET_ISLAND = "SELECT ID, NAME, START_X, START_Z, CENTER_X, CENTER_Z, END_X, END_Z, OWNER FROM ISLAND WHERE ID = '%s'";

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

    public void deleteIsland(int ID){
        Connection connection = getConnection();
        DSLContext context = MySQLDSL.using(connection);
        context.execute(String.format(DELETE_ISLAND, ID));
        context.commit();
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createIsland(String ID, String name, int startX, int startZ, int centerX, int centerZ, int endX, int endZ, String owner){
        Connection connection = getConnection();
        DSLContext context = MySQLDSL.using(connection);
        context.execute(String.format(INSERT_ISLAND, ID, name, startX, startZ, centerX, centerZ, endX, endZ, owner));
        context.commit();
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addPlayer(String UUID, String island){
        Connection connection = getConnection();
        DSLContext context = MySQLDSL.using(connection);
        context.execute(String.format(INSERT_PLAYER, UUID, island));
        context.commit();
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removePlayerByUUID(String UUID){
        Connection connection = getConnection();
        DSLContext context = MySQLDSL.using(connection);
        context.execute(String.format(DELETE_PLAYER_BY_UUID, UUID));
        context.commit();
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removePlayerByIsland(int island){
        Connection connection = getConnection();
        DSLContext context = MySQLDSL.using(connection);
        context.execute(String.format(DELETE_PLAYER_BY_ISLAND, island));
        context.commit();
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getIslandID(String UUID){
        Connection connection = getConnection();
        DSLContext context = MySQLDSL.using(connection);
        Result<Record> records = context.resultQuery(String.format(GET_ISLAND_ID, UUID)).fetch();
        context.commit();
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ((String) records.get(0).getValue("ISLAND"));
    }

    public boolean hasIsland(String UUID){
        Connection connection = getConnection();
        DSLContext context = MySQLDSL.using(connection);
        Result<Record> records = context.resultQuery(String.format(GET_ISLAND_ID, UUID)).fetch();
        context.commit();
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return !records.isEmpty();
    }

    public Island getIsland(String ID){
        Connection connection = getConnection();
        DSLContext context = MySQLDSL.using(connection);
        Result<Record> records = context.resultQuery(String.format(GET_ISLAND, ID)).fetch();
        Record record = records.get(0);
        Island island = new Island();
        island.setName(((String) record.getValue("NAME")))
                .setOwner(Bukkit.getServer().getPlayer(
                        (String) record.getValue("OWNER")
                ))
                .setCenterX((int) record.getValue("CENTER_X"))
                .setCenterZ((int) record.getValue("CENTER_Z"))
                .setStartX((int) record.getValue("START_X"))
                .setStartZ((int) record.getValue("START_Z"))
                .setEndX((int) record.getValue("END_X"))
                .setEndZ((int) record.getValue("END_Z"));
        context.commit();
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return island;
    }
}
