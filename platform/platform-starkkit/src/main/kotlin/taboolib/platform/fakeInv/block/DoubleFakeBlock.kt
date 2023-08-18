package taboolib.platform.fakeInv.block

import cn.nukkit.Player
import cn.nukkit.math.Vector3
import cn.nukkit.nbt.tag.CompoundTag


class DoubleFakeBlock(blockId: Int, tileId: String) : SingleFakeBlock(blockId, tileId) {

    override fun getPositions(player: Player): List<Vector3> {
        val blockPosition: Vector3 = player.position.add(getOffset(player)).floor()
        return if (blockPosition.floorY in 0..255) {
            if (blockPosition.floorX and 1 == 1) {
                listOf(blockPosition, blockPosition.east())
            } else listOf(blockPosition, blockPosition.west())
        } else emptyList()
    }

    override fun getOffset(player: Player): Vector3 {
        val offset = super.getOffset(player)
        offset.x *= 1.5
        offset.z *= 1.5
        return offset
    }

    override fun getBlockEntityDataAt(position: Vector3, title: String): CompoundTag {
        return super.getBlockEntityDataAt(position, title)
            .putInt("pairx", position.floorX + if (position.floorX and 1 == 1) 1 else -1)
            .putInt("pairz", position.floorZ)
    }
}