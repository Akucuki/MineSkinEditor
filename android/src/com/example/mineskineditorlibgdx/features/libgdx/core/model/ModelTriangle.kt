package com.example.mineskineditorlibgdx.model

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

data class ModelTriangle(
    val v1: Vector3,
    val v2: Vector3,
    val v3: Vector3,
    val uvs: Array<Vector2>
) {
    private val tmpV0 = Vector3()
    private val tmpV1 = Vector3()
    private val tmpV2 = Vector3()
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

    fun centroid(): Vector3 {
        tmpV0.set(v1)
        tmpV0.add(v2)
        tmpV0.add(v3)
        tmpV0.scl(1f / 3f)
        return tmpV0
    }

    fun hasPoint(point: Vector3): Boolean {
        tmpV0.set(v1).sub(point)
        tmpV1.set(v2).sub(point)
        tmpV2.set(v3).sub(point)

        tmpV1.crs(tmpV2)
        tmpV2.crs(tmpV0)

        if (tmpV1.dot(tmpV2) < 0f) return false
        tmpV0.crs(tmpV2.set(v2).sub(point))
        return tmpV1.dot(tmpV0) >= 0f
    }

    override fun toString() = "${this.javaClass.simpleName}($v1, $v2, $v3)"
}