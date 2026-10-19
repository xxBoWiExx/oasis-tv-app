# Oasis TV — app de la tele (Android TV / Google TV)

La app es un **visor**: abre a pantalla completa la página `tv.html` de Oasis TV (en tu Netlify) y la deja corriendo.
Todo el diseño vive en esa página, así que cuando mejoremos algo **solo se resube la carpeta a Netlify** y la tele se actualiza sola (recarga cada 6 horas o al reiniciarla). No hay que reinstalar la app ni usar Firebase aquí.

- **Paquete:** `mycraft.oasistv`
- **Dirección que abre:** `app/src/main/res/values/strings.xml` → `url_pantalla`
- **Control remoto:** ◀ ▶ = diapositiva anterior / siguiente · **OK** = muestra u oculta la información
- **Sin internet:** sigue mostrando lo último que recibió.
- Se abre sola al prender el TV box (paso 4) y no deja que la pantalla se apague.

> La carpeta `_noUsado` tiene la versión anterior (dibujaba en Kotlin). Ya no se usa; se puede borrar.

## Paso 1 — Probar sin TV box
En una computadora abre `https://oasis-tv.netlify.app/tv.html` en el navegador. Es exactamente lo que verá la tele.

## Paso 2 — Abrir el proyecto en Android Studio
1. Android Studio, luego **File → Open**, y elige la carpeta `FireTVApp/OasisTV`.
2. Espera a que termine el "Gradle sync" (la primera vez tarda unos minutos).
3. Si Android Studio ofrece actualizar "Android Gradle Plugin", puedes aceptar.
4. Si aparece un error, cópiamelo completo.

## Paso 3 — Probar en el emulador
1. Abre **Device Manager**, pulsa **+** y luego **Create Virtual Device**. Ve a **TV**, elige **Television (1080p)** y una imagen **Google TV** o **Android TV** (API 34). Pulsa Finish.
2. Elige ese emulador y pulsa ▶ **Run**. En el emulador, las flechas del teclado son el control y Enter es OK.

## Paso 4 — Instalar en el TV box
1. En el TV box ve a **Configuración**, luego **Sistema** y **Acerca de**. Baja hasta **Compilación del SO** (Build) y pulsa OK **7 veces**.
2. Regresa a **Sistema** y entra a **Opciones de desarrollador**:
   - Activa **Depuración USB**.
   - Activa **Depuración inalámbrica** y entra a ella. Pulsa **Vincular dispositivo con código**: aparecen una IP:puerto y un código.
3. En la Terminal de la Mac (Mac y TV box en el mismo WiFi):
   ```
   ~/Library/Android/sdk/platform-tools/adb pair IP:PUERTO_DE_VINCULACION
   ```
   Escribe el código. Luego conecta con la IP:puerto de la pantalla principal de "Depuración inalámbrica":
   ```
   ~/Library/Android/sdk/platform-tools/adb connect IP:PUERTO
   ```
4. En Android Studio elige el TV box en la lista de dispositivos y pulsa ▶ **Run**.
5. **Que se abra sola al prender.** Corre esto una sola vez:
   ```
   ~/Library/Android/sdk/platform-tools/adb shell appops set mycraft.oasistv SYSTEM_ALERT_WINDOW allow
   ```
6. En el TV box: **Configuración → Sistema → Energía**, pon **Apagar pantalla / Protector** en **Nunca**.
7. Prueba: desconecta el TV box de la corriente, vuelve a conectarlo y espera. Debe abrir Oasis TV solo.

> Si el TV box es Android 9 o anterior, en vez de "Depuración inalámbrica" usa **Depuración por red** y `adb connect IP:5555`.
