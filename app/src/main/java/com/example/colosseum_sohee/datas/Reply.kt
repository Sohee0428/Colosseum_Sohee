package com.example.colosseum_sohee.datas

import org.json.JSONObject

class Reply {

    var id = 0
    var content = ""

     lateinit var selectedSide : Side

    var writeNickname = ""

    companion object{

        fun getReplyFromJson(jsonObj: JSONObject) : Reply{
            val resultReply = Reply()

            resultReply.id = jsonObj.getInt("id")
            resultReply.content = jsonObj.getString("content")

            resultReply.selectedSide = Side.getSideFromJson(jsonObj.getJSONObject("selected_side"))

            resultReply.writeNickname = jsonObj.getJSONObject("user").getString("nick_name")

            return resultReply
        }
    }
}