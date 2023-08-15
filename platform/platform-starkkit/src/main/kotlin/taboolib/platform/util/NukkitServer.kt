package taboolib.platform.util

import cn.nukkit.Player
import cn.nukkit.Server

val onlinePlayers: List<Player>
    get() = Server.getInstance().onlinePlayers.values.toList()

fun Any.broadcast() = Server.getInstance().broadcastMessage(this.toString())