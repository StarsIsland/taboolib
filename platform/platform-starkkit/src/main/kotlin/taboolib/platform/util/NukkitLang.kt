package taboolib.platform.util

import cn.nukkit.command.CommandSender
import taboolib.common.platform.function.adaptCommandSender
import taboolib.module.lang.asLangText
import taboolib.module.lang.asLangTextList
import taboolib.module.lang.asLangTextOrNull
import taboolib.module.lang.sendLang

fun CommandSender.sendLang(node: String, vararg args: Any) {
    adaptCommandSender(this).sendLang(node, *args)
}

fun CommandSender.asLangTextOrNull(node: String, vararg args: Any): String? {
    return adaptCommandSender(this).asLangTextOrNull(node, *args)
}

fun CommandSender.asLangText(node: String, vararg args: Any): String {
    return adaptCommandSender(this).asLangText(node, *args)
}

fun CommandSender.asLangTextList(node: String, vararg args: Any): List<String> {
    return adaptCommandSender(this).asLangTextList(node, *args)
}