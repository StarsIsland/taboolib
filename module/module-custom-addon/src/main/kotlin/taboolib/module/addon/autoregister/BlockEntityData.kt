package taboolib.module.addon.autoregister

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class BlockEntityData(val name: String)