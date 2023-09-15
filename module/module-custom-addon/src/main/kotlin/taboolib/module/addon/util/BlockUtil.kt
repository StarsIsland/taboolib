package taboolib.module.addon.util

import cn.nukkit.block.customblock.data.*
import cn.nukkit.math.Vector3
import cn.nukkit.math.Vector3f

fun getFaceablePermuatation(
    material: Materials,
    property: String = "direction",
    geometry: Geometry? = null,
    collisionBox: CollisionBox? = null,
    selectionBox: SelectionBox? = null
) : List<Permutation> {
    val permutations = mutableListOf<Permutation>()

    for(direction in 0..3) {
        val rotation = when(direction) {
            0 -> { Vector3f(0f, 0f, 0f)}
            1 -> { Vector3f(0f, 270f, 0f)}
            2 -> { Vector3f(0f ,180f, 0f)}
            3 -> { Vector3f(0f, 90f, 0f)}
            else -> { Vector3f(0f, 0f, 0f) }
        }

        val component = Component.builder()
            .materialInstances(material)
            .geometry(geometry)
            .collisionBox(collisionBox)
            .selectionBox(selectionBox)
            .transformation(Transformation(Vector3.ZERO, Vector3(1.0, 1.0, 1.0), rotation.asVector3()))
            .build()

        val condition = "query.block_property('$property') == $direction"

        permutations.add(Permutation(component, condition))
    }

    return permutations
}