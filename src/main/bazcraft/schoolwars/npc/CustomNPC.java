package bazcraft.schoolwars.npc;

import bazcraft.schoolwars.Schoolwars;
import bazcraft.schoolwars.teams.Team;
import bazcraft.schoolwars.vragen.Vraag;
import bazcraft.schoolwars.vragen.VraagType;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Random;

public class CustomNPC {
    private Schoolwars plugin;
    private NPC npc;
    private VraagType type;
    private Team team;
    private String name;
    private Vraag actieveVraag;

    public CustomNPC(String npcName, VraagType type, Team team, Schoolwars plugin){
        this.type = type;
        this.team = team;
        name = npcName;
        this.plugin = plugin;
        setNieuweVraag();
    }

    public void setNpc(NPC npc) {
        this.npc = npc;
    }

    public NPC getNpc(){
        return this.npc;
    }

    public VraagType getType(){
        return this.type;
    }

    public Team getTeam(){
        return this.team;
    }

    public String getName() {
        return name;
    }

    public boolean setNieuweVraag(){
        Random rand = new Random();
        ArrayList<Vraag> vragenLijst = new ArrayList<>();

        for(Vraag vraag: plugin.getVragenManager().getVragenLijst()){
            if(vraag.getType() == this.type && !vraag.getTeamsBeantwoord().get(team)){
                vragenLijst.add(vraag);
            }
        }
        if(vragenLijst.size() != 0){
            int randomVraag = rand.nextInt(vragenLijst.size());
            actieveVraag = vragenLijst.get(randomVraag);
            return true;
        }
        if(type == VraagType.NORMAAL){
            team.setBeantwoordenVragenN(true);
        }else if(type == VraagType.SPECIAAL){
            team.setBeantwoordenVragenS(true);
        }
        return false;
    }

    public Vraag getActieveVraag(){
        return actieveVraag;
    }
}
