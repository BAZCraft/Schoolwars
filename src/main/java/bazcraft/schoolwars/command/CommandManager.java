package bazcraft.schoolwars.command;

import bazcraft.schoolwars.NPC.CustomNPC;
import bazcraft.schoolwars.NPC.NPCManager;
import bazcraft.schoolwars.NPC.NPCTeam;
import bazcraft.schoolwars.NPC.NPCType;
import bazcraft.schoolwars.Schoolwars;
import net.citizensnpcs.npc.ai.speech.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class CommandManager implements CommandExecutor {

    private final Schoolwars plugin;

    public CommandManager(Schoolwars plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player)sender;
        switch (label) {
            case "spelers":
                String ingame = "";
                String spec = "";
                for (Player n : Bukkit.getOnlinePlayers()) {
                    if (plugin.getGameManager().getIngamePlayers().contains(n)) {
                        ingame += "\n- " + n.getDisplayName();
                    } else {
                        spec += "\n- " + n.getDisplayName();
                    }
                }
                sender.sendMessage("ingamespelers: " + ingame + "\nspectators: " + spec);
                return true;
            case "antwoord":
                if (args.length > 0){
                    if(args[0].equals(this.plugin.getVragenManager().getActieveVraagBlauw().getAntwoord().toLowerCase(Locale.ROOT))){
                        player.sendMessage(ChatColor.GREEN + "Juist antwoord!");
                        plugin.getVragenManager().getActieveVraagBlauw().setBlauw(true);
                        if(plugin.getVragenManager().getVragenLijst().indexOf(plugin.getVragenManager().getActieveVraagBlauw())+1 == this.plugin.getVragenManager().getVragenLijst().size()){
                            player.sendMessage(ChatColor.AQUA + "Game: " + ChatColor.GREEN + "Je hebt alle vragen beantwoord!");
                        }else{
                            plugin.getVragenManager().setActieveVraagBlauw(plugin.getVragenManager().getVraag());
                        }
                    }else{
                        player.sendMessage(ChatColor.RED + "Fout antwoord!");
                    }
                }
                return true;
        }
        return false;
    }
}
