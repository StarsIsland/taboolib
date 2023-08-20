package taboolib.platform.form.window

import cn.nukkit.Player
import cn.nukkit.form.element.Element
import cn.nukkit.form.element.ElementButtonImageData
import cn.nukkit.form.response.FormResponseCustom
import cn.nukkit.form.window.FormWindow
import cn.nukkit.form.window.FormWindowCustom
import com.google.gson.Gson
import org.jetbrains.annotations.NotNull
import java.util.*
import java.util.function.BiConsumer
import java.util.function.Consumer


open class AdvancedFormWindowCustom : FormWindowCustom {

    protected var buttonClickedListener: BiConsumer<FormResponseCustom, Player>? = null
    protected var formClosedListener: Consumer<Player>? = null

    constructor(title: String) : super(title) {}
    constructor(title: String, contents: List<Element>) : super(title, contents) {}
    constructor(title: String, contents: List<Element>, icon: String) : super(title, contents, icon) {}
    constructor(title: String, contents: List<Element>, icon: ElementButtonImageData?) : super(
        title,
        contents,
        icon
    ) {
    }

    fun onResponded(@NotNull listener: BiConsumer<FormResponseCustom, Player>): AdvancedFormWindowCustom {
        buttonClickedListener = listener
        return this
    }

    fun onClosed(@NotNull listener: Consumer<Player>): AdvancedFormWindowCustom {
        formClosedListener = Objects.requireNonNull(listener)
        return this
    }

    fun showToPlayer(@NotNull player: Player) {
        player.showFormWindow(this)
    }

    protected fun callResponded(@NotNull formResponseCustom: FormResponseCustom, @NotNull player: Player) {
        buttonClickedListener?.accept(formResponseCustom, player)
    }

    protected fun callClosed(@NotNull player: Player) {
        formClosedListener?.accept(player)
    }

    override fun getJSONData(): String {
        return GSON.toJson(this, FormWindowCustom::class.java)
    }

    companion object {

        val GSON: Gson = Gson()

        fun onEvent(@NotNull formWindow: FormWindow, @NotNull player: Player): Boolean {
            if (formWindow is AdvancedFormWindowCustom) {
                if (formWindow.wasClosed() || formWindow.response == null) {
                    formWindow.callClosed(player)
                } else {
                    formWindow.callResponded(formWindow.response, player)
                }
                return true
            }
            return false
        }
    }
}