package taboolib.platform.form

import cn.nukkit.Player
import cn.nukkit.event.player.PlayerFormRespondedEvent
import cn.nukkit.event.player.PlayerServerSettingsRequestEvent
import cn.nukkit.event.player.PlayerSettingsRespondedEvent
import cn.nukkit.network.protocol.ServerSettingsResponsePacket
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.submit
import taboolib.platform.form.window.AdvancedFormWindowCustom
import taboolib.platform.form.window.AdvancedFormWindowModal
import taboolib.platform.form.window.AdvancedFormWindowSimple


object WindowListener {

    @SubscribeEvent
    fun onPlayerFormResponded(event: PlayerFormRespondedEvent) {
        if (AdvancedFormWindowSimple.onEvent(event.window, event.player)) {
            return
        }
        if (AdvancedFormWindowModal.onEvent(event.window, event.player)) {
            return
        }
        AdvancedFormWindowCustom.onEvent(event.window, event.player)
    }

    @SubscribeEvent
    fun onPlayerSettingsResponded(event: PlayerSettingsRespondedEvent) {
        AdvancedFormWindowCustom.onEvent(event.window, event.player)
    }

    @SubscribeEvent
    fun onSettingsRequest(event: PlayerServerSettingsRequestEvent) {
        val player: Player = event.player
        val map = HashMap(event.settings)
        event.settings = HashMap()
        //必须延迟一下，否则客户端不显示
        submit(delay = 20) {
            for (entry in map.entries) {
                val pk = ServerSettingsResponsePacket()
                pk.formId = entry.key
                pk.data = entry.value.jsonData
                player.dataPacket(pk)
            }
        }
    }
}