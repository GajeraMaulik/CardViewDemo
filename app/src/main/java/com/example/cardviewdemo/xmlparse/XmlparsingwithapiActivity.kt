package com.example.cardviewdemo.xmlparse

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.cardviewdemo.adapter.NewsAdapter
import com.example.cardviewdemo.databinding.ActivityXmlparsingwithapiBinding
import com.example.cardviewdemo.services.APIServices
import com.example.cardviewdemo.services.model.Item
import com.example.cardviewdemo.services.model.Source
import com.example.cardviewdemo.services.Client
import org.xml.sax.SAXException
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.io.InputStream
import javax.xml.parsers.ParserConfigurationException
import android.util.Log.d
import android.util.Xml
import com.example.cardviewdemo.R
import org.xmlpull.v1.XmlPullParserException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import java.net.URL
import kotlin.collections.ArrayList


lateinit var binding: ActivityXmlparsingwithapiBinding
lateinit var apiService: APIServices
lateinit var sourceList: MutableList<Source>
//lateinit var guidList:MutableList<Guid>
lateinit var itemList:ArrayList<Item>
//lateinit var newsAdapter: NewsAdapter
//lateinit var guid: Guid
lateinit var  isinput:InputStream
//var source = Source()
lateinit var item: Item
lateinit var pd:ProgressDialog
lateinit var apiServices: APIServices

@SuppressLint("StaticFieldLeak")
var c: Context? = null
var rv: RecyclerView? = null

var BASE_URL="https://news.google.com/"
var ns:String?=null
class XmlparsingwithapiActivity  :AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityXmlparsingwithapiBinding.inflate(layoutInflater)
        setContentView(binding.root)


     //   guid = Guid()
        //     source = Source()
        sourceList = ArrayList()
        //guidList = ArrayList()
        itemList= ArrayList()

        d("010101","01001 ---->$itemList")


        apiService = Client.getNews(BASE_URL).create(APIServices::class.java)

      //  DownloadXmlTask().downloadUrl("https://news.google.com/rss?hl=en-IN&gl=IN&ceid=IN:en/")

//        isinput=URL(BASE_URL).openStream()



        apiService.getItem()
            .enqueue(object :Callback<Item>{
                override fun onResponse(call: Call<Item>, response: Response<Item>) {
                    if (response.code() == 200) {
                        assert(response.body() != null)
                        if (response.body()?.success != 1) {
                          //  parseRss(isinput)
                        parse(isinput)
                        binding.rvnews.adapter = NewsAdapter(applicationContext, itemList,
                                arrayOf("title","link","pubDate","Description"), intArrayOf(R.id.Title,R.id.Link,R.id.pubDate,R.id.Description))

                       }
                    }
                }

                override fun onFailure(call: Call<Item>, t: Throwable) {

                }

            })
    }

/*
    inner class DownloadXmlTask : AsyncTask<String, Unit, Boolean>() {
        override fun onPreExecute() {
            super.onPreExecute()
            pd.setTitle("Parse");
            pd.setMessage("Parsing...Please wait");
            pd.show();
        }

        override fun doInBackground(vararg p0: String?): Boolean {
            return parseRss()
        }

        override fun onPostExecute(isParsed: Boolean) {
            super.onPostExecute(isParsed);

            pd.dismiss();
            if (isParsed) {
                //BIND

            } else {
                Toast.makeText(c, "Unable To Parse", Toast.LENGTH_SHORT).show();
            }
        }
    }
*/

        @Throws(IOException::class)
         fun downloadUrl(urlString: String): InputStream? {
            val url = URL(urlString)
            return (url.openConnection() as? HttpURLConnection)?.run {
                readTimeout = 10000
                connectTimeout = 15000
                requestMethod = "GET"
                doInput = true
                // Starts the query
                connect()
                inputStream
            }
        }


        fun parseRss(inputStream: InputStream): Boolean {

            try {

                val factory: XmlPullParserFactory = XmlPullParserFactory.newInstance()
                val parser: XmlPullParser = factory.newPullParser()


                parser.setInput(isinput, null)

                var event = parser.eventType

                var tagValue: String? = null
                var isSiteMeta = true

                itemList.clear()
                itemList = ArrayList()

                do {
                    val tagName = parser.name

                    when (event) {
                        XmlPullParser.START_TAG ->
                            if (tagName.equals("rss")){
                                isSiteMeta =false
                            }
                        XmlPullParser.START_TAG ->
                            if (tagName.equals("channel")){
                                isSiteMeta=false
                            }
                        XmlPullParser.START_TAG ->
                            if (tagName.equals("item")) {
                                isSiteMeta = false
                            }

                        XmlPullParser.TEXT ->
                            tagValue = parser.text
                        XmlPullParser.END_TAG ->
                            if (!isSiteMeta) {
                                if (tagName.equals("title")) {
                                    item.setTitle(tagValue!!)
                                } else if (tagName.equals("link", true)) {
                                    val link = tagValue
                                    item.setLink(link!!)
                                } else if (tagName.equals("pubDate", true)) {
                                    val date: String? = tagValue
                                    item.setPubDate(date!!)
                                } else if (tagName.equals("description", true)) {
                                    val deac = tagValue
                                    item.setDescription(deac!!.substring(deac.indexOf("/>") + 2))


                                }
                            }
                    }
                    if (tagName.equals("item", true)) {
                        itemList.add(item)
                        isSiteMeta = true
                    }

                    event = parser.next()

                } while (event != XmlPullParser.END_DOCUMENT)

                return true

            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: ParserConfigurationException) {
                e.printStackTrace()
            } catch (e: SAXException) {
                e.printStackTrace()
            }
            return false
        }
    fun parse(inputStream: InputStream): Item {
        inputStream.use { inputStream->
            val parse:XmlPullParser= Xml.newPullParser()
            parse.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false)
            parse.setInput(inputStream,null)
            parse.nextTag()
            return readdata(parse)
        }
    }
    fun readdata(parser: XmlPullParser): Item {
        parser.require(XmlPullParser.START_TAG, ns, "channel")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            if (parser.name == "item") {
                itemList.add(oderinfo(parser))
            }else{
                skip(parser)
            }
        }
        return Item()
    }
    fun oderinfo(parser: XmlPullParser): Item {
        parser.require(XmlPullParser.START_TAG, ns, "item")
        var title: String?=""
        var link: String?=""
        var pubDate: String?=""
        var  description: String?=""

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when (parser.name) {
                "Title" -> title =  readTitle(parser)
                "Link" -> link = readLink(parser)
                "PubDate" -> pubDate = readPubdate(parser)
                "Description" ->description = readDesc(parser)
                else -> skip(parser)
            }
        }
        return Item(description!!,link!!,pubDate!!,title!!)
    }
    fun readTitle(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "title")
        val title = readString(parser)
        parser.require(XmlPullParser.END_TAG,ns,"title")
        return title
    }
    fun readLink(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "link")
        val link = readString(parser)
        parser.require(XmlPullParser.END_TAG,ns,"link")
        return link
    }
    fun readPubdate(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "pubDate")
        val pubDate = readString(parser)
        parser.require(XmlPullParser.END_TAG,ns,"pubDate")
        return pubDate
    }
    fun readDesc(parser: XmlPullParser): String {
        parser.require(XmlPullParser.START_TAG, ns, "description")
        val description = readString(parser)
        parser.require(XmlPullParser.END_TAG,ns,"description")
        return description
    }

    @Throws(XmlPullParserException::class, IOException::class)
    private fun readString(parser: XmlPullParser): String {
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        return result
    }
    private fun skip(parser: XmlPullParser) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            throw IllegalStateException()
        }
        var depth = 1
        while (depth != 0) {
            when (parser.next()) {
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.START_TAG -> depth++
            }
        }
    }

}
