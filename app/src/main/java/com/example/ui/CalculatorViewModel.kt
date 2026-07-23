package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.CalculationEntity
import com.example.data.HistoryRepository
import com.example.engine.AngleMode
import com.example.engine.ConversionUnit
import com.example.engine.ExpressionEvaluator
import com.example.engine.UnitCategory
import com.example.engine.UnitConverterEngine
import com.example.ui.theme.AppThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class MainTab {
    CALCULATOR, CONVERTER, FORMULAS, HISTORY
}

sealed class CalculatorKey {
    data class Digit(val value: String) : CalculatorKey()
    data class Operator(val symbol: String) : CalculatorKey()
    data class Function(val name: String) : CalculatorKey()
    data class Constant(val symbol: String) : CalculatorKey()
    object Decimal : CalculatorKey()
    object Equals : CalculatorKey()
    object Clear : CalculatorKey()
    object Delete : CalculatorKey()
    object ToggleSign : CalculatorKey()
    object ParenthesisOpen : CalculatorKey()
    object ParenthesisClose : CalculatorKey()
    object MemoryClear : CalculatorKey()
    object MemoryRecall : CalculatorKey()
    object MemoryAdd : CalculatorKey()
    object MemorySubtract : CalculatorKey()
}

class CalculatorViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: HistoryRepository
    private val evaluator = ExpressionEvaluator()

    init {
        val db = AppDatabase.getDatabase(application)
        repository = HistoryRepository(db.calculationDao())
    }

    val historyList: StateFlow<List<CalculationEntity>> = repository.allHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteList: StateFlow<List<CalculationEntity>> = repository.favoriteHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // UI States
    private val _expression = MutableStateFlow("")
    val expression: StateFlow<String> = _expression.asStateFlow()

    private val _previewResult = MutableStateFlow("")
    val previewResult: StateFlow<String> = _previewResult.asStateFlow()

    private val _memoryValue = MutableStateFlow(0.0)
    val memoryValue: StateFlow<Double> = _memoryValue.asStateFlow()

    private val _angleMode = MutableStateFlow(AngleMode.DEG)
    val angleMode: StateFlow<AngleMode> = _angleMode.asStateFlow()

    private val _activeTab = MutableStateFlow(MainTab.CALCULATOR)
    val activeTab: StateFlow<MainTab> = _activeTab.asStateFlow()

    private val _isScientificExpanded = MutableStateFlow(false)
    val isScientificExpanded: StateFlow<Boolean> = _isScientificExpanded.asStateFlow()

    private val _themeMode = MutableStateFlow(AppThemeMode.DARK_CYBER)
    val themeMode: StateFlow<AppThemeMode> = _themeMode.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // Unit Converter States
    private val _selectedCategory = MutableStateFlow(UnitCategory.LENGTH)
    val selectedCategory: StateFlow<UnitCategory> = _selectedCategory.asStateFlow()

    private val _fromUnit = MutableStateFlow(UnitConverterEngine.unitsMap[UnitCategory.LENGTH]!![0])
    val fromUnit: StateFlow<ConversionUnit> = _fromUnit.asStateFlow()

    private val _toUnit = MutableStateFlow(UnitConverterEngine.unitsMap[UnitCategory.LENGTH]!![1])
    val toUnit: StateFlow<ConversionUnit> = _toUnit.asStateFlow()

    private val _converterInputValue = MutableStateFlow("1")
    val converterInputValue: StateFlow<String> = _converterInputValue.asStateFlow()

    private val _converterResultValue = MutableStateFlow("")
    val converterResultValue: StateFlow<String> = _converterResultValue.asStateFlow()

    init {
        updateConverterResult()
    }

    fun setTab(tab: MainTab) {
        _activeTab.value = tab
    }

    fun toggleScientific() {
        _isScientificExpanded.value = !_isScientificExpanded.value
    }

    fun toggleAngleMode() {
        _angleMode.value = if (_angleMode.value == AngleMode.DEG) AngleMode.RAD else AngleMode.DEG
        calculatePreview()
    }

    fun setThemeMode(mode: AppThemeMode) {
        _themeMode.value = mode
    }

    fun onKeyPress(key: CalculatorKey) {
        _errorMessage.value = null
        when (key) {
            is CalculatorKey.Digit -> appendToExpression(key.value)
            is CalculatorKey.Operator -> appendOperator(key.symbol)
            is CalculatorKey.Function -> appendFunction(key.name)
            is CalculatorKey.Constant -> appendToExpression(key.symbol)
            is CalculatorKey.Decimal -> appendToExpression(".")
            is CalculatorKey.ParenthesisOpen -> appendToExpression("(")
            is CalculatorKey.ParenthesisClose -> appendToExpression(")")
            is CalculatorKey.Clear -> clearAll()
            is CalculatorKey.Delete -> deleteLastChar()
            is CalculatorKey.ToggleSign -> toggleSign()
            is CalculatorKey.Equals -> evaluateFinal()
            is CalculatorKey.MemoryClear -> _memoryValue.value = 0.0
            is CalculatorKey.MemoryRecall -> appendToExpression("M")
            is CalculatorKey.MemoryAdd -> addToMemory()
            is CalculatorKey.MemorySubtract -> subtractFromMemory()
        }
    }

    private fun appendToExpression(str: String) {
        _expression.value += str
        calculatePreview()
    }

    private fun appendOperator(op: String) {
        val current = _expression.value
        if (current.isEmpty() && op == "-") {
            _expression.value = "-"
            return
        }
        if (current.isNotEmpty() && isLastCharOperator(current)) {
            // Replace last operator
            _expression.value = current.dropLast(1) + op
        } else {
            _expression.value += op
        }
        calculatePreview()
    }

    private fun appendFunction(funcName: String) {
        when (funcName) {
            "x²" -> _expression.value += "^2"
            "x³" -> _expression.value += "^3"
            "xʸ" -> _expression.value += "^"
            "√" -> _expression.value += "√("
            "³√" -> _expression.value += "³√("
            "1/x" -> _expression.value += "1/("
            "n!" -> _expression.value += "!"
            "|x|" -> _expression.value += "abs("
            else -> _expression.value += "$funcName("
        }
        calculatePreview()
    }

    private fun isLastCharOperator(s: String): Boolean {
        if (s.isEmpty()) return false
        val last = s.last()
        return last == '+' || last == '-' || last == '×' || last == '÷' || last == '^'
    }

    private fun clearAll() {
        _expression.value = ""
        _previewResult.value = ""
        _errorMessage.value = null
    }

    private fun deleteLastChar() {
        if (_expression.value.isNotEmpty()) {
            _expression.value = _expression.value.dropLast(1)
            calculatePreview()
        }
    }

    private fun toggleSign() {
        val curr = _expression.value
        if (curr.startsWith("-")) {
            _expression.value = curr.substring(1)
        } else if (curr.isNotEmpty()) {
            _expression.value = "-($curr)"
        }
        calculatePreview()
    }

    private fun calculatePreview() {
        val expr = _expression.value.trim()
        if (expr.isBlank()) {
            _previewResult.value = ""
            return
        }
        try {
            val res = evaluator.evaluate(expr, _angleMode.value, _memoryValue.value)
            _previewResult.value = res
        } catch (e: Exception) {
            _previewResult.value = ""
        }
    }

    fun evaluateFinal() {
        val expr = _expression.value.trim()
        if (expr.isBlank()) return

        try {
            val res = evaluator.evaluate(expr, _angleMode.value, _memoryValue.value)
            if (res.isNotEmpty() && res != "Undefined" && res != "Infinity") {
                viewModelScope.launch {
                    val category = if (_isScientificExpanded.value) "Scientific" else "Basic"
                    repository.addCalculation(expr, res, category)
                }
                _expression.value = res
                _previewResult.value = ""
            } else {
                _errorMessage.value = res
            }
        } catch (e: Exception) {
            _errorMessage.value = e.localizedMessage ?: "Invalid Expression"
        }
    }

    private fun addToMemory() {
        try {
            val currentVal = _previewResult.value.toDoubleOrNull()
                ?: evaluator.evaluate(_expression.value, _angleMode.value, _memoryValue.value).toDoubleOrNull()
            if (currentVal != null) {
                _memoryValue.value += currentVal
            }
        } catch (_: Exception) {}
    }

    private fun subtractFromMemory() {
        try {
            val currentVal = _previewResult.value.toDoubleOrNull()
                ?: evaluator.evaluate(_expression.value, _angleMode.value, _memoryValue.value).toDoubleOrNull()
            if (currentVal != null) {
                _memoryValue.value -= currentVal
            }
        } catch (_: Exception) {}
    }

    fun insertIntoExpression(text: String) {
        _expression.value += text
        calculatePreview()
        _activeTab.value = MainTab.CALCULATOR
    }

    // History Actions
    fun toggleFavorite(item: CalculationEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(item)
        }
    }

    fun deleteHistoryItem(item: CalculationEntity) {
        viewModelScope.launch {
            repository.deleteCalculation(item)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }

    // Unit Converter Logic
    fun setUnitCategory(category: UnitCategory) {
        _selectedCategory.value = category
        val units = UnitConverterEngine.unitsMap[category] ?: return
        _fromUnit.value = units[0]
        _toUnit.value = if (units.size > 1) units[1] else units[0]
        updateConverterResult()
    }

    fun setFromUnit(unit: ConversionUnit) {
        _fromUnit.value = unit
        updateConverterResult()
    }

    fun setToUnit(unit: ConversionUnit) {
        _toUnit.value = unit
        updateConverterResult()
    }

    fun swapUnits() {
        val temp = _fromUnit.value
        _fromUnit.value = _toUnit.value
        _toUnit.value = temp
        updateConverterResult()
    }

    fun setConverterInput(input: String) {
        _converterInputValue.value = input
        updateConverterResult()
    }

    private fun updateConverterResult() {
        val valDouble = _converterInputValue.value.toDoubleOrNull() ?: 0.0
        val result = UnitConverterEngine.convert(
            category = _selectedCategory.value,
            value = valDouble,
            fromUnit = _fromUnit.value,
            toUnit = _toUnit.value
        )
        _converterResultValue.value = result
    }
}
