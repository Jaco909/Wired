package engineer;

import engineer.forms.arrowTrap.ArrowContainer;
import engineer.forms.arrowTrap.ArrowContainerForm;
import engineer.forms.fakewall.FakeWallLogicGateContainer;
import engineer.forms.fakewall.FakeWallLogicGateForm;
import engineer.forms.led.LEDContainer;
import engineer.forms.led.LEDContainerForm;
import engineer.forms.light.LightLogicGateContainer;
import engineer.forms.light.LightLogicGateForm;
import engineer.forms.light.ProjectileLogicGateContainer;
import engineer.forms.lightGenerator.LightGeneratorLogicGateContainer;
import engineer.forms.lightGenerator.LightGeneratorLogicGateForm;
import engineer.forms.projectile.ProjectileLogicGateForm;
import engineer.forms.storage.StorageLogicGateContainer;
import engineer.forms.storage.StorageLogicGateForm;
import engineer.forms.timegame.TimeLogicGateContainer;
import engineer.forms.timegame.TimeLogicGateForm;
import engineer.forms.vanilla.*;
import engineer.forms.weather.WeatherLogicGateContainer;
import engineer.forms.weather.WeatherLogicGateForm;
import engineer.items.CustomGate;
import engineer.objects.*;
import engineer.objects.LEDPanelObject;
import engineer.objects.bench.LogicBench;
import engineer.objects.bench.LogicBenchTier2;
import engineer.objects.bench.LogicBenchTier3;
import engineer.objects.bench.LogicBenchTier4;
import engineer.objects.logic.*;
import engineer.objects.vanilla.NewArrowObjectEntity;
import engineer.objects.vanilla.TileMaskedPressurePlateObject;
import engineer.packets.*;

import engineer.projectiles.CannonballTrapProjectile;
import engineer.scripts.Settings;
import necesse.engine.modLoader.ModSettings;
import necesse.engine.modLoader.annotations.ModEntry;
import necesse.engine.network.PacketReader;
import necesse.engine.registries.*;
import necesse.entity.projectile.CannonBallProjectile;
import necesse.entity.projectile.Projectile;
import necesse.inventory.container.logicGate.*;
import necesse.inventory.item.ItemCategory;
import necesse.inventory.recipe.Ingredient;
import necesse.inventory.recipe.Recipe;
import necesse.inventory.recipe.Recipes;
import necesse.level.gameLogicGate.SimpleEntityLogicGate;
import necesse.level.gameLogicGate.entities.*;
import necesse.level.gameObject.*;
import necesse.level.gameObject.furniture.InventoryObject;

import java.awt.*;
import java.util.Iterator;

import static necesse.engine.registries.ContainerRegistry.registerLogicGateContainer;
import static necesse.engine.registries.ContainerRegistry.registerOEContainer;
import static necesse.inventory.item.ItemCategory.craftingManager;

@ModEntry
public class Engineer {

    //Define containers
    public static int TIME_LOGIC_GATE_CONTAINER;
    public static int STORAGE_LOGIC_GATE_CONTAINER;
    public static int LIGHT_LOGIC_GATE_CONTAINER;
    public static int FAKEWALL_LOGIC_GATE_CONTAINER;
    public static int WEATHER_LOGIC_GATE_CONTAINER;
    public static int PROJECTILE_LOGIC_GATE_CONTAINER;
    public static int NEW_SIMPLE_LOGIC_GATE_CONTAINER;
    public static int NEW_BUFFER_LOGIC_GATE_CONTAINER;
    public static int NEW_COUNTER_LOGIC_GATE_CONTAINER;
    public static int NEW_TIMER_LOGIC_GATE_CONTAINER;
    public static int NEW_SOUND_LOGIC_GATE_CONTAINER;
    public static int NEW_DELAY_LOGIC_GATE_CONTAINER;
    public static int NEW_SRLATCH_LOGIC_GATE_CONTAINER;
    public static int NEW_SENSOR_LOGIC_GATE_CONTAINER;
    public static int NEW_TFLIPFLOP_LOGIC_GATE_CONTAINER;
    public static int LED_CONTAINER;
    public static int ARROW_CONTAINER;
    public static int LIGHT_GENERATOR_LOGIC_GATE_CONTAINER;

    //Define settings file
    public static Settings settings;



    public void init() {
        System.out.println("Wired Loaded!");

        /*/////////////////////////////////////////
        //CATEGORIES
        /////////////////////////////////////////*/

        //Important
        ItemCategory.masterManager.createCategory("A-A-A", "tools");
        craftingManager.createCategory("A-A-A", new String[]{"tools"});

        //Logic
        ItemCategory.masterManager.createCategory("A-A-B", "logicgates");
        craftingManager.createCategory("A-A-B", new String[]{"logicgates"});
        ItemCategory.masterManager.createCategory("A-B-A", "logiccalculators");
        craftingManager.createCategory("A-B-A", new String[]{"logiccalculators"});
        ItemCategory.masterManager.createCategory("A-C-A", "logicsensors");
        craftingManager.createCategory("A-C-A", new String[]{"logicsensors"});
        ItemCategory.masterManager.createCategory("A-D-A", "logicactivators");
        craftingManager.createCategory("A-D-A", new String[]{"logicactivators"});

        //Triggers
        ItemCategory.masterManager.createCategory("B-A-A", "triggers");
        craftingManager.createCategory("B-A-A", new String[]{"triggers"});
        ItemCategory.masterManager.createCategory("B-B-A", "pressureplate");
        craftingManager.createCategory("B-B-A", new String[]{"pressureplate"});

        //Misc
        ItemCategory.masterManager.createCategory("C-A-A", "deco");
        craftingManager.createCategory("C-A-A", new String[]{"deco"});
        ItemCategory.masterManager.createCategory("Z-Z-Z", "other");
        craftingManager.createCategory("Z-Z-Z", new String[]{"other"});

        //Traps
        ItemCategory.masterManager.createCategory("Z-A-A", "arrowtrap");
        craftingManager.createCategory("G-A-A", new String[]{"arrowtrap"});
        ItemCategory.masterManager.createCategory("Z-A-B", "flametrap");
        craftingManager.createCategory("G-A-B", new String[]{"flametrap"});
        ItemCategory.masterManager.createCategory("Z-A-C", "voidtrap");
        craftingManager.createCategory("G-A-C", new String[]{"voidtrap"});
        ItemCategory.masterManager.createCategory("Z-A-D", "cannonballtrap");
        craftingManager.createCategory("G-A-D", new String[]{"cannonballtrap"});
        ItemCategory.masterManager.createCategory("Z-A-E", "sawtrap");
        craftingManager.createCategory("G-A-D", new String[]{"sawtrap"});
        ItemCategory.masterManager.createCategory("Z-B-A", "floortrap");
        craftingManager.createCategory("G-B-A", new String[]{"floortrap"});

        /*/////////////////////////////////////////
        //OBJECTS
        /////////////////////////////////////////*/

        //CRAFTING BENCHES
        RecipeTechRegistry.registerTech("LOGICBENCH", "LogicBench");
        ObjectRegistry.registerObject("LogicBench", new LogicBench(), 10f, true);
        RecipeTechRegistry.registerTech("LOGICBENCH2", "LogicBenchTier2");
        ObjectRegistry.registerObject("LogicBenchTier2", new LogicBenchTier2(), 10f, true);
        RecipeTechRegistry.registerTech("LOGICBENCH3", "LogicBenchTier3");
        ObjectRegistry.registerObject("LogicBenchTier3", new LogicBenchTier3(), 10f, true);
        RecipeTechRegistry.registerTech("LOGICBENCH4", "LogicBenchTier4");
        ObjectRegistry.registerObject("LogicBenchTier4", new LogicBenchTier4(), 10f, true);

        //PRESSURE PLATES
        //Too many unique variables to easily define dynamically
        //Do it the long and gross way
        ObjectRegistry.registerObject("woodpathpressureplate", new TileMaskedPressurePlateObject("pressureplatemask", "woodpath", new Color(103, 61, 18)), 5.0F, true);
        ObjectRegistry.registerObject("deadwoodpressureplate", new MaskedPressurePlateObject("pressureplatemask", "deadwoodfloor", new Color(59, 43, 40)), 5.0F, true);
        ObjectRegistry.registerObject("pinepressureplate", new MaskedPressurePlateObject("pressureplatemask", "pinefloor", new Color(125, 83, 27)), 5.0F, true);
        ObjectRegistry.registerObject("palmpressureplate", new MaskedPressurePlateObject("pressureplatemask", "palmfloor", new Color(119, 83, 26)), 5.0F, true);
        ObjectRegistry.registerObject("bamboopressureplate", new MaskedPressurePlateObject("pressureplatemask", "bamboofloor", new Color(116, 74, 71)), 5.0F, true);
        ObjectRegistry.registerObject("strawpressureplate", new MaskedPressurePlateObject("pressureplatemask", "strawtile", new Color(255, 207, 5)), 5.0F, true);
        ObjectRegistry.registerObject("stonebrickpressureplate", new MaskedPressurePlateObject("pressureplatemask", "stonebrickfloor", new Color(125, 136, 146)), 5.0F, true);
        ObjectRegistry.registerObject("stonetiledpressureplate", new MaskedPressurePlateObject("pressureplatemask", "stonetiledfloor", new Color(125, 136, 146)), 5.0F, true);
        ObjectRegistry.registerObject("snowstonebrickpressureplate", new MaskedPressurePlateObject("pressureplatemask", "snowstonebrickfloor", new Color(74, 112, 156)), 5.0F, true);
        ObjectRegistry.registerObject("swampstonebrickpressureplate", new MaskedPressurePlateObject("pressureplatemask", "swampstonebrickfloor", new Color(63, 96, 80)), 5.0F, true);
        ObjectRegistry.registerObject("sandstonebrickpressureplate", new MaskedPressurePlateObject("pressureplatemask", "sandstonebrickfloor", new Color(160, 136, 94)), 5.0F, true);
        ObjectRegistry.registerObject("sandbrickpressureplate", new MaskedPressurePlateObject("pressureplatemask", "sandbrick", new Color(160, 136, 94)), 5.0F, true);
        ObjectRegistry.registerObject("deepstonebrickpressureplate", new MaskedPressurePlateObject("pressureplatemask", "deepstonebrickfloor", new Color(57, 59, 60)), 5.0F, true);
        ObjectRegistry.registerObject("deepstonetiledpressureplate", new MaskedPressurePlateObject("pressureplatemask", "deepstonetiledfloor", new Color(66, 73, 75)), 5.0F, true);
        ObjectRegistry.registerObject("deepsnowstonebrickpressureplate", new MaskedPressurePlateObject("pressureplatemask", "deepsnowstonebrickfloor", new Color(75, 78, 80)), 5.0F, true);
        ObjectRegistry.registerObject("deepswampstonebrickpressureplate", new MaskedPressurePlateObject("pressureplatemask", "deepswampstonebrickfloor", new Color(63, 96, 80)), 5.0F, true);
        ObjectRegistry.registerObject("stonepathpressureplate", new TileMaskedPressurePlateObject("pressureplatemask", "stonepath", new Color(110, 117, 127)), 5.0F, true);
        ObjectRegistry.registerObject("snowstonepathpressureplate", new MaskedPressurePlateObject("pressureplatemask", "snowstonepath", new Color(74, 112, 156)), 5.0F, true);
        ObjectRegistry.registerObject("sandstonepathpressureplate", new TileMaskedPressurePlateObject("pressureplatemask", "sandstonepath", new Color(160, 136, 94)), 5.0F, true);
        ObjectRegistry.registerObject("swampstonepathpressureplate", new TileMaskedPressurePlateObject("pressureplatemask", "swampstonepath", new Color(57, 77, 60)), 5.0F, true);
        ObjectRegistry.registerObject("spidercobbletilepressureplate", new MaskedPressurePlateObject("pressureplatemask", "spidercobbletile", new Color(50, 90, 104)), 5.0F, true);
        ObjectRegistry.registerObject("spidercastlepressureplate", new MaskedPressurePlateObject("pressureplatemask", "spidercastlefloor", new Color(47, 44, 71)), 5.0F, true);
        ObjectRegistry.registerObject("spidercastlecarpetpressureplate", new TileMaskedPressurePlateObject("pressureplatemask", "spidercastlecarpet", new Color(151, 84, 117)), 5.0F, true);
        ObjectRegistry.registerObject("slimerocktilepressureplate", new MaskedPressurePlateObject("pressureplatemask", "slimerocktile", new Color(47, 117, 47)), 5.0F, true);
        ObjectRegistry.registerObject("ancientruinpressureplate", new MaskedPressurePlateObject("pressureplatemask", "ancientruinfloor", new Color(114, 90, 81)), 5.0F, true);
        ObjectRegistry.registerObject("cryptpathpressureplate", new TileMaskedPressurePlateObject("pressureplatemask", "cryptpath", new Color(66, 66, 78)), 5.0F, true);
        ObjectRegistry.registerObject("darkmoonpathpressureplate", new TileMaskedPressurePlateObject("pressureplatemask", "darkmoonpath", new Color(40, 103, 108)), 5.0F, true);
        ObjectRegistry.registerObject("darkfullmoonpathpressureplate", new TileMaskedPressurePlateObject("pressureplatemask", "darkfullmoonpath", new Color(86, 80, 111)), 5.0F, true);
        ObjectRegistry.registerObject("dawnpathpressureplate", new TileMaskedPressurePlateObject("pressureplatemask", "dawnpath", new Color(236, 197, 129)), 5.0F, true);
        ObjectRegistry.registerObject("lavapathpressureplate", new TileMaskedPressurePlateObject("pressureplatemask", "lavapath", new Color(255, 90, 30)), 5.0F, true);
        ObjectRegistry.registerObject("moonpathpressureplate", new TileMaskedPressurePlateObject("pressureplatemask", "moonpath", new Color(200, 253, 253)), 5.0F, true);

        /*/////////////////////////////////////////
        //TRAPS
        /////////////////////////////////////////*/
        //Some wall types are cursed and don't load when registering dynamically
        //So do it the long and gross way

        //Arrow Traps
        ObjectRegistry.registerObject("pinearrowtrap", new engineer.objects.vanilla.WallArrowTrapObject((WallObject)ObjectRegistry.getObject("pinewall")), 5.0F, true);
        ObjectRegistry.registerObject("palmarrowtrap", new engineer.objects.vanilla.WallArrowTrapObject((WallObject)ObjectRegistry.getObject("palmwall")), 5.0F, true);
        ObjectRegistry.registerObject("brickarrowtrap", new engineer.objects.vanilla.WallArrowTrapObject((WallObject)ObjectRegistry.getObject("brickwall")), 5.0F, true);
        ObjectRegistry.registerObject("bambooarrowtrap", new engineer.objects.vanilla.WallArrowTrapObject((WallObject)ObjectRegistry.getObject("bamboowall")), 5.0F, true);
        ObjectRegistry.registerObject("dawnarrowtrap", new engineer.objects.vanilla.WallArrowTrapObject((WallObject)ObjectRegistry.getObject("dawnwall")), 5.0F, true);
        ObjectRegistry.registerObject("duskarrowtrap", new engineer.objects.vanilla.WallArrowTrapObject((WallObject)ObjectRegistry.getObject("duskwall")), 5.0F, true);
        ObjectRegistry.registerObject("cryptarrowtrap", new engineer.objects.vanilla.WallArrowTrapObject((WallObject)ObjectRegistry.getObject("cryptwall")), 5.0F, true);
        ObjectRegistry.registerObject("ancientruinwallarrowtrap", new engineer.objects.vanilla.WallArrowTrapObject((WallObject)ObjectRegistry.getObject("ancientruinwall")), 5.0F, true);

        //Flame Traps
        ObjectRegistry.registerObject("woodflametrap", new WallFlameTrapObject((WallObject)ObjectRegistry.getObject("woodwall")), 50.0F, true);
        ObjectRegistry.registerObject("pineflametrap", new WallFlameTrapObject((WallObject)ObjectRegistry.getObject("pinewall")), 50.0F, true);
        ObjectRegistry.registerObject("palmflametrap", new WallFlameTrapObject((WallObject)ObjectRegistry.getObject("palmwall")), 50.0F, true);
        ObjectRegistry.registerObject("bambooflametrap", new WallFlameTrapObject((WallObject)ObjectRegistry.getObject("bamboowall")), 50.0F, true);
        ObjectRegistry.registerObject("brickflametrap", new WallFlameTrapObject((WallObject)ObjectRegistry.getObject("brickwall")), 50.0F, true);
        ObjectRegistry.registerObject("snowstoneflametrap", new WallFlameTrapObject((WallObject)ObjectRegistry.getObject("snowstonewall")), 50.0F, true);
        ObjectRegistry.registerObject("iceflametrap", new WallFlameTrapObject((WallObject)ObjectRegistry.getObject("icewall")), 50.0F, true);
        ObjectRegistry.registerObject("dawnflametrap", new WallFlameTrapObject((WallObject)ObjectRegistry.getObject("dawnwall")), 50.0F, true);
        ObjectRegistry.registerObject("duskflametrap", new WallFlameTrapObject((WallObject)ObjectRegistry.getObject("duskwall")), 50.0F, true);
        ObjectRegistry.registerObject("cryptflametrap", new WallFlameTrapObject((WallObject)ObjectRegistry.getObject("cryptwall")), 50.0F, true);
        ObjectRegistry.registerObject("spidercastleflametrap", new WallFlameTrapObject((WallObject)ObjectRegistry.getObject("spidercastlewall")), 50.0F, true);
        ObjectRegistry.registerObject("ancientruinwallflametrap", new WallFlameTrapObject((WallObject)ObjectRegistry.getObject("ancientruinwall")), 50.0F, true);

        //Saw Traps
/*
        ObjectRegistry.replaceObject("woodsawtrap", new WallSawTrapObject((WallObject)ObjectRegistry.getObject("woodwall")), 50.0F, false);
        ObjectRegistry.registerObject("pinesawtrap", new WallSawTrapObject((WallObject)ObjectRegistry.getObject("pinewall")), 50.0F, false);
        ObjectRegistry.registerObject("palmsawtrap", new WallSawTrapObject((WallObject)ObjectRegistry.getObject("palmwall")), 50.0F, false);
        ObjectRegistry.registerObject("bamboosawtrap", new WallSawTrapObject((WallObject)ObjectRegistry.getObject("bamboowall")), 50.0F, false);
        ObjectRegistry.registerObject("bricksawtrap", new WallSawTrapObject((WallObject)ObjectRegistry.getObject("brickwall")), 50.0F, false);
        ObjectRegistry.replaceObject("stonesawtrap", new WallSawTrapObject((WallObject)ObjectRegistry.getObject("stonewall")), 50.0F, false);
        ObjectRegistry.replaceObject("snowstonesawtrap", new WallSawTrapObject((WallObject)ObjectRegistry.getObject("snowstonewall")), 50.0F, false);
        ObjectRegistry.replaceObject("dungeonsawtrap", new WallSawTrapObject((WallObject)ObjectRegistry.getObject("dungeonwall")), 50.0F, false);
        ObjectRegistry.replaceObject("icesawtrap", new WallSawTrapObject((WallObject)ObjectRegistry.getObject("icewall")), 50.0F, false);
        ObjectRegistry.replaceObject("swampstonesawtrap", new WallSawTrapObject((WallObject)ObjectRegistry.getObject("swampstonewall")), 50.0F, false);
        ObjectRegistry.replaceObject("sandstonesawtrap", new WallSawTrapObject((WallObject)ObjectRegistry.getObject("sandstonewall")), 50.0F, false);
        ObjectRegistry.replaceObject("deepstonesawtrap", new WallSawTrapObject((WallObject)ObjectRegistry.getObject("deepstonewall")), 50.0F, false);
        ObjectRegistry.replaceObject("deepsnowstonesawtrap", new WallSawTrapObject((WallObject)ObjectRegistry.getObject("deepsnowstonewall")), 50.0F, false);
        ObjectRegistry.replaceObject("deepswampstonesawtrap", new WallSawTrapObject((WallObject)ObjectRegistry.getObject("deepswampstonewall")), 50.0F, false);
        ObjectRegistry.replaceObject("deepsandstonesawtrap", new WallSawTrapObject((WallObject)ObjectRegistry.getObject("deepsandstonewall")), 50.0F, false);
        ObjectRegistry.replaceObject("obsidiansawtrap", new WallSawTrapObject((WallObject)ObjectRegistry.getObject("obsidianwall")), 50.0F, false);
        ObjectRegistry.registerObject("dawnsawtrap", new WallSawTrapObject((WallObject)ObjectRegistry.getObject("dawnwall")), 50.0F, false);
        ObjectRegistry.registerObject("dusksawtrap", new WallSawTrapObject((WallObject)ObjectRegistry.getObject("duskwall")), 50.0F, false);
        ObjectRegistry.registerObject("cryptsawtrap", new WallSawTrapObject((WallObject)ObjectRegistry.getObject("cryptwall")), 50.0F, false);
        ObjectRegistry.registerObject("spidercastlesawtrap", new WallSawTrapObject((WallObject)ObjectRegistry.getObject("spidercastlewall")), 50.0F, false);
        ObjectRegistry.registerObject("ancientruinwallsawtrap", new WallSawTrapObject((WallObject)ObjectRegistry.getObject("ancientruinwall")), 50.0F, false);
*/

        //Void Traps
        ObjectRegistry.registerObject("woodvoidtrap", new WallVoidTrapObject((WallObject)ObjectRegistry.getObject("woodwall")), 50.0F, true);
        ObjectRegistry.registerObject("pinevoidtrap", new WallVoidTrapObject((WallObject)ObjectRegistry.getObject("pinewall")), 50.0F, true);
        ObjectRegistry.registerObject("palmvoidtrap", new WallVoidTrapObject((WallObject)ObjectRegistry.getObject("palmwall")), 50.0F, true);
        ObjectRegistry.registerObject("bamboovoidtrap", new WallVoidTrapObject((WallObject)ObjectRegistry.getObject("bamboowall")), 50.0F, true);
        ObjectRegistry.registerObject("brickvoidtrap", new WallVoidTrapObject((WallObject)ObjectRegistry.getObject("brickwall")), 50.0F, true);
        ObjectRegistry.registerObject("stonevoidtrap", new WallVoidTrapObject((WallObject)ObjectRegistry.getObject("stonewall")), 50.0F, true);
        ObjectRegistry.registerObject("snowstonevoidtrap", new WallVoidTrapObject((WallObject)ObjectRegistry.getObject("snowstonewall")), 50.0F, true);
        ObjectRegistry.registerObject("icevoidtrap", new WallVoidTrapObject((WallObject)ObjectRegistry.getObject("icewall")), 50.0F, true);
        ObjectRegistry.registerObject("swampstonevoidtrap", new WallVoidTrapObject((WallObject)ObjectRegistry.getObject("swampstonewall")), 50.0F, true);
        ObjectRegistry.registerObject("sandstonevoidtrap", new WallVoidTrapObject((WallObject)ObjectRegistry.getObject("sandstonewall")), 50.0F, true);
        ObjectRegistry.registerObject("deepstonevoidtrap", new WallVoidTrapObject((WallObject)ObjectRegistry.getObject("deepstonewall")), 50.0F, true);
        ObjectRegistry.registerObject("deepswampstonevoidtrap", new WallVoidTrapObject((WallObject)ObjectRegistry.getObject("deepswampstonewall")), 50.0F, true);
        ObjectRegistry.registerObject("deepsandstonevoidtrap", new WallVoidTrapObject((WallObject)ObjectRegistry.getObject("deepsandstonewall")), 50.0F, true);
        ObjectRegistry.registerObject("deepsnowstonevoidtrap", new WallVoidTrapObject((WallObject)ObjectRegistry.getObject("deepsnowstonewall")), 50.0F, true);
        ObjectRegistry.registerObject("obsidianvoidtrap", new WallVoidTrapObject((WallObject)ObjectRegistry.getObject("obsidianwall")), 50.0F, true);
        ObjectRegistry.registerObject("dawnvoidtrap", new WallVoidTrapObject((WallObject)ObjectRegistry.getObject("dawnwall")), 50.0F, true);
        ObjectRegistry.registerObject("duskvoidtrap", new WallVoidTrapObject((WallObject)ObjectRegistry.getObject("duskwall")), 50.0F, true);
        ObjectRegistry.registerObject("cryptvoidtrap", new WallVoidTrapObject((WallObject)ObjectRegistry.getObject("cryptwall")), 50.0F, true);
        ObjectRegistry.registerObject("spidercastlevoidtrap", new WallVoidTrapObject((WallObject)ObjectRegistry.getObject("spidercastlewall")), 50.0F, true);
        ObjectRegistry.registerObject("ancientruinwallvoidtrap", new WallVoidTrapObject((WallObject)ObjectRegistry.getObject("ancientruinwall")), 50.0F, true);

        //Cannonball Traps
        ObjectRegistry.registerObject("woodcannonballtrap", new WallCannonBallTrapObject((WallObject)ObjectRegistry.getObject("woodwall")), 50.0F, true);
        ObjectRegistry.registerObject("pinecannonballtrap", new WallCannonBallTrapObject((WallObject)ObjectRegistry.getObject("pinewall")), 50.0F, true);
        ObjectRegistry.registerObject("palmcannonballtrap", new WallCannonBallTrapObject((WallObject)ObjectRegistry.getObject("palmwall")), 50.0F, true);
        ObjectRegistry.registerObject("bamboocannonballtrap", new WallCannonBallTrapObject((WallObject)ObjectRegistry.getObject("bamboowall")), 50.0F, true);
        ObjectRegistry.registerObject("brickcannonballtrap", new WallCannonBallTrapObject((WallObject)ObjectRegistry.getObject("brickwall")), 50.0F, true);
        ObjectRegistry.registerObject("stonecannonballtrap", new WallCannonBallTrapObject((WallObject)ObjectRegistry.getObject("stonewall")), 50.0F, true);
        ObjectRegistry.registerObject("snowstonecannonballtrap", new WallCannonBallTrapObject((WallObject)ObjectRegistry.getObject("snowstonewall")), 50.0F, true);
        ObjectRegistry.registerObject("icecannonballtrap", new WallCannonBallTrapObject((WallObject)ObjectRegistry.getObject("icewall")), 50.0F, true);
        ObjectRegistry.registerObject("swampstonecannonballtrap", new WallCannonBallTrapObject((WallObject)ObjectRegistry.getObject("swampstonewall")), 50.0F, true);
        ObjectRegistry.registerObject("sandstonecannonballtrap", new WallCannonBallTrapObject((WallObject)ObjectRegistry.getObject("sandstonewall")), 50.0F, true);
        ObjectRegistry.registerObject("dungeoncannonballtrap", new WallCannonBallTrapObject((WallObject)ObjectRegistry.getObject("dungeonwall")), 50.0F, true);
        ObjectRegistry.registerObject("deepstonecannonballtrap", new WallCannonBallTrapObject((WallObject)ObjectRegistry.getObject("deepstonewall")), 50.0F, true);
        ObjectRegistry.registerObject("deepswampstonecannonballtrap", new WallCannonBallTrapObject((WallObject)ObjectRegistry.getObject("deepswampstonewall")), 50.0F, true);
        ObjectRegistry.registerObject("deepsandstonecannonballtrap", new WallCannonBallTrapObject((WallObject)ObjectRegistry.getObject("deepsandstonewall")), 50.0F, true);
        ObjectRegistry.registerObject("deepsnowstonecannonballtrap", new WallCannonBallTrapObject((WallObject)ObjectRegistry.getObject("deepsnowstonewall")), 50.0F, true);
        ObjectRegistry.registerObject("obsidiancannonballtrap", new WallCannonBallTrapObject((WallObject)ObjectRegistry.getObject("obsidianwall")), 50.0F, true);
        ObjectRegistry.registerObject("dawncannonballtrap", new WallCannonBallTrapObject((WallObject)ObjectRegistry.getObject("dawnwall")), 50.0F, true);
        ObjectRegistry.registerObject("duskcannonballtrap", new WallCannonBallTrapObject((WallObject)ObjectRegistry.getObject("duskwall")), 50.0F, true);
        ObjectRegistry.registerObject("cryptcannonballtrap", new WallCannonBallTrapObject((WallObject)ObjectRegistry.getObject("cryptwall")), 50.0F, true);
        ObjectRegistry.registerObject("spidercastlecannonballtrap", new WallCannonBallTrapObject((WallObject)ObjectRegistry.getObject("spidercastlewall")), 50.0F, true);
        ObjectRegistry.registerObject("ancientruinwallcannonballtrap", new WallCannonBallTrapObject((WallObject)ObjectRegistry.getObject("ancientruinwall")), 50.0F, true);

        //Floor Traps
        //ObjectRegistry.replaceObject("spiketrap", new SpikeTrapObject(), 20.0F, true);
        //ObjectRegistry.replaceObject("traptrack", new TrapTrackObject(), 10.0F, true);

        /*/////////////////////////////////////////
        //MISC OBJECTS
        /////////////////////////////////////////*/
        ObjectRegistry.replaceObject("ledpanel", new LEDPanelObject(),10.0F,true);
        ObjectRegistry.registerObject("fakewall", new FakeWallObject(),0.0F,false);

        //I don't know why I can make chests dynamically register but not traps.
        //whatever
        Iterator objects = ObjectRegistry.getObjects().iterator();
        while (objects.hasNext()) {
            GameObject object = (GameObject)objects.next();
            
            if (object instanceof WallArrowTrapObject) {
                if (object.getStringID().contains("arrowtrap") && !object.getStringID().contains("ancientruinwall")) {
                    ObjectRegistry.replaceObject(object.getStringID(), new engineer.objects.vanilla.WallArrowTrapObject((WallObject) ObjectRegistry.getObject(object.getStringID().substring(0, object.getStringID().length() - 9) + "wall")), 5, true);
                } else if (object.getStringID().contains("arrowtrap") && !object.getStringID().contains("wall")) {
                    ObjectRegistry.replaceObject(object.getStringID(), new engineer.objects.vanilla.WallArrowTrapObject((WallObject) ObjectRegistry.getObject(object.getStringID().substring(0, object.getStringID().length() - 9))), 5, true);
                } else if (object.getStringID().contains("arrowtrap") && object.getStringID().contains("ancientruinwall")) {
                    ObjectRegistry.replaceObject(object.getStringID(), new engineer.objects.vanilla.WallArrowTrapObject((WallObject) ObjectRegistry.getObject(object.getStringID().substring(0, object.getStringID().length() - 9))), 5, true);
                }
            } else if (object instanceof InventoryObject) {
                if (!object.getStringID().equalsIgnoreCase("barrel")) {
                    ObjectRegistry.registerObject(object.getStringID() + "Trapped", new TrappedContainerObject(object.getStringID(), 40, object.toolType, object.mapColor), 10, false);
                }
            }
        }

        //Because for some reason these walls are cursed and don't dynamically load
        //I guess this is why I had to manually define every trap
        ObjectRegistry.replaceObject("woodarrowtrap",new engineer.objects.vanilla.WallArrowTrapObject((WallObject) ObjectRegistry.getObject("woodwall")),5,true);
        ObjectRegistry.replaceObject("deepsandstonearrowtrap",new engineer.objects.vanilla.WallArrowTrapObject((WallObject) ObjectRegistry.getObject("deepsandstonewall")),5,true);
        ObjectRegistry.replaceObject("deepsnowstonearrowtrap",new engineer.objects.vanilla.WallArrowTrapObject((WallObject) ObjectRegistry.getObject("deepsnowstonewall")),5,true);
        ObjectRegistry.replaceObject("deepstonearrowtrap",new engineer.objects.vanilla.WallArrowTrapObject((WallObject) ObjectRegistry.getObject("deepstonewall")),5,true);
        ObjectRegistry.replaceObject("dungeonarrowtrap",new engineer.objects.vanilla.WallArrowTrapObject((WallObject) ObjectRegistry.getObject("dungeonwall")),5,true);
        ObjectRegistry.replaceObject("sandstonearrowtrap",new engineer.objects.vanilla.WallArrowTrapObject((WallObject) ObjectRegistry.getObject("sandstonewall")),5,true);
        ObjectRegistry.replaceObject("obsidianarrowtrap",new engineer.objects.vanilla.WallArrowTrapObject((WallObject) ObjectRegistry.getObject("obsidianwall")),5,true);
        ObjectRegistry.replaceObject("spidercastlearrowtrap",new engineer.objects.vanilla.WallArrowTrapObject((WallObject) ObjectRegistry.getObject("spidercastlewall")),5,true);


        /*/////////////////////////////////////////
        //PACKETS
        /////////////////////////////////////////*/
        PacketRegistry.registerPacket(LEDColorPacket.class);
        PacketRegistry.registerPacket(LEDInputPacket.class);
        PacketRegistry.registerPacket(ArrowTrapInventoryPacket.class);
        PacketRegistry.registerPacket(FakeWallIDPacket.class);
        PacketRegistry.registerPacket(OpenTrapChestPacket.class);
        PacketRegistry.registerPacket(CloseTrapChestPacket.class);
        PacketRegistry.registerPacket(UpdateSotrageSizePacket.class);

        /*/////////////////////////////////////////
        //LOGIC GATES
        /////////////////////////////////////////*/
        LogicGateRegistry.registerLogicGate("timeGate", new SimpleEntityLogicGate(TimeLogicGateEntity::new), 5.0F, true);
        LogicGateRegistry.registerLogicGate("storageGate", new SimpleEntityLogicGate(StorageLogicGateEntity::new), 5.0F, true);
        LogicGateRegistry.registerLogicGate("lightGate", new SimpleEntityLogicGate(LightLogicGateEntity::new), 5.0F, true);
        LogicGateRegistry.registerLogicGate("fakewallGate", new SimpleEntityLogicGate(WallLogicGateEntity::new), 5.0F, true);
        LogicGateRegistry.registerLogicGate("weatherGate", new SimpleEntityLogicGate(WeatherLogicGateEntity::new), 5.0F, true);
        LogicGateRegistry.registerLogicGate("projectileGate", new SimpleEntityLogicGate(ProjectileLogicGateEntity::new), 5.0F, true);
        LogicGateRegistry.registerLogicGate("lightGeneratorGate", new CustomLightGate(LightGeneratorLogicGateEntity::new), 5.0F, false);
        LogicGateRegistry.registerLogicGate("xnorGate", new SimpleEntityLogicGate(XNORLogicGateEntity::new), 5.0F, false);

        ProjectileRegistry.registerProjectile("trapCannonball", CannonballTrapProjectile.class,"cannonball","cannonball_shadow");


        //ItemRegistry.registerItem("chestTool", new ChestTool(0), 50f, true,true);
        //ItemRegistry.registerItem("wireRemote", new WireRemote(0), 50f, true,true);

        /*/////////////////////////////////////////
        //CONTAINERS
        /////////////////////////////////////////*/
        ARROW_CONTAINER = registerOEContainer((client, uniqueSeed, oe, content) -> new ArrowContainerForm(client, new ArrowContainer(client.getClient(), uniqueSeed, (NewArrowObjectEntity)oe, new PacketReader(content))), (client, uniqueSeed, oe, content, serverObject) -> new ArrowContainer(client, uniqueSeed, (NewArrowObjectEntity)oe, new PacketReader(content)));
        LED_CONTAINER = registerOEContainer((client, uniqueSeed, oe, content) -> new LEDContainerForm(client, new LEDContainer(client.getClient(), uniqueSeed, (LEDObjectEntity)oe)), (client, uniqueSeed, oe, content, serverObject) -> new LEDContainer(client, uniqueSeed, (LEDObjectEntity)oe));
        TIME_LOGIC_GATE_CONTAINER = registerLogicGateContainer((client, uniqueSeed, lge, content) -> new TimeLogicGateForm(client, new TimeLogicGateContainer(client.getClient(), uniqueSeed, (TimeLogicGateEntity)lge)), (client, uniqueSeed, lge, content, serverObject) -> new TimeLogicGateContainer(client, uniqueSeed, (TimeLogicGateEntity)lge));
        STORAGE_LOGIC_GATE_CONTAINER = registerLogicGateContainer((client, uniqueSeed, lge, content) -> new StorageLogicGateForm(client, new StorageLogicGateContainer(client.getClient(), uniqueSeed, (StorageLogicGateEntity)lge)), (client, uniqueSeed, lge, content, serverObject) -> new StorageLogicGateContainer(client, uniqueSeed, (StorageLogicGateEntity)lge));
        LIGHT_LOGIC_GATE_CONTAINER = registerLogicGateContainer((client, uniqueSeed, lge, content) -> new LightLogicGateForm(client, new LightLogicGateContainer(client.getClient(), uniqueSeed, (LightLogicGateEntity)lge)), (client, uniqueSeed, lge, content, serverObject) -> new LightLogicGateContainer(client, uniqueSeed, (LightLogicGateEntity)lge));
        FAKEWALL_LOGIC_GATE_CONTAINER = registerLogicGateContainer((client, uniqueSeed, lge, content) -> new FakeWallLogicGateForm(client, new FakeWallLogicGateContainer(client.getClient(), uniqueSeed, (WallLogicGateEntity)lge)), (client, uniqueSeed, lge, content, serverObject) -> new FakeWallLogicGateContainer(client, uniqueSeed, (WallLogicGateEntity)lge));
        WEATHER_LOGIC_GATE_CONTAINER = registerLogicGateContainer((client, uniqueSeed, lge, content) -> new WeatherLogicGateForm(client, new WeatherLogicGateContainer(client.getClient(), uniqueSeed, (WeatherLogicGateEntity)lge)), (client, uniqueSeed, lge, content, serverObject) -> new WeatherLogicGateContainer(client, uniqueSeed, (WeatherLogicGateEntity)lge));
        PROJECTILE_LOGIC_GATE_CONTAINER = registerLogicGateContainer((client, uniqueSeed, lge, content) -> new ProjectileLogicGateForm(client, new ProjectileLogicGateContainer(client.getClient(), uniqueSeed, (ProjectileLogicGateEntity)lge)), (client, uniqueSeed, lge, content, serverObject) -> new ProjectileLogicGateContainer(client, uniqueSeed, (ProjectileLogicGateEntity)lge));
        LIGHT_GENERATOR_LOGIC_GATE_CONTAINER = registerLogicGateContainer((client, uniqueSeed, lge, content) -> new LightGeneratorLogicGateForm(client, new LightGeneratorLogicGateContainer(client.getClient(), uniqueSeed, (LightGeneratorLogicGateEntity)lge)), (client, uniqueSeed, lge, content, serverObject) -> new LightGeneratorLogicGateContainer(client, uniqueSeed, (LightGeneratorLogicGateEntity)lge));

        NEW_SIMPLE_LOGIC_GATE_CONTAINER = registerLogicGateContainer((client, uniqueSeed, lge, content) -> new NewSimpleLogicGateContainerForm(client, new SimpleLogicGateContainer(client.getClient(), uniqueSeed, (SimpleLogicGateEntity)lge)), (client, uniqueSeed, lge, content, serverObject) -> new SimpleLogicGateContainer(client, uniqueSeed, (SimpleLogicGateEntity)lge));
        NEW_BUFFER_LOGIC_GATE_CONTAINER = registerLogicGateContainer((client, uniqueSeed, lge, content) -> new NewBufferLogicGateContainerForm(client, new BufferLogicGateContainer(client.getClient(), uniqueSeed, (BufferLogicGateEntity)lge)), (client, uniqueSeed, lge, content, serverObject) -> new BufferLogicGateContainer(client, uniqueSeed, (BufferLogicGateEntity)lge));
        NEW_COUNTER_LOGIC_GATE_CONTAINER = registerLogicGateContainer((client, uniqueSeed, lge, content) -> new NewCounterLogicGateContainerForm(client, new CounterLogicGateContainer(client.getClient(), uniqueSeed, (CounterLogicGateEntity)lge)), (client, uniqueSeed, lge, content, serverObject) -> new CounterLogicGateContainer(client, uniqueSeed, (CounterLogicGateEntity)lge));
        NEW_DELAY_LOGIC_GATE_CONTAINER = registerLogicGateContainer((client, uniqueSeed, lge, content) -> new NewDelayLogicGateContainerForm(client, new DelayLogicGateContainer(client.getClient(), uniqueSeed, (DelayLogicGateEntity)lge)), (client, uniqueSeed, lge, content, serverObject) -> new DelayLogicGateContainer(client, uniqueSeed, (DelayLogicGateEntity)lge));
        NEW_SRLATCH_LOGIC_GATE_CONTAINER = registerLogicGateContainer((client, uniqueSeed, lge, content) -> new NewSRLatchLogicGateContainerForm(client, new SRLatchLogicGateContainer(client.getClient(), uniqueSeed, (SRLatchLogicGateEntity)lge)), (client, uniqueSeed, lge, content, serverObject) -> new SRLatchLogicGateContainer(client, uniqueSeed, (SRLatchLogicGateEntity)lge));
        NEW_SENSOR_LOGIC_GATE_CONTAINER = registerLogicGateContainer((client, uniqueSeed, lge, content) -> new NewSensorLogicGateContainerForm(client, new SensorLogicGateContainer(client.getClient(), uniqueSeed, (SensorLogicGateEntity)lge)), (client, uniqueSeed, lge, content, serverObject) -> new SensorLogicGateContainer(client, uniqueSeed, (SensorLogicGateEntity)lge));
        NEW_SOUND_LOGIC_GATE_CONTAINER = registerLogicGateContainer((client, uniqueSeed, lge, content) -> new NewSoundLogicGateContainerForm(client, new SoundLogicGateContainer(client.getClient(), uniqueSeed, (SoundLogicGateEntity)lge)), (client, uniqueSeed, lge, content, serverObject) -> new SoundLogicGateContainer(client, uniqueSeed, (SoundLogicGateEntity)lge));
        NEW_TFLIPFLOP_LOGIC_GATE_CONTAINER = registerLogicGateContainer((client, uniqueSeed, lge, content) -> new NewTFlipFlopLogicGateContainerForm(client, new TFlipFlopLogicGateContainer(client.getClient(), uniqueSeed, (TFlipFlopLogicGateEntity)lge)), (client, uniqueSeed, lge, content, serverObject) -> new TFlipFlopLogicGateContainer(client, uniqueSeed, (TFlipFlopLogicGateEntity)lge));
        NEW_TIMER_LOGIC_GATE_CONTAINER = registerLogicGateContainer((client, uniqueSeed, lge, content) -> new NewTimerLogicGateContainerForm(client, new TimerLogicGateContainer(client.getClient(), uniqueSeed, (TimerLogicGateEntity)lge)), (client, uniqueSeed, lge, content, serverObject) -> new TimerLogicGateContainer(client, uniqueSeed, (TimerLogicGateEntity)lge));

    }

    public void postInit() {
        Iterator objectList = ObjectRegistry.getObjects().iterator();
        while (objectList.hasNext()) {
            GameObject object = (GameObject) objectList.next();

            //Trapped Chests
            /*if (object.getStringID().contains("Trapped") && object.showsWire) {
                //String plateName = object.getStringID().substring(0, object.getStringID().length() - 13);
                Recipes.registerModRecipe(new Recipe(
                        object.getStringID(),
                        1,
                        RecipeTechRegistry.getTech("LOGICBENCH"),
                        new Ingredient[]{
                                new Ingredient(ObjectRegistry.getObject(object.getStringID().substring(0,object.getStringID().length()-7)).getStringID(), 1),
                                new Ingredient("wire", 5)
                        }
                ).setCraftingCategory("triggers"));
            }*/

            //Pressure Plates
            if (object.getStringID().contains("pressureplate")) {
                String plateName = object.getStringID().substring(0, object.getStringID().length() - 13);
                if (TileRegistry.streamTiles().anyMatch((t) -> t.idData.getStringID().equalsIgnoreCase(plateName + "floor"))) {
                    Recipes.registerModRecipe(new Recipe(
                            object.getStringID(),
                            1,
                            RecipeTechRegistry.getTech("LOGICBENCH"),
                            new Ingredient[]{
                                    new Ingredient(TileRegistry.getTile(plateName + "floor").getTileItem().getStringID(), 5),
                                    new Ingredient("wire", 1)
                            }
                    ).setCraftingCategory("pressureplate"));
                }
                if (TileRegistry.streamTiles().anyMatch((t) -> t.idData.getStringID().equalsIgnoreCase(plateName + "tile"))  && !plateName.equalsIgnoreCase("sandstone")) {
                    Recipes.registerModRecipe(new Recipe(
                            object.getStringID(),
                            1,
                            RecipeTechRegistry.getTech("LOGICBENCH"),
                            new Ingredient[]{
                                    new Ingredient(TileRegistry.getTile(plateName + "tile").getTileItem().getStringID(), 5),
                                    new Ingredient("wire", 1)
                            }
                    ).setCraftingCategory("pressureplate"));
                }
                if (TileRegistry.streamTiles().anyMatch((t) -> t.idData.getStringID().equalsIgnoreCase(plateName))) {
                    Recipes.registerModRecipe(new Recipe(
                            object.getStringID(),
                            1,
                            RecipeTechRegistry.getTech("LOGICBENCH"),
                            new Ingredient[]{
                                    new Ingredient(TileRegistry.getTile(plateName).getTileItem().getStringID(), 5),
                                    new Ingredient("wire", 1)
                            }
                    ).setCraftingCategory("pressureplate"));
                }
            }

            //Traps
            if (object instanceof engineer.objects.vanilla.WallArrowTrapObject) {
                Recipes.registerModRecipe(new Recipe(
                        object.getStringID() + "",
                        1,
                        RecipeTechRegistry.getTech("LOGICBENCH2"),
                        new Ingredient[]{
                                new Ingredient(((engineer.objects.vanilla.WallArrowTrapObject) object).wallObject.getStringID() + "", 1),
                                new Ingredient("stonearrow", 25),
                                new Ingredient("wire", 1)
                        }
                ).setCraftingCategory("arrowtrap"));
            }
            if (object instanceof WallFlameTrapObject) {
                Recipes.registerModRecipe(new Recipe(
                        object.getStringID() + "",
                        1,
                        RecipeTechRegistry.getTech("LOGICBENCH2"),
                        new Ingredient[]{
                                new Ingredient(((WallFlameTrapObject) object).wallObject.getStringID() + "", 1),
                                new Ingredient("firemone", 5),
                                new Ingredient("wire", 1)
                        }
                ).setCraftingCategory("flametrap"));
            }
            if (object instanceof WallVoidTrapObject) {
                Recipes.registerModRecipe(new Recipe(
                        object.getStringID() + "",
                        1,
                        RecipeTechRegistry.getTech("LOGICBENCH3"),
                        new Ingredient[]{
                                new Ingredient(((WallVoidTrapObject) object).wallObject.getStringID() + "", 1),
                                new Ingredient("voidshard", 10),
                                new Ingredient("wire", 1)
                        }
                ).setCraftingCategory("voidtrap"));
            }
            if (object instanceof WallCannonBallTrapObject) {
                Recipes.registerModRecipe(new Recipe(
                        object.getStringID() + "",
                        1,
                        RecipeTechRegistry.getTech("LOGICBENCH4"),
                        new Ingredient[]{
                                new Ingredient(((WallCannonBallTrapObject) object).wallObject.getStringID() + "", 1),
                                new Ingredient("cannonball", 5),
                                new Ingredient("wire", 1)
                        }
                ).setCraftingCategory("cannonballtrap"));
            }
        }

        //Vanilla
        Recipes.registerModRecipe(new Recipe(
                "wrench",
                1,
                RecipeTechRegistry.getTech("LOGICBENCH"),
                new Ingredient[]{
                        new Ingredient("ironbar", 10)
                }
        ).setCraftingCategory("tools"));
        Recipes.registerModRecipe(new Recipe(
                "cutter",
                1,
                RecipeTechRegistry.getTech("LOGICBENCH"),
                new Ingredient[]{
                        new Ingredient("ironbar", 10)
                }
        ).setCraftingCategory("tools"));
        Recipes.registerModRecipe(new Recipe(
                "wire",
                10,
                RecipeTechRegistry.getTech("LOGICBENCH"),
                new Ingredient[]{
                        new Ingredient("copperbar", 1)
                }
        ).setCraftingCategory("tools"));
        Recipes.registerModRecipe(new Recipe(
                "rocklever",
                1,
                RecipeTechRegistry.getTech("LOGICBENCH"),
                new Ingredient[]{
                        new Ingredient("anystone", 10),
                        new Ingredient("wire", 5)
                }
        ).setCraftingCategory("triggers"));
        Recipes.registerModRecipe(new Recipe(
                "ledpanel",
                1,
                RecipeTechRegistry.getTech("LOGICBENCH"),
                new Ingredient[]{
                        new Ingredient("torch", 4),
                        new Ingredient("wire", 5),
                        new Ingredient("ironbar", 2)
                }
        ).setCraftingCategory("deco"));
        Recipes.registerModRecipe(new Recipe(
                "fireworkdispenser",
                1,
                RecipeTechRegistry.getTech("LOGICBENCH"),
                new Ingredient[]{
                        new Ingredient("ironbar", 5),
                        new Ingredient("wire", 10)
                }
        ).setCraftingCategory("deco"));
        Recipes.registerModRecipe(new Recipe(
                "tnt",
                1,
                RecipeTechRegistry.getTech("LOGICBENCH2"),
                new Ingredient[]{
                        new Ingredient("dynamitestick", 4),
                        new Ingredient("wire", 10)
                }
        ).setCraftingCategory("deco"));
        Recipes.registerModRecipe(new Recipe(
                "slimerocktile",
                5,
                RecipeTechRegistry.LANDSCAPING,
                new Ingredient[]{
                        new Ingredient("slimeum", 1)
                }
        ));
        Recipes.registerModRecipe(new Recipe(
                "LogicBench",
                1,
                RecipeTechRegistry.DEMONIC_WORKSTATION,
                new Ingredient[]{
                        new Ingredient("copperbar", 10),
                        new Ingredient("ironbar", 10),
                        new Ingredient("anylog", 20)
                }
        ));
        Recipes.registerModRecipe(new Recipe(
                "LogicBench",
                1,
                RecipeTechRegistry.getTech("LOGICBENCH"),
                new Ingredient[]{
                        new Ingredient("copperbar", 10),
                        new Ingredient("ironbar", 10),
                        new Ingredient("anylog", 20)
                }
        ).setCraftingCategory("other"));
        Recipes.registerModRecipe(new Recipe(
                "LogicBenchTier2",
                1,
                RecipeTechRegistry.getTech("LOGICBENCH2"),
                new Ingredient[]{
                        new Ingredient("quartz", 20),
                        new Ingredient("ironbar", 10),
                        new Ingredient("wire", 20)
                }
        ).setCraftingCategory("other"));
        Recipes.registerModRecipe(new Recipe(
                "LogicBenchTier3",
                1,
                RecipeTechRegistry.getTech("LOGICBENCH3"),
                new Ingredient[]{
                        new Ingredient("voidshard", 10),
                        new Ingredient("ironbar", 10),
                        new Ingredient("wire", 20)
                }
        ).setCraftingCategory("other"));
        Recipes.registerModRecipe(new Recipe(
                "LogicBenchTier4",
                1,
                RecipeTechRegistry.getTech("LOGICBENCH4"),
                new Ingredient[]{
                        new Ingredient("tungstenbar", 10),
                        new Ingredient("primordialessence", 10),
                        new Ingredient("wire", 20)
                }
        ).setCraftingCategory("other"));

        //logic gates
        Recipes.registerModRecipe(new Recipe(
                "andgate",
                1,
                RecipeTechRegistry.getTech("LOGICBENCH"),
                new Ingredient[]{
                        new Ingredient("copperbar", 1),
                        new Ingredient("wire", 5)
                }
        ).setCraftingCategory("logicgates"));
        Recipes.registerModRecipe(new Recipe(
                "nandgate",
                1,
                RecipeTechRegistry.getTech("LOGICBENCH"),
                new Ingredient[]{
                        new Ingredient("copperbar", 1),
                        new Ingredient("wire", 5)
                }
        ).setCraftingCategory("logicgates"));
        Recipes.registerModRecipe(new Recipe(
                "orgate",
                1,
                RecipeTechRegistry.getTech("LOGICBENCH"),
                new Ingredient[]{
                        new Ingredient("copperbar", 1),
                        new Ingredient("wire", 5)
                }
        ).setCraftingCategory("logicgates"));
        Recipes.registerModRecipe(new Recipe(
                "norgate",
                1,
                RecipeTechRegistry.getTech("LOGICBENCH"),
                new Ingredient[]{
                        new Ingredient("copperbar", 1),
                        new Ingredient("wire", 5)
                }
        ).setCraftingCategory("logicgates"));
        Recipes.registerModRecipe(new Recipe(
                "xorgate",
                1,
                RecipeTechRegistry.getTech("LOGICBENCH"),
                new Ingredient[]{
                        new Ingredient("copperbar", 1),
                        new Ingredient("wire", 5)
                }
        ).setCraftingCategory("logicgates"));
        Recipes.registerModRecipe(new Recipe(
                "srlatchgate",
                1,
                RecipeTechRegistry.getTech("LOGICBENCH"),
                new Ingredient[]{
                        new Ingredient("copperbar", 1),
                        new Ingredient("wire", 5)
                }
        ).setCraftingCategory("logicgates"));
        Recipes.registerModRecipe(new Recipe(
                "tflipflopgate",
                1,
                RecipeTechRegistry.getTech("LOGICBENCH"),
                new Ingredient[]{
                        new Ingredient("copperbar", 1),
                        new Ingredient("wire", 5)
                }
        ).setCraftingCategory("logicgates"));

        //calculators
        Recipes.registerModRecipe(new Recipe(
                "countergate",
                1,
                RecipeTechRegistry.getTech("LOGICBENCH"),
                new Ingredient[]{
                        new Ingredient("copperbar", 1),
                        new Ingredient("wire", 5)
                }
        ).setCraftingCategory("logiccalculators"));
        Recipes.registerModRecipe(new Recipe(
                "timergate",
                1,
                RecipeTechRegistry.getTech("LOGICBENCH"),
                new Ingredient[]{
                        new Ingredient("copperbar", 1),
                        new Ingredient("wire", 5)
                }
        ).setCraftingCategory("logiccalculators"));
        Recipes.registerModRecipe(new Recipe(
                "delaygate",
                1,
                RecipeTechRegistry.getTech("LOGICBENCH"),
                new Ingredient[]{
                        new Ingredient("copperbar", 1),
                        new Ingredient("wire", 5)
                }
        ).setCraftingCategory("logiccalculators"));
        Recipes.registerModRecipe(new Recipe(
                "buffergate",
                1,
                RecipeTechRegistry.getTech("LOGICBENCH"),
                new Ingredient[]{
                        new Ingredient("copperbar", 1),
                        new Ingredient("wire", 5)
                }
        ).setCraftingCategory("logiccalculators"));

        //sensors
        Recipes.registerModRecipe(new Recipe(
                "sensorgate",
                1,
                RecipeTechRegistry.getTech("LOGICBENCH"),
                new Ingredient[]{
                        new Ingredient("copperbar", 1),
                        new Ingredient("wire", 5)
                }
        ).setCraftingCategory("logicsensors"));
        Recipes.registerModRecipe(new Recipe(
                "timeGate",
                1,
                RecipeTechRegistry.getTech("LOGICBENCH"),
                new Ingredient[]{
                        new Ingredient("copperbar", 1),
                        new Ingredient("wire", 5)
                }
        ).setCraftingCategory("logicsensors"));
        Recipes.registerModRecipe(new Recipe(
                "storageGate",
                1,
                RecipeTechRegistry.getTech("LOGICBENCH"),
                new Ingredient[]{
                        new Ingredient("copperbar", 1),
                        new Ingredient("wire", 5)
                }
        ).setCraftingCategory("logicsensors"));
        Recipes.registerModRecipe(new Recipe(
                "lightGate",
                1,
                RecipeTechRegistry.getTech("LOGICBENCH"),
                new Ingredient[]{
                        new Ingredient("copperbar", 1),
                        new Ingredient("wire", 5)
                }
        ).setCraftingCategory("logicsensors"));
        Recipes.registerModRecipe(new Recipe(
                "weatherGate",
                1,
                RecipeTechRegistry.getTech("LOGICBENCH"),
                new Ingredient[]{
                        new Ingredient("copperbar", 1),
                        new Ingredient("wire", 5)
                }
        ).setCraftingCategory("logicsensors"));
        Recipes.registerModRecipe(new Recipe(
                "projectileGate",
                1,
                RecipeTechRegistry.getTech("LOGICBENCH"),
                new Ingredient[]{
                        new Ingredient("copperbar", 1),
                        new Ingredient("wire", 5)
                }
        ).setCraftingCategory("logicsensors"));

        //activators
        Recipes.registerModRecipe(new Recipe(
                "soundgate",
                1,
                RecipeTechRegistry.getTech("LOGICBENCH"),
                new Ingredient[]{
                        new Ingredient("copperbar", 1),
                        new Ingredient("wire", 5)
                }
        ).setCraftingCategory("logicactivators"));
        Recipes.registerModRecipe(new Recipe(
                "fakewallGate",
                1,
                RecipeTechRegistry.getTech("LOGICBENCH"),
                new Ingredient[]{
                        new Ingredient("copperbar", 1),
                        new Ingredient("wire", 5)
                }
        ).setCraftingCategory("logicactivators"));

    }

    public ModSettings initSettings() {
        settings = new Settings();
        return settings;
    }
}
