package com.example.mineskineditorlibgdx.model

import android.graphics.Bitmap
import com.example.mineskineditorlibgdx.R

enum class BodyPartType(val partNameStringId: Int) {
    HEAD(R.string.head),
    BODY(R.string.body),
    LEFT_ARM(R.string.left_arm),
    RIGHT_ARM(R.string.right_arm),
    LEFT_LEG(R.string.left_leg),
    RIGHT_LEG(R.string.right_leg);

    enum class FaceType(val faceNameStringId: Int) {
        TOP(R.string.top),
        RIGHT(R.string.right),
        FRONT(R.string.front),
        LEFT(R.string.left),
        BACK(R.string.back),
        BOTTOM(R.string.bottom);
    }
}

data class BodyPartFace(
    val type: BodyPartType.FaceType,
    val bitmap: Bitmap
)

data class BodyPart(
    val type: BodyPartType,
    val faces: List<BodyPartFace>
//    val top: Bitmap,
//    val right: Bitmap,
//    val front: Bitmap,
//    val left: Bitmap,
//    val back: Bitmap,
//    val bottom: Bitmap
)