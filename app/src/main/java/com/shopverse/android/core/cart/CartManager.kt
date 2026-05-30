package com.shopverse.android.core.cart

import com.shopverse.core.domain.cart.DeleteAllProductInCartUseCase
import com.shopverse.core.domain.cart.DeleteProductFromCartUseCase
import com.shopverse.core.domain.cart.InsertOrUpdateProductToCartUseCase
import com.shopverse.core.domain.cart.SelectAllProductInCartUseCase
import com.shopverse.core.model.LocalCartItem
import com.shopverse.core.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CartManager(
    private val selectAllProductInCart: SelectAllProductInCartUseCase,
    private val insertOrUpdateProductToCart: InsertOrUpdateProductToCartUseCase,
    private val deleteProductFromCart: DeleteProductFromCartUseCase,
    private val deleteAllProductInCart: DeleteAllProductInCartUseCase,
) {

    private var isInitialized: Boolean = false
    private val items = LinkedHashMap<String, LocalCartItem>()

    private val mutableIdsFlow = MutableStateFlow<Set<String>>(emptySet())
    val idsFlow: StateFlow<Set<String>> = mutableIdsFlow.asStateFlow()

    private val mutableItemsFlow = MutableStateFlow<List<LocalCartItem>>(emptyList())
    val itemsFlow: StateFlow<List<LocalCartItem>> = mutableItemsFlow.asStateFlow()

    private fun checkInit() {
        check(isInitialized) { "First call CartManager.init() at application initialization" }
    }

    suspend fun init() {
        if (isInitialized) return
        selectAllProductInCart().forEach { items[it.id] = it }
        publish()
        isInitialized = true
    }

    suspend fun add(product: Product) {
        checkInit()
        if (items.containsKey(product.id)) return
        val item = product.toCartItem()
        items[item.id] = item
        insertOrUpdateProductToCart(item)
        publish()
    }

    suspend fun remove(productId: String) {
        checkInit()
        if (items.remove(productId) == null) return
        deleteProductFromCart(productId = productId)
        publish()
    }

    suspend fun clear() {
        checkInit()
        if (items.isEmpty()) return
        items.clear()
        deleteAllProductInCart()
        publish()
    }

    private fun publish() {
        mutableIdsFlow.value = items.keys.toSet()
        mutableItemsFlow.value = items.values.toList()
    }

    fun isProductInCart(product: Product): Boolean {
        checkInit()
        return items.containsKey(product.id)
    }

    private fun Product.toCartItem(): LocalCartItem = LocalCartItem(
        id = id,
        slug = slug,
        title = title,
        currentPrice = currentPrice,
        oldPrice = oldPrice,
        currency = currency,
        image = image,
        count = 1,
    )
}
