package landmaster.plustic.traits;

import landmaster.plustic.api.*;
import landmaster.plustic.entity.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraftforge.common.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.fml.common.eventhandler.*;
import slimeknights.tconstruct.library.traits.*;
import slimeknights.tconstruct.library.utils.*;

public class BlindBandit extends AbstractTrait {
	public static final BlindBandit blindbandit = new BlindBandit();
	
	public BlindBandit() {
		super("blindbandit", 0xFF00FF);
		MinecraftForge.EVENT_BUS.register(this);
		Toggle.toggleable.add(identifier);
	}
	
	@Override
	public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damageDealt, boolean wasCritical, boolean wasHit) {
		if (wasHit && target.isEntityAlive() && random.nextFloat() < 0.38f
				&& Toggle.getToggleState(tool, identifier)) {
			EntityBlindBandit bandit = new EntityBlindBandit(player.worldObj);
			bandit.setPosition(player.posX,
					player.posY,
					player.posZ);
			player.worldObj.spawnEntityInWorld(bandit);
		}
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void defend(LivingHurtEvent event) {
		ItemStack tool = event.getEntityLiving().getHeldItemMainhand();
		if (event.getEntity().getEntityWorld().isRemote
				|| !Toggle.getToggleState(tool, identifier)
				|| event.isCanceled()
				|| !TinkerUtil.hasTrait(
						TagUtil.getTagSafe(tool),
						getIdentifier()))
			return;
		if (random.nextFloat() < 0.38f) {
			EntityBlindBandit bandit = new EntityBlindBandit(event.getEntity().worldObj, event.getEntity());
			bandit.setPosition(event.getEntity().posX,
					event.getEntity().posY,
					event.getEntity().posZ);
			event.getEntity().worldObj.spawnEntityInWorld(bandit);
		}
	}
}
