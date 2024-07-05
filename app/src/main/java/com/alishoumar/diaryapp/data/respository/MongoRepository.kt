package com.alishoumar.diaryapp.data.respository

import com.alishoumar.diaryapp.model.Diary
import com.alishoumar.diaryapp.model.RequestState
import io.realm.kotlin.types.ObjectId
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

typealias Diaries = RequestState<Map<LocalDate, List<Diary>>>
interface MongoRepository {
    fun configureTheRealm()

    fun getAllDiaries(): Flow<Diaries>

    fun getSelectedDiary(diaryId: io.realm.kotlin.types.ObjectId) :Flow<RequestState<Diary>>

    suspend fun insertDiary(diary:Diary) : RequestState<Diary>
    suspend fun updateDiary(diary:Diary) : RequestState<Diary>

    suspend fun deleteDiary(diaryId: ObjectId) : RequestState<Diary>
}