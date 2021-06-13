package cf.itoncek.deathstructure.listener;

import cf.itoncek.deathstructure.DeathStructure;
import cf.itoncek.deathstructure.structure.StructureUtil;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.loot.LootTables;

import java.util.Random;

public class PlayerDeathListener implements Listener {

    Random random = new Random();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        int min = 1;
        int max = 10;

        Random random = new Random();

        int value = random.nextInt(max + min) + min;
        String structure = null;
            int randomX = random.nextInt(20);
            int randomZ = random.nextInt(20);
            switch (value){
                case 1:
                    structure = "bastion";
                    StructureUtil.loadAndSlowlyPlaceCaio(structure, event.getEntity().getLocation().subtract(0,3,0), 60, null, LootTables.BASTION_OTHER.getLootTable(), DeathStructure.plugin);
                    break;
                case 2:
                    structure = "end_city";
                    StructureUtil.loadAndSlowlyPlaceCaio(structure, event.getEntity().getLocation().subtract(40+randomX,3,40+randomZ), 100, null, LootTables.END_CITY_TREASURE.getLootTable(), DeathStructure.plugin);
                    break;
                case 3:
                    structure = "fortress";
                    StructureUtil.loadAndSlowlyPlaceCaio(structure, event.getEntity().getLocation().subtract(randomX,3, randomZ), 30, EntityType.BLAZE, LootTables.NETHER_BRIDGE.getLootTable(), DeathStructure.plugin);
                    break;
                case 4:
                    structure = "jungle_pyramid";
                    StructureUtil.loadAndSlowlyPlaceCaio(structure, event.getEntity().getLocation().subtract(randomX,3,randomZ), 60, null, LootTables.JUNGLE_TEMPLE.getLootTable(), DeathStructure.plugin);
                    break;
                case 5:
                    structure = "pillager_outpost";
                    StructureUtil.loadAndSlowlyPlaceCaio(structure, event.getEntity().getLocation().subtract(randomX,0,randomZ), 30, null, LootTables.PILLAGER_OUTPOST.getLootTable(), DeathStructure.plugin);
                    break;
                case 6:
                    structure = "pyramid";
                    StructureUtil.loadAndSlowlyPlaceCaio(structure, event.getEntity().getLocation().subtract(randomX,15,randomZ), 30, null, LootTables.DESERT_PYRAMID.getLootTable(), DeathStructure.plugin);
                    break;
                case 7:
                    structure = "sunken_ship";
                    StructureUtil.loadAndSlowlyPlaceCaio(structure, event.getEntity().getLocation().subtract(randomX,3,randomZ), 30, null, LootTables.SHIPWRECK_SUPPLY.getLootTable(), DeathStructure.plugin);
                    break;
                case 8:
                    structure = "ruined_portal";
                    StructureUtil.loadAndSlowlyPlaceCaio(structure, event.getEntity().getLocation().subtract(randomX,10,randomZ), 30, null, LootTables.RUINED_PORTAL.getLootTable(), DeathStructure.plugin);
                    break;
                case 9:
                    structure = "villagesmall";
                    StructureUtil.loadAndSlowlyPlaceCaio(structure, event.getEntity().getLocation().subtract(randomX,1,randomZ), 30, null, LootTables.VILLAGE_WEAPONSMITH.getLootTable(), DeathStructure.plugin);
                    break;
                case 10:
                    structure = "stronghold";
                    StructureUtil.loadAndSlowlyPlaceCaio(structure, event.getEntity().getLocation().subtract(40,4,40), 50, EntityType.SILVERFISH, LootTables.STRONGHOLD_CORRIDOR.getLootTable(), DeathStructure.plugin);
            }
        }
    }
