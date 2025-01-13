package com.nemodream.bangkkujaengi.customer.data.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.nemodream.bangkkujaengi.customer.data.model.Post
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocialDiscoveryRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun getPosts(): List<Post> =
        firestore.collection("Posts")
            .get()
            .await()
            .documents
            .mapNotNull { doc ->
                doc.toPost()
            }

    /*
    * Firebase Firestore에서 게시글 데이터 가져오기
    * */
    private fun DocumentSnapshot.toPost(): Post? {
        val id = getString("id") ?: return null
        val nickname = getString("nickname") ?: return null
        val title = getString("title") ?: return null
        val content = getString("content") ?: return null
        val imageList = get("imageList") as? List<String> ?: emptyList()
        val savedCount = getLong("savedCount")?.toInt() ?: 0
        val commentCount = getLong("commentCount")?.toInt() ?: 0

        return Post(
            id = id,
            nickname = nickname,
            title = title,
            content = content,
            imageList = imageList,
            savedCount = savedCount,
            commentCount = commentCount
        )
    }
}
