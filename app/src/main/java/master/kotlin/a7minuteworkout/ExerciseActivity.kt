package master.kotlin.a7minuteworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import master.kotlin.a7minuteworkout.databinding.ActivityExerciseBinding
import master.kotlin.a7minuteworkout.databinding.DialogCustomBacComfirmationBinding
import org.w3c.dom.Text
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var tts:TextToSpeech? = null
    private var restTimer:CountDownTimer? = null
    private var restProgress = 0
    private var restTimerDuration:Long = 10

    private var exerciseTimer:CountDownTimer? = null
    private var exerciseProgress = 0
    private var exerciseTimerDuration:Long = 30

    private var exerciseList:ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1
    private var player: MediaPlayer? = null

    private var exerciseAdapter : ExerciseStatusAdapter? = null

    private lateinit var binding: ActivityExerciseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarExerciseActivity)

        val actionbar = supportActionBar
        actionbar?.apply {
            // 뒤로가기 버튼 설정
            setDisplayHomeAsUpEnabled(true)
        }
        binding.toolbarExerciseActivity.setNavigationOnClickListener {
            customDialogForBackButton()
        }
        exerciseList = Constants.defaultExerciseList()
        tts = TextToSpeech(this, this)
        // Activity 시작과 동시에 말하기 위해 이전 Activity에서 끌어와 사용
        MainActivity.tts!!.speak("Start Exercise!", TextToSpeech.QUEUE_FLUSH, null,"")
        setupRestView()
        setupExerciseStatusRecyclerView()
    }

    // onInit은 Activity가 생성된 뒤에 설정이 완료되기 때문에 Activity 실행과 동시에 말 할 수 없다.
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
        speakOut("I Can Speak")
    }

    private fun speakOut(text:String){
        Log.d("STTS:: ", "SPEAKING")
        tts!!.speak(text, TextToSpeech.QUEUE_ADD, null, "")
    }

    override fun onDestroy() {
        if(restTimer!= null) {
            restTimer!!.cancel()
            restProgress = 0
        }
        if(exerciseTimer!= null) {
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }
        if(tts != null){
            tts!!.stop()
            tts!!.shutdown()
        }
        if(player != null){
            player!!.stop()
        }
        super.onDestroy()
    }

    private fun setRestProgressBar(){
        binding.progressBar.progress = restProgress
        // https://tourspace.tistory.com/109
        // object로 익명 클래스 생성. 호출 시 매번 객체가 생성되고, 익명 내부에서 외부 클래스에 접근 가능함

        restTimer = object:CountDownTimer(restTimerDuration*1000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                binding.progressBar.progress = restTimerDuration.toInt()-restProgress
                binding.tvTimer.text = (restTimerDuration-restProgress).toString()
            }
            override fun onFinish() {
                currentExercisePosition++

                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()

                setupExerciseView()
            }
        }.start()

    }

    private fun setExerciseProgressBar(){
        binding.progressBarExercise.progress = exerciseProgress
        exerciseTimer = object:CountDownTimer(exerciseTimerDuration*1000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                binding.progressBarExercise.progress = exerciseTimerDuration.toInt()-exerciseProgress
                binding.tvExerciseTimer.text = (exerciseTimerDuration-exerciseProgress).toString()
            }

            override fun onFinish() {
                if(currentExercisePosition<exerciseList?.size!! - 1){
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setupRestView()
                }else{
                    finish()
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                }
            }
        }.start()
    }

    private fun setupExerciseView(){
        binding.llRestView.visibility = View.GONE
        binding.llExerciseView.visibility = View.VISIBLE

        if(exerciseTimer != null){
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }
        binding.ivImage.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding.tvExerciseName.text = exerciseList!![currentExercisePosition].getName()
        // TTS 말하기
        var speakText = binding.tvExerciseName.text.toString()
        speakOut(speakText)
        setExerciseProgressBar()
    }

    private fun setupRestView(){
        // media 파일 접근할 때 crash가 있을 수도 있으므로
        try{
            // 이전 방식, 주소쓰다 오타날수있어서 잘 안씀
            //val soundURI = Uri.parse("android:resource://master.kotlin.a7minuteworkout/"+R.raw.press_start)
            Log.d("Player::","소리 출력")
            player = MediaPlayer.create(applicationContext, R.raw.press_start)
            player!!.isLooping = false
            player!!.start()
        }catch (e:Exception){
            e.printStackTrace()
        }


        binding.llRestView.visibility = View.VISIBLE
        binding.llExerciseView.visibility = View.GONE
        if(restTimer != null){
            restTimer!!.cancel()
            restProgress = 10
        }

        binding.tvUpcomingExerciseName.text = exerciseList!![currentExercisePosition+1].getName()

        setRestProgressBar()
    }

    // 현재 어느 운동인지 보여주는 RecyclerView setup 함수
    private fun setupExerciseStatusRecyclerView(){
        binding.apply {
            rvExerciseStatus.layoutManager =
                    LinearLayoutManager(this@ExerciseActivity, LinearLayoutManager.HORIZONTAL, false)
            exerciseAdapter = ExerciseStatusAdapter(exerciseList!!, this@ExerciseActivity)
            rvExerciseStatus.adapter = exerciseAdapter
        }
    }

    private fun customDialogForBackButton(){
        val dialogBinding = DialogCustomBacComfirmationBinding.inflate(LayoutInflater.from(this))
        val customDialog = Dialog(this)
        customDialog.setContentView(dialogBinding.root)
        dialogBinding.tvYes.setOnClickListener {
            finish()
            customDialog.dismiss()
        }
        dialogBinding.tvNo.setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()
    }
    // 백 버튼도 다이얼로그 뜨게
    override fun onBackPressed() {
        customDialogForBackButton()
    }
}