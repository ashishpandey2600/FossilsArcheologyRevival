package com.github.revival.server.entity.mob;

import com.github.revival.Revival;
import com.github.revival.server.entity.ai.DinoAIAvoidEntity;
import com.github.revival.server.entity.ai.DinoAIFollowOwner;
import com.github.revival.server.entity.ai.DinoAIHunt;
import com.github.revival.server.entity.ai.DinoAILeapAtTarget;
import com.github.revival.server.entity.ai.DinoAILookIdle;
import com.github.revival.server.entity.ai.DinoAIRiding;
import com.github.revival.server.entity.ai.DinoAIWander;
import com.github.revival.server.entity.ai.DinoAIWatchClosest;
import com.github.revival.server.entity.mob.test.DinoAIFeeder;
import com.github.revival.server.entity.mob.test.EntityNewPrehistoric;
import com.github.revival.server.entity.mob.test.EntityToyBase;
import com.github.revival.server.enums.EnumPrehistoric;
import com.github.revival.server.enums.EnumPrehistoricAI.*;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFleeSun;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityDeinonychus extends EntityNewPrehistoric {

    public EntityDeinonychus(World world) {
        super(world, EnumPrehistoric.Deinonychus, 2, 6, 10, 32, 0.23, 0.35);
        this.getNavigator().setAvoidsWater(true);
        this.getNavigator().setCanSwim(true);
        this.getNavigator().setAvoidSun(true);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(3, new DinoAIRiding(this, 1.0F));
        this.tasks.addTask(3, new DinoAIAvoidEntity(this, 16.0F, 0.8D, 1.33D));
        this.tasks.addTask(4, new DinoAILeapAtTarget(this));
        this.tasks.addTask(5, new EntityAIRestrictSun(this));
        this.tasks.addTask(6, new EntityAIFleeSun(this, 1.0D));
        this.tasks.addTask(7, new DinoAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
        this.tasks.addTask(8, new DinoAIFeeder(this, 16));
        this.tasks.addTask(9, new DinoAIWander(this, 1.0D));
        this.tasks.addTask(10, new DinoAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(10, new DinoAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(4, new DinoAIHunt(this, 200, false));
        this.nearByMobsAllowed = 9;
        this.pediaScale = 24F;
        this.hasFeatherToggle = true;
        this.featherToggle = Revival.CONFIG.featheredDeinonychus;
        this.setSize(1.8F, 1.25F);
        minSize = 0.3F;
        maxSize = 1;
        teenAge = 4;
        developsResistance = false;
        breaksBlocks = false;
    }

    @Override
    public int getAttackLength() {
        return 35;
    }

    @Override
    public void setSpawnValues() {
    }

    @Override
    public Activity aiActivityType() {

        return Activity.NOCTURNAL;
    }

    @Override
    public Attacking aiAttackType() {

        return Attacking.JUMP;
    }

    @Override
    public Climbing aiClimbType() {

        return Climbing.ARTHROPOD;
    }

    @Override
    public Following aiFollowType() {

        return Following.AGRESSIVE;
    }

    @Override
    public Jumping aiJumpType() {

        return Jumping.TWOBLOCKS;
    }

    @Override
    public Response aiResponseType() {

        return Response.TERITORIAL;
    }

    @Override
    public Stalking aiStalkType() {

        return Stalking.STEALTH;
    }

    @Override
    public Taming aiTameType() {

        return Taming.IMPRINTING;
    }

    @Override
    public Untaming aiUntameType() {

        return Untaming.ATTACK;
    }

    @Override
    public Moving aiMovingType() {

        return Moving.WALK;
    }

    @Override
    public WaterAbility aiWaterAbilityType() {

        return WaterAbility.NONE;
    }

    @Override
    public boolean doesFlock() {

        return false;
    }

    @Override
    public Item getOrderItem() {

        return Items.bone;
    }

    @Override
    public int getAdultAge() {
        return 10;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.getAttackTarget() != null && this.ridingEntity != null) {
            if (this.ridingEntity == this.getAttackTarget() && this.ticksExisted % 20 == 0) {
                IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.attackDamage);
                this.getAttackTarget().attackEntityFrom(DamageSource.causeMobDamage(this), (float) iattributeinstance.getAttributeValue());
            }
        }
        if (this.getAttackTarget() != null && this.getAnimation() == ATTACK_ANIMATION && (this.getAnimationTick() >= 17 && this.getAnimationTick() <= 20) && this.onGround) {
            double d0 = this.getAttackTarget().posX - this.posX;
            double d1 = this.getAttackTarget().posZ - this.posZ;
            float f = MathHelper.sqrt_double(d0 * d0 + d1 * d1);
            this.motionX += d0 / (double) f * 1.4D * 0.800000011920929D + this.motionX * 0.20000000298023224D;
            this.motionZ += d1 / (double) f * 1.4D * 0.800000011920929D + this.motionZ * 0.20000000298023224D;
            this.getLookHelper().setLookPositionWithEntity(this.getAttackTarget(), 10, 12);
            this.motionY = 0.6;
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        return false;
    }

    @Override
    public void applyEntityCollision(Entity entity) {
        super.applyEntityCollision(entity);
        if (this.getAttackTarget() != null) {
            if (this.getAttackTarget() == entity && this.getAnimation() == ATTACK_ANIMATION && !onGround && this.ridingEntity != entity && (entity instanceof EntityToyBase)) {
                entity.attackEntityFrom(DamageSource.causeMobDamage(this), 1);
            } else if (this.getAttackTarget() == entity && this.getAnimation() == ATTACK_ANIMATION && !onGround && this.ridingEntity != entity) {
                this.mountEntity(entity);
            }
        }
    }

    @Override
    public boolean canAttackClass(Class clazz) {
        return (clazz != EntityVelociraptor.class) && super.canAttackClass(clazz);
    }

    @Override
    public boolean attackEntityFrom(DamageSource dmg, float i) {
        if (this.ridingEntity != null) {
            if (this.getLastAttacker() != null) {
                if (this.getLastAttacker() == this.ridingEntity) {
                    if (this.getRNG().nextInt(2) == 0) {
                        this.mountEntity(null);
                    }
                }
            }
        }
        return super.attackEntityFrom(dmg, i);
    }
    
    public int getMaxHunger() {
        return 100;
    }
    
	@Override
	public boolean canBeRidden() {
		return false;
	}
}
