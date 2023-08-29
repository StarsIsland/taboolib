package taboolib.module.form.window

import cn.nukkit.Player
import cn.nukkit.form.element.ElementButton
import cn.nukkit.form.window.FormWindow
import cn.nukkit.form.window.FormWindowSimple
import com.google.gson.Gson
import org.jetbrains.annotations.NotNull
import taboolib.module.form.element.ResponseElementButton
import java.util.*
import java.util.function.BiConsumer
import java.util.function.Consumer


open class AdvancedFormWindowSimple : FormWindowSimple {

    protected var buttonClickedListener: BiConsumer<ElementButton, Player>? = null
    protected var formClosedListener: Consumer<Player>? = null

    @JvmOverloads
    constructor(title: String, content: String? = "") : super(title, content) {
    }

    constructor(title: String, content: String, buttons: List<ElementButton>) : super(title, content, buttons) {}

    fun addButton(text: String, listener: Consumer<Player>): AdvancedFormWindowSimple {
        this.addButton(ResponseElementButton(text).onClicked(listener))
        return this
    }

    fun onClicked(@NotNull listener: BiConsumer<ElementButton, Player>): AdvancedFormWindowSimple {
        buttonClickedListener = Objects.requireNonNull(listener)
        return this
    }

    fun onClosed(@NotNull listener: Consumer<Player>): AdvancedFormWindowSimple {
        formClosedListener = Objects.requireNonNull(listener)
        return this
    }

    fun showToPlayer(@NotNull player: Player) {
        player.showFormWindow(this)
    }

    protected fun callClicked(@NotNull elementButton: ElementButton, @NotNull player: Player) {
        buttonClickedListener?.accept(elementButton, player)
    }

    protected fun callClosed(@NotNull player: Player) {
        formClosedListener?.accept(player)
    }

    override fun getJSONData(): String {
        return GSON.toJson(this, FormWindowSimple::class.java)
    }

    companion object {
        val GSON: Gson = Gson()

        fun onEvent(@NotNull formWindow: FormWindow, @NotNull player: Player): Boolean {
            if (formWindow is AdvancedFormWindowSimple) {
                val advancedFormWindowSimple = formWindow
                if (advancedFormWindowSimple.wasClosed() || advancedFormWindowSimple.response == null) {
                    advancedFormWindowSimple.callClosed(player)
                } else {
                    val elementButton = advancedFormWindowSimple.response.clickedButton
                    if (elementButton is ResponseElementButton && (elementButton as ResponseElementButton).callClicked(
                            player
                        )
                    ) {
                        return true
                    } else {
                        advancedFormWindowSimple.callClicked(elementButton, player)
                    }
                }
                return true
            }
            return false
        }
    }
}