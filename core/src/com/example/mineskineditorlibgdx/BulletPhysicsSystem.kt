package com.example.mineskineditorlibgdx

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g3d.Model
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.bullet.DebugDrawer
import com.badlogic.gdx.physics.bullet.collision.*
import com.badlogic.gdx.physics.bullet.dynamics.*
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw
import com.badlogic.gdx.utils.Disposable

class BulletPhysicsSystem : Disposable {

    private var dynamicsWorld: btDynamicsWorld? = null
    private var collisionConfig: btCollisionConfiguration? = null
    private var dispatcher: btDispatcher? = null
    private var broadphase: btBroadphaseInterface? = null
    private var constraintSolver: btConstraintSolver? = null
    private var debugDrawer: DebugDrawer? = null

    private var fixedTimeStep = 1 / 60f

    private var lastRayFrom = Vector3()
    private var lastRayTo = Vector3()
    private var rayColor = Vector3(1f, 0f, 1f)

    init {
        collisionConfig = btDefaultCollisionConfiguration()
        dispatcher = btCollisionDispatcher(collisionConfig)
        broadphase = btDbvtBroadphase()
        constraintSolver = btSequentialImpulseConstraintSolver()
        dynamicsWorld = btDiscreteDynamicsWorld(
            dispatcher,
            broadphase,
            constraintSolver,
            collisionConfig
        )
        debugDrawer = DebugDrawer()
        debugDrawer?.debugMode = btIDebugDraw.DebugDrawModes.DBG_DrawWireframe
        dynamicsWorld?.debugDrawer = debugDrawer
    }

    fun update(delta: Float) {
        dynamicsWorld?.stepSimulation(delta, 5, fixedTimeStep)
    }

    fun render(camera: Camera) {
        debugDrawer?.begin(camera)
        debugDrawer?.drawLine(lastRayFrom, lastRayTo, rayColor)
        dynamicsWorld?.debugDrawWorld()
        debugDrawer?.end()
    }

    fun addBody(body: btRigidBody) {
        dynamicsWorld?.addRigidBody(body)
    }

    fun raycast(from: Vector3, to: Vector3, callback: RayResultCallback) {
        lastRayFrom.set(from).sub(0f, 0f, 0f)
        dynamicsWorld?.rayTest(from, to, callback)
        if (callback.hasHit() && callback is ClosestRayResultCallback) {
            lastRayTo.set(from)
            lastRayTo.lerp(to, callback.closestHitFraction)
        } else {
            lastRayTo.set(to)
        }
    }

    override fun dispose() {
        collisionConfig?.dispose()
        dispatcher?.dispose()
        broadphase?.dispose()
        constraintSolver?.dispose()
        dynamicsWorld?.dispose()
    }
}
