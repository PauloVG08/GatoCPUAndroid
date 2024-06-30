/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.example.pevgappgato2.presentation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import com.example.pevgappgato2.R
import com.example.pevgappgato2.presentation.theme.PevgAppGato2Theme
import kotlin.random.Random

var juegoTerminado = false
var jugadorEnTurno = "X"
var indicarTurno = ""
private lateinit var txtsalida: TextView
private lateinit var txtorden: TextView
private lateinit var casillas: Array<TextView>
private val handler = Handler(Looper.getMainLooper())

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setTheme(android.R.style.Theme_DeviceDefault)
        setContentView(R.layout.main_layout)
        txtsalida = findViewById(R.id.txtsalida)
        txtorden = findViewById(R.id.txtorden)
        casillas = arrayOf(
            findViewById<TextView>(R.id.cuadro0),
            findViewById<TextView>(R.id.cuadro1),
            findViewById<TextView>(R.id.cuadro2),
            findViewById<TextView>(R.id.cuadro3),
            findViewById<TextView>(R.id.cuadro4),
            findViewById<TextView>(R.id.cuadro5),
            findViewById<TextView>(R.id.cuadro6),
            findViewById<TextView>(R.id.cuadro7),
            findViewById<TextView>(R.id.cuadro8),
        );
        var index = 0
        casillas.forEach {
            val num = index++
            it.setOnClickListener {
                marcarCasilla(num)
            }
        }

        iniciarJuego()
        val button = findViewById<Button>(R.id.btniniciar)
        button.setOnClickListener {
            iniciarJuego()
        }
    }
}

fun iniciarJuego(){
    juegoTerminado = false
    jugadorEnTurno = elegirSigno();
    indicarTurno = elegirQuienInicia()
    txtsalida.text = "Turno de " + jugadorEnTurno
    txtorden.text = "El jugador que inicia es: " + indicarTurno

    casillas.forEach {
        it.text = "."
    }

    if (indicarTurno == "CPU") {
        handler.postDelayed({ elegirCasillaCPU() }, 1000)
    }
}

fun marcarCasilla(index: Int){
    if(juegoTerminado) return

    if(casillas[index].text == "."){
        casillas[index].text = jugadorEnTurno
        verificaGanador()
    }
}

fun verificaGanador(){
    val combinaciones = arrayOf(
        arrayOf(0,1,2), // Horizontales
        arrayOf(3,4,5),
        arrayOf(6,7,8),
        arrayOf(0,3,6), // Verticales
        arrayOf(1,4,7),
        arrayOf(2,5,8),
        arrayOf(0,4,8), // Diagonal
        arrayOf(6,4,2) )
    combinaciones.forEach {
        if(casillas[it[0]].text == jugadorEnTurno &&
            casillas[it[1]].text == jugadorEnTurno &&
            casillas[it[2]].text == jugadorEnTurno){
            juegoTerminado = true
        }
    }
    if(juegoTerminado){
        mensajeGanador("GANADOR")
    } else {
        var casillasDisponibles = false
        casillas.forEach {
            if(it.text == ".") casillasDisponibles = true
        }
        if(casillasDisponibles){
            cambiarJugador()
        } else {
            mensajeGanador("EMPATE")
        }
    }
}

fun mensajeGanador(resultado: String){
    if(resultado == "GANADOR"){
        txtsalida.text="GANÓ EL SIGNO "+ jugadorEnTurno;
        txtorden.text = "GANÓ " + indicarTurno;
    } else {
        txtsalida.text = "EMPATE"
        txtorden.text = "LAS MAQUINAS NOS SUSTITUIRÁN"
    }
}

fun cambiarJugador(){
    if(jugadorEnTurno == "X"){
        jugadorEnTurno = "O"
    }else{
        jugadorEnTurno = "X"
    }

    if(indicarTurno == "CPU"){
        indicarTurno = "JUGADOR";
    }else{
        indicarTurno = "CPU"
    }

    txtsalida.text = "Turno del signo: " + jugadorEnTurno
    txtorden.text = "Turno de: " + indicarTurno

    if (indicarTurno == "CPU") {
        handler.postDelayed({ elegirCasillaCPU() }, 1000)
    }
}

fun elegirQuienInicia(): String{
    var azar:Int = Random.nextInt(0, 2)

    if(azar == 0){
        indicarTurno = "CPU";
    }else{
        indicarTurno = "JUGADOR";
    }

    return indicarTurno;
}

fun elegirSigno(): String{
    var azar:Int = Random.nextInt(0, 2)

    if(azar == 0){
        jugadorEnTurno = "X";
    }else{
        jugadorEnTurno = "O";
    }

    return jugadorEnTurno;
}

fun elegirCasillaCPU(){
    var noCasilla = Random.nextInt(0, 9)

    while(casillas[noCasilla].text != "."){
        noCasilla = Random.nextInt(0, 9)
    }

    casillas[noCasilla].text = jugadorEnTurno;

    if(!juegoTerminado){
        verificaGanador();
    }
}


