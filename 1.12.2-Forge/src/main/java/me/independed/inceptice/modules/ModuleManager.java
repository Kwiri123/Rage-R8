
/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.InputEvent$KeyInputEvent
 *  org.lwjgl.input.Keyboard
 */
package me.independed.inceptice.modules;

import me.independed.inceptice.modules.combat.*;
import me.independed.inceptice.modules.ghost.Panic;
import me.independed.inceptice.modules.hud.*;
import me.independed.inceptice.modules.misc.*;
import me.independed.inceptice.modules.movement.*;
import me.independed.inceptice.modules.player.*;
import me.independed.inceptice.modules.visual.*;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ModuleManager {
    public static ArrayList modules;

    public ModuleManager() throws IOException {

        modules = new ArrayList();

        modules.add(new Sprint());

        modules.add(new KillAura());

        modules.add(new WallHack());

        modules.add(new FullBright());

        modules.add(new HitBox());

        modules.add(new NoFall());

        modules.add(new AimAssist());

        modules.add(new Fly());

        modules.add(new Criticals());

        modules.add(new Esp());

        modules.add(new ClickGui());

        modules.add(new NoClip());

        modules.add(new TriggerBot());

        modules.add(new Tracers());

        modules.add(new Trajectories());

        modules.add(new BowSpam());

        modules.add(new Velocity());

        modules.add(new InventoryMove());

        modules.add(new NoSlowdown());

        modules.add(new LongJump());

        modules.add(new Speed());

        modules.add(new HighJump());

        modules.add(new AutoParkour());

        modules.add(new wTap());

        modules.add(new AutoTotem());

        modules.add(new Panic());

        modules.add(new FakePlayer());

        modules.add(new Derp());

        modules.add(new Jesus());

        modules.add(new FakeCreative());

        modules.add(new ChestESP());

        modules.add(new AirWalk());

        modules.add(new HUD());

        modules.add(new Strafe());

        modules.add(new KeyStrokes());

        modules.add(new Teleport());

        modules.add(new InventoryView());

        modules.add(new ArmorView());

        modules.add(new FuckYou());

        modules.add(new Timer());

        modules.add(new AutoWalk());

        modules.add(new TargetStrafe());

        modules.add(new AutoGApple());

        modules.add(new PVPBot());

        modules.add(new NoPush());

        modules.add(new AutoPotion());

        modules.add(new FakeHacker());

        modules.add(new MCF());

        modules.add(new TargetHUD());

        modules.add(new ItemESP());

        modules.add(new Particles());

        modules.add(new WaterSpeed());

        modules.add(new Nuker());

        modules.add(new NoHurtCam());

        modules.add(new GodMode());

        modules.add(new Spammer());

        modules.add(new Spider());

        modules.add(new NoWeb());

        modules.add(new AirStack());

        modules.add(new FastUse());

    }

    public static List getModulesByCategory(Module.Category category) throws IOException {
        ArrayList<Module> arrayList = new ArrayList<Module>();
        for (Object modulew : ModuleManager.getAllSortedModules()) {
            Module module = (Module) modulew;
            if (module.category != category) continue;
            arrayList.add(module);
        }
        return arrayList;
    }

    public static Module getModuleByName(String string) {
        for (Object modulew : modules) {
            Module module = (Module) modulew;
            if (module.name != string) continue;
            return module;
        }
        return null;
    }

    public static List getAllSortedModules() throws IOException {
        ArrayList arrayList = new ArrayList(ModuleManager.getModuleList());
        arrayList.sort((Comparator) new ModuleManager());
        return arrayList;
    }

    public static List getSortedModules() throws IOException {
        ArrayList<Module> arrayList = new ArrayList<Module>();
        for (Object modulew : ModuleManager.getModuleList()) {
            Module module = (Module) modulew;
            if (!module.isToggled()) continue;
            arrayList.add(module);
        }
        arrayList.sort((Comparator<? super Module>) new ModuleManager());
        return arrayList;
    }

    public static ArrayList getModuleList() {
        return modules;
    }

    @SubscribeEvent
    public void key(InputEvent.KeyInputEvent keyInputEvent) {
        if (Minecraft.getMinecraft().world == null || Minecraft.getMinecraft().player == null) {
            return;
        }
        if (Keyboard.isCreated() && Keyboard.getEventKeyState()) {
            int n = Keyboard.getEventKey();
            if (n <= 0) {
                return;
            }
            for (Object modulew : modules) {
                Module module = (Module) modulew;
                if (module.getKey() != n || n <= 0) continue;
                module.toggle();
            }
        }
    }
}

