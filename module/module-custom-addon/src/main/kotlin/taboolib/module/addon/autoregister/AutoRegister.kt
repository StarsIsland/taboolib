package taboolib.module.addon.autoregister

import java.lang.annotation.Inherited

@Target(AnnotationTarget.CLASS)
@Inherited
@Retention(AnnotationRetention.RUNTIME)
annotation class AutoRegister(val value: RegisterType)