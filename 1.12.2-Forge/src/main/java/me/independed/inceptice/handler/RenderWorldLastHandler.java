/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 */
package me.independed.inceptice.handler;

import me.independed.inceptice.modules.Module;
import me.independed.inceptice.modules.ModuleManager;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RenderWorldLastHandler {

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent renderWorldLastEvent) {
        for (Object modulew : ModuleManager.getModuleList()) {
            Module module = (Module) modulew;
            if (!module.isToggled()) continue;
            module.onRenderWorldLast(renderWorldLastEvent.getPartialTicks());
        }
    }
}

