
/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraftforge.common.MinecraftForge
 */
package me.independed.inceptice.event;

import me.independed.inceptice.Main;
import me.zero.alpine.listener.Listenable;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

public class EventProcessor {
    public static EventProcessor INSTANCE;
    Minecraft mc = Minecraft.getMinecraft();

    public EventProcessor() {
        INSTANCE = this;
    }

    public void init() {
        MinecraftForge.EVENT_BUS.register((Object) this);
    }
}

