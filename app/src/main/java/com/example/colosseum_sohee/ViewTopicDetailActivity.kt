package com.example.colosseum_sohee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.colosseum_sohee.adapters.ReplyAdapter
import com.example.colosseum_sohee.datas.Reply
import com.example.colosseum_sohee.datas.Topic
import com.example.colosseum_sohee.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_view_topic_detail.*
import org.json.JSONObject

class ViewTopicDetailActivity : BaseActivity() {

    lateinit var mTopic : Topic

    val mReplyList = ArrayList<Reply>()

    lateinit var mReplyAdapter : ReplyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_topic_detail)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        voteToFirstSideBtn.setOnClickListener{

//            API확인 => 토큰 + 어떤 진영 선택(해당 진영의 id 값)

            ServerUtil.postRequestVote(mContext, mTopic.sides[0].id, object : ServerUtil.Companion.JsonResponseHandler{
                override fun onResponse(jsonObj: JSONObject) {

//                    서버의 응답 대응 => 서버에서 최신 투표 현황을 받아서 다시 ui에 반영

                    getTopicDetailFromServer()
                }
            })
        }

        voteToSecondSideBtn.setOnClickListener {

            ServerUtil.postRequestVote(mContext, mTopic.sides[1].id, object : ServerUtil.Companion.JsonResponseHandler{
                override fun onResponse(jsonObj: JSONObject) {

                    getTopicDetailFromServer()
                }

            })
        }
   }

    override fun setValues() {

        mTopic = intent.getSerializableExtra("topic") as Topic

        topicTitleTxt.text = mTopic.title
        Glide.with(mContext).load(mTopic.imageURL).into(topicImg)

        mReplyAdapter = ReplyAdapter(mContext, R.layout.reply_list_item, mReplyList)
        replyListView.adapter = mReplyAdapter

//        현재 투표 현황을 다시 서버에서 받아오자
        getTopicDetailFromServer()

    }

    fun getTopicDetailFromServer(){

        ServerUtil.getRequestTopicDetail(mContext, mTopic.id, object : ServerUtil.Companion.JsonResponseHandler {
            override fun onResponse(jsonObj: JSONObject) {

                val dataObj = jsonObj.getJSONObject("data")
                val topicObj = dataObj.getJSONObject("topic")

                val topic = Topic.getTopicDataFromJson(topicObj)

                mTopic = topic

//                topicObj 내부의 replies JSONArray 파싱 => 의견 목록에 담아주자

                val replyArr = topicObj.getJSONArray("replies")

                for (i in 0 until replyArr.length()) {

                    val replyObj = replyArr.getJSONObject(i)
                    val reply = Reply.getReplyFromJson(replyObj)
                    mReplyList.add(reply)
                }

//                최신 득표 현황까지 받아서 mTopic에 저장됨
//                ui에 득표 현황 반영

                runOnUiThread {
                    firstSideTxt.text = mTopic.sides[0].title
                    firstSideVoteTxt.text = "${mTopic.sides[0].voteCount}표"

                    secondSideTxt.text = mTopic.sides[1].title
                    secondSideVoteTxt.text = "${mTopic.sides[1].voteCount}표"

//                    댓글 목록 새로고침
                    mReplyAdapter.notifyDataSetChanged()
                }


            }

        })

    }
}