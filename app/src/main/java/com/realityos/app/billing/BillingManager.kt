// app/src/main/java/com/realityos/app/billing/BillingManager.kt
package com.realityos.app.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*

class BillingManager(
    private val context: Context,
    private val listener: Listener
) : PurchasesUpdatedListener {

    interface Listener {
        fun onElitePurchased()
        fun onBillingError()
    }

    private val client = BillingClient.newBuilder(context)
        .enablePendingPurchases()
        .setListener(this)
        .build()

    fun start() {
        client.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(result: BillingResult) { }
            override fun onBillingServiceDisconnected() { }
        })
    }

    fun purchaseElite(activity: Activity) {
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(ELITE_PRODUCT_ID)
                        .setProductType(BillingClient.ProductType.INAPP)
                        .build()
                )
            ).build()

        client.queryProductDetailsAsync(params) { result, list ->
            if (result.responseCode != BillingClient.BillingResponseCode.OK ||
                list.isNullOrEmpty()
            ) {
                listener.onBillingError(); return@queryProductDetailsAsync
            }
            val details = list.first()
            val offer = details.oneTimePurchaseOfferDetails ?: run {
                listener.onBillingError(); return@queryProductDetailsAsync
            }
            val flowParams = BillingFlowParams.newBuilder()
                .setProductDetails(details)
                .build()
            client.launchBillingFlow(activity, flowParams)
        }
    }

    override fun onPurchasesUpdated(
        result: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        if (result.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            if (purchases.any { it.products.contains(ELITE_PRODUCT_ID) }) {
                listener.onElitePurchased()
            }
        } else {
            listener.onBillingError()
        }
    }

    companion object {
        const val ELITE_PRODUCT_ID = "elite_commit_unlock"
    }
}
