package com.example.pixelshooter.entities

import android.graphics.Canvas
import com.example.pixelshooter.utils.GameConstants

class Bullet(
    x: Float,
    y: Float,
    private val isPlayerBullet: Boolean
) : GameObject(
    x, y,
    GameConstants.BULLET_WIDTH,
    GameConstants.BULLET_HEIGHT
) {

    init {
        paint.color = GameConstants.COLOR_BULLET
    }

    override fun update(deltaTime: Float) {
        val speed = GameConstants.BULLET_SPEED * deltaTime / 1000f
        if (isPlayerBullet) {
            x += speed * 60f // Mover hacia la derecha
        } else {
            x -= speed * 60f // Mover hacia la izquierda (balas enemigas)
        }
    }

    override fun draw(canvas: Canvas) {
        canvas.drawRect(x, y, x + width, y + height, paint)
    }
}