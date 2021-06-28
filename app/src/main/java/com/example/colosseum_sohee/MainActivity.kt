package com.example.colosseum_sohee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.colosseum_sohee.adapters.TopicAdapter
import com.example.colosseum_sohee.datas.Topic
import com.example.colosseum_sohee.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : BaseActivity() {

    val mTopicList = ArrayList<Topic>()

    lateinit var mTopicAdapter: TopicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setValues()
        setupEvents()
    }

    override fun setupEvents() {

    }

    override fun setValues() {

        getTopicListFromServer()

        mTopicAdapter = TopicAdapter(mContext, R.layout.topic_list_item, mTopicList)
        topicListView.adapter = mTopicAdapter

    }


    fun getTopicListFromServer() {
//        서버에서 주제 목록을 받아오자 => /v2/main_info => ServerUtil에 기능 추가 필요

        ServerUtil.getRequestMainInfo(mContext, object : ServerUtil.Companion.JsonResponseHandler {
            override fun onResponse(jsonObj: JSONObject) {
//                서버에서 주제 목록을 받아온 상황

                val dataObj = jsonObj.getJSONObject("data")
                val topicArr = dataObj.getJSONArray("topics")

//                topicArr 안에 있는 여러 개의 주제들을 반복적으로 파싱 => fon문을 활용
//                배열에 10개의 주제가 있은경우 index는 0~9까지

                for (index in 0 until topicArr.length()) {

//                    index 위치에 맞는 주제들을 Topics 클래스 형태로 변환

                    val topicObj = topicArr.getJSONObject(index)
                    val topicData = Topic.getTopicDataFromJson(topicObj)

//                    변환된 주제응 mTopicList에 추가
                    mTopicList.add(topicData)

                }

//                어댑터가 먼저 세팅되고 나중에 목록이 추가되는 것일 수도 있다. => 목록에 추가되는 것도 새로고침 필요 (ui 영향이 감)

                runOnUiThread {
                    mTopicAdapter.notifyDataSetChanged()
                }
            }

        })
    }


}