package com.example.mineskineditorlibgdx.utils

import com.badlogic.gdx.graphics.Mesh
import com.badlogic.gdx.graphics.VertexAttributes
import com.badlogic.gdx.math.*
import com.badlogic.gdx.math.collision.Ray
import com.example.mineskineditorlibgdx.model.ModelTriangle

object RaycastGeometry {

    private val v0 = Vector3()
    private val v1 = Vector3()
    private val v2 = Vector3()
    private val v3 = Vector3()

    private val p0 = Vector2()
    private val p1 = Vector2()

    fun getModelTriangles(
        indices: ShortArray,
        vertices: FloatArray,
        mesh: Mesh
    ): List<ModelTriangle> {
        val triangles = mutableListOf<ModelTriangle>()
        for (i in indices.indices step 3) {
            val stride = mesh.vertexSize / 4
            val vertexIndex1 = indices[i].toInt() * stride
            val vertexIndex2 = indices[i + 1].toInt() * stride
            val vertexIndex3 = indices[i + 2].toInt() * stride

            val vertex1 = Vector3(
                vertices[vertexIndex1],
                vertices[vertexIndex1 + 1],
                vertices[vertexIndex1 + 2]
            )
            val vertex2 = Vector3(
                vertices[vertexIndex2],
                vertices[vertexIndex2 + 1],
                vertices[vertexIndex2 + 2]
            )
            val vertex3 = Vector3(
                vertices[vertexIndex3],
                vertices[vertexIndex3 + 1],
                vertices[vertexIndex3 + 2]
            )

            val vertexAttributes = mesh.vertexAttributes
            val uvOffset =
                vertexAttributes.findByUsage(VertexAttributes.Usage.TextureCoordinates).offset / 4

            val uv1 = Vector2(
                vertices[vertexIndex1 + uvOffset],
                vertices[vertexIndex1 + uvOffset + 1]
            )
            val uv2 = Vector2(
                vertices[vertexIndex2 + uvOffset],
                vertices[vertexIndex2 + uvOffset + 1]
            )
            val uv3 = Vector2(
                vertices[vertexIndex3 + uvOffset],
                vertices[vertexIndex3 + uvOffset + 1]
            )

            val uvs = arrayOf(uv1, uv2, uv3)

            val triangle = ModelTriangle(vertex1, vertex2, vertex3, uvs)
            triangles.add(triangle)
        }
        return triangles
    }

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

    fun getTriangleCentroid(triangle: ModelTriangle): Vector3 {
        v0.set(triangle.v1)
        v0.add(triangle.v2)
        v0.add(triangle.v3)
        v0.scl(1f / 3f)
        return v0
    }

    fun intersectRayTriangle(
        ray: Ray,
        t1: Vector3,
        t2: Vector3,
        t3: Vector3,
        intersection: Vector3?
    ): Boolean {
        val p = Plane(Vector3(), 0f)

        val edge1 = v0.set(t2).sub(t1)
        val edge2 = v1.set(t3).sub(t1)

        val pvec = v2.set(ray.direction).crs(edge2)
        var det = edge1.dot(pvec)
        if (MathUtils.isZero(det)) {
            p.set(t1, t2, t3)
            if (p.testPoint(ray.origin) == Plane.PlaneSide.OnPlane && isPointInTriangle(
                    ray.origin,
                    t1,
                    t2,
                    t3
                )
            ) {
                intersection?.set(ray.origin)
                return true
            }
            return false
        }

        det = 1f / det
        val tvec = v3.set(ray.origin).sub(t1)
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

    fun isPointInTriangle(point: Vector3, t1: Vector3, t2: Vector3, t3: Vector3): Boolean {
        v0.set(t1).sub(point)
        v1.set(t2).sub(point)
        v2.set(t3).sub(point)

        v1.crs(v2)
        v2.crs(v0)

        if (v1.dot(v2) < 0f) return false
        v0.crs(v2.set(t2).sub(point))
        return v1.dot(v0) >= 0f
    }

}
