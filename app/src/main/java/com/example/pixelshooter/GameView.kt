package com.example.pixelshooter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.pixelshooter.entities.Bullet
import com.example.pixelshooter.entities.Enemy
import com.example.pixelshooter.entities.Player
import com.example.pixelshooter.utils.GameConstants
import kotlin.random.Random

class GameView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {

    private var gameThread: GameThread? = null
    private var isGameRunning = false

    // Entidades del juego
    private lateinit var player: Player
    private val bullets = mutableListOf<Bullet>()
    private val enemies = mutableListOf<Enemy>()

    // Timing
    private var lastEnemySpawn = 0L

    // Paint para UI
    private val uiPaint = Paint().apply {
        color = 0xFFFFFFFF.toInt()
        textSize = 48f
        isAntiAlias = true
    }

    init {
        holder.addCallback(this)
        isFocusable = true
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        initializeGame()
        startGameThread()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        stopGameThread()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // Reposicionar jugador si cambia el tamaño
        if (::player.isInitialized) {
            player.x = width * GameConstants.PLAYER_START_X_RATIO
            player.y = height * GameConstants.PLAYER_START_Y_RATIO
        }
    }

    private fun initializeGame() {
        val startX = width * GameConstants.PLAYER_START_X_RATIO
        val startY = height * GameConstants.PLAYER_START_Y_RATIO
        player = Player(startX, startY)

        bullets.clear()
        enemies.clear()
        lastEnemySpawn = System.currentTimeMillis()
    }

    private fun startGameThread() {
        if (gameThread?.isAlive != true) {
            isGameRunning = true
            gameThread = GameThread()
            gameThread?.start()
        }
    }

    private fun stopGameThread() {
        isGameRunning = false
        gameThread?.join()
    }

    fun resume() {
        if (!isGameRunning) {
            startGameThread()
        }
    }

    fun pause() {
        stopGameThread()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                // Mover jugador a la posición Y del toque
                player.moveToY(event.y - player.height/2, height)

                // Disparar automáticamente mientras se toca
                player.shoot()?.let { bullet ->
                    bullets.add(bullet)
                }
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    private fun update(deltaTime: Float) {
        // Actualizar jugador
        player.update(deltaTime)

        // Actualizar balas
        bullets.forEach { it.update(deltaTime) }
        bullets.removeAll { it.isOffScreen(width, height) || !it.isActive }

        // Actualizar enemigos
        enemies.forEach { it.update(deltaTime) }
        enemies.removeAll { it.isOffScreen(width, height) || !it.isActive }

        // Spawnar enemigos
        spawnEnemies()

        // Verificar colisiones
        checkCollisions()
    }

    private fun spawnEnemies() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastEnemySpawn > GameConstants.ENEMY_SPAWN_RATE) {
            val enemyY = Random.nextFloat() * (height - GameConstants.ENEMY_SIZE)
            val enemy = Enemy(width.toFloat(), enemyY)
            enemies.add(enemy)
            lastEnemySpawn = currentTime
        }
    }

    private fun checkCollisions() {
        // Balas vs Enemigos
        for (bullet in bullets.toList()) {
            for (enemy in enemies.toList()) {
                if (bullet.intersects(enemy)) {
                    bullet.isActive = false
                    enemy.isActive = false
                    // Aquí puedes añadir efectos de explosión más tarde
                }
            }
        }

        // Jugador vs Enemigos
        for (enemy in enemies.toList()) {
            if (player.intersects(enemy)) {
                // Game Over o quitar vida
                enemy.isActive = false
                // Implementar sistema de vidas más tarde
            }
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        // Limpiar pantalla
        canvas.drawColor(GameConstants.COLOR_BACKGROUND)

        // Dibujar entidades
        player.draw(canvas)
        bullets.forEach { it.draw(canvas) }
        enemies.forEach { it.draw(canvas) }

        // Dibujar UI básica
        canvas.drawText("Enemies: ${enemies.size}", 50f, 100f, uiPaint)
        canvas.drawText("Bullets: ${bullets.size}", 50f, 160f, uiPaint)
    }

    inner class GameThread : Thread() {
        override fun run() {
            var lastTime = System.currentTimeMillis()

            while (isGameRunning) {
                val currentTime = System.currentTimeMillis()
                val deltaTime = (currentTime - lastTime).toFloat()
                lastTime = currentTime

                // Actualizar lógica del juego
                update(deltaTime)

                // Dibujar
                var canvas: Canvas? = null
                try {
                    canvas = holder.lockCanvas()
                    canvas?.let { draw(it) }
                } finally {
                    canvas?.let { holder.unlockCanvasAndPost(it) }
                }

                // Control de FPS
                val frameTime = System.currentTimeMillis() - currentTime
                val sleepTime = GameConstants.FRAME_TIME_MS - frameTime
                if (sleepTime > 0) {
                    sleep(sleepTime)
                }
            }
        }
    }
}