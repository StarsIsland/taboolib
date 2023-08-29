package taboolib.module.addon.autoregister

import cn.nukkit.block.Block
import cn.nukkit.block.customblock.CustomBlock
import cn.nukkit.blockentity.BlockEntity
import cn.nukkit.entity.Entity
import cn.nukkit.entity.custom.CustomEntity
import cn.nukkit.entity.provider.CustomClassEntityProvider
import cn.nukkit.item.Item
import cn.nukkit.item.customitem.CustomItem
import cn.nukkit.item.enchantment.Enchantment
import java.lang.reflect.Modifier
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
            val modifiers = clazz.modifiers

            if (Modifier.isAbstract(modifiers) || clazz.isAnnotationPresent(AutoRegister::class.java)) {
                return
            }

            when {
                isTypeOf<CustomItem>(clazz) -> registerCustomItem(clazz)
                isTypeOf<BlockEntity>(clazz) -> registerBlockEntity(clazz)
                isTypeOf<CustomBlock>(clazz) -> registerCustomBlock(clazz)
                isTypeOf<Enchantment>(clazz) -> registerEnchantment(clazz)
                isTypeOf<Entity>(clazz) -> registerEntity(clazz)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private inline fun <reified T> isTypeOf(clazz: Class<*>): Boolean {
        return T::class.java.isAssignableFrom(clazz)
    }

    private fun registerCustomItem(clazz: Class<*>) {
        Item.registerCustomItem(clazz.castToType<CustomItem>())
    }

    private fun registerBlockEntity(clazz: Class<*>) {
        clazz.getAnnotation(BlockEntityData::class.java)?.let { data ->
            BlockEntity.registerBlockEntity(data.name, clazz.castToType<BlockEntity>())
        }
    }

    private fun registerCustomBlock(clazz: Class<*>) {
        Block.registerCustomBlock(listOf(clazz.castToType<CustomBlock>()))
    }

    private fun registerEnchantment(clazz: Class<*>) {
        clazz.getDeclaredConstructor().newInstance().let {
            if (it is Enchantment) {
                Enchantment.register(it)
            }
        }
    }

    private fun registerEntity(clazz: Class<*>) {
        Entity.registerCustomEntity(CustomClassEntityProvider(clazz.castToType<Entity>()))
    }

    private inline fun <reified T> Class<*>.castToType(): Class<out T>? {
        return this as? Class<out T>
    }
}