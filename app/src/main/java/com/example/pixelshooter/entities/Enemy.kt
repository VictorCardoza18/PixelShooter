package com.example.pixelshooter.entities

import android.graphics.Canvas
import com.example.pixelshooter.utils.GameConstants

class Enemy(x: Float, y: Float) : GameObject(
    x, y,
    GameConstants.ENEMY_SIZE,
    GameConstants.ENEMY_SIZE
) {

    init {
        paint.color = GameConstants.COLOR_ENEMY
    }

    override fun update(deltaTime: Float) {
        x -= GameConstants.ENEMY_SPEED * deltaTime / 1000f * 60f
    }

    override fun draw(canvas: Canvas) {
        // Dibujar enemigo como rectángulo con algunos detalles
        canvas.drawRect(x, y, x + width, y + height, paint)

        // Añadir detalles para que parezca más amenazante
        paint.color = 0xFFFFFFFF.toInt() // Blanco para detalles
        canvas.drawRect(x, y + height/2 - 2, x + 8, y + height/2 + 2, paint)
        paint.color = GameConstants.COLOR_ENEMY // Restaurar color
    }
}