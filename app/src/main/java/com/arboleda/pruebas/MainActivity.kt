package com.arboleda.pruebas

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.widget.MediaController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.*


class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    /////////////////////////////////////////
    val RQ_ESCUCHA = 102
    lateinit var textToSpeech: TextToSpeech
    /////////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ////////////////////////// boton iniciador del reconocimoento
        botonescucha.setOnClickListener {
            videoView.pause()
            entradaDeVoz()
        }
        var cancionOvideo = "todopasa"

        ////////////Reproductor de video y audio
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        var offline = Uri.parse("android.resource://$packageName/${R.raw.todopasa}")
        //val offline2= Uri.parse("android.resource://$packageName/${R.raw.michael}")
        videoView.setMediaController(mediaController)
        videoView.setVideoURI(offline)
        //videoView.setVideoURI(offline2)
        ////////////////////////////////////////////////////////////////////////////////////////
        textToSpeech = TextToSpeech(this,this)
    }



    //////////////////////////////////////////////////////////////////////////// RECONOCIMIENTO
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RQ_ESCUCHA && resultCode == Activity.RESULT_OK){
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
             cajadetexto.text = result?.get(0).toString()

            var contenedor = result?.get(0).toString()
            ordenes(contenedor)

        }
    }


    fun entradaDeVoz(){
        videoView.pause()
        if(!SpeechRecognizer.isRecognitionAvailable(this)){
            Toast.makeText(this, "El reconocimiento no esta habilitado", Toast.LENGTH_SHORT).show()
        }else{
            val i = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Habla Porfavor")
            startActivityForResult(i, RQ_ESCUCHA)
        }
    }
    //////////////////////////////////////////////////////////////////////////// RECONOCIMIENTO

/////////////////////////////////////////////// Funcion de la voz (Hablar)
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS){
            val res: Int = textToSpeech.setLanguage(Locale.ITALY)
            if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED){
                Toast.makeText(this,"LENGUAJE NO SOPORTADO", Toast.LENGTH_LONG).show()
            }else{Toast.makeText(this,"FALLO EL INICIO",Toast.LENGTH_LONG).show()}
        }
    }
///////////////////////////////////////////////////////

    fun almacenarRepetir(){

    }



    //// da las ordenes a los botones
    fun ordenes(recivir: String) {
        if (recivir == "iniciar" || recivir == "reproducir" || recivir == "Iniciar" || recivir == "Reproducir" ) {
            videoView.start()
        }
        else if (recivir == "detener"  || recivir == "Detener"){
            videoView.stopPlayback()

        }else if (recivir == "repetir"|| recivir == "Repetir"){
            videoView.stopPlayback()

            videoView.start()
        }
        else{
            textToSpeech.speak("Comando, o video, no encontrado, vuelva a intentarlo, porfavor ",TextToSpeech.QUEUE_FLUSH,null)
        }

    }



}