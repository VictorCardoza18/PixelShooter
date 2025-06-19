package com.example.pixelshooter

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.pixelshooter.ui.theme.PixelShooterTheme

class MainActivity : ComponentActivity() {
    lateinit var gameView: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configurar pantalla completa inmersiva
        setupFullscreen()

        // Mantener pantalla encendida
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        setContent {
            PixelShooterTheme {
                GameScreen()
            }
        }
    }

    private fun setupFullscreen() {
        enableEdgeToEdge()

        // Ocultar barras de sistema de forma inmersiva
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }

    override fun onResume() {
        super.onResume()
        if (::gameView.isInitialized) {
            gameView.resume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (::gameView.isInitialized) {
            gameView.pause()
        }
    }
}

@Composable
fun GameScreen() {
    val context = LocalContext.current

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->
            GameView(ctx).also { gameView ->
                // Guardar referencia para el ciclo de vida
                (context as MainActivity).gameView = gameView
            }
        },
        update = { view ->
            // Aquí puedes actualizar el GameView si es necesario
        }
    )
}

// Composable temporal para testing (puedes eliminarlo después)
@Composable
fun GameMenuScreen() {
    // Aquí podrías agregar un menú principal más adelante
    // Por ahora, muestra directamente el juego
    GameScreen()
}