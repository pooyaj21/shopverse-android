package com.shopverse.core.data.cart

import com.shopverse.core.data.cart.db.CartDao
import com.shopverse.core.data.cart.db.toDomain
import com.shopverse.core.data.cart.db.toEntity
import com.shopverse.core.model.LocalCartItem

interface CartRepository {
    suspend fun selectAll(): List<LocalCartItem>
    suspend fun insertOrUpdate(item: LocalCartItem)
    suspend fun deleteByProductId(productId: String)
    suspend fun deleteAll()
}

class CartRepositoryImpl(private val dao: CartDao) : CartRepository {

    override suspend fun selectAll(): List<LocalCartItem> =
        dao.selectAll().map { it.toDomain() }

    override suspend fun insertOrUpdate(item: LocalCartItem) {
        dao.insertOrUpdate(item.toEntity())
    }

    override suspend fun deleteByProductId(productId: String) {
        dao.deleteByProductId(productId)
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }
}
