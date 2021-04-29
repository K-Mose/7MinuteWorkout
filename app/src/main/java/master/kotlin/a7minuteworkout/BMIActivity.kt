package master.kotlin.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import master.kotlin.a7minuteworkout.databinding.ActivityBMIBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBMIBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBMIBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // actionbar setup
        setSupportActionBar(binding.toolbarBmiActivity)

        val actionbar = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "CALCULATOR BMI"
        }

        binding.toolbarBmiActivity.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.btnCalculateUnits.setOnClickListener {
            if(validateMetricUnits()){
                val heightValue: Float = binding.etMetricUnitHeight.text.toString().toFloat() / 100
                val weightValue: Float = binding.etMetricUnitWeight.text.toString().toFloat()

                val bmi = weightValue / (heightValue * heightValue)
                displayBMIResult(bmi)
            }else{
                Toast.makeText(this@BMIActivity, "Please enter valid values.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun displayBMIResult(bmi: Float){
        val bmiLabel: String
        val bmiDescription:String

        when{
            bmi <= 15f -> {bmiLabel = "Very severely underweight";bmiDescription = "Oops! You really need to take better care of yourself!"}
            bmi in 15f .. 16f -> {bmiLabel = "Severely underweight"
                bmiDescription = "Oops!You really need to take better care of yourself! Eat more!"}
            bmi in 16f .. 18.5f -> {bmiLabel = "Underweight"
                bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"}
            bmi in 18.5f .. 25f -> {bmiLabel = "Normal"
                bmiDescription = "Congratulations! You are in a good shape!"}
            bmi in 25f .. 30f -> {bmiLabel = "Overweight"
                bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"}
            bmi in 30f .. 35f -> {bmiLabel = "Obese Class | (Moderately obese)"
                bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"}
            bmi in 35f .. 40f -> {bmiLabel = "Obese Class || (Severely obese)"
                bmiDescription = "OMG! You are in a very dangerous condition! Act now!"}
            else -> {bmiLabel = "Obese Class ||| (Very Severely obese)"
                bmiDescription = "OMG! You are in a very dangerous condition! Act now!"}
        }

        binding.tvYourBMI.visibility = View.VISIBLE
        binding.tvBMIType.visibility = View.VISIBLE
        binding.tvBMIDescription.visibility = View.VISIBLE
        binding.tvBMIValue.visibility = View.VISIBLE

        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()
        binding.tvBMIValue.text = bmiValue
        binding.tvBMIType.text = bmiLabel
        binding.tvBMIDescription.text = bmiDescription

    }

    private fun validateMetricUnits(): Boolean{
        var isValid = true

        if(binding.etMetricUnitHeight.text.toString().isEmpty() || (binding.etMetricUnitWeight.text.toString().isEmpty()))
            isValid = false
        return isValid
    }
}