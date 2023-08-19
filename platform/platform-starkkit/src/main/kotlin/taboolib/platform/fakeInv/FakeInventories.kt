package taboolib.platform.fakeInv

import cn.nukkit.block.Block
import cn.nukkit.blockentity.BlockEntity
import cn.nukkit.event.inventory.InventoryTransactionEvent
import cn.nukkit.inventory.InventoryType
import cn.nukkit.inventory.transaction.action.SlotChangeAction
import taboolib.common.platform.event.SubscribeEvent
import taboolib.platform.fakeInv.block.DoubleFakeBlock
import taboolib.platform.fakeInv.block.FakeBlock
import taboolib.platform.fakeInv.block.SingleFakeBlock
import java.util.*


object FakeInventories {

    @SubscribeEvent
    fun onInventoryTransaction(event: InventoryTransactionEvent) {
        event.transaction.actions.forEach { action ->
            if(action is SlotChangeAction && action.inventory is CustomInventory) {
                (action.inventory as CustomInventory).handle(action.slot, action.sourceItem, event);
            }
        }
    }


    private val FAKE_BLOCKS: MutableMap<InventoryType, FakeBlock> = EnumMap(
        InventoryType::class.java
    )

    init {
        FAKE_BLOCKS[InventoryType.CHEST] =
            SingleFakeBlock(Block.CHEST, BlockEntity.CHEST)
        FAKE_BLOCKS[InventoryType.ENDER_CHEST] =
            SingleFakeBlock(Block.ENDER_CHEST, BlockEntity.ENDER_CHEST)
        FAKE_BLOCKS[InventoryType.DOUBLE_CHEST] =
            DoubleFakeBlock(Block.CHEST, BlockEntity.CHEST)
        FAKE_BLOCKS[InventoryType.FURNACE] =
            SingleFakeBlock(Block.FURNACE, BlockEntity.FURNACE)
        FAKE_BLOCKS[InventoryType.WORKBENCH] =
            SingleFakeBlock(Block.CRAFTING_TABLE, InventoryType.WORKBENCH.defaultTitle)
        FAKE_BLOCKS[InventoryType.BREWING_STAND] =
            SingleFakeBlock(Block.BREWING_STAND_BLOCK, BlockEntity.BREWING_STAND)
        FAKE_BLOCKS[InventoryType.DISPENSER] =
            SingleFakeBlock(Block.DISPENSER, InventoryType.DISPENSER.defaultTitle)
        FAKE_BLOCKS[InventoryType.DROPPER] =
            SingleFakeBlock(Block.DROPPER, InventoryType.DROPPER.defaultTitle)
        FAKE_BLOCKS[InventoryType.HOPPER] =
            SingleFakeBlock(Block.HOPPER_BLOCK, BlockEntity.HOPPER)
        FAKE_BLOCKS[InventoryType.SHULKER_BOX] =
            SingleFakeBlock(Block.SHULKER_BOX, BlockEntity.SHULKER_BOX)
    }

    fun getFakeBlock(inventoryType: InventoryType): FakeBlock {
        return FAKE_BLOCKS[inventoryType] ?: SingleFakeBlock(Block.CHEST, BlockEntity.CHEST)
    }

}