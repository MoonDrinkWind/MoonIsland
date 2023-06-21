package icu.moondrinkwind.moonisland;

import icu.moondrinkwind.moonisland.command.WorldTest;
import icu.moondrinkwind.moonisland.generator.IslandGenerator;
import icu.moondrinkwind.moonisland.listener.PlayerListener;
import icu.moondrinkwind.moonisland.listener.WeatherListener;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

public final class MoonIsland extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new WeatherListener(), this);
        Bukkit.getPluginCommand("worldtest").setExecutor(new WorldTest());
        Bukkit.getPluginCommand("moonisland").setExecutor(new icu.moondrinkwind.moonisland.command.MoonIsland());
        initWorld();
    }

    @Override
    public void onDisable() {
    }

    private void initWorld() {
        //init island
        WorldCreator creator = new WorldCreator("island");
        creator.generator(new IslandGenerator());
        creator.createWorld();
    }
}
