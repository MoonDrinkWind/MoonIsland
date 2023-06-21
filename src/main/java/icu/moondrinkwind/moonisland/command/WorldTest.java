package icu.moondrinkwind.moonisland.command;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WorldTest implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Bukkit.getWorlds().forEach(world -> {
            Bukkit.getLogger().info(world.getName());
            world.getPopulators().forEach(populator ->{
                Bukkit.getLogger().info(populator.getClass().getName());
            });
        });

        if (sender instanceof Player){
            Player player = (Player)sender;
            Location location = player.getLocation();
            if(args.length >=1){
                location.setWorld(Bukkit.getWorld(args[0]));
            }else{
                location.setWorld(Bukkit.getWorld("island"));
            }
            player.teleport(location);
        }

        return true;
    }
}
