package com.example.demothread_handler

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private val keyMessageError: Int = 102
    private val keyMessage: Int = 101
    private var mThread : Thread? = null

    // update UI từ background Thread
    private val mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if(msg.what == keyMessage){
                tvCount.text = msg.arg1.toString()
            }else if(msg.what == keyMessageError){
                Toast.makeText(this@MainActivity,"Stop",Toast.LENGTH_SHORT).show()
            }
        }
    }


    private  val runnable = Runnable {
        for (i in 0..20){
            val message = Message()
            try{
                Thread.sleep(500)
                message.what = keyMessage
                message.arg1 = i
                message.target = mHandler
                message.sendToTarget()
            }catch (e : Exception){
                message.what = keyMessageError
                message.target = mHandler
                message.sendToTarget()
                break
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Có 2 cách stop thì đang chọn cách gọi interrupt() để nhảy vào Exception
        tvStop.setOnClickListener {
            if(mThread!!.isAlive){
                mThread!!.interrupt()
            }
        }

        // Mỗi lần chạy xong Thread chết luôn nên k start lại đc mà phải tạo Thread mới
        tvStart.setOnClickListener {
            if(mThread == null || !mThread!!.isAlive){
                mThread = Thread(runnable)
                mThread!!.start()
            }
        }
    }
}