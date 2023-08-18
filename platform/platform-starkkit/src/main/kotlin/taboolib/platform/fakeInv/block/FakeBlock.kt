package taboolib.platform.fakeInv.block

import cn.nukkit.Player
import cn.nukkit.math.Vector3


abstract class FakeBlock {
    abstract fun create(player: Player, title: String)

    abstract fun remove(player: Player)

    open fun getPositions(player: Player): List<Vector3> {
        val blockPosition: Vector3 = player.position.add(getOffset(player)).floor()
        return if (blockPosition.floorY in 0..255) {
            listOf<Vector3>(blockPosition)
        } else emptyList<Vector3>()
    }

    protected open fun getOffset(player: Player): Vector3 {
        val offset: Vector3 = player.directionVector
        offset.x *= -(1 + player.width)
        offset.y *= -(1 + player.height)
        offset.z *= -(1 + player.width)
        return offset
    }
}