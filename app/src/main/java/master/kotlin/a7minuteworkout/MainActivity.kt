package master.kotlin.a7minuteworkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity(),TextToSpeech.OnInitListener {
    companion object{
        var tts:TextToSpeech? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tts = TextToSpeech(this, this)
        var llStart = findViewById<LinearLayout>(R.id.llStart)
        llStart.setOnClickListener {
            val intent = Intent(this, ExerciseActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onInit(status: Int) {
        // TTS 초기 설정
        // 상태가 성공(0) 이면
        if(status == TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.US)

            // 언어가 없거나 지원하지 않는다면 로그 찍음
            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS:: ", "The Language specified is not supported!")
            }
        }else{
            Log.e("TTS:: " ,"Initialization Failed")
        }
    }
}