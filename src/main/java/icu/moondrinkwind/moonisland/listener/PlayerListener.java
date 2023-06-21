package icu.moondrinkwind.moonisland.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class PlayerListener implements Listener {
    //players who are in water
    private final List<Player> players = new ArrayList<>();
    private class EffectPlayer extends BukkitRunnable{
        private Player player;
        private List<Player> players;

        public EffectPlayer(Player player, List<Player> players){
            this.player = player;
            this.players = players;
        }
        @Override
        public void run() {
            if(player.isInWater()){
                damagePlayer(player);
            }else{
                players.remove(player);
                cancel();
            }
        }

        private void damagePlayer(Player player){
            player.damage(3);
            List<PotionEffect> effects = new ArrayList<>();
            effects.add(new PotionEffect(PotionEffectType.CONFUSION, 60, 1));
            effects.add(new PotionEffect(PotionEffectType.SLOW, 60, 1));
            player.addPotionEffects(effects);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        if(player.isInWater() &&
                event.getFrom().getBlock().getType() != Material.WATER &&
                !players.contains(player)){
            players.add(player);
            new EffectPlayer(player, players).runTaskTimer(
                    Bukkit.getPluginManager().getPlugin("MoonIsland"),
                    0, 60);
        }
    }


}