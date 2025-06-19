package com.example.pixelshooter.entities

import android.graphics.Canvas
import com.example.pixelshooter.utils.GameConstants

class Player(x: Float, y: Float) : GameObject(
    x, y,
    GameConstants.PLAYER_SIZE,
    GameConstants.PLAYER_SIZE
) {
    private var targetY = y
    private var lastShotTime = 0L

    init {
        paint.color = GameConstants.COLOR_PLAYER
    }

    override fun update(deltaTime: Float) {
        // Movimiento suave hacia la posición objetivo
        val diff = targetY - y
        if (Math.abs(diff) > 1f) {
            y += diff * GameConstants.PLAYER_SPEED * deltaTime / 1000f
        }
    }

    override fun draw(canvas: Canvas) {
        // Dibujar como un rectángulo simple por ahora (luego será sprite)
        canvas.drawRect(x, y, x + width, y + height, paint)

        // Añadir algunos detalles para que parezca más una nave
        paint.color = 0xFFFFFFFF.toInt() // Blanco para detalles
        canvas.drawRect(x + width - 8, y + height/2 - 2, x + width, y + height/2 + 2, paint)
        paint.color = GameConstants.COLOR_PLAYER // Restaurar color
    }

    fun moveToY(newY: Float, screenHeight: Int) {
        targetY = newY.coerceIn(0f, screenHeight - height)
    }

    fun canShoot(): Boolean {
        return System.currentTimeMillis() - lastShotTime > GameConstants.BULLET_FIRE_RATE
    }

    fun shoot(): Bullet? {
        return if (canShoot()) {
            lastShotTime = System.currentTimeMillis()
            Bullet(x + width, y + height/2 - GameConstants.BULLET_HEIGHT/2, true)
        } else null
    }
}