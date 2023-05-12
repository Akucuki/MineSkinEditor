package com.example.mineskineditorlibgdx.features.libgdx.core.utils

import com.badlogic.gdx.math.GeometryUtils
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Plane
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.Ray
import com.example.mineskineditorlibgdx.model.ModelTriangle

object RaycastGeometry {

    private val v0 = Vector3()
    private val v1 = Vector3()
    private val v2 = Vector3()
    private val v3 = Vector3()

    private val p0 = Vector2()
    private val p1 = Vector2()

    private fun calculateBarycoord3D(
        point: Vector3,
        a: Vector3,
        b: Vector3,
        c: Vector3
    ): Vector3 {
        v0.set(b).sub(a)
        v1.set(c).sub(a)
        v2.set(point).sub(a)

        val d00 = v0.dot(v0)
        val d01 = v0.dot(v1)
        val d11 = v1.dot(v1)
        val d20 = v2.dot(v0)
        val d21 = v2.dot(v1)

        val denom = d00 * d11 - d01 * d01
        v3.set(0f, 0f, 0f)
        v3.x = (d11 * d20 - d01 * d21) / denom
        v3.y = (d00 * d21 - d01 * d20) / denom
        v3.z = 1.0f - v3.x - v3.y

        return v3
    }

    fun getIntersectionUV(
        intersectionPoint: Vector3,
        triangle: ModelTriangle
    ): Vector2 {
        val barycentricCoords = calculateBarycoord3D(
            intersectionPoint,
            triangle.v1,
            triangle.v2,
            triangle.v3
        )

        require(
            GeometryUtils.barycoordInsideTriangle(
                p0.set(
                    barycentricCoords.x,
                    barycentricCoords.y
                )
            )
        ) {
            "Barycentric coordinates are outside the triangle!"
        }

        val uv1 = triangle.uvs[0]
        val uv2 = triangle.uvs[1]
        val uv3 = triangle.uvs[2]

        return p1.set(
            barycentricCoords.z * uv1.x + barycentricCoords.x * uv2.x + barycentricCoords.y * uv3.x,
            barycentricCoords.z * uv1.y + barycentricCoords.x * uv2.y + barycentricCoords.y * uv3.y
        )
    }

    fun intersectRayTriangle(
        ray: Ray,
        modelTriangle: ModelTriangle,
        intersection: Vector3?
    ): Boolean {
        val p = Plane(Vector3(), 0f)

        val edge1 = v0.set(modelTriangle.v2).sub(modelTriangle.v1)
        val edge2 = v1.set(modelTriangle.v3).sub(modelTriangle.v1)

        val pvec = v2.set(ray.direction).crs(edge2)
        var det = edge1.dot(pvec)
        if (MathUtils.isZero(det)) {
            p.set(modelTriangle.v1, modelTriangle.v2, modelTriangle.v3)
            if (
                p.testPoint(ray.origin) == Plane.PlaneSide.OnPlane &&
                modelTriangle.hasPoint(ray.origin)
            ) {
                intersection?.set(ray.origin)
                return true
            }
            return false
        }

        det = 1f / det
        val tvec = v3.set(ray.origin).sub(modelTriangle.v1)
        val u = tvec.dot(pvec) * det
        if (u < 0f || u > 1f) return false

        val qvec = tvec.crs(edge1)
        val v = ray.direction.dot(qvec) * det
        if (v < 0f || u + v > 1f) return false

        val t = edge2.dot(qvec) * det
        if (t < 0f) return false

        if (intersection != null) {
            if (t <= MathUtils.FLOAT_ROUNDING_ERROR) {
                intersection.set(ray.origin)
            } else {
                ray.getEndPoint(intersection, t)
            }
        }
        return true
    }

}
