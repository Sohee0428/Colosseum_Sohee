package com.example.colosseum_sohee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.colosseum_sohee.datas.Topic
import com.example.colosseum_sohee.utils.ServerUtil
import org.json.JSONObject

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

        ServerUtil.getRequestMainInfo(mContext, object : ServerUtil.Companion.JsonResponseHandler{
            override fun onResponse(jsonObj: JSONObject) {
//                서버에서 주제 목록을 받아온 상황

                val dataObj = jsonObj.getJSONObject("data")
                val topicArr = dataObj.getJSONArray("topics")

//                topicArr 안에 있는 여러 개의 주제들을 반복적으로 파싱 => fon문을 활용
//                배열에 10개의 주제가 있은경우 index는 0~9까지

                for ( index in 0 until topicArr.length())

//                    index 위치에 맞는 주제들을 Topics 클래스 형태로 변환
//                    변환된 주제응 mTopicList에 추가


            }

       })
    }

}