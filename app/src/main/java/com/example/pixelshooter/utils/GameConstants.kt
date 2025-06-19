package com.example.pixelshooter.utils

object GameConstants {
    // Configuración de pantalla
    const val TARGET_FPS = 60
    const val FRAME_TIME_MS = 1000 / TARGET_FPS

    // Configuración del jugador
    const val PLAYER_SPEED = 8f
    const val PLAYER_SIZE = 64f
    const val PLAYER_START_X_RATIO = 0.1f // 10% desde la izquierda
    const val PLAYER_START_Y_RATIO = 0.5f // Centro vertical

    // Configuración de balas
    const val BULLET_SPEED = 12f
    const val BULLET_WIDTH = 8f
    const val BULLET_HEIGHT = 16f
    const val BULLET_FIRE_RATE = 300 // milisegundos entre disparos

    // Configuración de enemigos
    const val ENEMY_SPEED = 4f
    const val ENEMY_SIZE = 48f
    const val ENEMY_SPAWN_RATE = 2000 // milisegundos entre spawns

    // Colores (para pixel art temporal)
    const val COLOR_PLAYER = 0xFF00FF00.toInt() // Verde
    const val COLOR_ENEMY = 0xFFFF0000.toInt()  // Rojo
    const val COLOR_BULLET = 0xFFFFFF00.toInt() // Amarillo
    const val COLOR_BACKGROUND = 0xFF000022.toInt() // Azul oscuro
}