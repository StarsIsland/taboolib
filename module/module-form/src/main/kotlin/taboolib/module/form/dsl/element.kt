package taboolib.module.form.dsl

import cn.nukkit.Player
import cn.nukkit.form.element.ElementButton
import cn.nukkit.form.element.ElementButtonImageData
import cn.nukkit.form.element.ElementDropdown
import cn.nukkit.form.element.ElementInput
import cn.nukkit.form.element.ElementLabel
import cn.nukkit.form.element.ElementSlider
import cn.nukkit.form.element.ElementStepSlider
import cn.nukkit.form.element.ElementToggle
import cn.nukkit.form.response.FormResponseData
import taboolib.module.form.element.ResponseElementButton
import kotlin.reflect.KMutableProperty1

@DslMarker
@Target(AnnotationTarget.TYPE, AnnotationTarget.CLASS)
annotation class FormDslMarker

inline fun AdvancedFormWindowSimpleAdapter.Button(init: (@FormDslMarker ResponseElementButton).() -> Unit) {
    this.addButton(ResponseElementButton("Button").apply(init))
}

inline fun AdvancedFormWindowSimpleAdapter.Button(icon: ElementButtonImageData, init: (@FormDslMarker ResponseElementButton).() -> Unit) {
    this.addButton(ResponseElementButton("Button", icon).apply(init))
}

inline fun AdvancedFormWindowSimpleAdapter.Button(text: String, init: (@FormDslMarker ResponseElementButton).() -> Unit) {
    this.addButton(ResponseElementButton(text).apply(init))
}

inline fun AdvancedFormWindowSimpleAdapter.Button(text: String, icon: ElementButtonImageData, init: (@FormDslMarker ResponseElementButton).() -> Unit) {
    this.addButton(ResponseElementButton(text, icon).apply(init))
}

inline fun <T : FormCustomResponseModel> AdvancedFormWindowCustomAdapter<T>.Dropdown(bind: KMutableProperty1<T, FormResponseData>, init: (@FormDslMarker ElementDropdown).() -> Unit) {
    this.bindDropdown(ElementDropdown("").apply(init), bind)
}

inline fun <T : FormCustomResponseModel> AdvancedFormWindowCustomAdapter<T>.Input(bind: KMutableProperty1<T, String>, init: (@FormDslMarker ElementInput).() -> Unit) {
    this.bindInput(ElementInput("").apply(init), bind)
}

inline fun <T : FormCustomResponseModel> AdvancedFormWindowCustomAdapter<T>.Toggle(bind: KMutableProperty1<T, Boolean>, init: (@FormDslMarker ElementToggle).() -> Unit) {
    this.bindToggle(ElementToggle("").apply(init), bind)
}

fun <T> AdvancedFormWindowCustomAdapter<T>.Label(text: String) {
    this.addElement(ElementLabel(text))
}

inline fun <T : FormCustomResponseModel> AdvancedFormWindowCustomAdapter<T>.Slider(bind: KMutableProperty1<T, Float>, init: (@FormDslMarker ElementSlider).() -> Unit) {
    this.bindSlider(ElementSlider("", 0F, 10F, 1).apply(init), bind)
}

inline fun <T : FormCustomResponseModel> AdvancedFormWindowCustomAdapter<T>.StepSlider(bind: KMutableProperty1<T, FormResponseData>, init: (@FormDslMarker ElementStepSlider).() -> Unit) {
    this.bindStepSlider(ElementStepSlider("").apply(init), bind)
}

// 统一默认值字段名称
var ElementToggle.default: Boolean
    get() = this.isDefaultValue
    set(value) {
        this.isDefaultValue = value
    }

var ElementInput.default: String
    get() = this.defaultText
    set(value) {
        this.defaultText = value
    }

var ElementSlider.default: Float
    get() = this.defaultValue
    set(value) {
        this.defaultValue = value
    }


fun ResponseElementButton.onPlayerClick(click: (@FormDslMarker Player).() -> Unit) {
    this.onClicked(click)
}

inline fun ElementButton.icon(init: (@FormDslMarker ElementButtonImageData).() -> Unit) {
    val icon = ElementButtonImageData("", "")
    init(icon)
    this.addImage(icon)
}

fun <T> ElementDropdown.option(vararg options: T) {
    for (option in options) {
        if (option is String) {
            this.addOption(option)
        }
        if (option is Pair<*, *>) {
            this.addOption(option.first as String, option.second as Boolean)
        }
    }
}

class OptionList(val options: MutableList<String> = mutableListOf(), var default: String? = null) {

    // - "xxx" 添加一个 option
    operator fun String.unaryMinus() {
        options.add(this)
    }

    // + "xxx"  默认
    operator fun String.unaryPlus() {
        default = this
        options.add(this)
    }
}

fun ElementDropdown.option(init: (@FormDslMarker OptionList).() -> Unit) {
    OptionList().apply {
        init()
        options.forEach {
            this@option.addOption(it, it == default)
        }
    }
}

fun <T> ElementStepSlider.option(vararg options: T) {
    for (option in options) {
        if (option is String) {
            this.addStep(option, false)
        }
        if (option is Pair<*, *>) {
            this.addStep(option.first as String, option.second as Boolean)
        }
    }
}

fun ElementStepSlider.option(init: (@FormDslMarker OptionList).() -> Unit) {
    OptionList().apply {
        init()
        options.forEach {
            this@option.addStep(it, it == default)
        }
    }
}