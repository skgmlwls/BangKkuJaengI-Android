package com.nemodream.bangkkujaengi.customer.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.nemodream.bangkkujaengi.customer.data.model.Member
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemberRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    // 회원 데이터 저장
    fun addMemberData(member: Member, callback: (Boolean, String?) -> Unit) {
        val collection = firestore.collection("Member")

        // Firestore에 데이터 저장 (문서 ID 자동 생성)
        collection.add(member)
            .addOnSuccessListener { documentReference ->
                val documentId = documentReference.id

                // 생성된 문서 ID를 Member 객체의 id 필드에 업데이트
                collection.document(documentId).update("id", documentId)
                    // 성공 시 문서 ID 반환
                    .addOnSuccessListener {
                        callback(true, documentId)
                    }
                    // 업데이트 실패
                    .addOnFailureListener { e ->
                        callback(false, e.message)
                    }
            }
            // 저장 실패
            .addOnFailureListener { e ->
                callback(false, e.message)
            }
    }

    // 아이디 중복 확인
    suspend fun isIdAvailable(id: String): Boolean {
        return try {
            val snapshot = firestore
                .collection("Member")
                .whereEqualTo("memberId", id)
                .get()
                .await()

            snapshot.isEmpty
        } catch (e: Exception) {
            Log.e("isIdAvailable", "Error checking ID availability: ${e.message}", e)
            false
        }
    }

    // 닉네임 중복 확인
    suspend fun isNicknameAvailable(nickname: String): Boolean {
        return try {
            val snapshot = firestore
                .collection("Member")
                .whereEqualTo("memberNickname", nickname)
                .get()
                .await()

            snapshot.isEmpty
        } catch (e: Exception) {
            Log.e("isNicknameAvailable", "Error checking nickname availability: ${e.message}", e)
            false
        }
    }

    // 아이디로 사용자 정보 조회
    suspend fun getUserById(id: String): Pair<Member?, String?> {
        return try {
            val snapshot = firestore
                .collection("Member")
                .whereEqualTo("memberId", id)
                .get()
                .await()

            if (!snapshot.isEmpty) {
                val document = snapshot.documents.first()
                val member = document.toObject(Member::class.java)
                member?.let { it.copy(memberId = document.id) } to document.id
            } else {
                null to null // 아이디가 존재하지 않음
            }
        } catch (e: Exception) {
            null to null // 오류 발생 시
        }
    }
}