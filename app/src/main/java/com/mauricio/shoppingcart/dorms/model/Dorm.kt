package com.mauricio.shoppingcart.dorms.model

data class Dorm (
    val id: Long,
    val maxBed: Int,
    val pricePerBed: Double,
    private var totalBed: Int = 0) {
    fun addBed() {
        if (totalBed + 1 > maxBed) return
        totalBed++
    }
    fun removeBed() {
        if (totalBed - 1 < 0) return
        totalBed--
    }
    fun getTotalBed(): Int {
        return totalBed
    }
}