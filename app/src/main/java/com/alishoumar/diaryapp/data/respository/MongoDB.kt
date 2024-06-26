package com.alishoumar.diaryapp.data.respository

import com.alishoumar.diaryapp.model.Diary
import com.alishoumar.diaryapp.util.Constants.APP_ID
import com.alishoumar.diaryapp.util.RequestState
import com.alishoumar.diaryapp.util.toInstant
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.log.LogLevel
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId
import java.time.ZoneId

object MongoDB :MongoRepository{
    private val app = App.create(APP_ID)
    private val user = app.currentUser
    private lateinit var realm:Realm

    init {
        configureTheRealm()
    }
    override fun configureTheRealm() {
        if(user!= null){
            val config = SyncConfiguration
                .Builder(user, setOf(Diary::class))
                .initialSubscriptions{
                    sub ->
                    add(
                      query =  sub.query<Diary>("ownerId == $0", user.identity),
                        name = "User's Diaries"
                    )
                }.log(LogLevel.ALL)
                .build()

            realm = Realm.open(config)
        }
    }

    override fun getAllDiaries(): Flow<Diaries> {
        return if(user!= null){
            try {
                realm.query<Diary>(query = "ownerId ==$0" , user.identity)
                    .sort(property = "date", sortOrder = Sort.DESCENDING)
                    .asFlow()
                    .map {
                        result ->
                        RequestState.Success(
                            data = result.list.groupBy {
                                it.date.toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                            }
                        )
                    }
            }catch (e: Exception){
                flow { emit(RequestState.Error(UserNotAuthenticatedException())) }
            }
        }else{
            flow { emit(RequestState.Error(UserNotAuthenticatedException())) }
        }
    }

    override fun getSelectedDiary(diaryId: io.realm.kotlin.types.ObjectId): Flow<RequestState<Diary>> {
        return if(user != null ){
            try {
                realm.query<Diary>(query = "_id == $0", diaryId).asFlow().map {
                    RequestState.Success(data = it.list.first())
                }
            }catch (e:Exception){
                flow { emit(RequestState.Error(e))}
            }
        }else{
           flow { emit(RequestState.Error(UserNotAuthenticatedException()))}
        }
    }

    override suspend fun insertDiary(diary: Diary): RequestState<Diary> {
        return if (user != null){
            realm.write {
                try {
                    val addedDiary = copyToRealm(diary.apply {
                        ownerId = user.identity
                    })
                    RequestState.Success(data = addedDiary)
                }catch (e:Exception){
                    RequestState.Error(e)
                }
            }

        }else {
            RequestState.Error(UserNotAuthenticatedException())
        }
    }

    private class UserNotAuthenticatedException : Exception("User is not logged in")
}