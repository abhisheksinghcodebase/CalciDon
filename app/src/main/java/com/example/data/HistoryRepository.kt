package com.example.data

import kotlinx.coroutines.flow.Flow

class HistoryRepository(private val calculationDao: CalculationDao) {
    val allHistory: Flow<List<CalculationEntity>> = calculationDao.getAllHistory()
    val favoriteHistory: Flow<List<CalculationEntity>> = calculationDao.getFavoriteHistory()

    suspend fun addCalculation(expression: String, result: String, category: String = "General"): Long {
        if (expression.isBlank() || result.isBlank() || result == "Error") return -1
        val item = CalculationEntity(
            expression = expression,
            result = result,
            category = category
        )
        return calculationDao.insertCalculation(item)
    }

    suspend fun toggleFavorite(calculation: CalculationEntity) {
        val updated = calculation.copy(isFavorite = !calculation.isFavorite)
        calculationDao.updateCalculation(updated)
    }

    suspend fun deleteCalculation(calculation: CalculationEntity) {
        calculationDao.deleteCalculation(calculation)
    }

    suspend fun clearHistory() {
        calculationDao.clearAllHistory()
    }
}
