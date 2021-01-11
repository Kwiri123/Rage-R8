
/*
 * Decompiled with CFR 0.150.
 */
package me.independed.inceptice.modules.visual;

import me.independed.inceptice.modules.Module;
import me.independed.inceptice.modules.Module.Category;

public class FullBright
extends Module {
    public float oldGamma;

    public FullBright() {
        super("FullBright", "all bright", 50, Module.Category.RENDER);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.oldGamma = FullBright.mc.gameSettings.gammaSetting;
        FullBright.mc.gameSettings.gammaSetting = 10.0f;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        FullBright.mc.gameSettings.gammaSetting = this.oldGamma;
    }
}

