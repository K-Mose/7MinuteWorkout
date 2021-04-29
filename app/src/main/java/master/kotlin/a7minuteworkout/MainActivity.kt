package master.kotlin.a7minuteworkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import master.kotlin.a7minuteworkout.databinding.ActivityMainBinding
import java.util.*
import java.util.zip.Inflater

class MainActivity : AppCompatActivity(),TextToSpeech.OnInitListener {
    companion object{
        var tts:TextToSpeech? = null
    }
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        tts = TextToSpeech(this, this)

        binding.llStart.setOnClickListener {
            val intent = Intent(this, ExerciseActivity::class.java)
            startActivity(intent)
        }

        binding.llBMI.setOnClickListener {
            val intent = Intent(this, BMIActivity::class.java)
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