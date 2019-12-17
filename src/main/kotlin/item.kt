package me.hibikine.nekonotereloaded

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.init.SoundEvents
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.apache.logging.log4j.Level

object ItemPaw : Item() {
    init {
        this.maxStackSize = 1
        unlocalizedName = "paw"
        this.setRegistryName(NekonoteReloaded.ID, "item_paw")
    }


    override fun onItemUse(
            player: EntityPlayer?,
            world: World?,
            pos: BlockPos?,
            hand: EnumHand?,
            facing: EnumFacing?,
            hitX: Float,
            hitY: Float,
            hitZ: Float
    ): EnumActionResult {
        if (
                world == null ||
                player == null ||
                pos == null
        ) {
            return EnumActionResult.SUCCESS
        }
        NekonoteReloaded.logger.log(Level.INFO, "pos: (%d, %d, %d)".format(pos.x, pos.y, pos.z))
        NekonoteReloaded.logger.log(Level.INFO, "facing: %s".format(facing?.name ?: "null"))
        if (!world.isRemote) {
            putItem(player, world, pos)
        } else {
            playInteractive(world, pos)
        }
        return EnumActionResult.SUCCESS
    }

    fun putItem(player: EntityPlayer, world: World, pos: BlockPos) {
        val blockState = world.getBlockState(pos)
        val block = blockState.block
        val targetItemStack = ItemStack(getItemFromBlock(block))
        if (!player.inventory.hasItemStack(targetItemStack) && !player.capabilities.isCreativeMode) {
            return
        }
        for (i in -1..1) {
            for (j in -1..1) {
                if (i == 0 && j == 0) {
                    continue
                }

                val targetPos = pos.add(i, 0, j)
                if (world.getBlockState(targetPos).block != Blocks.AIR) {
                    continue
                }

                world.setBlockState(targetPos, blockState)
                if (!player.capabilities.isCreativeMode) {
                    val position = player.inventory.getSlotFor(targetItemStack)
                    if (position != -1) {
                        player.inventory.mainInventory[position].count -= 1
                    } else if (
                            !player.heldItemOffhand.isEmpty &&
                            player.heldItemOffhand.isItemEqual(targetItemStack)
                    ) {
                        player.heldItemOffhand.count -= 1
                    }
                    if (!player.inventory.hasItemStack(ItemStack(getItemFromBlock(block)))) {
                        return
                    }
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    fun playInteractive(world: World, pos: BlockPos) {
        world.spawnParticle(
                EnumParticleTypes.EXPLOSION_NORMAL,
                pos.x + 0.5,
                pos.y + 1.5,
                pos.z + 0.5,
                0.0,
                0.0,
                0.0,
                20
        )
        world.playSound(
                pos.x + 0.5,
                pos.y + 0.5,
                pos.z + 0.5,
                SoundEvents.ENTITY_CAT_AMBIENT,
                SoundCategory.PLAYERS,
                1.0f,
                1.0f,
                true)
    }
}
