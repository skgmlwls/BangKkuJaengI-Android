package com.nemodream.bangkkujaengi.customer.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.nemodream.bangkkujaengi.customer.data.model.Cart
import com.nemodream.bangkkujaengi.customer.data.model.Product
import com.nemodream.bangkkujaengi.customer.data.model.ShoppingCart
import kotlinx.coroutines.tasks.await

class ShoppingCartRepository {

    companion object {

        // 유저 id로 장바구니 아이템 정보 가져오기
        suspend fun getting_shopping_cart_item_by_userId(user_id: String): MutableList<Map<String, *>> {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("Cart")
            val result = collectionReference.whereEqualTo("userId", user_id).get().await()

            val shopping_cart_item_list = mutableListOf<Map<String, *>>()

            result.forEach {document ->
                val cartData = document.toObject(ShoppingCart::class.java) // ShoppingCart로 매핑

//                // Cart 데이터를 로그로 출력
//                Log.d("test200", "Cart Data: ${cartData.items}")

                val map = mapOf(
                    "cart_document_id" to document.id,
                    "cart_data" to cartData
                )
                shopping_cart_item_list.add(map)
            }

            shopping_cart_item_list.map {
                val cartData = it["cart_data"] as? ShoppingCart // cart_data를 ShoppingCart로 안전하게 캐스팅
                cartData?.items?.forEach { cart ->
                    Log.d("test200", "Product ID: ${cart.productId}") // 각 Cart 객체의 productId 출력
                }
            }

            // Log.d("test200", "gettingShoppingCartItemByUserId: ${shopping_cart_item_list[0]["cart_data"]}")

            return shopping_cart_item_list
        }

        suspend fun getting_prodcut_by_product_document_id(product_document_id_list: List<String>): MutableList<Map<String, *>> {
            Log.d("test200", "test2: ${product_document_id_list}")
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("Product")

            // 결과를 저장할 리스트
            val product_list = mutableListOf<Map<String, *>>()

            product_document_id_list.forEach {
                val result = collectionReference.document(it).get().await()
                val map = mapOf(
                    "product_document_id" to it,
                    "product_data" to result.toObject(Product::class.java)
                )
                product_list.add(map)
            }

            product_list.forEach {
                Log.d("test200", "getting_prodcut_by_product_document_id: ${it["product_document_id"]}")
                Log.d("test200", "getting_prodcut_by_product_document_id: ${it["product_data"]}")
            }

            return product_list
        }

        suspend fun add_cart_items_by_product_ids(user_id: String) {
            Log.d("add_cart_item", "여기옴")
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("Cart")
            val result = collectionReference.whereEqualTo("userId", user_id).get().await()

            val product_id_list = listOf("hJbgpcQCZMAvXqILdPqv", "S3oCzDi7Jk82IAAgkC8g", "9taEx4u4B4EIT0kcwC6Z")

            // 장바구니가 이미 존재하는지 확인
            if (result.isEmpty) {
                // 장바구니가 없으면 새로운 장바구니를 생성하고 모든 상품을 추가
                val newItems = product_id_list.map { productId ->
                    Cart(productId = productId, quantity = 1, checked = false)
                }
                val newCart = ShoppingCart(
                    userId = user_id,
                    items = newItems
                )
                collectionReference.add(newCart).await()
                Log.d("add_cart_item", "New cart created and products added for user: $user_id")
            } else {
                // 기존 장바구니에 상품 추가
                val documentReference = result.documents.first().reference
                val shoppingCart = result.documents.first().toObject(ShoppingCart::class.java)

                // 기존 items 리스트에 새 상품 추가
                val updatedItems = shoppingCart?.items?.toMutableList() ?: mutableListOf()

                product_id_list.forEach { productIdToAdd ->
                    val existingItem = updatedItems.find { it.productId == productIdToAdd }
                    if (existingItem != null) {
                        // 이미 존재하는 상품인 경우 수량 증가
                        existingItem.quantity += 1
                    } else {
                        // 새로운 상품인 경우 추가
                        updatedItems.add(Cart(productId = productIdToAdd, quantity = 1, checked = false))
                    }
                }

                // Firestore에 업데이트
                val updateMap = mapOf("items" to updatedItems.map { it.copy() }) // 복사본으로 Firestore 업데이트
                documentReference.update(updateMap).await()
                Log.d("add_cart_item", "Products added/updated in cart for user: $user_id")
            }
        }




        suspend fun update_cart_item_quantity(user_id: String, productId: String, quantity: Int) {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("Cart")
            val documentReference = collectionReference.document(user_id)

            Log.d("test123", "${user_id}")
            Log.d("test123", "update_cart_item_quantity_plus_one: ${productId}")

            // 기존 문서 데이터를 가져온다
            val documentSnapshot = documentReference.get().await()
            val shoppingCart = documentSnapshot.toObject(ShoppingCart::class.java)

            // items 리스트를 업데이트한다
            val updatedItems = shoppingCart?.items?.map { cart ->
                Log.d("test123", "update_cart_item_quantity_plus_one: ${cart.productId}")

                if (cart.productId == productId) {
                    cart.copy(quantity = quantity) // quantity 값을 업데이트
                } else {
                    cart
                }
            }

            // 수정할 데이터를 맵에 담는다
            val updateMap = mapOf(
                "items" to updatedItems
            )

            // Firestore에 수정된 데이터를 업데이트한다
            documentReference.update(updateMap).await()
        }

        suspend fun update_cart_item_checked(user_id: String, productId: String, checked: Boolean) {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("Cart")
            val documentReference = collectionReference.document(user_id)

            Log.d("test123", "${user_id}")
            Log.d("test123", "update_cart_item_quantity_plus_one: ${productId}")

            // 기존 문서 데이터를 가져온다
            val documentSnapshot = documentReference.get().await()
            val shoppingCart = documentSnapshot.toObject(ShoppingCart::class.java)

            // items 리스트를 업데이트한다
            val updatedItems = shoppingCart?.items?.map { cart ->
                Log.d("test123", "update_cart_item_quantity_plus_one: ${cart.productId}")

                if (cart.productId == productId) {
                    cart.copy(checked = checked) // quantity 값을 업데이트
                } else {
                    cart
                }
            }

            // 수정할 데이터를 맵에 담는다
            val updateMap = mapOf(
                "items" to updatedItems
            )

            // Firestore에 수정된 데이터를 업데이트한다
            documentReference.update(updateMap).await()
        }



        // 장바구니 아이템을 삭제하는 메소드
        suspend fun delete_cart_item_by_product_id(user_id: String, product_id: String) {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("Cart")
            val documentReference = collectionReference.document(user_id)

            Log.d("tt", "${user_id}")

            // 기존 문서 데이터를 가져온다
            val documentSnapshot = documentReference.get().await()
            val shoppingCart = documentSnapshot.toObject(ShoppingCart::class.java)

            // 삭제할 상품을 제외한 나머지 items 리스트 생성
            val updatedItems = shoppingCart?.items?.filter {
                it.productId != product_id
            }

            // Firestore의 "items" 배열을 업데이트
            val updateMap = mapOf(
                "items" to updatedItems
            )
            documentReference.update(updateMap).await()
        }

        suspend fun delete_cart_item_by_checked(user_id: String) {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("Cart")

            // 유저의 장바구니 데이터 가져오기
            val result = collectionReference.whereEqualTo("userId", user_id).get().await()

            if (result.isEmpty) {
                Log.d("delete_cart_item", "No cart found for user: $user_id")
                return
            }

            // 첫 번째 장바구니 문서 가져오기 (단일 장바구니 가정)
            val documentReference = result.documents.first().reference
            val shoppingCart = result.documents.first().toObject(ShoppingCart::class.java)

            if (shoppingCart != null) {
                // checked가 true인 항목 제외한 새로운 items 리스트 생성
                val updatedItems = shoppingCart.items.filter { !it.checked }

                // Firestore의 items 배열 업데이트
                val updateMap = mapOf("items" to updatedItems)
                documentReference.update(updateMap).await()

                Log.d("delete_cart_item", "Checked items deleted for user: $user_id")
            } else {
                Log.d("delete_cart_item", "Cart data is null for user: $user_id")
            }
        }


        // 이미지 url을 가져오는 메소드
        suspend fun getting_image(image_url: String) : String {
            val storageReference = FirebaseStorage.getInstance().reference
            // 파일명을 지정하여 이미지 데이터를 가져온다.
            val childStorageReference = storageReference.child("$image_url")
            val imageUri = childStorageReference.downloadUrl.await().toString()

            return imageUri
        }

    }

}