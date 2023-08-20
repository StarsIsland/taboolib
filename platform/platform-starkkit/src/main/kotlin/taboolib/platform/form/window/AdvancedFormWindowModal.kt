package taboolib.platform.form.window

import cn.nukkit.Player
import cn.nukkit.form.window.FormWindow
import cn.nukkit.form.window.FormWindowModal
import com.google.gson.Gson
import org.jetbrains.annotations.NotNull
import java.util.*
import java.util.function.Consumer


open class AdvancedFormWindowModal(title: String?, content: String?, trueButtonText: String?, falseButtonText: String?) :
    FormWindowModal(title, content, trueButtonText, falseButtonText) {
    protected var buttonTrueClickedListener: Consumer<Player>? = null
    protected var buttonFalseClickedListener: Consumer<Player>? = null
    protected var formClosedListener: Consumer<Player>? = null
    fun onClickedTrue(@NotNull listener: Consumer<Player>): AdvancedFormWindowModal {
        buttonTrueClickedListener = Objects.requireNonNull(listener)
        return this
    }

    fun onClickedFalse(@NotNull listener: Consumer<Player>): AdvancedFormWindowModal {
        buttonFalseClickedListener = Objects.requireNonNull(listener)
        return this
    }

    fun onClosed(@NotNull listener: Consumer<Player>): AdvancedFormWindowModal {
        formClosedListener = Objects.requireNonNull(listener)
        return this
    }

    fun showToPlayer(@NotNull player: Player) {
        player.showFormWindow(this)
    }

    protected fun callClickedTrue(@NotNull player: Player) {
        buttonTrueClickedListener?.accept(player)
    }

    protected fun callClickedFalse(@NotNull player: Player) {
        buttonFalseClickedListener?.accept(player)
    }

    protected fun callClosed(@NotNull player: Player) {
        formClosedListener?.accept(player)
    }

    override fun getJSONData(): String {
        return GSON.toJson(this, FormWindowModal::class.java)
    }

    companion object {
        val GSON: Gson = Gson()

        fun onEvent(@NotNull formWindow: FormWindow, @NotNull player: Player): Boolean {
            if (formWindow is AdvancedFormWindowModal) {
                val advancedFormWindowModal = formWindow
                if (advancedFormWindowModal.wasClosed() || advancedFormWindowModal.response == null) {
                    advancedFormWindowModal.callClosed(player)
                } else {
                    if (advancedFormWindowModal.response.clickedButtonId == 0) {
                        advancedFormWindowModal.callClickedTrue(player)
                    } else {
                        advancedFormWindowModal.callClickedFalse(player)
                    }
                }
                return true
            }
            return false
        }
    }
}