package com.example.colosseum_sohee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.colosseum_sohee.adapters.ReplyAdapter
import com.example.colosseum_sohee.datas.Reply
import com.example.colosseum_sohee.datas.Side
import com.example.colosseum_sohee.datas.Topic
import com.example.colosseum_sohee.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_view_topic_detail.*
import org.json.JSONObject

class ViewTopicDetailActivity : BaseActivity() {

    lateinit var mTopic : Topic

    val mReplyList = ArrayList<Reply>()

    lateinit var mReplyAdapter : ReplyAdapter

//    내가 선택한 진영이 어디인지? 초기값은 선택 진영이 없다(null)
    var mySelectedSide : Side? = null


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

        writeReplyBtn.setOnClickListener {

//            만약 선택된 진영이 없다면, 투표부터 하도록
            if (mySelectedSide == null) {
                Toast.makeText(mContext, "투표를 한 이후에만 의견을 등록할 수 있습니다.", Toast.LENGTH_SHORT).show()
            }
            else {
//                의견 작성 화면으로 이동
                val myIntent = Intent(mContext, EditReplyActivity::class.java)
                myIntent.putExtra("mySide", mySelectedSide)
                startActivity(myIntent)
            }
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

    override fun onResume() {
        super.onResume()

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

//                똑같은 데이터가 계속 쌓이는 것을 막자
                mReplyList.clear()

                for (i in 0 until replyArr.length()) {

                    val replyObj = replyArr.getJSONObject(i)
                    val reply = Reply.getReplyFromJson(replyObj)
                    mReplyList.add(reply)
                }

//                내가 선택한 진영이 있다면 => 파싱해서 mySelectedSide에 담아주자

//                topicObj 내부의 my_side를 추출 => null이 아닐때만 추출

                mySelectedSide = null

                if(!topicObj.isNull("my_side")) {

                    val mySideObj = topicObj.getJSONObject("my_side")
                    val mySide = Side.getSideFromJson(mySideObj)
                    mySelectedSide = mySide
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