package com.example.colosseum_sohee.datas

import org.json.JSONObject
import java.io.Serializable

class Side : Serializable{
    var id = 0
    var topicId = 0
    var title = ""
    var voteCount = 0

    companion object{

        fun getSideFromJson(jsonObject: JSONObject) : Side {

            var resultSide = Side()

            resultSide.id = jsonObject.getInt("id")
            resultSide.topicId = jsonObject.getInt("topic_id")
            resultSide.title = jsonObject.getString("title")
            resultSide.voteCount = jsonObject.getInt("vote_count")

            return resultSide
        }
    }
}