package bazcraft.schoolwars;

import bazcraft.schoolwars.gui.KitGUI;
import bazcraft.schoolwars.gui.Scoreboard;
import bazcraft.schoolwars.gui.VragenGUI;
import bazcraft.schoolwars.gui.shop.MainPage;
import bazcraft.schoolwars.Kit.KitTypes;
import bazcraft.schoolwars.npc.CustomNPC;
import bazcraft.schoolwars.npc.NPCType;
import bazcraft.schoolwars.vragen.Vraag;
import bazcraft.schoolwars.vragen.VraagType;
import bazcraft.schoolwars.teams.Team;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.CitizensEnableEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class EventListener implements Listener {

    private final Schoolwars plugin;

    public EventListener(Schoolwars plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        switch (GameState.getCurrentGamestate()) {
            case WAITING:
                if (plugin.getGameManager().addSpeler(event.getPlayer())) {
                    event.getPlayer().setGameMode(GameMode.ADVENTURE);
                    event.getPlayer().getInventory().addItem(new ItemStack(Material.BOW));

                    new Scoreboard(plugin, event.getPlayer()).createBoard();
                    break;
                }
            case INGAME, ENDGAME:
                event.getPlayer().setGameMode(GameMode.SPECTATOR);
                break;
        }
        event.getPlayer().teleport(plugin.getGameManager().getLobby());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event){
        event.getPlayer().getInventory().remove(Material.BOW);
        plugin.getKitManager().removeAllItemsFromPlayer(event.getPlayer());
        event.getPlayer().sendMessage("Remove all items");
        plugin.getGameManager().removeSpeler(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDamage(EntityDamageEvent event){
        //in de lobby mag men geen damage nemen
        if(GameState.getCurrentGamestate() == GameState.WAITING){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event){
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onNPCRightClick(NPCRightClickEvent event){
        Player player = event.getClicker();
        CustomNPC npc = plugin.getNpcManager().getCustomNPC(event.getNPC());
        if (npc != null) {
            if (npc.getType() == VraagType.SPECIAAL){
                MainPage mainPage = new MainPage();
                player.openInventory(mainPage.getInventory());
            } else {
                if (plugin.getTeamManager().getTeam(player) == npc.getTeam()){
                    VragenGUI gui = new VragenGUI(npc);
                    player.openInventory(gui.getInventory());
                } else {
                    player.sendMessage(ChatColor.GREEN + "Game: " + ChatColor.RED + "Dit is niet jouw leerkracht!");
                }
            }
        }
    }

    @EventHandler
    public void onGuiClick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        Team team = plugin.getTeamManager().getTeam(player);
        if (event.getClickedInventory() != null) {
            if (event.getClickedInventory().getHolder() instanceof MainPage) {
                if (event.getCurrentItem() != null) {
                    if (event.getCurrentItem().getType() == Material.PLAYER_HEAD){
                        plugin.getVragenManager().startVraag(player);
                    }
                }
                event.setCancelled(true);
            }else if(event.getClickedInventory().getHolder() instanceof VragenGUI){
                VragenGUI gui = (VragenGUI) event.getClickedInventory().getHolder();
                if(event.getCurrentItem() != null){
                    if(event.getCurrentItem().getType() == Material.BOOK){
                        boolean alleVragenBeantwoord = true;
                        for(Vraag vraag: plugin.getVragenManager().getVragenLijst()){
                            if(vraag.getType() == VraagType.NORMAAL && !vraag.getTeamsBeantwoord().get(team)){
                                alleVragenBeantwoord = false;
                                break;
                            }
                        }
                        if(alleVragenBeantwoord){
                            player.sendMessage(Schoolwars.prefix + " " + ChatColor.RED + "Alle vragen zijn al beantwoord!");
                        }else{
                            if(plugin.getKlasLokaal().getPlayersInClassRoom().containsKey(player)){
                                plugin.getVragenManager().startVraag(player);
                            }else{
                                plugin.getNpcManager().addGeselecteerdeNPC(player, gui.getNpc());
                                Bukkit.broadcastMessage(gui.getNpc().getName() + " Kaka op een stok is lekker en gezond");
                                plugin.getKlasLokaal().addPlayersInClassRoom(player);
                                plugin.getKlasLokaal().teleportToClassRoom(player);
                            }
                        }
                    }
                }
            }else if(event.getView().getTitle().equalsIgnoreCase(ChatColor.RED + "Kit Menu")){
                if(GameState.getCurrentGamestate() == GameState.WAITING){
                    if(Objects.requireNonNull(event.getCurrentItem()).getType() == Material.STONE_SWORD){
                        plugin.getKitManager().addPlayerWithKit(player, KitTypes.WARRIOR);
                        player.sendMessage(ChatColor.GREEN + "Game: " + ChatColor.AQUA + "Je hebt de " + ChatColor.RED + "Warrior" + ChatColor.AQUA + " kit gekozen");
                    }else if(event.getCurrentItem().getType().equals(Material.BOW)){
                        plugin.getKitManager().addPlayerWithKit(player, KitTypes.ARCHER);
                        player.sendMessage(ChatColor.GREEN + "Game: " + ChatColor.AQUA + "Je hebt de " + ChatColor.GREEN + "Archer" + ChatColor.AQUA + " kit gekozen");
                    }
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(GameState.getCurrentGamestate() == GameState.WAITING){
            if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK){
                if(event.getItem() != null){
                    if(Objects.requireNonNull(event.getItem()).getType() == Material.BOW){
                        KitGUI kitGUI = new KitGUI(player);
                        Inventory inventory = kitGUI.getGui();
                        player.openInventory(inventory);
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Team team = plugin.getTeamManager().getTeam(event.getPlayer());
        if (team != null) {
            event.setRespawnLocation(team.getSpawn());
        }
    }

    @EventHandler
    public void onItemPickUp(PlayerPickupArrowEvent event) {
        event.setCancelled(true);
    }


    @EventHandler(ignoreCancelled = true)
    public void onCitizensEnable(CitizensEnableEvent event) {

        for (CustomNPC n : plugin.getNpcManager().getNpcList()) {

            for (NPC m : CitizensAPI.getNPCRegistry()) {
                if (n.getName().equals(m.getName())) {
                    n.setNpc(m);
                    break;
                }
            }

            if (n.getNpc() == null) {
                CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, n.getName());
            }

        }

    }
}