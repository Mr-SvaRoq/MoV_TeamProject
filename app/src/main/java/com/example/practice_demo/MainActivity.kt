package com.example.practice_demo

import android.content.Context
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.practice_demo.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val myName: MyName = MyName("Dinh NT")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

//        val rollButton: Button = findViewById(R.id.roll_button)
//        rollButton.text = "Let`s Roll"
//        rollButton.setOnClickListener {
//            Toast.makeText(this, "u sucka", Toast.LENGTH_SHORT).show()
//            rollDice()

//        findViewById<Button>(R.id.buttonDone).setOnClickListener {
//            addNickname(it)
//        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.myName = myName

        binding.buttonDone.setOnClickListener {
            addNickname(it)
        }
    }

    private fun addNickname(view: View) {
//        val editText = findViewById<EditText>(R.id.nick_name_edit)
//        val nicknameTextView = findViewById<TextView>(R.id.nickname)

        binding.apply {
//            nickname.text = binding.nickNameEdit.text
            myName?.nickname = nickNameEdit.text.toString()
            invalidateAll()
            nickNameEdit.visibility = View.GONE
            buttonDone.visibility = View.GONE
            nickname.visibility = View.VISIBLE
        }


        // Hide the keyboard.
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}

//    private fun rollDice() {
//        val diceImage: ImageView = findViewById(R.id.id_diceImage)
//        val randomInt = Random.nextInt(6) + 1;
//
//        val drawableResource = when (randomInt) {
//            1 -> R.drawable.dice_1
//            2 -> R.drawable.dice_2
//            3 -> R.drawable.dice_3
//            4 -> R.drawable.dice_4
//            5 -> R.drawable.dice_5
//            else -> R.drawable.dice_6
//        }
//
//       diceImage.setImageResource(drawableResource)
//    }
//}