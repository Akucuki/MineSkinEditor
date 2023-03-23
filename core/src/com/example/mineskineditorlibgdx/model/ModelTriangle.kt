package com.example.mineskineditorlibgdx.model

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

data class ModelTriangle(
    val v1: Vector3,
    val v2: Vector3,
    val v3: Vector3,
    val uvs: Array<Vector2>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ModelTriangle

        if (v1 != other.v1) return false
        if (v2 != other.v2) return false
        if (v3 != other.v3) return false
        if (!uvs.contentEquals(other.uvs)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = v1.hashCode()
        result = 31 * result + v2.hashCode()
        result = 31 * result + v3.hashCode()
        result = 31 * result + uvs.contentHashCode()
        return result
    }
}