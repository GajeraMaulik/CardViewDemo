package com.example.cardviewdemo.viewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.cardviewdemo.services.model.Chats
import com.example.cardviewdemo.services.repository.FirebaseInstanceDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import java.util.*

class DatabaseViewModel : ViewModel(){
    var instance: FirebaseInstanceDatabase? = null
    var successAddUserDb: LiveData<Boolean>?= null
    var fetchUserCurrentData: LiveData<DataSnapshot>? = null
    var fetchUserNames: LiveData<DataSnapshot>?=null
    var fetchSelectedProfileUserData: LiveData<DataSnapshot>?=null
    var fetchlastmessage:LiveData<DataSnapshot>?=null
    var successAddChatDb: LiveData<Boolean>?=null
     var fetchedChat : LiveData<DataSnapshot>?= null
    var imageFileReference: LiveData<StorageReference>?=null
    var successAddImageUrlInDatabase: LiveData<Boolean>?=null
    var successAddUsernameInDatabase: LiveData<Boolean>?=null
    var successAddBioInDatabase: LiveData<Boolean>?=null
    var successAddStatusInDatabase: LiveData<Boolean>?=null
    var fetchSearchUser: LiveData<DataSnapshot>?=null
    var successAddIsSeen: LiveData<Boolean>?=null
    var getChaListUserDataSnapshot: LiveData<DataSnapshot>?=null
    var getTokenRefDb: LiveData<DatabaseReference>?=null



    init{

        instance = FirebaseInstanceDatabase()
    }



    fun addUserDatabase(userId: String, userName: String, emailId: String, timestamp: String,imageUrl: String) {
        successAddUserDb = instance?.addUserInDatabase(userId, userName, emailId, timestamp,imageUrl)
    }

    fun fetchingUserDataCurrent() {
         fetchUserCurrentData = instance?.fetchUserDataCurrent()
    }

    fun fetchUserByNameAll() {
        fetchUserNames = instance?.fetchAllUserByNames()
    }

    fun fetchSelectedUserProfileData(userId: String) {
        fetchSelectedProfileUserData = instance?.fetchSelectedUserIdData(userId)
    }

    fun fetchlastmessage(userId: String){
        fetchlastmessage = instance?.fetchlastmessage(userId)
    }

    fun addChatDb(currentUserId: String?,receiverId : String?, message: String?, timestamp: Long?) {
        successAddChatDb =
            instance?.addChatsInDatabase(currentUserId!!, receiverId!!, message!!, timestamp!!)
    }
  /*  fun addChatDb1(comunication: String?, message: String?, timestamp: Long?) {
        successAddChatDb =
            instance?.addChatsInDatabase1(comunication!!, message!!, timestamp!!)
    }*/

    fun fetchChatUser() {
        fetchedChat = instance?.fetchChatUser()!!
    }
  /*  fun fetchChatUser1() {
        fetchedChat = instance?.fetchChatUser1()!!
    }*/

    fun fetchImageFileReference(timeStamp: String?, imageUri: Uri?, context: Context?) {
        imageFileReference = instance?.fetchFileReference(timeStamp!!, imageUri!!, context!!)
    }

    fun addImageUrlInDatabase(imageUrl: String?, mUri: Any?) {
        successAddImageUrlInDatabase = instance?.addImageUrlInDatabase(imageUrl!!, mUri!!)
    }

    fun addBioInDatabase(bio: String?, bioUpdated: Any?) {
        successAddBioInDatabase = instance?.addBioInDatabase(bio!!, bioUpdated!!)
    }

    fun addUsernameInDatabase(usernameUpdated: String?, username: Any?) {
        successAddUsernameInDatabase = instance?.addUsernameInDatabase(usernameUpdated!!, username!!)
    }

    fun addStatusInDatabase(statusUpdated: String?, status: Any?) {
        successAddStatusInDatabase = instance?.addStatusInDatabase(statusUpdated!!, status!!)
    }

    fun fetchSearchedUser(searchQuery: String?) {
        fetchSearchUser = instance?.fetchSearchUser(searchQuery!!)
    }

    fun addIsSeenInDatabase(isSeen: String?, dataSnapshot: DataSnapshot?) {
        successAddIsSeen = instance?.addIsSeenInDatabase(isSeen!!, dataSnapshot!!)
    }

    fun getChaListUserDataSnapshot(currentUserId: String?) {
        getChaListUserDataSnapshot = instance?.getChatList(currentUserId)
    }

    fun getTokenDatabaseRef() {
        getTokenRefDb = instance?.tokenRef
    }


}
