package taboolib.module.ui.fakeInv

import cn.nukkit.event.inventory.InventoryTransactionEvent
import cn.nukkit.item.Item

fun interface ItemHandler {
    fun handle(item: Item, event: InventoryTransactionEvent)
}