/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 */
package me.independed.inceptice.event.events;

import net.minecraft.network.Packet;

public class EventNetworkPostPacketEvent
        extends EventNetworkPacketEvent {
    public EventNetworkPostPacketEvent(Packet packet) {
        super(packet);
    }
}

