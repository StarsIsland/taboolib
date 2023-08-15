package taboolib.platform.autoregister

import java.lang.annotation.Inherited

@Target(AnnotationTarget.CLASS)
@Inherited
@Retention(AnnotationRetention.RUNTIME)
annotation class AutoRegister(val value: RegisterType)