package master.kotlin.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import master.kotlin.a7minuteworkout.databinding.ActivityBMIBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW"
    val US_UNITS_VIEW = "US_UNIT_VIEW"

    var currentVisibleView: String = METRIC_UNITS_VIEW

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
        makeVisibleMetricUnitsView()
        binding.run {
            rgUnits.setOnCheckedChangeListener { group, checkedId ->
                if(checkedId == R.id.rbMetricUnits){
                    makeVisibleMetricUnitsView()
                }else{
                    makeVisibleUsUnitsView()
                }
            }
            toolbarBmiActivity.setNavigationOnClickListener {
                onBackPressed()
            }
            btnCalculateUnits.setOnClickListener {
                if(currentVisibleView == METRIC_UNITS_VIEW){
                    if(validateMetricUnits()){
                        val heightValue: Float = etMetricUnitHeight.text.toString().toFloat() / 100
                        val weightValue: Float = etMetricUnitWeight.text.toString().toFloat()

                        val bmi = weightValue / (heightValue * heightValue)
                        displayBMIResult(bmi)
                    }else{
                        Toast.makeText(this@BMIActivity, "Please enter valid values.", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    if(validateUsUnits()){
                        val heightValueFeet: Float = etUsUnitFeet.text.toString().toFloat()
                        val heightValueInch: Float = etUsUnitInch.text.toString().toFloat()
                        val weightValue: Float = etUsUnitWeight.text.toString().toFloat()
                        val heightValue: Float = heightValueInch + heightValueFeet * 12
                        val bmi = 703 * weightValue / (heightValue * heightValue)
                        displayBMIResult(bmi)
                    }else{
                        Toast.makeText(this@BMIActivity, "Please enter valid values.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    /*
    invisible - 보이지는 않지만 공간은 차지하고 있음
    gone - 보이지도 않고 공간도 없음
     */
    private fun makeVisibleUsUnitsView(){
        currentVisibleView = US_UNITS_VIEW
        binding.apply {
            tilMetricUnitWeight.visibility = View.GONE
            tilMetricUnitHeight.visibility = View.GONE
            tilUsUnitWeight.visibility = View.VISIBLE
            llUsUnitsHeight.visibility = View.VISIBLE
            llDisplayBMIResult.visibility = View.GONE
            etUsUnitFeet.text!!.clear()
            etUsUnitInch.text!!.clear()
            etUsUnitWeight.text!!.clear()
        }
    }

    private fun makeVisibleMetricUnitsView(){
        currentVisibleView = METRIC_UNITS_VIEW
        binding.apply {
            tilMetricUnitWeight.visibility = View.VISIBLE
            tilMetricUnitHeight.visibility = View.VISIBLE
            tilUsUnitWeight.visibility = View.GONE
            llUsUnitsHeight.visibility = View.GONE
            llDisplayBMIResult.visibility = View.GONE
            etMetricUnitWeight.text!!.clear()
            etMetricUnitHeight.text!!.clear()
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

        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()
        binding.apply {
            llDisplayBMIResult.visibility = View.VISIBLE
            tvBMIValue.text = bmiValue
            tvBMIType.text = bmiLabel
            tvBMIDescription.text = bmiDescription
        }
    }

    private fun validateMetricUnits(): Boolean{
        var isValid = true
        if(binding.etMetricUnitHeight.text.toString().isEmpty() || (binding.etMetricUnitWeight.text.toString().isEmpty()))
            isValid = false
        return isValid
    }
    private fun validateUsUnits(): Boolean{
        var isValid = true
        if((binding.etUsUnitInch.text.toString().isEmpty()) || (binding.etUsUnitFeet.text.toString().isEmpty()) || (binding.etUsUnitWeight.text.toString().isEmpty()))
            isValid = false
        return isValid
    }
}