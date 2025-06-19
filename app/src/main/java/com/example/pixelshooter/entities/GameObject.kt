package com.example.pixelshooter.entities

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

abstract class GameObject(
    var x: Float,
    var y: Float,
    var width: Float,
    var height: Float
) {
    var isActive = true
    protected val paint = Paint().apply {
        isAntiAlias = false // Para pixel art perfecto
    }

    // Rectángulo para colisiones
    val bounds: RectF
        get() = RectF(x, y, x + width, y + height)

    abstract fun update(deltaTime: Float)
    abstract fun draw(canvas: Canvas)

    // Verificar si está fuera de pantalla
    fun isOffScreen(screenWidth: Int, screenHeight: Int): Boolean {
        return x + width < 0 || x > screenWidth || y + height < 0 || y > screenHeight
    }

    // Verificar colisión con otro objeto
    fun intersects(other: GameObject): Boolean {
        return bounds.intersect(other.bounds)
    }
}