package mycraft.oasistv

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient

/**
 * Visor de la tele: abre a pantalla completa la página tv.html de Oasis TV.
 * Todo el dibujo vive en la página web, así que las mejoras llegan solas sin reinstalar la app.
 * Control remoto: ◀ ▶ cambiar diapositiva · OK mostrar/ocultar información.
 */
class MainActivity : Activity() {
    private lateinit var web: WebView
    private val manejador = Handler(Looper.getMainLooper())
    private val reintentar = Runnable { web.reload() }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // La pantalla nunca se apaga ni entra el protector mientras la app está abierta.
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        web = WebView(this).apply {
            setBackgroundColor(Color.BLACK)
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true // guarda la copia local del carrete (funciona sin internet)
            settings.databaseEnabled = true
            settings.cacheMode = WebSettings.LOAD_DEFAULT
            settings.mediaPlaybackRequiresUserGesture = false
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true
            isFocusable = true
            isFocusableInTouchMode = true
            webViewClient = object : WebViewClient() {
                override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                    // Sin internet y sin copia guardada: vuelve a intentar en 20 segundos.
                    if (request.isForMainFrame) {
                        manejador.removeCallbacks(reintentar)
                        manejador.postDelayed(reintentar, 20_000)
                    }
                }
            }
        }
        setContentView(web)
        web.loadUrl(getString(R.string.url_pantalla))
        web.requestFocus()
    }

    override fun onResume() {
        super.onResume()
        web.onResume()
        pantallaCompleta()
    }

    override fun onPause() {
        web.onPause()
        super.onPause()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) pantallaCompleta()
    }

    @Suppress("DEPRECATION")
    private fun pantallaCompleta() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
    }

    override fun onDestroy() {
        manejador.removeCallbacks(reintentar)
        web.destroy()
        super.onDestroy()
    }
}
