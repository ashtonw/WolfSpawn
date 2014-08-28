/**
 * Based on work by MikePrimm
 * https://github.com/Bukkit/CraftBukkit/pull/255
 */
package com.jynxdaddy.wolfspawn;

import net.minecraft.server.EntityWolf;
import net.minecraft.server.PathEntity;

import org.bukkit.craftbukkit.entity.CraftWolf;
import org.bukkit.entity.Wolf;

/**
 * Adapter for Wolf with updated API
 * @author Ashton
 * @edit jascotty2 - health set with setowner, and updated for 1.5
 */
public class UpdatedWolf {

    CraftWolf wolf;

    public UpdatedWolf(Wolf wolf) {
        this.wolf = (CraftWolf) wolf;
    }

    public Wolf getWolf() {
        return wolf;
    }

    public boolean isAngry() {
        return wolf.isAngry();
    }

    public void setAngry(boolean angry) {
        wolf.setAngry(angry);
    }

    public boolean isSitting() {
        return wolf.isSitting();
    }

    public void setSitting(boolean sitting) {
        wolf.setSitting(sitting);
    }

    public boolean isTame() {
        return getHandle().isTamed();
    }

    public void setTame(boolean tame) {
        if (tame && !wolf.getHandle().isTamed()) {// if was wild
            wolf.getHandle().health = (int) Math.round(20 * (wolf.getHandle().health / 8.));
        } else if (!tame && wolf.getHandle().isTamed()) {
            wolf.getHandle().health = (int) Math.round(8 * (wolf.getHandle().health / 20.));
        }
        wolf.getHandle().setTamed(tame);
    }

    public String getOwner() {
        return getHandle().getOwnerName();
    }

    public void setOwner(String player) {
        EntityWolf e = getHandle();
        if ((player != null) && (player.length() > 0)) {
            if (!e.isTamed()) {// if was wild
                e.health = (int) Math.round(20 * (e.health / 8.));
            }
            e.setTamed(true); /* Make him tame */
            e.setPathEntity((PathEntity) null); /* Clear path */
            e.setOwnerName(player); /* Set owner */
        } else {
            if (e.isTamed()) {// if was tame
                e.health = (int) Math.round(8 * (e.health / 20.));
            }
            e.setTamed(false); /* Make him not tame */
            e.setOwnerName(""); /* Clear owner */
        }
    }

    public EntityWolf getHandle() {
        return wolf.getHandle();
    }

    @Override
    public String toString() {
        return "CraftWolf[anger=" + isAngry() + ",owner=" + getOwner() + ",tame=" + isTame() + ",sitting=" + isSitting() + "]";
    }
}
