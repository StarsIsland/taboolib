package taboolib.platform.autoregister

import cn.nukkit.block.Block
import cn.nukkit.block.customblock.CustomBlock
import cn.nukkit.blockentity.BlockEntity
import cn.nukkit.entity.Entity
import cn.nukkit.entity.provider.CustomClassEntityProvider
import cn.nukkit.item.Item
import cn.nukkit.item.customitem.CustomItem
import cn.nukkit.item.enchantment.Enchantment
import taboolib.common.platform.function.info
import java.io.File
import java.net.URISyntaxException
import java.net.URL
import java.nio.file.Paths
import java.util.jar.JarFile

object AutoRegisterFunction {

    fun register(packageName: String) {
        val jarPath = this::class.java.protectionDomain.codeSource.location.path
        val jarFile = JarFile(jarPath)

        val packagePath = packageName.replace('.', '/')

        for (entry in jarFile.entries()) {
            val name = entry.name
            if (name.startsWith(packagePath) && name.endsWith(".class")) {
                val className = name.replace('/', '.').removeSuffix(".class")
                scanClasses(className)
            }
        }
    }


    private fun scanClasses(className: String) {
        try {
            val clazz = Class.forName(className)
            if(clazz.annotations.isEmpty()) return
            val annotation = clazz.getAnnotation(AutoRegister::class.java)
            annotation?.let {
                when (it.value) {
                    RegisterType.ITEM -> {
                        val customItemClass = clazz as? Class<out CustomItem>
                        customItemClass?.let { Item.registerCustomItem(it) }
                    }
                    RegisterType.BLOCK_ENTITY -> {
                        val customBlockEntity = clazz as? Class<out BlockEntity>
                        customBlockEntity?.let {
                            BlockEntity.registerBlockEntity(clazz.simpleName.lowercase(), it)
                        }
                    }
                    RegisterType.BLOCK -> {
                        val customBlock = clazz as? Class<out CustomBlock>
                        customBlock?.let {
                            Block.registerCustomBlock(listOf(it))
                        }
                    }
                    RegisterType.ENCHANTMENT -> {
                        val customEnchantment = clazz.getDeclaredConstructor().newInstance() as? Enchantment
                        customEnchantment?.let {
                            Enchantment.register(it)
                        }
                    }

                    RegisterType.ENTITY -> {
                        val customEntity = clazz as? Class<out Entity>

                        customEntity?.let {
                            Entity.registerCustomEntity(CustomClassEntityProvider(it))
                        }
                    }
                }
            }
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }

}