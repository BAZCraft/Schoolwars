package bazcraft.schoolwars;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Team {

    private final org.bukkit.scoreboard.Team scoreboard;
    private final BossBar teamHealthBar;
    private final BossBar publicHealthBar;
    private final Location spawn;

    public Team(String naam, int health, Location spawn, ChatColor color) {

        org.bukkit.scoreboard.Team temp = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(naam);
        if (temp == null) {
            temp = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(naam);
        }
        scoreboard = temp;

        teamHealthBar = Bukkit.createBossBar(color + naam.toUpperCase() + " §e(jij)", BarColor.valueOf(color.name()), BarStyle.SOLID);
        teamHealthBar.setProgress((health*1.0)/100);
        publicHealthBar = Bukkit.createBossBar(color + naam.toUpperCase(), teamHealthBar.getColor(), teamHealthBar.getStyle());
        this.spawn = spawn;

        scoreboard.setAllowFriendlyFire(false);
        scoreboard.setColor(color);

    }

    public void addSpeler(Player speler) {
        scoreboard.addEntry(speler.getName());
        teamHealthBar.addPlayer(speler);
    }

    public void removeSpeler(Player speler) {
        scoreboard.removeEntry(speler.getName());
        teamHealthBar.removePlayer(speler);
    }

    public void teleportSpelers() {
        for (String n : scoreboard.getEntries()) {
            Bukkit.getPlayer(n).teleport(spawn, PlayerTeleportEvent.TeleportCause.PLUGIN);
        }
    }

    public void removeHealth(int damage) {
        double convertedDamage = (damage*1.0)/100;
        double remainingHealth = teamHealthBar.getProgress()-convertedDamage;
        if (remainingHealth < 0) {
            teamHealthBar.setProgress(0);
        } else {
            teamHealthBar.setProgress(remainingHealth);
        }
        if (teamHealthBar.getProgress() == 0) {
            //TODO END GAME
        }
    }

    public BossBar getTeamHealthBar() {
        return teamHealthBar;
    }

    public BossBar getPublicHealthBar() {
        return publicHealthBar;
    }

    public org.bukkit.scoreboard.Team getScoreboard() {
        return scoreboard;
    }

    public Location getSpawn() {
        return spawn;
    }
}
