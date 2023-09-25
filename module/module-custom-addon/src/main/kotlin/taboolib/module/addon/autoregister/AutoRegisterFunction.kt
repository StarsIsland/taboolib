package taboolib.module.addon.autoregister

import cn.nukkit.block.Block
import cn.nukkit.block.customblock.CustomBlock
import cn.nukkit.blockentity.BlockEntity
import cn.nukkit.entity.Entity
import cn.nukkit.entity.provider.CustomClassEntityProvider
import cn.nukkit.item.Item
import cn.nukkit.item.customitem.CustomItem
import cn.nukkit.item.enchantment.Enchantment
import cn.nukkit.level.Level
import taboolib.common.platform.function.dev
import taboolib.common.platform.function.warning
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

            if (Modifier.isAbstract(modifiers) || !clazz.isAnnotationPresent(AutoRegister::class.java)) {
                return
            }

            dev("Scanned target class ${clazz.simpleName}")

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
        dev("Register custom item ${clazz.simpleName}")
        Item.registerCustomItem(clazz.castToType<CustomItem>())
    }

    private fun registerBlockEntity(clazz: Class<*>) {
        dev("Register block entity ${clazz.simpleName}")
        clazz.getAnnotation(BlockEntityData::class.java)?.let { data ->
            BlockEntity.registerBlockEntity(data.name, clazz.castToType<BlockEntity>())
        } ?: run {
            warning("${clazz.simpleName} Block Entity Register Skip Due to it do not have BlockEntityData")
        }
    }

    private fun registerCustomBlock(clazz: Class<*>) {
        dev("Register custom block ${clazz.simpleName}")
        val blockClazz = clazz.castToType<CustomBlock>()
        Block.registerCustomBlock(listOf(blockClazz))
        if (clazz.isAnnotationPresent(RandomTick::class.java)) {
            (blockClazz?.constructors?.first { it.parameterCount == 0 }?.newInstance() as Block?)?.id?.let { id ->
                Level.setCanRandomTick(id, true)
            }
        }
    }

    private fun registerEnchantment(clazz: Class<*>) {
        dev("Register custom enchantment ${clazz.simpleName}")
        clazz.getDeclaredConstructor().newInstance().let {
            if (it is Enchantment) {
                Enchantment.register(it)
            }
        }
    }

    private fun registerEntity(clazz: Class<*>) {
        dev("Register custom entity ${clazz.simpleName}")
        Entity.registerCustomEntity(CustomClassEntityProvider(clazz.castToType<Entity>()))
    }

    private inline fun <reified T> Class<*>.castToType(): Class<out T>? {
        return this as? Class<out T>
    }
}