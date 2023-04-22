package com.example.practice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.practice.databinding.ActivityMainBinding
import org.apache.poi.ss.usermodel.WorkbookFactory
import android.content.DialogInterface
import android.content.Intent
import android.provider.ContactsContract.CommonDataKinds.StructuredName
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import android.util.Log

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var rightAnswer: String? = null
    private var rightAnswerCount = 0
    private var quizCount = 1
    private var quizData = mutableListOf<MutableList<String>>()
    private val QUIZ_COUNT = 10



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //System.setProperty("org.xml.sax.driver", "com.sun.org.apache.xerces.internal.parsers.SAXParser")
        var result = 0
        var file = resources.assets.open("English_List_1.xlsx")
        var wb = WorkbookFactory.create(file)
        val sheet = wb.getSheet("名詞")
        var Cell1 : String?
        var Cell2 : String?
        for (i in 1..sheet.lastRowNum){
            Cell1 = sheet.getRow(i).getCell(2).stringCellValue
            Cell2 = sheet.getRow(i).getCell(1).stringCellValue
            result++
            quizData.add(mutableListOf(Cell1, Cell2))
        }

        showNextQuiz()

    }

    fun showNextQuiz() {
        var choiceAnswer = mutableListOf<String>()
        quizData.shuffle()

        binding.countLabel.text = getString(R.string.count_Label, quizCount)

        rightAnswer = quizData[0][1]
        choiceAnswer.add(quizData[0][1])
        for (i in 1..3){
            choiceAnswer.add(quizData[i][1])
        }
        choiceAnswer.shuffle()
        binding.questionLabel.text = quizData[0][0]

        binding.answerBtn1.text = choiceAnswer[0]
        binding.answerBtn2.text = choiceAnswer[1]
        binding.answerBtn3.text = choiceAnswer[2]
        binding.answerBtn4.text = choiceAnswer[3]

        quizData.removeAt(0)
    }

    fun checkAnswer(view: View) {
        val answerBtn : Button = findViewById(view.id)
        val btnText = answerBtn.text.toString()
        var alertTitle : String
        if (btnText == rightAnswer){
            alertTitle = "正解"
            rightAnswerCount++
        } else {
            alertTitle = "不正解"
        }

        AlertDialog.Builder(this)
            .setTitle(alertTitle)
            .setMessage("答え : $rightAnswer")
            .setPositiveButton("OK") { dialogInterface, i ->
                checkQuizCount()
            }
            .setCancelable(false)
            .show()
    }

    fun checkQuizCount() {
        if (quizCount == QUIZ_COUNT){
            val intent = Intent(this@MainActivity, ResultActivity::class.java)
            intent.putExtra("RIGHT_ANSWER_COUNT", rightAnswerCount)
            startActivity(intent)
        } else {
            quizCount++
            showNextQuiz()
        }
    }
}