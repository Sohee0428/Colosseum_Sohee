package com.example.colosseum_sohee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.colosseum_sohee.utils.ContextUtil
import com.example.colosseum_sohee.utils.ServerUtil
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        loginBtn.setOnClickListener {

//            입력한 이메일 / 아이디가 뭔지 변수에 저장
            val inputEmail = emailEdt.text.toString()
            val inputPw = passwordEdt.text.toString()

//            서버에 실제 회원이 맞는지 확인 요청 (Request)
            ServerUtil.postRequestLogin(inputEmail, inputPw, object : ServerUtil.Companion.JsonResponseHandler {
                override fun onResponse(jsonObj: JSONObject) {
//                    jsonObj : 서버에서 내려준 본문을 JSON 형태로 가공해준 결과문
//                     => 이 내부를 파싱(분석)해서 상황에 따른 대응
//                    예) 로그인 실패시 그 이유를 토스트로 띄운다


                    val code = jsonObj.getInt("code")

                    if(code == 200) {
//                        로그인 성공
                        
//                        val dataObj = jsonObj.getJSONObject("data")
//                        val userObj = jsonObj.getJSONObject("user")
//                        val nickname = jsonObj.getString("nick_name")
//
//                        runOnUiThread{
//                            Toast.makeText(mContext, "${nickname}님 로그인 성공!", Toast.LENGTH_SHORT).show()
//                        }


//                        서버가 주는 토큰을 추출해서 저장
                        val dataObj = jsonObj.getJSONObject("data")
                        val token = dataObj.getString("token")

                        ContextUtil.setToken(mContext, token)


                        val myIntent = Intent(mContext, MainActivity::class.java)
                        startActivity(myIntent)

                        finish()
                    }
                    else {
//                        로그인 실패

                        val message = jsonObj.getString("message")

                        runOnUiThread {
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                        }

                    }
                }


            })


        }

        signUpBtn.setOnClickListener {
            val myIntent = Intent(mContext, SignUpActivity::class.java)
            startActivity(myIntent)
        }

    }


    override fun setValues() {
    }
}