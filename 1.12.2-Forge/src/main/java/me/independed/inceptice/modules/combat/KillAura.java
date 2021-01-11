
/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.monster.EntityMob
 *  net.minecraft.entity.passive.EntityAnimal
 *  net.minecraft.entity.passive.EntityTameable
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Items
 *  net.minecraft.item.EnumAction
 *  net.minecraft.item.ItemAppleGold
 *  net.minecraft.item.ItemAxe
 *  net.minecraft.item.ItemShield
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.CPacketPlayer$Rotation
 *  net.minecraft.network.play.client.CPacketPlayerTryUseItem
 *  net.minecraft.network.play.client.CPacketUseEntity
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.fml.common.eventhandler.SubscribeEvent
 *  net.minecraftforge.fml.common.gameevent.TickEvent$PlayerTickEvent
 */
package me.independed.inceptice.modules.combat;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import me.independed.inceptice.friends.Friend;
import me.independed.inceptice.friends.FriendManager;
import me.independed.inceptice.modules.Module;
import me.independed.inceptice.modules.ModuleManager;
import me.independed.inceptice.settings.BooleanSetting;
import me.independed.inceptice.settings.ModeSetting;
import me.independed.inceptice.settings.NumberSetting;
import me.independed.inceptice.util.TimerUtil;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class KillAura
extends Module {
    public BooleanSetting pitchTarget;
    public boolean swichable = true;
    public BooleanSetting noSwingSet;
    public ModeSetting modeSetting;
    TimerUtil hit;
    public BooleanSetting yawTarget;
    public BooleanSetting invisibleTarget;
    public BooleanSetting animalsSet;
    private Random random = new Random();
    public ModeSetting modePrior;
    public NumberSetting fovSet;
    public NumberSetting rangeSet;
    public BooleanSetting mobsSet;
    public BooleanSetting playersSet;

    public boolean swichfucked() {
        this.swichable = !this.swichable;
        return this.swichable;
    }

    public static Vec3d getRandomCenter(AxisAlignedBB axisAlignedBB) {
        return new Vec3d(axisAlignedBB.minX + (axisAlignedBB.maxX - axisAlignedBB.minX) * 0.8 * Math.random(), axisAlignedBB.minY + (axisAlignedBB.maxY - axisAlignedBB.minY) * Math.random() + 0.1 * Math.random(), axisAlignedBB.minZ + (axisAlignedBB.maxZ - axisAlignedBB.minZ) * 0.8 * Math.random());
    }

    private boolean attackCheck(Entity entity) {
        for (Object friendw : FriendManager.friends) {
            Friend friend = (Friend) friendw;
            if (entity.getName() != friend.name) continue;
            return false;
        }
        if (!this.invisibleTarget.isEnabled() && entity.isInvisible()) {
            return false;
        }
        if (this.playersSet.isEnabled() && entity instanceof EntityPlayer && ((EntityPlayer)entity).getHealth() > 0.0f) {
            return true;
        }
        if (this.animalsSet.isEnabled() && entity instanceof EntityAnimal) {
            return !(entity instanceof EntityTameable);
        }
        return this.mobsSet.isEnabled() && entity instanceof EntityMob;
    }

    private boolean getDickLength(Entity entity) {
        return (double) KillAura.mc.player.getDistance(entity) <= this.rangeSet.getValue();
    }

    public void attackTarget(Entity entity) {
        if (entity instanceof EntityPlayer && KillAura.isPlayerShielded((EntityPlayer)entity) && KillAura.mc.player.getHeldItemMainhand().getItem() instanceof ItemAxe) {
            KillAura.mc.playerController.attackEntity((EntityPlayer) KillAura.mc.player, entity);
        }
        if (this.hit.hasReached(591.0)) {
            if (this.modeSetting.activeMode == "Bypass") {
                if (ModuleManager.getModuleByName("Criticals").isToggled() && !KillAura.mc.player.onGround && (double) KillAura.mc.player.fallDistance >= 0.15) {
                    this.hit.reset();
                    Entity entity2 = entity;
                    float[] arrf = KillAura.getRotations(entity2);
                    this.setRotation(arrf[0], arrf[1]);
                    KillAura.mc.player.setSprinting(false);
                    KillAura.mc.playerController.attackEntity((EntityPlayer) KillAura.mc.player, entity2);
                    if (!this.noSwingSet.enabled) {
                        KillAura.mc.player.swingArm(EnumHand.MAIN_HAND);
                    }
                } else if (!ModuleManager.getModuleByName("Criticals").isToggled()) {
                    this.hit.reset();
                    Entity entity3 = entity;
                    float[] arrf = KillAura.getRotations(entity3);
                    this.setRotation(arrf[0], arrf[1]);
                    KillAura.mc.playerController.attackEntity((EntityPlayer) KillAura.mc.player, entity3);
                    if (!this.noSwingSet.enabled) {
                        KillAura.mc.player.swingArm(EnumHand.MAIN_HAND);
                    }
                }
            } else if (ModuleManager.getModuleByName("Criticals").isToggled() && !KillAura.mc.player.onGround && (double) KillAura.mc.player.fallDistance >= 0.15) {
                this.hit.reset();
                Entity entity4 = entity;
                float[] arrf = new float[]{KillAura.mc.player.rotationYaw, KillAura.mc.player.rotationPitch};
                float[] arrf2 = KillAura.getRotations(entity4);
                this.setRotation(arrf2[0], arrf2[1]);
                KillAura.mc.player.setSprinting(false);
                arrf2[0] = arrf2[0] < 90.0f ? 360.0f - (90.0f - arrf2[0]) : arrf2[0] - 90.0f;
                KillAura.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(arrf2[0], KillAura.mc.player.rotationPitch, false));
                KillAura.mc.player.connection.sendPacket((Packet)new CPacketUseEntity(entity4, EnumHand.MAIN_HAND));
                KillAura.mc.playerController.attackEntity((EntityPlayer) KillAura.mc.player, entity4);
                if (!this.noSwingSet.enabled) {
                    KillAura.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                    KillAura.mc.player.swingArm(EnumHand.MAIN_HAND);
                }
                KillAura.mc.player.setSprinting(false);
                this.setRotation(arrf[0], arrf[1]);
            } else if (!ModuleManager.getModuleByName("Criticals").isToggled()) {
                this.hit.reset();
                Entity entity5 = entity;
                float[] arrf = new float[]{KillAura.mc.player.rotationYaw, KillAura.mc.player.rotationPitch};
                float[] arrf3 = KillAura.getRotations(entity5);
                this.setRotation(arrf3[0], arrf3[1]);
                KillAura.mc.player.setSprinting(false);
                arrf3[0] = arrf3[0] < 90.0f ? 360.0f - (90.0f - arrf3[0]) : arrf3[0] - 90.0f;
                KillAura.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(arrf3[0], KillAura.mc.player.rotationPitch, false));
                KillAura.mc.player.connection.sendPacket((Packet)new CPacketUseEntity(entity5, EnumHand.MAIN_HAND));
                KillAura.mc.playerController.attackEntity((EntityPlayer) KillAura.mc.player, entity5);
                if (!this.noSwingSet.enabled) {
                    KillAura.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                    KillAura.mc.player.swingArm(EnumHand.MAIN_HAND);
                }
                KillAura.mc.player.setSprinting(false);
                this.setRotation(arrf[0], arrf[1]);
            }
        }
    }

    public KillAura() {
        super("KiIlAura", "automatically hits enemies", 0, Module.Category.COMBAT);
        this.hit = new TimerUtil();
        this.rangeSet = new NumberSetting("Range", 3.5, 3.0, 10.0, 0.1);
        this.fovSet = new NumberSetting("Fov", 360.0, 0.0, 360.0, 2.0);
        this.noSwingSet = new BooleanSetting("No Swing", false);
        this.animalsSet = new BooleanSetting("Animals", false);
        this.playersSet = new BooleanSetting("Players", true);
        this.mobsSet = new BooleanSetting("Mobs", false);
        this.invisibleTarget = new BooleanSetting("Invisible", false);
        this.yawTarget = new BooleanSetting("Yaw", true);
        this.pitchTarget = new BooleanSetting("Pitch", false);
        this.modeSetting = new ModeSetting("Mode", "Bypass", "Bypass", "Simple");
        this.modePrior = new ModeSetting("Priority", "Distance", "Distance", "Health", "LivingTime");
        this.addSettings(this.rangeSet);
        this.addSettings(this.noSwingSet);
        this.addSettings(this.animalsSet);
        this.addSettings(this.playersSet);
        this.addSettings(this.mobsSet, this.invisibleTarget);
        this.addSettings(this.modeSetting);
        this.addSettings(this.fovSet);
        this.addSettings(this.modePrior);
        this.addSettings(this.yawTarget, this.pitchTarget);
    }

    public static void resetPressed(KeyBinding keyBinding) {
        KillAura.setPressed(keyBinding, GameSettings.isKeyDown((KeyBinding)keyBinding));
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent playerTickEvent) {
        if (KillAura.mc.player == null || KillAura.mc.player.isDead) {
            return;
        }
        List list = KillAura.mc.world.loadedEntityList.stream().filter(entity -> entity != KillAura.mc.player).filter(this::getDickLength).filter(entity -> !entity.isDead).filter(this::lambda$onPlayerTick$3).filter(entity -> entity.ticksExisted > 30).filter(this::lambda$onPlayerTick$5).collect(Collectors.toList());
        if (this.modePrior.activeMode == "Distance") {
            list.stream().sorted(Comparator.comparing(entity -> Float.valueOf(KillAura.mc.player.getDistance((Entity) entity)))).collect(Collectors.toList());
        } else if (this.modePrior.activeMode == "Health") {
            list.stream().sorted(Comparator.comparing(entity -> Float.valueOf(((EntityPlayer)entity).getHealth()))).collect(Collectors.toList());
        } else {
            list.stream().sorted(Comparator.comparing(entity -> Float.valueOf(((EntityPlayer)entity).ticksExisted))).collect(Collectors.toList());
        }
        if (list.size() > 0) {
            if (this.modePrior.activeMode == "LivingTime") {
                this.attackTarget((Entity)list.get(list.size() - 1));
            } else {
                this.attackTarget((Entity)list.get(0));
            }
        }
    }

    private boolean lambda$onPlayerTick$3(Entity entity) {
        return this.attackCheck(entity);
    }

    private static boolean isPlayerUsingMainhand(EntityPlayer entityPlayer) {
        ItemStack itemStack = entityPlayer.getHeldItemMainhand();
        return entityPlayer.getItemInUseCount() > 0 && (itemStack.getItemUseAction() == EnumAction.EAT && !entityPlayer.isCreative() && (entityPlayer.getFoodStats().needFood() || itemStack.getItem() instanceof ItemAppleGold) || itemStack.getItemUseAction() == EnumAction.BOW && KillAura.canShootBow(entityPlayer) || itemStack.getItemUseAction() == EnumAction.DRINK || itemStack.getItemUseAction() == EnumAction.BLOCK);
    }

    public static void setPressed(KeyBinding keyBinding, boolean bl) {
        try {
            Field field = keyBinding.getClass().getDeclaredField("pressed");
        } catch (NoSuchFieldException e) {

        }
    }

    private boolean lambda$onPlayerTick$5(Entity entity) {
        return (double)(Math.abs(KillAura.getRotations(entity)[0] - entity.rotationYaw) % 180.0f) < this.fovSet.getValue() / 2.0;
    }

    private static boolean canShootBow(EntityPlayer entityPlayer) {
        if (entityPlayer.isCreative()) {
            return true;
        }
        for (ItemStack itemStack : entityPlayer.inventory.mainInventory) {
            if (itemStack.getItem() != Items.ARROW) continue;
            return true;
        }
        return false;
    }

    public static float[] getRotations(Entity entity) {
        double d = entity.posX + (entity.posX - entity.lastTickPosX) - KillAura.mc.player.posX;
        double d2 = entity.posY + (double)entity.getEyeHeight() - KillAura.mc.player.posY + (double) KillAura.mc.player.getEyeHeight() - 3.5;
        double d3 = entity.posZ + (entity.posZ - entity.lastTickPosZ) - KillAura.mc.player.posZ;
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

    private static boolean isPlayerShielded(EntityPlayer entityPlayer) {
        return entityPlayer.getItemInUseCount() > 0 && (entityPlayer.getHeldItemMainhand().getItem() instanceof ItemShield || entityPlayer.getHeldItemOffhand().getItem() instanceof ItemShield && !KillAura.isPlayerUsingMainhand(entityPlayer));
    }

    private void setRotation(float f, float f2) {
        block21: {
            KillAura.mc.player.renderYawOffset = f;
            KillAura.mc.player.rotationYawHead = f;
            f = this.random.nextBoolean() ? (float)((double)f + (double)this.random.nextInt(100) * 0.02) : (float)((double)f - (double)this.random.nextInt(100) * 0.02);
            f2 = this.random.nextBoolean() ? (float)((double)f2 + (double)this.random.nextInt(100) * 0.04) : (float)((double)f2 - (double)this.random.nextInt(100) * 0.04);
            if (this.yawTarget.isEnabled()) {
                if (f >= 0.0f) {
                    if (KillAura.mc.player.rotationYaw < f) {
                        while (KillAura.mc.player.rotationYaw < f) {
                            KillAura.mc.player.rotationYaw = (float)((double) KillAura.mc.player.rotationYaw + (double)this.random.nextInt(100) * 0.001);
                        }
                    } else {
                        while (KillAura.mc.player.rotationYaw > f) {
                            KillAura.mc.player.rotationYaw = (float)((double) KillAura.mc.player.rotationYaw - (double)this.random.nextInt(100) * 0.001);
                        }
                    }
                } else if (KillAura.mc.player.rotationYaw < f) {
                    while (KillAura.mc.player.rotationYaw < f) {
                        KillAura.mc.player.rotationYaw = (float)((double) KillAura.mc.player.rotationYaw + (double)this.random.nextInt(100) * 0.001);
                    }
                } else {
                    while (KillAura.mc.player.rotationYaw > f) {
                        KillAura.mc.player.rotationYaw = (float)((double) KillAura.mc.player.rotationYaw - (double)this.random.nextInt(100) * 0.001);
                    }
                }
            }
            if (!this.pitchTarget.isEnabled()) break block21;
            if (f2 >= 0.0f) {
                if (KillAura.mc.player.rotationPitch < f2) {
                    while (KillAura.mc.player.rotationPitch < f2) {
                        KillAura.mc.player.rotationPitch = (float)((double) KillAura.mc.player.rotationPitch + (double)this.random.nextInt(100) * 0.001);
                    }
                } else {
                    while (KillAura.mc.player.rotationPitch > f2) {
                        KillAura.mc.player.rotationPitch = (float)((double) KillAura.mc.player.rotationPitch - (double)this.random.nextInt(100) * 0.001);
                    }
                }
            } else if (KillAura.mc.player.rotationPitch < f2) {
                while (KillAura.mc.player.rotationPitch < f2) {
                    KillAura.mc.player.rotationPitch = (float)((double) KillAura.mc.player.rotationPitch + (double)this.random.nextInt(100) * 0.001);
                }
            } else {
                while (KillAura.mc.player.rotationPitch > f2) {
                    KillAura.mc.player.rotationPitch = (float)((double) KillAura.mc.player.rotationPitch - (double)this.random.nextInt(100) * 0.001);
                }
            }
        }
    }
}

