package icu.moondrinkwind.moonisland.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class WeatherListener implements Listener {
    private List<Player> players;

    public WeatherListener(){
        players = new ArrayList<>();
    }
    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event){
        World world = event.getWorld();
        players = world.getPlayers();
        new Rain(players, world).runTaskTimer(Bukkit.getPluginManager().getPlugin("MoonIsland"), 0, 60);
    }

    public static boolean isUnderBlock(Player player){
        Location location = player.getLocation();
        Block block = location.getBlock();
        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();
        for (; y <= 256; y++){
            location.setY(y);
            if (location.getBlock().getType() != Material.AIR){
                return true;
            }
        }
        return false;
    }

    private static class Rain extends BukkitRunnable{
        private final World world;
        private final List<Player> players;

        public Rain(List<Player> players, World world){
            this.players = players;
            this.world = world;
        }

        @Override
        public void run() {
            if (world.isClearWeather()) {
                cancel();
            }
            players.forEach(player -> {
                if (!isUnderBlock(player)){
                    player.damage(1);
                }
            });
        }


    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        if(player.getLocation().getWorld().isClearWeather() || isUnderBlock(player)){
            players.remove(player);
            return;
        }
        players.add(player);
    }
}
