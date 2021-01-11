
/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$PlayerTickEvent
 */
package me.independed.inceptice.modules.combat;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.settings.NumberSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AimAssist
extends Module {
    public NumberSetting range = new NumberSetting("Range", 4.0, 0.5, 10.0, 0.1);

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent playerTickEvent) {
        List list = AimAssist.mc.world.loadedEntityList.stream().filter(entity -> entity != AimAssist.mc.player).filter(this::getDickLength).filter(entity -> !entity.isDead).filter(entity -> entity instanceof EntityPlayer).filter(entity -> !entity.isInvisible()).filter(entity -> ((EntityPlayer)entity).getHealth() > 0.0f).sorted(Comparator.comparing(entity -> Float.valueOf(AimAssist.mc.player.getDistance(entity)))).collect(Collectors.toList());
        int n = list.size();
        if (n > 0) {
            float[] arrf = this.getRotations((EntityPlayer)list.get(0));
            float f = arrf[0];
            float f2 = arrf[1];
            if (AimAssist.mc.player.rotationYaw < f) {
                AimAssist.mc.player.rotationYaw += 0.5f;
            }
            if (AimAssist.mc.player.rotationYaw > f) {
                AimAssist.mc.player.rotationYaw -= 0.5f;
            }
            if (AimAssist.mc.player.rotationPitch < f2) {
                AimAssist.mc.player.rotationPitch += 0.5f;
            }
            if (AimAssist.mc.player.rotationPitch > f2) {
                AimAssist.mc.player.rotationPitch -= 0.5f;
            }
        }
    }

    public AimAssist() {
        super("AimAssist", "aims at entity", 0, Module.Category.COMBAT);
    }

    private boolean getDickLength(Entity entity) {
        return (double) AimAssist.mc.player.getDistance(entity) <= this.range.getValue();
    }

    public float[] getRotations(EntityPlayer entityPlayer) {
        double d = entityPlayer.posX + (entityPlayer.posX - entityPlayer.lastTickPosX) - AimAssist.mc.player.posX;
        double d2 = entityPlayer.posY + (double)entityPlayer.getEyeHeight() - AimAssist.mc.player.posY + (double) AimAssist.mc.player.getEyeHeight() - 3.5;
        double d3 = entityPlayer.posZ + (entityPlayer.posZ - entityPlayer.lastTickPosZ) - AimAssist.mc.player.posZ;
        double d4 = Math.sqrt(Math.pow(d, 2.0) + Math.pow(d3, 2.0));
        float f = (float)Math.toDegrees(-Math.atan(d / d3));
        float f2 = (float)(-Math.toDegrees(Math.atan(d2 / d4)));
        if (d < 0.0 && d3 < 0.0) {
            f = (float)(90.0 + Math.toDegrees(Math.atan(d3 / d)));
        } else if (d > 0.0 && d3 < 0.0) {
            f = (float)(-90.0 + Math.toDegrees(Math.atan(d3 / d)));
        }
        return new float[]{f, f2};
    }
}

