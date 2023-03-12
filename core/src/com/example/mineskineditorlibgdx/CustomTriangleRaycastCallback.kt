package com.example.mineskineditorlibgdx

import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback
import com.badlogic.gdx.physics.bullet.collision.LocalRayResult
import com.badlogic.gdx.physics.bullet.collision.btTriangleRaycastCallback
import com.badlogic.gdx.physics.bullet.linearmath.btVector3

class CustomTriangleRaycastCallback(
    from: Vector3,
    to: Vector3
) : ClosestRayResultCallback(from, to) {

    var triangleIndex = -1

    override fun addSingleResult(rayResult: LocalRayResult, normalInWorldSpace: Boolean): Float {
        val hitFraction = super.addSingleResult(rayResult, normalInWorldSpace)
        if (rayResult.localShapeInfo != null) {
            triangleIndex = rayResult.localShapeInfo.triangleIndex
        }
        return hitFraction
    }
}