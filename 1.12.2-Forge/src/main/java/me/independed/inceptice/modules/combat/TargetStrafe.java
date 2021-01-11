
/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.item.EntityArmorStand
 *  net.minecraft.entity.monster.EntityMob
 *  net.minecraft.entity.passive.EntityAnimal
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.potion.Potion
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$PlayerTickEvent
 */
package me.independed.inceptice.modules.combat;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import me.independed.inceptice.friends.Friend;
import me.independed.inceptice.friends.FriendManager;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.modules.Module.Category;
import me.independed.inceptice.settings.BooleanSetting;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.util.RotationUtils;
import me.independed.inceptice.util.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TargetStrafe
extends Module {
    public TimerUtil timerUtil;
    public BooleanSetting playerSet;
    public NumberSetting speedRot;
    public BooleanSetting animalSett;
    public BooleanSetting mobSet;
    public BooleanSetting invisibleTarget;
    public NumberSetting distance = new NumberSetting("Radius", 2.0, 0.0, 5.0, 0.1);
    public boolean sideDirection = true;
    public static int direction = -1;
    public double increment = 0.05;
    public BooleanSetting renderCircle;

    public double getMovementSpeed() {
        double d = 0.2873;
        if (Minecraft.getMinecraft().player.isPotionActive(Objects.requireNonNull(Potion.getPotionById((int)1)))) {
            int n = Objects.requireNonNull(Minecraft.getMinecraft().player.getActivePotionEffect(Objects.requireNonNull(Potion.getPotionById((int)1)))).getAmplifier();
            d *= 1.0 + 0.2 * (double)(n + 1);
        }
        return d;
    }

    public Entity getTargetEz() {
        if (TargetStrafe.mc.player == null || TargetStrafe.mc.player.isDead) {
            return null;
        }
        List list = TargetStrafe.mc.world.loadedEntityList.stream().filter(entity -> entity != TargetStrafe.mc.player).filter(entity -> TargetStrafe.mc.player.getDistance(entity) <= 7.0f).filter(entity -> !entity.isDead).filter(this::lambda$getTargetEz$3).sorted(Comparator.comparing(entity -> Float.valueOf(TargetStrafe.mc.player.getDistance(entity)))).collect(Collectors.toList());
        if (list.size() > 0) {
            return (Entity)list.get(0);
        }
        return null;
    }

    public final boolean doStrafeAtSpeed(double d) {
        boolean bl = true;
        Entity entity = this.getTargetEz();
        if (entity != null) {
            if (TargetStrafe.mc.player.onGround) {
                TargetStrafe.mc.player.jump();
            }
            float[] arrf = RotationUtils.getNeededRotations((EntityLivingBase)entity);
            if ((double)Minecraft.getMinecraft().player.getDistance(entity) <= this.distance.getValue()) {
                TargetStrafe.mc.player.renderYawOffset = arrf[0];
                TargetStrafe.mc.player.rotationYawHead = arrf[0];
                TargetStrafe.setSpeed(d - (0.1 - this.speedRot.getValue() / 100.0), arrf[0], direction, 0.0);
            } else {
                TargetStrafe.setSpeed(d - (0.1 - this.speedRot.getValue() / 100.0), arrf[0], direction, 1.0);
                TargetStrafe.mc.player.renderYawOffset = arrf[0];
                TargetStrafe.mc.player.rotationYawHead = arrf[0];
            }
        }
        return bl;
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent playerTickEvent) {
        if (TargetStrafe.mc.player.collidedHorizontally && this.timerUtil.hasReached(80.0)) {
            this.timerUtil.reset();
            this.invertStrafe();
        }
        TargetStrafe.mc.player.movementInput.moveForward = 0.0f;
        double d = this.getMovementSpeed();
        this.doStrafeAtSpeed(d);
    }

    public TargetStrafe() {
        super("TargetStrafe", "targets closest player. note: use with kill_aura", 0, Module.Category.COMBAT);
        this.speedRot = new NumberSetting("Speed", 5.0, 1.0, 10.0, 1.0);
        this.invisibleTarget = new BooleanSetting("Invisible", false);
        this.playerSet = new BooleanSetting("Players", true);
        this.mobSet = new BooleanSetting("Mobs", false);
        this.animalSett = new BooleanSetting("Animals", false);
        this.renderCircle = new BooleanSetting("Render", true);
        this.timerUtil = new TimerUtil();
        this.settings.add(this.invisibleTarget);
        this.settings.add(this.distance);
        this.settings.add(this.speedRot);
        this.settings.add(this.playerSet);
        this.settings.add(this.mobSet);
        this.settings.add(this.animalSett);
    }

    public static void setSpeed(double d, float f, double d2, double d3) {
        double d4 = d3;
        double d5 = d2;
        float f2 = f;
        if (d4 == 0.0 && d5 == 0.0) {
            TargetStrafe.mc.player.motionZ = 0.0;
            TargetStrafe.mc.player.motionX = 0.0;
        } else {
            if (d4 != 0.0) {
                if (d5 > 0.0) {
                    f2 += (float)(d4 > 0.0 ? -45 : 45);
                } else if (d5 < 0.0) {
                    f2 += (float)(d4 > 0.0 ? 45 : -45);
                }
                d5 = 0.0;
                if (d4 > 0.0) {
                    d4 = 1.0;
                } else if (d4 < 0.0) {
                    d4 = -1.0;
                }
            }
            double d6 = Math.cos(Math.toRadians(f2 + 90.0f));
            double d7 = Math.sin(Math.toRadians(f2 + 90.0f));
            TargetStrafe.mc.player.motionX = d4 * d * d6 + d5 * d * d7;
            TargetStrafe.mc.player.motionZ = d4 * d * d7 - d5 * d * d6;
        }
    }

    private void drawRadius(Entity entity, double d, double d2, double d3, double d4) {
        GlStateManager.enableDepth();
        double d5 = 0.0;
        Double.compare(d5, 0.01);
    }

    private boolean lambda$getTargetEz$3(Entity entity) {
        return this.attackCheck(entity);
    }

    @Override
    public void onRenderWorldLast(float f) {
        if (TargetStrafe.mc.player == null || TargetStrafe.mc.player.isDead) {
            return;
        }
        List list = TargetStrafe.mc.world.loadedEntityList.stream().filter(entity -> entity != TargetStrafe.mc.player).filter(entity -> TargetStrafe.mc.player.getDistance(entity) <= 15.0f).filter(entity -> !entity.isDead).filter(this::attackCheckin).filter(entity -> !(entity instanceof EntityArmorStand)).sorted(Comparator.comparing(entity -> Float.valueOf(TargetStrafe.mc.player.getDistance(entity)))).collect(Collectors.toList());
        if (list.size() > 0) {
            double d = this.distance.getValue();
            Entity entity2 = (Entity)list.get(0);
            double d2 = entity2.lastTickPosX + (entity2.posX - entity2.lastTickPosX) * (double)f - TargetStrafe.mc.getRenderManager().viewerPosX;
            double d3 = entity2.lastTickPosY + (entity2.posY - entity2.lastTickPosY) * (double)f - TargetStrafe.mc.getRenderManager().viewerPosY;
            double d4 = entity2.lastTickPosZ + (entity2.posZ - entity2.lastTickPosZ) * (double)f - TargetStrafe.mc.getRenderManager().viewerPosZ;
            if (this.increment < 2.03 && this.sideDirection) {
                if (this.increment >= 2.0) {
                    this.sideDirection = false;
                    this.increment = 2.0;
                    d3 -= this.increment;
                }
                this.increment += 0.02;
                this.drawRadius((Entity)list.get(0), d2, d3 += this.increment, d4, d);
            }
            if (this.increment > 0.01 && !this.sideDirection) {
                if (this.increment <= 0.02) {
                    this.sideDirection = true;
                    this.increment = 0.01;
                }
                this.increment -= 0.02;
                this.drawRadius((Entity)list.get(0), d2, d3 += this.increment, d4, d);
            }
        }
    }

    public boolean attackCheck(Entity entity) {
        for (Object friendw : FriendManager.friends) {
            Friend friend = (Friend) friendw;
            if (entity.getName() != friend.name) continue;
            return false;
        }
        if (this.invisibleTarget.isEnabled() && entity.isInvisible() && (this.playerSet.isEnabled() && entity instanceof EntityPlayer || this.mobSet.isEnabled() && entity instanceof EntityMob || this.animalSett.isEnabled() && entity instanceof EntityAnimal)) {
            return true;
        }
        if (this.playerSet.isEnabled() && entity instanceof EntityPlayer && !entity.isInvisible()) {
            return true;
        }
        if (this.animalSett.isEnabled() && entity instanceof EntityAnimal && !entity.isInvisible()) {
            return true;
        }
        return this.mobSet.isEnabled() && entity instanceof EntityMob && !entity.isInvisible();
    }

    private void invertStrafe() {
        direction = -direction;
    }

    public boolean attackCheckin(Entity entity) {
        return entity instanceof EntityPlayer && ((EntityPlayer)entity).getHealth() > 0.0f && Math.abs(TargetStrafe.mc.player.rotationYaw - RotationUtils.getNeededRotations((EntityLivingBase)entity)[0]) % 180.0f < 190.0f && !entity.isInvisible() && !entity.getUniqueID().equals(TargetStrafe.mc.player.getUniqueID());
    }
}

