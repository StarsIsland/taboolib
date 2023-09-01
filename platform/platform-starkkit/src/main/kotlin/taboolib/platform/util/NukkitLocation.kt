package taboolib.platform.util

import cn.nukkit.level.Location

fun Location.toCommonLocation(): taboolib.common.util.Location {
    return taboolib.common.util.Location(
        level.name, x, y, z, yaw.toFloat(),
        pitch.toFloat()
    )
}

fun taboolib.common.util.Location.toLocation() : Location {
    return Location(this.x, this.y, this.z, this.yaw.toDouble(), this.pitch.toDouble(), nukkitPlugin.server.getLevelByName(this.world))
}