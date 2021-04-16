package master.kotlin.a7minuteworkout

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private var tts:TextToSpeech? = null
    private var restTimer:CountDownTimer? = null
    private var restProgress = 0
    private var restTimerDuration:Long = 1

    private var exerciseTimer:CountDownTimer? = null
    private var exerciseProgress = 0
    private var exerciseTimerDuration:Long = 1

    private var exerciseList:ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1
    private var player: MediaPlayer? = null

    private var exerciseAdapter : ExerciseStatusAdapter? = null

    // 뷰 설정
    private lateinit var progressBarExercise:ProgressBar
    private lateinit var tvExerciseTimer:TextView
    private lateinit var progressBar:ProgressBar
    private lateinit var tvTimer:TextView
    private lateinit var llRestView:LinearLayout
    private lateinit var llExerciseView:LinearLayout
    private lateinit var tvUpcomingExerciseName:TextView
    private lateinit var tvUpcoming:TextView
    private lateinit var ivImageView:ImageView
    private lateinit var tvExerciseName:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)
        val toolbarExerciseActivity = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_exercise_activity)
        setSupportActionBar(toolbarExerciseActivity)

        // 뷰 설정
        progressBarExercise = findViewById(R.id.progressBarExercise)
        tvExerciseTimer = findViewById(R.id.tvExerciseTimer)
        progressBar = findViewById(R.id.progressBar)
        tvTimer = findViewById(R.id.tvTimer)
        llRestView = findViewById(R.id.llRestView)
        llExerciseView = findViewById(R.id.llExerciseView)
        tvUpcoming = findViewById(R.id.tvUpcoming)
        tvUpcomingExerciseName = findViewById(R.id.tvUpcomingExerciseName)
        ivImageView = findViewById(R.id.ivImage)
        tvExerciseName = findViewById(R.id.tvExerciseName)

        val actionbar = supportActionBar
        if(actionbar != null){
            actionbar.setDisplayHomeAsUpEnabled(true) // 뒤로가기 방지버튼??
        }
        toolbarExerciseActivity.setNavigationOnClickListener {
            onBackPressed()
            finish()
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
        progressBar.progress = restProgress
        // https://tourspace.tistory.com/109
        // object로 익명 클래스 생성. 호출 시 매번 객체가 생성되고, 익명 내부에서 외부 클래스에 접근 가능함

        restTimer = object:CountDownTimer(restTimerDuration*1000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                progressBar.progress = restProgress-restProgress
                tvTimer.text = (restTimerDuration-restProgress).toString()
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
        progressBarExercise.progress = exerciseProgress
        exerciseTimer = object:CountDownTimer(exerciseTimerDuration*1000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                progressBarExercise.progress = exerciseTimerDuration.toInt()-exerciseProgress
                tvExerciseTimer.text = (exerciseTimerDuration-exerciseProgress).toString()
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
        llRestView.visibility = View.GONE
        llExerciseView.visibility = View.VISIBLE

        if(exerciseTimer != null){
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }
        ivImageView.setImageResource(exerciseList!![currentExercisePosition].getImage())
        tvExerciseName.text = exerciseList!![currentExercisePosition].getName()
        // TTS 말하기
        var speakText = tvExerciseName.text.toString()
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


        llRestView.visibility = View.VISIBLE
        llExerciseView.visibility = View.GONE
        if(restTimer != null){
            restTimer!!.cancel()
            restProgress = 0
        }

        tvUpcomingExerciseName.text = exerciseList!![currentExercisePosition+1].getName()

        setRestProgressBar()
    }

    // 현재 어느 운동인지 보여주는 RecyclerView setup 함수
    private fun setupExerciseStatusRecyclerView(){
        val rvExerciseStatus = findViewById<RecyclerView>(R.id.rvExerciseStatus)
        rvExerciseStatus.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!, this)
        rvExerciseStatus.adapter = exerciseAdapter
    }
}