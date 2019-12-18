package me.hibikine.nekonotereloaded

import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraftforge.client.event.ModelRegistryEvent
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Mod(
        modid = NekonoteReloaded.ID,
        name = NekonoteReloaded.Name,
        version = NekonoteReloaded.Version,
        modLanguage = "kotlin")
@Mod.EventBusSubscriber
class NekonoteReloaded {
    companion object {
        const val ID = "nekonotereloaded"
        const val Name = "Nekonote Reloaded"
        const val Version = "0.1.0"
        @JvmStatic
        var logger: Logger = LogManager.getLogger()
            private set
        private val creativeTab = object : CreativeTabs("nekonoteReloaded") {
            override fun createIcon(): ItemStack {
                return ItemStack(ItemPaw)
            }

        }

        @SubscribeEvent
        @JvmStatic
        fun register(e: RegistryEvent.Register<Item>) {
            e.registry.register(ItemPaw.setCreativeTab(creativeTab))
        }

        @SubscribeEvent
        @JvmStatic
        fun registerModels(e: ModelRegistryEvent) {
            ModelLoader.setCustomModelResourceLocation(
                    ItemPaw,
                    0,
                    ModelResourceLocation(ItemPaw.registryName!!, "inventory"))
        }
    }
}
