/*
 * Decompiled with CFR 0.150.
 */
package me.independed.inceptice.settings;

public class BooleanSetting
        extends Setting {
    public boolean enabled;

    public BooleanSetting(String string, boolean bl) {
        this.name = string;
        this.enabled = bl;
    }

    public void toggle() {
        this.enabled = !this.enabled;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean bl) {
        this.enabled = bl;
    }
}

