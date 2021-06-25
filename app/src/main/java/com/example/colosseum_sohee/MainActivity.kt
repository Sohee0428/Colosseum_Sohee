package com.example.colosseum_sohee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.colosseum_sohee.datas.Topic

class MainActivity : BaseActivity() {

    val mTopicList = ArrayList<Topic>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setValues()
        setupEvents()
    }

    override fun setupEvents() {

    }

    override fun setValues() {
    }

    fun getTopicListFromServer(){
//        서버에서 주제 목록을 받아오자 => /v2/main_info => ServerUtil에 기능 추가 필요
    }

}