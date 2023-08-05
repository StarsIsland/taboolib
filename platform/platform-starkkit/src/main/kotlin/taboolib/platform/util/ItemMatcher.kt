@file:Isolated
import cn.nukkit.Player
import cn.nukkit.inventory.Inventory
import cn.nukkit.item.Item
import taboolib.common.Isolated

/**
 * 检查玩家背包中的特定物品是否达到特定数量
 *
 * @param item   物品
 * @param amount 检查数量
 * @param remove 是否移除
 * @return boolean
 */
fun Player.checkItem(item: Item, amount: Int = 1, remove: Boolean = false): Boolean {
    if (item == Item.AIR_ITEM) {
        error("air")
    }
    return inventory.checkItem(item, amount, remove)
}

/**
 * 检查背包中的特定物品是否达到特定数量
 *
 * @param item      物品
 * @param amount    检查数量
 * @param remove    是否移除
 * @return boolean
 */
fun Inventory.checkItem(item: Item, amount: Int = 1, remove: Boolean = false): Boolean {
    if (item == Item.AIR_ITEM) {
        error("air")
    }
    return hasItem(amount) { it == item } && (!remove || takeItem(amount) { it == item })
}

/**
 * 检查背包中符合特定规则的物品是否达到特定该数量
 *
 * @param matcher   规则
 * @param amount    数量
 * @return boolean
 */
fun Inventory.hasItem(amount: Int = 1, matcher: (itemStack: Item) -> Boolean): Boolean {
    var checkAmount = amount
    contents.values.forEach { itemStack ->
        if (itemStack != Item.AIR_ITEM && matcher(itemStack)) {
            checkAmount -= itemStack.count
            if (checkAmount <= 0) {
                return true
            }
        }
    }
    return false
}

/**
 * 移除背包中特定数量的符合特定规则的物品
 *
 * @param matcher   规则
 * @param amount    实例
 * @return boolean
 */
fun Inventory.takeItem(amount: Int = 1, matcher: (itemStack: Item) -> Boolean): Boolean {
    var takeAmount = amount

    for (i in 0..contents.size) {
        val itemStack = contents[i] ?: continue
        if (itemStack != Item.AIR_ITEM && matcher(itemStack)) {
            takeAmount -= itemStack.count
            if (takeAmount < 0) {
                itemStack.count = itemStack.count - (takeAmount + itemStack.count)
                return true
            } else {
                setItem(i, null)
                if (takeAmount == 0) {
                    return true
                }
            }
        }
    }

    return false
}

/**
 * 获取背包中符合特定规则的物品的数量
 *
 * @return amount
 */
fun Inventory.countItem(matcher: (itemStack: Item) -> Boolean): Int {
    var amount = 0
    contents.values.forEach { itemStack ->
        if (itemStack != Item.AIR_ITEM && matcher(itemStack)) {
            amount += itemStack.count
        }
    }
    return amount
}
