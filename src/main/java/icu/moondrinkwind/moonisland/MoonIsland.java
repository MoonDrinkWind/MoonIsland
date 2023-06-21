package icu.moondrinkwind.moonisland;

import icu.moondrinkwind.moonisland.command.WorldTest;
import icu.moondrinkwind.moonisland.data.DatabaseService;
import icu.moondrinkwind.moonisland.entity.Island;
import icu.moondrinkwind.moonisland.generator.IslandGenerator;
import icu.moondrinkwind.moonisland.listener.PlayerListener;
import icu.moondrinkwind.moonisland.listener.WeatherListener;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.util.mysql.MySQLDSL;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public final class MoonIsland extends JavaPlugin {
    private DatabaseService databaseService;

    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new WeatherListener(), this);
        initWorld();
        initData();
        Bukkit.getPluginCommand("worldtest").setExecutor(new WorldTest());
        Bukkit.getPluginCommand("moonisland").setExecutor(new icu.moondrinkwind.moonisland.command.MoonIsland());
    }

    @Override
    public void onDisable() {
        saveConfig();
    }

    private void initWorld() {
        //init island
        WorldCreator creator = new WorldCreator("island");
        creator.generator(new IslandGenerator());
        creator.createWorld();
    }

    private void initData(){
        saveConfig();
        saveDefaultConfig();
        databaseService = new DatabaseService();
        Connection connection = databaseService.getConnection();
        DSLContext context = MySQLDSL.using(connection);

        context.execute(DatabaseService.PLAYER_TABLE);
        context.execute(DatabaseService.ISLAND_TABLE);

        Result<Record> islands = context.select().from("ISLAND").fetch();
        islands.forEach(record -> {
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
                    .setEndZ((int) record.getValue("END_Y"));

            Result<Record> playerList = context.select().from("PLAYER").where("island = " + island.getID()).fetch();
            List<Player> players = new ArrayList<>();
            playerList.forEach(playerRecord -> {
                players.add(Bukkit.getPlayer((String) playerRecord.getValue("UUID")));
            });
        });
        context.commit();
    }

    public DatabaseService getDatabaseService() {
        return databaseService;
    }
}
