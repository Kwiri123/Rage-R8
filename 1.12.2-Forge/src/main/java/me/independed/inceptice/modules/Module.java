
/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraftforge.client.event.sound.PlaySoundEvent
 *  net.minecraftforge.common.MinecraftForge
 */
package me.independed.inceptice.modules;

import me.independed.inceptice.Main;
import me.independed.inceptice.settings.ModeSetting;
import me.independed.inceptice.settings.Setting;
import me.independed.inceptice.ui.Hud;
import me.zero.alpine.listener.Listenable;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Module {
    protected static final Minecraft mc = Minecraft.getMinecraft();
    public boolean toggled;
    public String name;
    public int index = 0;
    public Category category;
    public boolean expanded;
    public int key;
    public List settings = new ArrayList();
    public String description;
    public int moduleIndex;
    public String categoryName;
    private boolean state;
    private boolean visible;

    public Module(String string, String string2, int n, Module.Category category) {
        this.name = string;
        this.description = string2;
        this.key = n;
        this.category = category;
        this.toggled = false;
    }

    public static ArrayList getSortedCategories() {
        ArrayList<Module.Category> arrayList = new ArrayList<Module.Category>();
        for (Module.Category category3 : Module.Category.values()) {
            arrayList.add(category3);
        }
        arrayList.sort((Module.Category category, Module.Category category2) -> {
            String string = category.name();
            String string2 = category2.name();
            int n = Hud.myRenderer.getStringWidth(string2) - Hud.myRenderer.getStringWidth(string);
            return n != 0 ? n : string2.compareTo(string);
        });
        return arrayList;
    }

    public static int placeInListPlayer(Module module) {
        int n = 2;
        for (Object modulew : ModuleManager.getModuleList()) {
            Module module2 = (Module) modulew;
            if (module2.getCategory().equals(Category.PLAYER) && !module2.equals(module)) {
                ++n;
                continue;
            }
            if (!module2.getCategory().equals(Category.PLAYER) || !module2.equals(module)) continue;
            return n;
        }
        return 2;
    }

    public static int size(Module.Category category) {
        int n = 0;
        for (Object modulew : ModuleManager.getModuleList()) {
            Module module = (Module) modulew;
            if (module.getCategory() != category) continue;
            ++n;
        }
        return n;
    }

    public static int placeInListMovement(Module module) {
        int n = 2;
        for (Object modulew : ModuleManager.getModuleList()) {
            Module module2 = (Module) modulew;
            if (module2.getCategory().equals(Category.MOVEMENT) && !module2.equals(module)) {
                ++n;
                continue;
            }
            if (!module2.getCategory().equals(Category.MOVEMENT) || !module2.equals(module)) continue;
            return n;
        }
        return 2;
    }

    public static int placeInListCombat(Module module) {
        int n = 2;
        for (Object modulew : ModuleManager.getModuleList()) {
            Module module2 = (Module) modulew;
            if (module2.getCategory().equals(Category.COMBAT) && !module2.equals(module)) {
                ++n;
                continue;
            }
            if (!module2.getCategory().equals(Category.COMBAT) || !module2.equals(module)) continue;
            return n;
        }
        return 2;
    }

    public static int placeInListRender(Module module) {
        int n = 2;
        for (Object modulew : ModuleManager.getModuleList()) {
            Module module2 = (Module) modulew;
            if (module2.getCategory().equals(Category.RENDER) && !module2.equals(module)) {
                ++n;
                continue;
            }
            if (!module2.getCategory().equals(Category.RENDER) || !module2.equals(module)) continue;
            return n;
        }
        return 2;
    }

    public void onPlaySound(PlaySoundEvent playSoundEvent) {
    }

    public void onRenderWorldLast(float f) {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String string) {
        this.name = string;
    }

    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object) this);
        Main.EVENT_BUS.unsubscribe((Listenable) (Object) this);
    }

    public void onUpdate() {
    }

    public boolean isToggled() {
        return this.toggled;
    }

    public void setToggled(boolean bl) {
        this.toggled = bl;
        if (bl) {
            this.onEnable();
        } else {
            this.onDisable();
        }
    }

    public void onLocalPlayerUpdate() {
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean bl) {
        this.visible = bl;
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int n) {
        this.key = n;
    }

    public boolean getState() {
        return this.state;
    }

    public void setState(boolean bl) {
        if (bl) {
            this.state = true;
            this.onEnable();
        } else {
            this.state = false;
            this.onDisable();
        }
    }

    public void onClientTick() {
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String string) {
        this.description = string;
    }

    public String getActiveModeSetting() {
        for (Object setting : this.settings) {
            if (!(setting instanceof ModeSetting)) continue;
            ModeSetting modeSetting = (ModeSetting) setting;
            return modeSetting.activeMode;
        }
        return null;
    }

    public void onPlayerTick() {
    }

    public void addSettings(Setting... arrsetting) {
        this.settings.addAll(Arrays.asList(arrsetting));
    }

    public Setting getSettingByName(String name) {
        for (Object setw : this.settings) {
            Setting set = (Setting) setw;
            if (set.name.equalsIgnoreCase(name)) {
                return set;
            }
        }
        System.err.println("[" + "Inceptice" + "] Error Setting NOT found: '" + name + "'!");
        return null;
    }

    public boolean isEnabled() {
        return this.toggled;
    }

    public void onEnable() {
        MinecraftForge.EVENT_BUS.register((Object) this);
        Main.EVENT_BUS.subscribe((Listenable) (Object) this);
    }

    public void setup() {
    }

    public void toggle() {
        boolean bl = this.toggled = !this.toggled;
        if (this.toggled) {
            this.onEnable();
        } else {
            this.onDisable();
        }
    }

    public Module.Category getCategory() {
        return this.category;
    }

    public enum Category {
        MOVEMENT("MOVEMENT"),
        COMBAT("COMBAT"),
        RENDER("RENDER"),
        PLAYER("PLAYER"),
        WORLD("MISC"),
        GHOST("GHOST"),
        HUD("HUD");

        Category(String name) {
        }
    }
}

