package com.example.colosseum_sohee.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.colosseum_sohee.R
import com.example.colosseum_sohee.datas.Topic

class TopicAdapter (val mContext: Context,
                    resId: Int, // 다른 곳에서 사용하지 않을것이기 때문에 val 없이 작성
                    val mList: List<Topic>) : ArrayAdapter<Topic>(mContext, resId, mList) {

    val mInflater = LayoutInflater.from(mContext)


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var tempRow = convertView

        if(tempRow == null) {
            tempRow = mInflater.inflate(R.layout.topic_list_item, null)
        }

        val row = tempRow!!

        return row
    }
}