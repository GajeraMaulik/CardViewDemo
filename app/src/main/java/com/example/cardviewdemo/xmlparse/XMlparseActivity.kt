package com.example.cardviewdemo.xmlparse

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.d
import android.util.Xml
import com.example.cardviewdemo.R
import com.example.cardviewdemo.adapter.OderinfoAdapter
import com.example.cardviewdemo.databinding.ActivityXmlparseBinding
import com.example.cardviewdemo.services.model.Orderinfo
import org.xml.sax.SAXException
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream
import javax.xml.parsers.ParserConfigurationException

class  XMlparseActivity : AppCompatActivity() {

    lateinit var binding: ActivityXmlparseBinding
    lateinit var orderinfoadapter: OderinfoAdapter
    lateinit var oderinfoList: MutableList<Orderinfo>
    val ns: String? = null

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityXmlparseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar= supportActionBar
        actionBar!!.title="XmlParser"
        actionBar.setDisplayHomeAsUpEnabled(true)

        oderinfoList = ArrayList()

        orderinfoadapter = OderinfoAdapter(oderinfoList, R.layout.item_orderinfo,
            arrayOf("ord_no",
                "cus_no",
                "item_no",
                "qty_ordered",
                "line_no",
                "cus_name",
                "upc_cd",
                "SCC14",
                "CasePack"),
            intArrayOf(R.id.ord_no,
                R.id.cus_no,
                R.id.item_no,
                R.id.qty_ordered,
                R.id.line_no,
                R.id.cus_name,
                R.id.upc_cd,
                R.id.Scc14,
                R.id.CasePack
            ))

        binding.rvoderInfo.adapter = orderinfoadapter


        try {
            val istrem: InputStream = assets.open("file1.xml")
            //val builderFactory = DocumentBuilderFactory.newInstance()
          //  val docBuilder = builderFactory.newDocumentBuilder()
          //  val doc = docBuilder.parse(istrem)
            //val nList = doc.getElementsByTagName("zwms")
             parse(istrem)
            orderinfoadapter.notifyDataSetChanged()
            d("11111", "======>$oderinfoList")
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ParserConfigurationException) {
            e.printStackTrace()
        } catch (e: SAXException) {
            e.printStackTrace()
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    fun parse(inputStream: InputStream): Orderinfo {
        inputStream.use { inputStream->
            val parse:XmlPullParser=Xml.newPullParser()
            parse.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,false)
            parse.setInput(inputStream,null)
            parse.nextTag()
            return readdata(parse)
        }
    }

     @Throws(XmlPullParserException::class, IOException::class)
    fun readdata(parser: XmlPullParser): Orderinfo {
        parser.require(XmlPullParser.START_TAG, ns, "zwms")
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            if (parser.name == "OrderInfo") {
                oderinfoList.add(oderinfo(parser))
            }else{
                skip(parser)
            }
        }
        return Orderinfo()
    }

    @Throws(XmlPullParserException::class, IOException::class)
    fun oderinfo(parser: XmlPullParser): Orderinfo {
        parser.require(XmlPullParser.START_TAG, ns, "OrderInfo")
        var ord_no: Int? = 0
        var cus_no: Int? = 0
        var item_no: String? = ""
        var qty_ordered: Double? = 0.0
        var line_no: Int? = 0
        var cus_name: String? = ""
        var upc_cd: String? = ""
        var SCC14: String? = ""
        var CasePack: String? = ""
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }
            when (parser.name) {
                "ord_no" -> ord_no =  readordno(parser)
                "cus_no" -> cus_no = readcusno(parser)
                "item_no" -> item_no = readitemno(parser)
                "qty_ordered" ->qty_ordered = readqtyordered(parser)
                "line_no" -> line_no = readlineno(parser)
                "cus_name" -> cus_name = readcusname(parser)
                "upc_cd" ->     upc_cd = readupccd(parser)
                "SCC14"  ->  SCC14     = readScc14(parser)
                "CasePack" -> CasePack = readCasepack(parser)
                else -> skip(parser)
            }
        }
        return Orderinfo(ord_no!!,cus_no!!,item_no!!,qty_ordered,line_no!!,cus_name!!,upc_cd!!,SCC14!!,CasePack!!)
    }

     @Throws(XmlPullParserException::class, IOException::class)
    fun readordno(parser: XmlPullParser): Int {
        parser.require(XmlPullParser.START_TAG, ns, "ord_no")
        val ord_no = readInt(parser)
        parser.require(XmlPullParser.END_TAG,ns,"ord_no")
        return ord_no
    }

   @Throws(XmlPullParserException::class, IOException::class)
    fun readcusno(parser: XmlPullParser): Int {
            parser.require(XmlPullParser.START_TAG, ns, "cus_no")
            val ord_no = readInt(parser)
            parser.require(XmlPullParser.END_TAG,ns,"cus_no")
            return ord_no
   }
     @Throws(XmlPullParserException::class, IOException::class)
   fun readitemno(parser: XmlPullParser): String {
           parser.require(XmlPullParser.START_TAG, ns, "item_no")
           val ord_no = readString(parser)
           parser.require(XmlPullParser.END_TAG,ns,"item_no")
           return ord_no
       }
     @Throws(XmlPullParserException::class, IOException::class)
      fun readqtyordered(parser: XmlPullParser): Double {
            parser.require(XmlPullParser.START_TAG, ns, "qty_ordered")
            val ord_no = readDouble(parser)
            parser.require(XmlPullParser.END_TAG,ns,"qty_ordered")
            return ord_no
        }
     @Throws(XmlPullParserException::class, IOException::class)
       fun readlineno(parser: XmlPullParser): Int {
                parser.require(XmlPullParser.START_TAG, ns, "line_no")
                val ord_no = readInt(parser)
                parser.require(XmlPullParser.END_TAG,ns,"line_no")
                return ord_no
            }
     @Throws(XmlPullParserException::class, IOException::class)
     fun readcusname(parser: XmlPullParser): String {
             parser.require(XmlPullParser.START_TAG, ns, "cus_name")
             val ord_no = readString(parser)
             parser.require(XmlPullParser.END_TAG,ns,"cus_name")
             return ord_no
         }
     @Throws(XmlPullParserException::class, IOException::class)
       fun readupccd(parser: XmlPullParser): String {
               parser.require(XmlPullParser.START_TAG, ns, "upc_cd")
               val ord_no = readString(parser)
               parser.require(XmlPullParser.END_TAG,ns,"upc_cd")
               return ord_no
           }
    @Throws(XmlPullParserException::class, IOException::class)
     fun readScc14(parser: XmlPullParser): String {
               parser.require(XmlPullParser.START_TAG, ns, "SCC14")
               val ord_no = readString(parser)
               parser.require(XmlPullParser.END_TAG,ns,"SCC14")
               return ord_no
           }
      @Throws(XmlPullParserException::class, IOException::class)
   fun readCasepack(parser: XmlPullParser): String {
           parser.require(XmlPullParser.START_TAG, ns, "CasePack")
           val ord_no = readString(parser)
           parser.require(XmlPullParser.END_TAG,ns,"CasePack")
           return ord_no
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

     @Throws(XmlPullParserException::class, IOException::class)
    private fun readInt(parser: XmlPullParser): Int {
        var result1 = 0
        if (parser.next() == XmlPullParser.TEXT) {
            result1 = parser.text.toInt()
            parser.nextTag()
        }
        return result1
    }

     @Throws(XmlPullParserException::class, IOException::class)
    private fun readDouble(parser: XmlPullParser): Double {
        var result2 = 0.0
        if (parser.next() == XmlPullParser.TEXT) {
            result2 = parser.text.toDouble()
            parser.nextTag()
        }
        return result2

    }

    @Throws(XmlPullParserException::class, IOException::class)
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

   override fun onSupportNavigateUp(): Boolean {
            onBackPressed()
            return true
   }
}
