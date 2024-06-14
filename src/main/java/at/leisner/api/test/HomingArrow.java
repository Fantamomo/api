package at.leisner.api.test;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class HomingArrow extends Arrow {

    private LivingEntity target;

    public HomingArrow(Location location) {
        super(EntityType.ARROW, ((CraftWorld) location.getWorld()).getHandle());

        this.setPosRaw(location.x(), location.y(), location.z());

        ((CraftWorld) location.getWorld()).getHandle().addFreshEntity(this, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }

    public void setTarget(LivingEntity target) {
        this.target = target;
    }

    @Override
    public void tick() {
        super.tick();
        if (target != null && !target.isRemoved()) {
            double dx = target.getX() - this.getX();
            double dy = target.getY() + target.getEyeHeight() - this.getY();
            double dz = target.getZ() - this.getZ();
            double distance = Math.sqrt(dx * dx + dz * dz);

            this.setDeltaMovement(dx * 0.1, dy * 0.1, dz * 0.1);
            this.hasImpulse = true;
        }
    }

    public static LivingEntity findTarget(Player player, Level world) {
        Vec3 viewVector = player.getViewVector(1.0F).normalize();
        Vec3 startVec = player.getEyePosition(1.0F);
        Vec3 endVec = startVec.add(viewVector.scale(50.0));

        // Find the nearest target entity in the line of sight
        LivingEntity target = null;
        double closestDistance = Double.MAX_VALUE;

        for (LivingEntity entity : world.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().expandTowards(viewVector.scale(50.0)).inflate(1.0))) {
            if (entity != player && entity.isAlive()) {
                Vec3 entityVec = entity.getEyePosition(1.0F);
                double distance = startVec.distanceTo(entityVec);

                if (distance < closestDistance) {
                    closestDistance = distance;
                    target = entity;
                }
            }
        }
        return target;
    }
}
