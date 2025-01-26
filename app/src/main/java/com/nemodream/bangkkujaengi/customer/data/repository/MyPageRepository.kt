package com.nemodream.bangkkujaengi.customer.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.nemodream.bangkkujaengi.customer.data.model.Member
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyPageRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
) {

    // 현재 로그인된 사용자 정보를 받아온다.
    suspend fun getMemberInfo(memberDocId: String): Member {
        // 현재 로그인된 사용자 정보를 받아온다.
        return firestore.collection("Member")
            .document(memberDocId)
            .get()
            .await()
            .toObject(Member::class.java)!!
    }
}