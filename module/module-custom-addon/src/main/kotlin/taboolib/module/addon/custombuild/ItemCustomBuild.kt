package taboolib.module.addon.custombuild

import cn.nukkit.nbt.tag.CompoundTag
import cn.nukkit.nbt.tag.ListTag

fun blockPlacer(nbt: CompoundTag, targetBlock: String = "") {
    val compoundTag = CompoundTag()

    compoundTag.putString("block", targetBlock)
        .putList("use_on", ListTag())

    nbt.getCompound("components")
        .getCompound("item_properties")
        .putCompound("minecraft:block_placer", compoundTag)
}

fun canDestroyBlockInCreative(nbt: CompoundTag, can: Boolean) {
    nbt.getCompound("components")
        .getCompound("item_properties")
        .putBoolean("can_destroy_in_creative", can)
}

fun interactButton(nbt: CompoundTag, buttonText: String) {
    nbt.getCompound("components")
        .getCompound("item_properties")
        .putString("minecraft:interact_button", buttonText)
}

fun fuel(nbt: CompoundTag, time: Int) {
    nbt.getCompound("components")
        .getCompound("item_properties")
        .putCompound("minecraft:fuel", CompoundTag().putInt("duration", time))
}

