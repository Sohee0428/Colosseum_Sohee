package com.example.colosseum_sohee.utils

import android.content.Context
import android.util.Log
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.json.JSONObject
import java.io.IOException
import java.util.logging.Handler

class ServerUtil {

    //    어떤 내용 => 서버 연결 전담
    companion object {

        interface JsonResponseHandler {
            fun onResponse(jsonObj: JSONObject)
        }

        //    모든 기능의 기본이 되는 주소
        val BASE_URL = "http://54.180.52.26"

        //        로그인 하는 기능
        fun postRequestLogin(email: String, pw: String, handler: JsonResponseHandler?) {

//            서버에 입력받은 email, pw 전달 => 로그인 기능 POST /user 로 전달 => 요청(Request) 실행
//            라이브러리 (OkHttp) 활용해서 짜보기

//            http://54.180.52.26/user + POST + 파라미터들 첨부

//            호스트 부소 + 기능 주소 결합
            val urlString = "${BASE_URL}/user"

//            POST 방식 => 파라미터를 폼 데이터에 담아주자 (짐 싸주기)

            val formData = FormBody.Builder()
                .add("email", email)
                .add("password", pw)
                .build()

//            어디로(주소) 어떤 방식으로 무엇을 들고 갈것인지 ahen whdgkqgoens request 변수 생성
            val request = Request.Builder()
                .url(urlString)
                .post(formData)
                .build()

//            클라이언트로써의 동작 : requst 요청 실행 => OkHttp 라이브러리 지원
            val client = OkHttpClient()

//            실제로 서버에 요청 날리기 => 갔다 와서는 뭘 할것인지
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
//                    서버에 연결 자체를 실패한 경우 - 서버 마비, 인터넷 단선
                }

                override fun onResponse(call: Call, response: Response) {
//                    로그인 성공, 로그인 실패 (연결 성공 -> 검사 실패) - 응답이 돌아온 경우

//                    응답 본문을 STRING으로 저장
                    val bodyString = response.body!!.string()

//                    bodyString 변수에는 한글이 깨져있다. => JSONObject로 변환하면 한글 정상 처리
                    val jsonObj = JSONObject(bodyString)

                    Log.d("응답 본문", jsonObj.toString())

//                    handler 변수가 null이 아니라면 (실체가 있다면) - 그 내부에 적힌 내용 실행
                    handler?.onResponse(jsonObj)
                }
            }
            )

        }

        //        회원가입 하는 기능
        fun putRequestSignUp(
            email: String,
            pw: String,
            nickname: String,
            handler: JsonResponseHandler?
        ) {

//            서버에 입력받은 email, pw, nick 전달 => 회원가입 기능 PUT /user 로 전달 => 요청(Request) 실행
//            라이브러리 (OkHttp) 활용해서 짜보기

//            http://54.180.52.26/user + PUT + 파라미터들 첨부

//            호스트 부소 + 기능 주소 결합
            val urlString = "${BASE_URL}/user"

//            PUT 방식 => 파라미터를 폼 데이터에 담아주자 (짐 싸주기)

            val formData = FormBody.Builder()
                .add("email", email)
                .add("password", pw)
                .add("nick_name", nickname)
                .build()

//            어디로(주소) 어떤 방식으로 무엇을 들고 갈것인지 모두 종합해둔 request 변수 생성
            val request = Request.Builder()
                .url(urlString)
                .put(formData)
                .build()


//            여기 부터는 동일


//            클라이언트로써의 동작 : requst 요청 실행 => OkHttp 라이브러리 지원
            val client = OkHttpClient()

//            실제로 서버에 요청 날리기 => 갔다 와서는 뭘 할것인지
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
//                    서버에 연결 자체를 실패한 경우 - 서버 마비, 인터넷 단선
                }

                override fun onResponse(call: Call, response: Response) {
//                    로그인 성공, 로그인 실패 (연결 성공 -> 검사 실패) - 응답이 돌아온 경우

//                    응답 본문을 STRING으로 저장
                    val bodyString = response.body!!.string()

//                    bodyString 변수에는 한글이 깨져있다. => JSONObject로 변환하면 한글 정상 처리
                    val jsonObj = JSONObject(bodyString)

                    Log.d("응답 본문", jsonObj.toString())

//                    handler 변수가 null이 아니라면 (실체가 있다면) - 그 내부에 적힌 내용 실행
                    handler?.onResponse(jsonObj)
                }
            }
            )

        }

        //        이메일 / 닉네임 중복 확인 기능
        fun getRequestDuplCheck(type: String, value: String, handler: JsonResponseHandler?) {

//            어디로 가느냐 + 어떤 데이터 인지를 같이 명시
//            URL 저적으면서 + 파라미터 첨부도 같이 => 보조도구(Builder)

            val urlBuilder = "${BASE_URL}/user_check".toHttpUrlOrNull()!!.newBuilder()

            urlBuilder.addEncodedQueryParameter("type", type)
            urlBuilder.addEncodedQueryParameter("value", value)

            val urlString = urlBuilder.build().toString()

            Log.d("완성된 url", urlString)

            val request = Request.Builder()
                .url(urlString)
                .get()
                .build()

            val client = OkHttpClient()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {

                    val bodyString = response.body!!.string()

                    val jsonObj = JSONObject(bodyString)
                    Log.d("서버 응답 본문", jsonObj.toString())

                    handler?.onResponse(jsonObj)

                }


            })

        }

        //        진행중인 주제 목록 확인 기능
        fun getRequestMainInfo(context: Context, handler: JsonResponseHandler?) {

//            어디로 가느냐 + 어떤 데이터 인지를 같이 명시
//            URL 저적으면서 + 파라미터 첨부도 같이 => 보조도구(Builder)

            val urlBuilder = "${BASE_URL}/v2/main_info".toHttpUrlOrNull()!!.newBuilder()

//            urlBuilder.addEncodedQueryParameter("type", type)
//            urlBuilder.addEncodedQueryParameter("value", value)

            val urlString = urlBuilder.build().toString()

            Log.d("완성된 url", urlString)

            val request = Request.Builder()
                .url(urlString)
                .get()
                .header("X-Http-Token", ContextUtil.getToken(context))
                .build()

            val client = OkHttpClient()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {

                    val bodyString = response.body!!.string()

                    val jsonObj = JSONObject(bodyString)
                    Log.d("서버 응답 본문", jsonObj.toString())

                    handler?.onResponse(jsonObj)

                }


            })

        }

        //        원하는 주제 상세현황 확인 기능
        fun getRequestTopicDetail(context: Context, topicId: Int, handler: JsonResponseHandler?) {

//            어디로 가느냐 + 어떤 데이터 인지를 같이 명시
//            URL 저적으면서 + 파라미터 첨부도 같이 => 보조도구(Builder)

//                주소를 정하는 방법 2가지
//            val urlBuilder = "${BASE_URL}/topic/${topicId}".toHttpUrlOrNull()!!.newBuilder()
            val urlBuilder = "${BASE_URL}/topic".toHttpUrlOrNull()!!.newBuilder()
            urlBuilder.addEncodedPathSegment(topicId.toString())

            urlBuilder.addEncodedQueryParameter("order_type", "NEW")
//            urlBuilder.addEncodedQueryParameter("value", value)

            val urlString = urlBuilder.build().toString()

            Log.d("완성된 url", urlString)

            val request = Request.Builder()
                .url(urlString)
                .get()
                .header("X-Http-Token", ContextUtil.getToken(context))
                .build()

            val client = OkHttpClient()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {

                    val bodyString = response.body!!.string()

                    val jsonObj = JSONObject(bodyString)
                    Log.d("서버 응답 본문", jsonObj.toString())

                    handler?.onResponse(jsonObj)

                }


            })

        }

        //        특정 진영에 투표하기
        fun postRequestVote(context: Context, sideId: Int, handler: JsonResponseHandler?) {

            val urlString = "${BASE_URL}/topic_vote"

            val formData = FormBody.Builder()
                .add("side_id", sideId.toString())
                .build()

            val request = Request.Builder()
                .url(urlString)
                .post(formData)
                .header("X-Http-Token", ContextUtil.getToken(context))
                .build()

//            클라이언트로써의 동작 : requst 요청 실행 => OkHttp 라이브러리 지원
            val client = OkHttpClient()

//            실제로 서버에 요청 날리기 => 갔다 와서는 뭘 할것인지
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
//                    서버에 연결 자체를 실패한 경우 - 서버 마비, 인터넷 단선
                }

                override fun onResponse(call: Call, response: Response) {
//                    로그인 성공, 로그인 실패 (연결 성공 -> 검사 실패) - 응답이 돌아온 경우

//                    응답 본문을 STRING으로 저장
                    val bodyString = response.body!!.string()

//                    bodyString 변수에는 한글이 깨져있다. => JSONObject로 변환하면 한글 정상 처리
                    val jsonObj = JSONObject(bodyString)

                    Log.d("응답 본문", jsonObj.toString())

//                    handler 변수가 null이 아니라면 (실체가 있다면) - 그 내부에 적힌 내용 실행
                    handler?.onResponse(jsonObj)
                }
            }
            )

        }

        //        의견 등록하기
        fun postRequestReply(
            context: Context,
            topicId: Int,
            content: String,
            handler: JsonResponseHandler?
        ) {

            val urlString = "${BASE_URL}/topic_reply"

            val formData = FormBody.Builder()
                .add("topic_id", topicId.toString())
                .add("content", content)
                .build()

            val request = Request.Builder()
                .url(urlString)
                .post(formData)
                .header("X-Http-Token", ContextUtil.getToken(context))
                .build()

//            클라이언트로써의 동작 : requst 요청 실행 => OkHttp 라이브러리 지원
            val client = OkHttpClient()

//            실제로 서버에 요청 날리기 => 갔다 와서는 뭘 할것인지
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
//                    서버에 연결 자체를 실패한 경우 - 서버 마비, 인터넷 단선
                }

                override fun onResponse(call: Call, response: Response) {
//                    로그인 성공, 로그인 실패 (연결 성공 -> 검사 실패) - 응답이 돌아온 경우

//                    응답 본문을 STRING으로 저장
                    val bodyString = response.body!!.string()

//                    bodyString 변수에는 한글이 깨져있다. => JSONObject로 변환하면 한글 정상 처리
                    val jsonObj = JSONObject(bodyString)

                    Log.d("응답 본문", jsonObj.toString())

//                    handler 변수가 null이 아니라면 (실체가 있다면) - 그 내부에 적힌 내용 실행
                    handler?.onResponse(jsonObj)
                }
            }
            )

        }

        //        의견에 좋아요 / 싫어요 찍기
        fun postRequestLikeOrDislike(
            context: Context,
            replyId: Int,
            isLike: Boolean,
            handler: JsonResponseHandler?
        ) {

            val urlString = "${BASE_URL}/topic_reply_like"

            val formData = FormBody.Builder()
                .add("reply_id", replyId.toString())
                .add("is_like", isLike.toString())
                .build()

            val request = Request.Builder()
                .url(urlString)
                .post(formData)
                .header("X-Http-Token", ContextUtil.getToken(context))
                .build()

//            클라이언트로써의 동작 : requst 요청 실행 => OkHttp 라이브러리 지원
            val client = OkHttpClient()

//            실제로 서버에 요청 날리기 => 갔다 와서는 뭘 할것인지
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
//                    서버에 연결 자체를 실패한 경우 - 서버 마비, 인터넷 단선
                }

                override fun onResponse(call: Call, response: Response) {
//                    로그인 성공, 로그인 실패 (연결 성공 -> 검사 실패) - 응답이 돌아온 경우

//                    응답 본문을 STRING으로 저장
                    val bodyString = response.body!!.string()

//                    bodyString 변수에는 한글이 깨져있다. => JSONObject로 변환하면 한글 정상 처리
                    val jsonObj = JSONObject(bodyString)

                    Log.d("응답 본문", jsonObj.toString())

//                    handler 변수가 null이 아니라면 (실체가 있다면) - 그 내부에 적힌 내용 실행

                    handler?.onResponse(jsonObj)
                }
            }
            )

        }
    }
}