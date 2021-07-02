package com.example.colosseum_sohee.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.colosseum_sohee.R
import com.example.colosseum_sohee.datas.Reply
import com.example.colosseum_sohee.datas.Topic
import org.w3c.dom.Text

class ReplyAdapter (val mContext: Context,
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

        contentTxt.text = data.content

        selectedSideTxt.text = "(${data.selectedSide.title})"

        userNicknameTxt.text = data.writeNickname

        return row
    }
}