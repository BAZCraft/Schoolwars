package bazcraft.schoolwars.NPC;

import bazcraft.schoolwars.Schoolwars;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.ArrayList;

public class NPCManager {

    private ArrayList<CustomNPC> npcList;
    private ArrayList<CustomNPC> geselecteerdeNPC;
    private final Schoolwars plugin;

    public NPCManager(Schoolwars plugin){
        this.plugin = plugin;
        this.npcList = new ArrayList<>();
        this.geselecteerdeNPC = new ArrayList<>();
        //Hard Coded NPC

        CustomNPC leerkrachtNPCBlauw = new CustomNPC(ChatColor.BLUE + "Leerkracht", NPCType.LEERKRACHTNPC, plugin.getTeamManager().getBLUE());
        CustomNPC leerkrachtNPCRood = new CustomNPC(ChatColor.RED + "Leerkracht", NPCType.LEERKRACHTNPC, plugin.getTeamManager().getRED());

        npcList.add(leerkrachtNPCBlauw);
        npcList.add(leerkrachtNPCRood);
        npcList.add(new CustomNPC("§cShop", NPCType.SHOP, plugin.getTeamManager().getRED()));
        npcList.add(new CustomNPC("§9Shop", NPCType.SHOP, plugin.getTeamManager().getBLUE()));
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
           case LEERKRACHTNPC:
               if(npc.getTeam().getPublicHealthBar().getTitle().equals(ChatColor.BLUE + "BLAUW")){
                   //blauw team
                   loc = new Location(Bukkit.getServer().getWorld("world"), 317.5, 44.0, -164.5, 90, 0);
               }else{
                   //rood team
                   loc = new Location(Bukkit.getServer().getWorld("world"), 110.5, 44.0, -37.5, -91.5f, 0);
               }
               break;
           case SHOP:
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

    public ArrayList<CustomNPC> getGeselecteerdeNPC() {
        return geselecteerdeNPC;
    }

    public void addGeselecteerdeNPC(CustomNPC npc) {
        this.geselecteerdeNPC.add(npc);
    }

    public void removeGeselecteerdeNPC(CustomNPC npc){
        this.geselecteerdeNPC.remove(npc);
    }

    public ArrayList<CustomNPC> getNpcList(){
        return this.npcList;
    }
}
