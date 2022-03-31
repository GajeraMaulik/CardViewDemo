package com.example.cardviewdemo.services.repository

import android.content.Context
import android.net.Uri
import android.util.Log.d
import android.webkit.MimeTypeMap
import androidx.lifecycle.MutableLiveData
import com.example.cardviewdemo.SharePref
import com.example.cardviewdemo.chat.*
import com.example.cardviewdemo.services.model.Chats
import com.example.cardviewdemo.services.model.Users
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*


class FirebaseInstanceDatabase {
     val instance = FirebaseDatabase.getInstance().reference
    private val firebaseUser = FirebaseAuth.getInstance().currentUser
    private val storageReference = FirebaseStorage.getInstance().getReference("Profile")
     var users=Users()
     var chats= Chats()
  lateinit var context:Context
    fun fetchAllUserByNames(): MutableLiveData<DataSnapshot> {
        val fetchAllUSerName = MutableLiveData<DataSnapshot>()
        instance.child("Users").orderByChild(firebaseUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    fetchAllUSerName.value = dataSnapshot
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        return fetchAllUSerName
    }

    private fun getFileExtension(uri: Uri, context: Context): String? {
        val contentResolver = Objects.requireNonNull(context).contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getMimeTypeFromExtension(contentResolver.getType(uri))
    }

    fun fetchFileReference(
        timeStamp: String,
        imageUri: Uri,
        context: Context,
    ): MutableLiveData<StorageReference> {
        val fetchFileReferenceImage = MutableLiveData<StorageReference>()
        val fileReference =
            storageReference.child(timeStamp + "." + getFileExtension(imageUri, context))
        fetchFileReferenceImage.value = fileReference
        return fetchFileReferenceImage
    }

    fun fetchSelectedUserIdData(userId: String?): MutableLiveData<DataSnapshot> {
        val fetchSelectedUserIDData = MutableLiveData<DataSnapshot>()
        instance.child("Users").child(userId!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    fetchSelectedUserIDData.value = dataSnapshot
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        return fetchSelectedUserIDData
    }

    val tokenRef: MutableLiveData<DatabaseReference>
        get() {
            val getTokenReference = MutableLiveData<DatabaseReference>()
            val reference = FirebaseDatabase.getInstance().getReference("Tokens")
            getTokenReference.value = reference
            return getTokenReference
        }

    fun fetchUserDataCurrent(): MutableLiveData<DataSnapshot> {
        val fetchCurrentUserData = MutableLiveData<DataSnapshot>()
        instance.child("Users").child(firebaseUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    fetchCurrentUserData.value = dataSnapshot
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        return fetchCurrentUserData
    }

    fun fetchSearchUser(searchString: String): MutableLiveData<DataSnapshot> {
      //  users = Users()

        val fetchSearchUserData = MutableLiveData<DataSnapshot>()
        val query = instance.child("Users").orderByChild("search")
            .startAt(searchString)
            .endAt(searchString + "\uf8ff")
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //d("TAG","fetchSearchUser : ${dataSnapshot.children("username")}")
               //  users  = dataSnapshot.getValue(Users::class.java)!!
                //val newName : String = users.getUsername()
                //d("TAG","fetchSearchUser : $newName")
                fetchSearchUserData.value = dataSnapshot
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        return fetchSearchUserData
    }

    fun fetchChatUser(): MutableLiveData<DataSnapshot> {

        val fetchUserChat = MutableLiveData<DataSnapshot>()
        instance.child("Chats").child("${newCurrentuser}_$newReceiver").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                d("TAG","before:$dataSnapshot")

                fetchUserChat.value = dataSnapshot

                d("TAG","after:$dataSnapshot")
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        return fetchUserChat
    }

/*    fun fetchChatUser1(): MutableLiveData<DataSnapshot> {

        val fetchUserChat = MutableLiveData<DataSnapshot>()
        instance.child("Chats").get()
            .addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                d("TAG","before:$dataSnapshot")

                fetchUserChat.value = dataSnapshot
                d("TAG","after:$dataSnapshot")
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        return fetchUserChat
    }*/

    fun addChatsInDatabase(
        currentUserId: String,
        receiverId: String,
        message: String,
        timestamp: Long,
    ): MutableLiveData<Boolean> {
        val successAddChatsDb = MutableLiveData<Boolean>()
        val hashMap = HashMap<String, Any>()
        hashMap["receiverId"] = receiverId
        hashMap["currentuserId"] = currentUserId
        hashMap["message"] = message
        hashMap["timestamp"] = timestamp
        hashMap["seen"] = false
        instance.child("Chats").child("${newCurrentuser}_$newReceiver").child("$timestamp")   .setValue(hashMap).addOnCompleteListener {
           // instance.child("Chats").child(receiverId).push().setValue(hashMap)

         successAddChatsDb.value = true
        }.addOnFailureListener { successAddChatsDb.value = false }



        //creating chatList in database for better performance in chatListFragment .
        val chatRef = instance.child("ChatList")
            .child(currentUserId)
            .child(receiverId)
        chatRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()) {
                    chatRef.child("id").setValue(receiverId)
                    chatRef.child("timestamp").setValue(timestamp)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        val chatRef2 = instance.child("ChatList")
            .child(receiverId)
            .child(currentUserId)
        chatRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()) {
                    chatRef2.child("id").setValue(currentUserId)
                    chatRef2.child("timestamp").setValue(timestamp)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        return successAddChatsDb
    }

/*    fun addChatsInDatabase1(
         comunication:String,
        message: String,
        timestamp: Long,
    ): MutableLiveData<Boolean> {
        val successAddChatsDb = MutableLiveData<Boolean>()
        val hashMap = HashMap<String, Any>()
        hashMap["comunication"] = comunication
        hashMap["message"] = message
        hashMap["timestamp"] = timestamp
        hashMap["seen"] = false
        instance.child("Chats").child(comunication).push().setValue(hashMap).addOnCompleteListener {
            // instance.child("Chats").child(receiverId).push().setValue(hashMap)

            successAddChatsDb.value = true
        }.addOnFailureListener { successAddChatsDb.value = false }

        //creating chatList in database for better performance in chatListFragment .
        val chatRef = instance.child("ChatList")
            .child(newCurrentuser!!)
          .child(newReceiver!!)
        chatRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()) {
                    chatRef.child("id").setValue(newCurrentuser!!)
                    chatRef.child("timestamp").setValue(timestamp)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        val chatRef2 = instance.child("ChatList")
            .child(newReceiver!!)
            .child(newCurrentuser!!)
        chatRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()) {
                    chatRef2.child("id").setValue(newReceiver)
                    chatRef2.child("timestamp").setValue(timestamp)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        return successAddChatsDb
    }*/

    fun getChatList(currentUserId: String?): MutableLiveData<DataSnapshot> {
        val getChatLists = MutableLiveData<DataSnapshot>()
        val chatRef = instance.child("ChatList")
            .child(firebaseUser!!.uid)
        chatRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                getChatLists.value = dataSnapshot
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        return getChatLists
    }

    fun addIsSeenInDatabase(isSeen: String, dataSnapshot: DataSnapshot): MutableLiveData<Boolean> {
        val successAddIsSeen = MutableLiveData<Boolean>()
        val map = HashMap<String, Any>()
        map[isSeen] = true
        dataSnapshot.ref.updateChildren(map).addOnSuccessListener { successAddIsSeen.value = true }
            .addOnFailureListener {
                successAddIsSeen.value = false
            }
        return successAddIsSeen
    }

    fun addImageUrlInDatabase(imageUrl: String, mUri: Any): MutableLiveData<Boolean> {
        val successAddUriImage = MutableLiveData<Boolean>()
        val reference = FirebaseDatabase.getInstance().getReference("Users").child(
            firebaseUser!!.uid)
        val map = HashMap<String, Any>()
        map[imageUrl] = mUri
        reference.updateChildren(map).addOnCompleteListener { successAddUriImage.value = true }
            .addOnFailureListener {
                successAddUriImage.value = false
            }
        return successAddUriImage
    }

    fun addUsernameInDatabase(usernameUpdated: String, username: Any): MutableLiveData<Boolean> {
        val successAddUserName = MutableLiveData<Boolean>()
        val reference = FirebaseDatabase.getInstance().getReference("Users").child(
            firebaseUser!!.uid)
        val map = HashMap<String, Any>()
        map[usernameUpdated] = username
        val searchUserNam = username.toString().toLowerCase() // changing search userName as well
        map["search"] = searchUserNam
        reference.updateChildren(map).addOnCompleteListener { successAddUserName.value = true }
            .addOnFailureListener {
                successAddUserName.value = false
            }
        return successAddUserName
    }

    fun addBioInDatabase(bioUpdated: String, bio: Any): MutableLiveData<Boolean> {
        val successAddBio = MutableLiveData<Boolean>()
        val reference = FirebaseDatabase.getInstance().getReference("Users").child(
            firebaseUser!!.uid)
        val map = HashMap<String, Any>()
        map[bioUpdated] = bio
        reference.updateChildren(map).addOnCompleteListener { successAddBio.value = true }
            .addOnFailureListener {
                successAddBio.value = false
            }
        return successAddBio
    }

    fun addStatusInDatabase(statusUpdated: String, status: Any): MutableLiveData<Boolean> {
        val successAddStatus = MutableLiveData<Boolean>()
        val id = firebaseUser!!.uid
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        val map = HashMap<String, Any>()
        map[statusUpdated] = status
        ref.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    ref.child(id).updateChildren(map)
                    successAddStatus.setValue(true)
                } else {
                    successAddStatus.setValue(false)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                successAddStatus.value = false
            }
        })
        return successAddStatus
    }

    fun addUserInDatabase(
        userId: String,
        userName: String,
        emailId: String,
        timestamp: String,
        imageUrl: String,
    ): MutableLiveData<Boolean> {
        val successAddUserDb = MutableLiveData<Boolean>()
        val hashMap = HashMap<String, String>()
        hashMap["id"] = userId
        hashMap["username"] = userName
        hashMap["emailId"] = emailId
        hashMap["timestamp"] = timestamp
        hashMap["imageUrl"] = imageUrl
        hashMap["bio"] = "Hey there!"
        hashMap["status"] = "offline"
        hashMap["search"] = userName.toLowerCase()
       // SharePref.save(context,"user","username")
        instance.child("Users").child(userId).setValue(hashMap).addOnCompleteListener {
            successAddUserDb.value = true
        }.addOnFailureListener { successAddUserDb.value = false }
        return successAddUserDb
    }
}
