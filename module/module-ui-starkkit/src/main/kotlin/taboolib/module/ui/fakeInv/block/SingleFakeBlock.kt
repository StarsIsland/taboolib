package taboolib.module.ui.fakeInv.block

import cn.nukkit.Player;
import cn.nukkit.blockstate.BlockStateRegistry;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.BlockEntityDataPacket;
import cn.nukkit.network.protocol.UpdateBlockPacket;
import taboolib.module.ui.fakeInv.block.FakeBlock

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.function.Consumer

open class SingleFakeBlock(private val blockId: Int, private val tileId: String) : FakeBlock() {

    private lateinit var lastPositions: List<Vector3>

    override fun create(player: Player, title: String) {
        val positions = getPositions(player)
        lastPositions = positions
        positions.forEach(Consumer { position: Vector3 ->
            val updateBlockPacket = UpdateBlockPacket()
            updateBlockPacket.blockRuntimeId = BlockStateRegistry.getRuntimeId(blockId, 0)
            updateBlockPacket.flags = UpdateBlockPacket.FLAG_NETWORK
            updateBlockPacket.x = position.floorX
            updateBlockPacket.y = position.floorY
            updateBlockPacket.z = position.floorZ
            player.dataPacket(updateBlockPacket)
            val blockEntityDataPacket = BlockEntityDataPacket()
            blockEntityDataPacket.x = position.floorX
            blockEntityDataPacket.y = position.floorY
            blockEntityDataPacket.z = position.floorZ
            try {
                blockEntityDataPacket.namedTag =
                    NBTIO.write(getBlockEntityDataAt(position, title), ByteOrder.LITTLE_ENDIAN, true)
            } catch (exception: IOException) {
                exception.printStackTrace()
            }
            player.dataPacket(blockEntityDataPacket)
        })
    }

    override fun remove(player: Player) {
        lastPositions.forEach(Consumer { position: Vector3 ->
            val packet = UpdateBlockPacket()
            packet.blockRuntimeId = player.getLevel().getBlock(position).runtimeId
            packet.flags = UpdateBlockPacket.FLAG_NETWORK
            packet.x = position.floorX
            packet.y = position.floorY
            packet.z = position.floorZ
            player.dataPacket(packet)
        })
    }

    protected open fun getBlockEntityDataAt(position: Vector3, title: String): CompoundTag {
        return CompoundTag()
            .putString("id", tileId)
            .putString("CustomName", title)
    }
}