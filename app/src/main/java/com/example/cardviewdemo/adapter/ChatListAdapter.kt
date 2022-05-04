package com.example.cardviewdemo.adapter
import android.app.Activity
import android.app.Notification
import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
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
import com.example.cardviewdemo.services.model.ChatList
import com.example.cardviewdemo.services.model.Chats
import com.example.cardviewdemo.services.model.Users
import com.example.cardviewdemo.services.repository.FirebaseInstanceDatabase
import com.example.cardviewdemo.viewModel.DatabaseViewModel
import com.google.android.material.internal.TextDrawableHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

var unreads=0
var unseen=0

class ChatListAdapter: RecyclerView.Adapter<ChatListAdapter.ChatListHolder> {
    private val firebaseUser = FirebaseAuth.getInstance().currentUser!!

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

    var chatsArrayList =ArrayList<ChatList?>()
    var chats = Chats()
    var timeStamp: Long? = null
    var isChat: Boolean = false
        var seensize =ArrayList<Boolean>()

    constructor() {

    }

    constructor(chatsArrayList: ArrayList<ChatList?>, context: Context, isChat: Boolean) {
        this.chatsArrayList = chatsArrayList
        this.context = context
        this.isChat = isChat
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




        d("seen1","-------->$channelid")
        firebaseInstanceDatabase.instance.child("Chats").child("$channelid").child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(datasnapshot: DataSnapshot) {
                    d("seen1", "seen: $datasnapshot")
                        var unread = 0
                    for (snapshot in datasnapshot.children) {
                        val chats = snapshot.getValue(Chats::class.java)
                        d("seen1","seen1 : ${chats!!.getSeen()}")
                       // seensize = chats.seen as ArrayList<Boolean>
                        val seen=snapshot.child("seen").getValue(Boolean::class.java)!!


                        d("seen","count----20---$seen")
                        if (chats.getReceiverId().equals(firebaseUser.uid) && seen ==false){
                            unread++
                            d("seen","count---31-$unreads")
                        }
                        d("seen","count---1-$unreads")

                    }
                    d("TAG","count----2---$unreads")
                     unseen = unread
                    d("seen","--->$unseen")
                    SharePref.save(context,"unseen","$unseen")
                    if (unread != 0) {
                        holder.tv_onseen_message.visibility = View.VISIBLE
                        holder.tv_onseen_message.text = unseen.toString()
                    }


                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

       // firebaseInstanceDatabase.instance.child()

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
                        //d("dgdfhdfh","${chatList.getUnreadmessage()}")
                        //    holder.tv_onseen_message.visibility = View.GONE
                        d("ch", "cha : ${chatList.getchannelid()}")
                        context.startActivity(intent)
                        Activity().finish()
                    }
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