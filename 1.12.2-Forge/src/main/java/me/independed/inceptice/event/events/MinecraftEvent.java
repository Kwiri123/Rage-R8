
/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 */
package me.independed.inceptice.event.events;


import me.zero.alpine.event.type.Cancellable;
import net.minecraft.client.Minecraft;

public class MinecraftEvent
        extends Cancellable {
    Minecraft mc = Minecraft.getMinecraft();
    private final float partialTicks = this.mc.getRenderPartialTicks();
    private MinecraftEvent$Era era = MinecraftEvent$Era.PRE;

    public MinecraftEvent() {
    }

    public MinecraftEvent(MinecraftEvent$Era minecraftEvent$Era) {
        this.era = minecraftEvent$Era;
    }

    public float getPartialTicks() {
        return this.partialTicks;
    }

    public MinecraftEvent$Era getEra() {
        return this.era;
    }
}

