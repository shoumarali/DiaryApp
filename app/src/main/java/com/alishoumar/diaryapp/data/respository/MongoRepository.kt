package com.alishoumar.diaryapp.data.respository

import com.alishoumar.diaryapp.model.Diary
import com.alishoumar.diaryapp.util.RequestState
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

typealias Diaries = RequestState<Map<LocalDate, List<Diary>>>
interface MongoRepository {
    fun configureTheRealm()

    fun getAllDiaries(): Flow<Diaries>
}