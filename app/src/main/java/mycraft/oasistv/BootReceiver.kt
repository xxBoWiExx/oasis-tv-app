package mycraft.oasistv

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/** Abre la app sola cuando la tele (o el TV box) termina de encender. */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED,
            "android.intent.action.QUICKBOOT_POWERON",
            Intent.ACTION_MY_PACKAGE_REPLACED -> {
                val abrir = Intent(context, MainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                try {
                    context.startActivity(abrir)
                } catch (_: Exception) {
                    // Sin el permiso de "mostrar sobre otras apps" Android puede bloquearlo.
                }
            }
        }
    }
}
