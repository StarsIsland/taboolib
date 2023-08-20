package taboolib.platform.form.element

import cn.nukkit.Player
import cn.nukkit.form.element.ElementButton
import cn.nukkit.form.element.ElementButtonImageData
import org.jetbrains.annotations.NotNull
import java.util.*
import java.util.function.Consumer


class ResponseElementButton : ElementButton {
    private var clickedListener: Consumer<Player>? = null

    constructor(text: String?) : super(text) {}
    constructor(text: String?, image: ElementButtonImageData?) : super(text, image) {

    }

    fun onClicked(@NotNull clickedListener: Consumer<Player>): ResponseElementButton {
        this.clickedListener = Objects.requireNonNull(clickedListener)
        return this
    }

    fun callClicked(@NotNull player: Player): Boolean {
        if (clickedListener != null) {
            clickedListener?.accept(player)
            return true
        }
        return false
    }
}