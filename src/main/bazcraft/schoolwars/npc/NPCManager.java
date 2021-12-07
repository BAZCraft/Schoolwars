package bazcraft.schoolwars.npc;

import bazcraft.schoolwars.Schoolwars;
import bazcraft.schoolwars.vragen.VraagType;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class NPCManager {

    private ArrayList<CustomNPC> npcList;
    private HashMap<Player, CustomNPC> geselecteerdeNPC;
    private final Schoolwars plugin;

    public NPCManager(Schoolwars plugin){
        this.plugin = plugin;
        this.npcList = new ArrayList<>();
        this.geselecteerdeNPC = new HashMap<>();
        //Hard Coded NPC

        CustomNPC leerkrachtNPCBlauw = new CustomNPC(ChatColor.BLUE + "Leerkracht", VraagType.NORMAAL, plugin.getTeamManager().getBLUE(), plugin);
        CustomNPC leerkrachtNPCRood = new CustomNPC(ChatColor.RED + "Leerkracht", VraagType.NORMAAL, plugin.getTeamManager().getRED(), plugin);

        npcList.add(leerkrachtNPCBlauw);
        npcList.add(leerkrachtNPCRood);
        npcList.add(new CustomNPC("§cShop", VraagType.SPECIAAL, plugin.getTeamManager().getRED(), plugin));
        npcList.add(new CustomNPC("§9Shop", VraagType.SPECIAAL, plugin.getTeamManager().getBLUE(), plugin));
    }

    public CustomNPC getCustomNPC(NPC npc) {
        for (CustomNPC n : npcList) {
            if (n.getNpc().equals(npc)) {
                return n;
            }
        }
        return null;
    }

    public void spawnNPC(CustomNPC npc){
        /*
        Rood:
            -leerkracht = 110.5 44.0 -37.5 -91.5 0
            -sportLeerkracht = 86.5 28.0 -130.5 40 0

        Blauw:
            -leerkracht = 317.5 44.0 -164.5 90 0
            -sportLeerkracht = 341.5 28.0 -71.5 -140.0 0

        Speciaale npc = 214.0 28.0 -101.5 0 0
         */
       Location loc = null;
       switch (npc.getType()){
           case NORMAAL:
               if(npc.getTeam().getPublicHealthBar().getTitle().equals(ChatColor.BLUE + "BLAUW")){
                   //blauw team
                   loc = new Location(Bukkit.getServer().getWorld("world"), 317.5, 44.0, -164.5, 90, 0);
               }else{
                   //rood team
                   loc = new Location(Bukkit.getServer().getWorld("world"), 110.5, 44.0, -37.5, -91.5f, 0);
               }
               break;
           case SPECIAAL:
               if(npc.getTeam().getPublicHealthBar().getTitle().equals(ChatColor.BLUE + "BLAUW")){
                   loc = new Location(Bukkit.getServer().getWorld("world"), 29.5, 40, -92.5, 90, 0);
               } else {
                   loc = new Location(Bukkit.getServer().getWorld("world"), 119.5, 44.0, -36.5, -91.5f, 0);
               }
               break;
       }
       if (loc != null) {
           npc.getNpc().spawn(loc);
       }
    }

    public void spawnAllNPC(ArrayList<CustomNPC> customNPC){
        for(CustomNPC npc: customNPC){
            this.spawnNPC(npc);
        }
    }

    public void addNPC(CustomNPC npc){
        this.npcList.add(npc);
    }

    public void removeAllNPC(){
        for(CustomNPC npc: this.npcList){
            npc.getNpc().despawn();
        }
    }

    public void setNpcList(ArrayList<CustomNPC> npcList) {
        this.npcList = npcList;
    }

    public HashMap<Player, CustomNPC> getGeselecteerdeNPC() {
        return geselecteerdeNPC;
    }

    public void addGeselecteerdeNPC(Player player, CustomNPC npc){
        this.geselecteerdeNPC.put(player, npc);
    }

    public void removeGeselecteerdeNPC(Player player){
        this.geselecteerdeNPC.remove(player);
    }

    public ArrayList<CustomNPC> getNpcList(){
        return this.npcList;
    }
}
