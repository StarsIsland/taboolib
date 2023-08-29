package taboolib.module.ui.fakeInv

import cn.nukkit.Player
import cn.nukkit.event.inventory.InventoryTransactionEvent
import cn.nukkit.inventory.BaseInventory
import cn.nukkit.inventory.InventoryType
import cn.nukkit.item.Item
import cn.nukkit.math.Vector3
import cn.nukkit.network.protocol.ContainerClosePacket
import cn.nukkit.network.protocol.ContainerOpenPacket
import taboolib.common.platform.function.submit
import taboolib.module.ui.fakeInv.block.FakeBlock
import java.util.*
import kotlin.collections.HashMap


class CustomInventory : BaseInventory {

    private var invTitle: String
    private val fakeBlock: FakeBlock
    private var defaultItemHandler: ItemHandler = ItemHandler { _, _ ->  }
    private val handlers: HashMap<Int, ItemHandler> = HashMap()

    constructor(inventoryType: InventoryType, title: String?) : super(null, inventoryType) {
        this.invTitle = title ?: inventoryType.defaultTitle
        this.fakeBlock = FakeInventories.getFakeBlock(inventoryType)
    }

    override fun onOpen(player: Player) {
        this.fakeBlock.create(player, invTitle)

        submit(delay = 3) {
            val packet = ContainerOpenPacket()
            packet.windowId = player.getWindowId(this@CustomInventory)
            packet.type = getType().networkType

            val position: Vector3 = fakeBlock.getPositions(player)[0]
            packet.x = position.floorX
            packet.y = position.floorY
            packet.z = position.floorZ
            player.dataPacket(packet)

            super.onOpen(player)

            this@CustomInventory.sendContents(player)
        }
    }

    override fun onClose(player: Player) {
        val packet = ContainerClosePacket()
        packet.windowId = player.getWindowId(this)
        packet.wasServerInitiated = player.closingWindowId !== packet.windowId
        player.dataPacket(packet)

        super.onClose(player)

        fakeBlock.remove(player)
    }

    override fun close(who: Player) {
        super.close(who)
    }

    fun addItem(handler: ItemHandler, vararg slots: Item): Array<Item?> {
        val itemSlots: MutableList<Item> = ArrayList<Item>()
        for (slot in slots) {
            if (slot.getId() !== 0 && slot.getCount() > 0) {
                itemSlots.add(slot.clone())
            }
        }
        val emptySlots: MutableList<Int> = ArrayList()
        for (i in 0 until getSize()) {
            val item: Item = getItem(i)
            if (item.getId() === Item.AIR || item.getCount() <= 0) {
                emptySlots.add(i)
            }
            for (slot in Collections.unmodifiableList(itemSlots)) {
                if (slot.equals(item) && item.getCount() < item.maxStackSize) {
                    var amount: Int = (item.maxStackSize - item.getCount()).coerceAtMost(slot.getCount())
                    amount = amount.coerceAtMost(getMaxStackSize())
                    if (amount > 0) {
                        slot.setCount(slot.getCount() - amount)
                        item.setCount(item.getCount() + amount)
                        this.setItem(i, item, handler)
                        if (slot.getCount() <= 0) {
                            itemSlots.remove(slot)
                        }
                    }
                }
            }
            if (itemSlots.isEmpty()) {
                break
            }
        }
        if (itemSlots.isNotEmpty() && emptySlots.isNotEmpty()) {
            for (slotIndex in emptySlots) {
                if (itemSlots.isNotEmpty()) {
                    val slot: Item = itemSlots[0]
                    var amount: Int = slot.maxStackSize.coerceAtMost(slot.getCount())
                    amount = amount.coerceAtMost(getMaxStackSize())
                    slot.setCount(slot.getCount() - amount)
                    val item: Item = slot.clone()
                    item.setCount(amount)
                    this.setItem(slotIndex, item, handler)
                    if (slot.getCount() <= 0) {
                        itemSlots.remove(slot)
                    }
                }
            }
        }
        return itemSlots.toTypedArray()
    }

    fun setItem(index: Int, item: Item, handler: ItemHandler?) {
        super.setItem(index, item)
        this.handlers[index] = handler ?: defaultItemHandler
    }

    fun setDefaultItemHandler(handler: ItemHandler) {
        this.defaultItemHandler = handler
    }

    override fun getTitle(): String {
        return invTitle
    }

    fun setTitle(title: String) {
        this.invTitle = title
    }

    fun handle(index: Int, item: Item, event: InventoryTransactionEvent) {
        val handler: ItemHandler = this.handlers.getOrDefault(index, defaultItemHandler)
        handler.handle(item, event)
    }
}