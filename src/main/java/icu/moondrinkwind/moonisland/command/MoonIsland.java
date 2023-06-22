package icu.moondrinkwind.moonisland.command;

import cn.hutool.core.util.IdUtil;
import icu.moondrinkwind.moonisland.data.DatabaseService;
import icu.moondrinkwind.moonisland.entity.Island;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MoonIsland implements CommandExecutor {
    private DatabaseService databaseService;

    public MoonIsland() {
        databaseService = ((icu.moondrinkwind.moonisland.MoonIsland) Bukkit.getServer().getPluginManager().getPlugin("MoonIsland")).getDatabaseService();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;

        if (args.length == 0 || args[0].equalsIgnoreCase("home")) {
            if (!databaseService.hasIsland(player.getUniqueId().toString())){
                createIsland(player);
            }else{
                Island island = databaseService.getIsland(databaseService.getIslandID(player.getUniqueId().toString()));
                Location location = new Location(Bukkit.getWorld("island"), island.getCenterX(), 65, island.getCenterZ());
                player.teleport(location);
            }
        }

        return true;
    }

    private void createIsland(Player player) {
        if (DatabaseService.getIslands().isEmpty()) {
            Island island = new Island();
            island.setID(IdUtil.getSnowflakeNextIdStr())
                    .setCenterX(0)
                    .setCenterZ(0)
                    .setStartX(16 * 20)
                    .setStartZ(16 * 20)
                    .setEndX(-16 * 20)
                    .setEndZ(-16 * 20)
                    .setOwner(player);
            DatabaseService.getIslands().add(island);
            databaseService.createIsland(island.getID(), "NULL",
                    island.getStartX(), island.getStartZ(), island.getCenterX(), island.getCenterZ(), island.getEndX(), island.getEndZ(), island.getOwner().getUniqueId().toString());
            databaseService.addPlayer(player.getUniqueId().toString(), island.getID());
            Location center = new Location(Bukkit.getWorld("island"), island.getCenterX() ,64, island.getCenterZ());
            center.getBlock().setType(Material.BEDROCK);
            center.setY(65);
            player.teleport(center);
        }
    }
}
