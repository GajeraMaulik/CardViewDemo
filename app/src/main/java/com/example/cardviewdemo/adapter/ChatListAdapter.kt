package com.example.cardviewdemo.adapter
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cardviewdemo.R
import com.example.cardviewdemo.SharePref
import com.example.cardviewdemo.chat.*
import com.example.cardviewdemo.databinding.ActivityMessagingViewBinding
import com.example.cardviewdemo.fragments.BottomSheetProfileDetailUser
import com.example.cardviewdemo.fragments.unseen
import com.example.cardviewdemo.services.model.ChatList
import com.example.cardviewdemo.services.model.Chats
import com.example.cardviewdemo.services.model.Users
import com.example.cardviewdemo.services.repository.FirebaseInstanceDatabase
import com.example.cardviewdemo.viewModel.DatabaseViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import java.io.IOException
import java.util.HashMap
import kotlin.collections.ArrayList


class ChatListAdapter: RecyclerView.Adapter<ChatListAdapter.ChatListHolder> {


    lateinit var users : Users
    lateinit var binding: ActivityMessagingViewBinding
    lateinit var firebaseInstanceDatabase: FirebaseInstanceDatabase
    lateinit var bottomSheetProfileDetailUser: BottomSheetProfileDetailUser
    lateinit var databaseViewModel: DatabaseViewModel
    lateinit var context: Context
    lateinit var username:String
    lateinit var imageUrl:String
    lateinit var user_status:String
    lateinit var bio:String
    lateinit var intent:Intent

    lateinit var chatsArrayList :List<ChatList?>
    var chatsList = ChatList()
    var chats=Chats()
    var timeStamp: Long? = null
    var isChat: Boolean = false
        var seensize =ArrayList<Boolean>()

    constructor() {

    }

    constructor(chatsArrayList: List<ChatList?>, context: Context, isChat: Boolean) {
        this.context = context
        this.isChat = isChat
        this.chatsArrayList=chatsArrayList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int, ): ChatListHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_list_item_view, parent, false)
        init(view)
        return ChatListHolder(view)
    }
    private fun init(view: View) {
        firebaseInstanceDatabase = FirebaseInstanceDatabase()
        databaseViewModel = DatabaseViewModel()
        users = Users()
        intent = Intent()


    }

    override fun onBindViewHolder(holder:ChatListHolder, position: Int) {
        val chatList = chatsArrayList[position]


      //  firebaseInstanceDatabase.instance.child("Chatlist")

        Log.d("dsgrgrewgz", chatList?.getUnreadmessage().toString())

        firebaseInstanceDatabase.instance.child("Users").child(chatList!!.getId().toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val users = snapshot.getValue(Users::class.java)
                    //   val chatlist= snapshot.getValue(ChatList::class.java)
                    user_status = users!!.getStatus()
                    imageUrl = users.getImageUrl()
                    username = users.getUsername()
                    bio = users.getBio()
                    holder.tv_name.text = username


                    d("user", "chatList : $username")
                    if (isChat) {
                        try {
                            if (user_status.contains("online") && isNetworkConnected()) {
                                holder.iv_status_user_list.setBackgroundResource(R.drawable.online_status)
                            } else {
                                holder.iv_status_user_list.setBackgroundResource(R.drawable.offline_status)
                            }
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    } else {
                        holder.iv_status_user_list.visibility = View.GONE
                    }
                    if (imageUrl == "default") {
                        holder.iv_profile_image.setImageResource(R.drawable.sample_img)
                    } else {
                        Glide.with(context.applicationContext).asBitmap().load(imageUrl)
                            .into(holder.iv_profile_image)
                    }
                    holder.itemView.setOnClickListener {

                        val intent = Intent(context, MessagingActivity::class.java)
                        intent.putExtra("userId", users.getId())
                        intent.putExtra("channelid", chatList.getchannelid())
                        //    holder.tv_onseen_message.visibility = View.GONE
                        d("euijhj", "cha : ${chatList.getchannelid()}")
                        d("euijhj","----->${chatList.getId()}")
                        context.startActivity(intent)
                        d("jdiaia","=======$userId_receiver")
                        d("jdiaia","=======$channelid")
                       setunreadcounter()

                        Activity().finish()


                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        firebaseInstanceDatabase.instance.child("ChatList").child("$userId_receiver").child("$channelid")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    d("value","before$snapshot")
                    val unseen =snapshot.child("unreadsmessage").value
                    d("value","$unseen")
                    holder.tv_onseen_message.text= unseen.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })

        holder.tv_last_message.text = chatList.getlastmsg()

        holder.iv_profile_image.setOnClickListener(View.OnClickListener {
            bottomSheetProfileDetailUser =
                BottomSheetProfileDetailUser(username, imageUrl, bio)
            val manager = (context as AppCompatActivity?)!!.supportFragmentManager
            bottomSheetProfileDetailUser.show(manager, "edit")
        })
    }
    fun setunreadcounter(){
        val sender_unseen= HashMap<String,Any>()
        sender_unseen["unreadsmessage"]=0
        val receiver_unseen= HashMap<String,Any>()
        receiver_unseen["unreadsmessage"]= unseen

        val userId =intent.getStringExtra("userid")
       // d("jdiaia","=======$userId_receiver")
       // d("jdiaia","=======$channelid")


        firebaseInstanceDatabase.instance.child("ChatList").child("$userId_receiver")
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.child("$channelid").ref.orderByChild("unreadsmessage").ref.setValue(0)
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }

    @Throws(InterruptedException::class, IOException::class)
    fun isNetworkConnected(): Boolean {
        val command = "ping -c 1 google.com"
        return Runtime.getRuntime().exec(command).waitFor() == 0
    }

    override fun getItemCount(): Int {
        return chatsArrayList.size
    }
    class ChatListHolder internal constructor(itemView:View):RecyclerView.ViewHolder(itemView){
        var iv_profile_image: CircleImageView = itemView.findViewById(R.id.profile_image)
        var tv_name: TextView = itemView.findViewById(R.id.user_name_list)
        var iv_status_user_list: ImageView = itemView.findViewById(R.id.iv_status_user_list)
        var tv_last_message: TextView = itemView.findViewById(R.id.last_message)
        var tv_onseen_message:TextView=itemView.findViewById(R.id.onseenMessage)
//        var iv_chat_fragment_empty:ImageView=itemView.findViewById(R.id.iv_chat_fragment_empty)

    }
}


