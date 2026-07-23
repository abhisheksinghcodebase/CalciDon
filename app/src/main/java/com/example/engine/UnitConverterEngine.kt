package com.example.engine

import kotlin.math.abs
import kotlin.math.floor

enum class UnitCategory(val displayName: String) {
    LENGTH("Length"),
    MASS("Mass"),
    TEMPERATURE("Temperature"),
    VOLUME("Volume"),
    SPEED("Speed"),
    AREA("Area"),
    DIGITAL("Digital Data")
}

data class ConversionUnit(
    val name: String,
    val symbol: String,
    val factorToBase: Double = 1.0 // Ratio to base unit
)

object UnitConverterEngine {

    val unitsMap: Map<UnitCategory, List<ConversionUnit>> = mapOf(
        UnitCategory.LENGTH to listOf(
            ConversionUnit("Meter", "m", 1.0),
            ConversionUnit("Kilometer", "km", 1000.0),
            ConversionUnit("Centimeter", "cm", 0.01),
            ConversionUnit("Millimeter", "mm", 0.001),
            ConversionUnit("Mile", "mi", 1609.344),
            ConversionUnit("Yard", "yd", 0.9144),
            ConversionUnit("Foot", "ft", 0.3048),
            ConversionUnit("Inch", "in", 0.0254)
        ),
        UnitCategory.MASS to listOf(
            ConversionUnit("Kilogram", "kg", 1.0),
            ConversionUnit("Gram", "g", 0.001),
            ConversionUnit("Milligram", "mg", 0.000001),
            ConversionUnit("Pound", "lb", 0.45359237),
            ConversionUnit("Ounce", "oz", 0.028349523125),
            ConversionUnit("Metric Ton", "t", 1000.0)
        ),
        UnitCategory.TEMPERATURE to listOf(
            ConversionUnit("Celsius", "°C", 1.0),
            ConversionUnit("Fahrenheit", "°F", 1.0),
            ConversionUnit("Kelvin", "K", 1.0)
        ),
        UnitCategory.VOLUME to listOf(
            ConversionUnit("Liter", "L", 1.0),
            ConversionUnit("Milliliter", "mL", 0.001),
            ConversionUnit("Gallon (US)", "gal", 3.78541),
            ConversionUnit("Fluid Ounce", "fl oz", 0.0295735),
            ConversionUnit("Cubic Meter", "m³", 1000.0)
        ),
        UnitCategory.SPEED to listOf(
            ConversionUnit("Meters / sec", "m/s", 1.0),
            ConversionUnit("Kilometers / hr", "km/h", 0.277777778),
            ConversionUnit("Miles / hr", "mph", 0.44704),
            ConversionUnit("Knot", "kn", 0.514444)
        ),
        UnitCategory.AREA to listOf(
            ConversionUnit("Square Meter", "m²", 1.0),
            ConversionUnit("Square Kilometer", "km²", 1000000.0),
            ConversionUnit("Square Foot", "ft²", 0.09290304),
            ConversionUnit("Acre", "ac", 4046.8564224),
            ConversionUnit("Hectare", "ha", 10000.0)
        ),
        UnitCategory.DIGITAL to listOf(
            ConversionUnit("Byte", "B", 1.0),
            ConversionUnit("Kilobyte", "KB", 1024.0),
            ConversionUnit("Megabyte", "MB", 1048576.0),
            ConversionUnit("Gigabyte", "GB", 1073741824.0),
            ConversionUnit("Terabyte", "TB", 1099511627776.0)
        )
    )

    fun convert(
        category: UnitCategory,
        value: Double,
        fromUnit: ConversionUnit,
        toUnit: ConversionUnit
    ): String {
        if (value.isNaN() || value.isInfinite()) return ""

        if (category == UnitCategory.TEMPERATURE) {
            val convertedVal = convertTemperature(value, fromUnit.symbol, toUnit.symbol)
            return formatNumber(convertedVal)
        }

        // Standard multiplicative factor conversion via Base Unit
        val baseValue = value * fromUnit.factorToBase
        val resultValue = baseValue / toUnit.factorToBase

        return formatNumber(resultValue)
    }

    private fun convertTemperature(valNum: Double, fromSymbol: String, toSymbol: String): Double {
        // First convert fromSymbol to Celsius
        val celsius = when (fromSymbol) {
            "°C" -> valNum
            "°F" -> (valNum - 32) * 5.0 / 9.0
            "K" -> valNum - 273.15
            else -> valNum
        }

        // Then convert Celsius to toSymbol
        return when (toSymbol) {
            "°C" -> celsius
            "°F" -> (celsius * 9.0 / 5.0) + 32
            "K" -> celsius + 273.15
            else -> celsius
        }
    }

    private fun formatNumber(num: Double): String {
        if (num == floor(num) && abs(num) < 1e10) {
            return String.format("%.0f", num)
        }
        if (abs(num) >= 1e10 || (abs(num) < 1e-5 && num != 0.0)) {
            return String.format("%.6e", num)
        }
        return String.format("%.6f", num).trimEnd('0').trimEnd('.')
    }
}
