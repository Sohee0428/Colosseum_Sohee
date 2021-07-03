package com.example.colosseum_sohee.adapters

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.example.colosseum_sohee.R
import com.example.colosseum_sohee.datas.Reply
import com.example.colosseum_sohee.datas.Topic
import com.example.colosseum_sohee.utils.ServerUtil
import org.json.JSONObject
import org.w3c.dom.Text

class ReplyAdapter (val mContext: Activity,
                    resId: Int, // 다른 곳에서 사용하지 않을것이기 때문에 val 없이 작성
                    val mList: List<Reply>) : ArrayAdapter<Reply>(mContext, resId, mList) {

    val mInflater = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var tempRow = convertView

        if(tempRow == null) {
            tempRow = mInflater.inflate(R.layout.reply_list_item, null)
        }

        val row = tempRow!!

        val data = mList[position]

        val selectedSideTxt = row.findViewById<TextView>(R.id.selectedSideTxt)
        val userNicknameTxt = row.findViewById<TextView>(R.id.userNicknameTxt)
        val contentTxt = row.findViewById<TextView>(R.id.contentTxt)

        val likeCountBtn = row.findViewById<TextView>(R.id.likeCountBtn)
        val dislikeCountBtn = row.findViewById<TextView>(R.id.dislikeCountBtn)


        contentTxt.text = data.content

        selectedSideTxt.text = "(${data.selectedSide.title})"

        userNicknameTxt.text = data.writeNickname

        likeCountBtn.text = "좋아요 ${data.likeCount}개"
        dislikeCountBtn.text = "싫어요 ${data.dislikeCount}개"

        if(data.myLike) {

//            좋아요만 - 글씨 빨간색 + 배경도 빨간 테두리 / 싫어요 - 회색

            likeCountBtn.setBackgroundResource(R.drawable.red_border_box)
            likeCountBtn.setTextColor(Color.parseColor("#ff0000"))

            dislikeCountBtn.setBackgroundResource(R.drawable.gray_border_box)
            dislikeCountBtn.setTextColor(Color.parseColor("#a0a0a0"))
        }
        else if (data.myDislike) {

//            싫어요만 - 글씨 파란색 + 배경도 파란 테두리 / 좋아요 - 회색

            dislikeCountBtn.setBackgroundResource(R.drawable.blue_border_box)
            dislikeCountBtn.setTextColor(Color.parseColor("#0000ff"))

            likeCountBtn.setBackgroundResource(R.drawable.gray_border_box)
            likeCountBtn.setTextColor(Color.parseColor("#a0a0a0"))
        }
        else{

//            좋아요 / 싫어요 찍지 않은 상태 - 둘 다 회색

            dislikeCountBtn.setBackgroundResource(R.drawable.gray_border_box)
            dislikeCountBtn.setTextColor(Color.parseColor("#a0a0a0"))

            likeCountBtn.setBackgroundResource(R.drawable.gray_border_box)
            likeCountBtn.setTextColor(Color.parseColor("#a0a0a0"))
        }

       likeCountBtn.setOnClickListener {

//            좋아요 api 호출

            ServerUtil.postRequestLikeOrDislike(mContext, data.id, true, object : ServerUtil.Companion.JsonResponseHandler{
                override fun onResponse(jsonObj: JSONObject) {
                    if (jsonObj.has("data")) {
                        val likeCount = jsonObj.getJSONObject("data").getJSONObject("reply")
                            .getString("like_count")
                        val disLikeCount = jsonObj.getJSONObject("data").getJSONObject("reply")
                            .getString("dislike_count")
                        val isCheck = jsonObj.getJSONObject("data").getJSONObject("reply")
                            .getBoolean("my_like")

                        mContext.runOnUiThread {
                            likeCountBtn.text = "좋아요 ${likeCount}개"
                            dislikeCountBtn.text = "싫어요 ${disLikeCount}개"
                        }

                        if (isCheck) {
                            likeCountBtn.setBackgroundResource(R.drawable.red_border_box)
                            likeCountBtn.setTextColor(Color.parseColor("#ff0000"))

                            dislikeCountBtn.setBackgroundResource(R.drawable.gray_border_box)
                            dislikeCountBtn.setTextColor(Color.parseColor("#a0a0a0"))
                        } else {
                            likeCountBtn.setBackgroundResource(R.drawable.gray_border_box)
                            likeCountBtn.setTextColor(Color.parseColor("#a0a0a0"))
                        }
                    } else {
                        mContext.runOnUiThread {
                            Toast.makeText(mContext, "서버와 통신이 불안정합니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }

        dislikeCountBtn.setOnClickListener {
            ServerUtil.postRequestLikeOrDislike(mContext, data.id, false, object : ServerUtil.Companion.JsonResponseHandler {
                override fun onResponse(jsonObj: JSONObject) {
                    if (jsonObj.has("data")) {
                        val likeCount = jsonObj.getJSONObject("data").getJSONObject("reply")
                            .getString("like_count")
                        val disLikeCount = jsonObj.getJSONObject("data").getJSONObject("reply")
                            .getString("dislike_count")

                        val isCheck = jsonObj.getJSONObject("data").getJSONObject("reply")
                            .getBoolean("my_dislike")

                        mContext.runOnUiThread {
                            likeCountBtn.text = "좋아요 ${likeCount}개"
                            dislikeCountBtn.text = "싫어요 ${disLikeCount}개"
                        }

                        if (isCheck) {
                            dislikeCountBtn.setBackgroundResource(R.drawable.blue_border_box)
                            dislikeCountBtn.setTextColor(Color.parseColor("#0000ff"))

                            likeCountBtn.setBackgroundResource(R.drawable.gray_border_box)
                            likeCountBtn.setTextColor(Color.parseColor("#a0a0a0"))
                        } else {
                            dislikeCountBtn.setBackgroundResource(R.drawable.gray_border_box)
                            dislikeCountBtn.setTextColor(Color.parseColor("#a0a0a0"))
                        }
                    } else {
                        mContext.runOnUiThread {
                            Toast.makeText(mContext, "서버와 통신이 불안정합니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }

        return row
    }
}