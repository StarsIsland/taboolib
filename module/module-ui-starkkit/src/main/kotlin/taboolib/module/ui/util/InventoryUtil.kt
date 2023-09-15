package taboolib.module.ui.util

import cn.nukkit.Player
import cn.nukkit.event.inventory.InventoryTransactionEvent
import cn.nukkit.inventory.BaseInventory
import cn.nukkit.inventory.Inventory
import cn.nukkit.inventory.transaction.InventoryTransaction
import cn.nukkit.inventory.transaction.action.SlotChangeAction
import cn.nukkit.item.Item
import it.unimi.dsi.fastutil.longs.Long2LongMap
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap

fun getSlotTransactionResult(inventory: Inventory, transaction: InventoryTransaction): Item {
    var item = BaseInventory.AIR_ITEM
    for (each in transaction.actionList) {
        if (each is SlotChangeAction) {
            if (each.inventory === inventory) {
                item = each.targetItem
            }
        }
    }
    return item
}

fun isTransactionUnsafe(
    sourceInventory: Inventory, displayInventory: Inventory,
    transaction: InventoryTransaction
): Boolean {
    for (each in transaction.actionList) {
        if (each is SlotChangeAction) {
            if (each.inventory === displayInventory) {
                val slot = each.slot
                if (slot < 0 || slot >= sourceInventory.size) {
                    return true
                }
                if (!sourceInventory.getItem(slot).equalsExact(each.sourceItem)) {
                    return true
                }
            }
        }
    }
    return false
}

fun getSlotTransactionResult(inventory: Inventory, event: InventoryTransactionEvent): Item {
    return getSlotTransactionResult(inventory, event.transaction)
}

fun isTransactionUnsafe(
    sourceInventory: Inventory, displayInventory: Inventory,
    event: InventoryTransactionEvent
): Boolean {
    return isTransactionUnsafe(sourceInventory, displayInventory, event.transaction)
}

private val lastClickTime: Long2LongMap = Long2LongOpenHashMap()

fun ensurePlayerSafeForCustomInv(player: Player): Boolean {
    val lastClick = lastClickTime.getOrDefault(player.loaderId.toLong(), 0L)
    val currentClick = System.currentTimeMillis()
    if (currentClick - lastClick < 100) {
        return false
    }
    lastClickTime.put(player.loaderId.toLong(), currentClick)
    return !player.isSneaking
}