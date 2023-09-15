package taboolib.module.ui.manager

import cn.nukkit.Server;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import org.jetbrains.annotations.NotNull;
import taboolib.module.ui.fakeInv.CustomInventory
import java.util.function.Supplier

class UIManager(@NotNull uiInventoryGenerator: Supplier<CustomInventory>, @NotNull uiInventoryUpdater: UIUpdater) {

    private val uiInventoryGenerator: Supplier<CustomInventory>
    private val uiInventoryUpdater: UIUpdater
    private val displayInventories: Object2IntMap<CustomInventory> = Object2IntOpenHashMap()

    interface UIUpdater {
        fun update(inventory: CustomInventory?, immediately: Boolean)
    }

    init {
        this.uiInventoryGenerator = uiInventoryGenerator
        this.uiInventoryUpdater = uiInventoryUpdater
    }

    fun open(): CustomInventory {
        val inventory: CustomInventory = uiInventoryGenerator.get()
        displayInventories.put(inventory, Server.getInstance().tick)
        return inventory
    }

    @JvmOverloads
    fun update(immediately: Boolean = false) {
        val currentTick: Int = Server.getInstance().tick
        val iterator: ObjectIterator<Object2IntMap.Entry<CustomInventory>> =
            displayInventories.object2IntEntrySet().iterator()
        while (iterator.hasNext()) {
            val entry: Object2IntMap.Entry<CustomInventory> = iterator.next()
            val each: CustomInventory = entry.key
            val createTick: Int = entry.intValue
            if (currentTick - createTick > 3 && each.getViewers().size === 0) {
                iterator.remove()
                continue
            }
            uiInventoryUpdater.update(each, immediately)
        }
    }

    val isUIDisplaying: Boolean
        get() = !displayInventories.isEmpty()
}