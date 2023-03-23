package com.example.mineskineditorlibgdx.utils

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute

fun ModelInstance.setFirstMaterialTexture(texture: Texture) {
    materials.first().set(TextureAttribute.createDiffuse(texture))
}