package at.leisner.api.test;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class CustomBow extends BowItem {

    public CustomBow(Item.Properties properties) {
        super(properties);
    }

//    @Override
//    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level world, @NotNull LivingEntity entity, int timeLeft) {
//        if (entity instanceof Player player) {
//            boolean hasInfiniteArrows = player.getAbilities().instabuild || player.getInventory().contains(Items.ARROW.getDefaultInstance());
//            int useDuration = this.getUseDuration(stack) - timeLeft;
//
//            if (useDuration >= 20) {
//                if (!world.isClientSide) {
//                    HomingArrow arrow = new HomingArrow(world);
//                    arrow.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
//                    arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 3.0F, 1.0F);
//
//                    // Find target
//                    LivingEntity target = findTarget(player, world);
//                    if (target != null) {
//                        arrow.setTarget(target);
//                    }
//
//                    world.addFreshEntity(arrow);
//                }
//
//                if (!hasInfiniteArrows && !player.getAbilities().instabuild) {
//                    stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
//                }
//            }
//        }
//    }

    private LivingEntity findTarget(Player player, Level world) {
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
