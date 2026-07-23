package com.example.engine

data class ConstantItem(
    val name: String,
    val symbol: String,
    val value: String,
    val unit: String,
    val description: String
)

data class FormulaItem(
    val title: String,
    val category: String,
    val formulaText: String,
    val expressionTemplate: String, // Expression inserted to calculator when tapped
    val explanation: String
)

object FormulaReference {

    val constantsList = listOf(
        ConstantItem("Pi", "π", "3.1415926535", "", "Ratio of circle circumference to diameter"),
        ConstantItem("Euler's Number", "e", "2.7182818284", "", "Base of natural logarithm"),
        ConstantItem("Golden Ratio", "φ", "1.6180339887", "", "Divine proportion in art & nature"),
        ConstantItem("Speed of Light", "c", "2.99792e8", "m/s", "Speed of light in vacuum"),
        ConstantItem("Standard Gravity", "g", "9.80665", "m/s²", "Acceleration due to Earth gravity"),
        ConstantItem("Planck Constant", "h", "6.62607e-34", "J·s", "Quantum scale constant"),
        ConstantItem("Avogadro Constant", "N_A", "6.02214e23", "mol⁻¹", "Number of particles per mole"),
        ConstantItem("Universal Gas Const", "R", "8.31446", "J/(mol·K)", "Ideal gas equation constant")
    )

    val formulasList = listOf(
        FormulaItem(
            title = "Circle Area",
            category = "Geometry",
            formulaText = "A = π × r²",
            expressionTemplate = "π*",
            explanation = "Area of a circle given radius r."
        ),
        FormulaItem(
            title = "Sphere Volume",
            category = "Geometry",
            formulaText = "V = (4/3) × π × r³",
            expressionTemplate = "(4/3)*π*",
            explanation = "Volume of a 3D sphere given radius r."
        ),
        FormulaItem(
            title = "Cylinder Volume",
            category = "Geometry",
            formulaText = "V = π × r² × h",
            expressionTemplate = "π*",
            explanation = "Volume of a cylinder given base radius r and height h."
        ),
        FormulaItem(
            title = "Pythagorean Theorem",
            category = "Geometry",
            formulaText = "c = √(a² + b²)",
            expressionTemplate = "sqrt(",
            explanation = "Hypotenuse of a right-angled triangle."
        ),
        FormulaItem(
            title = "Quadratic Roots",
            category = "Algebra",
            formulaText = "x = (-b ± √(b² - 4ac)) / (2a)",
            expressionTemplate = "",
            explanation = "Roots of quadratic equation ax² + bx + c = 0."
        ),
        FormulaItem(
            title = "Mass-Energy Equivalence",
            category = "Physics",
            formulaText = "E = m × c²",
            expressionTemplate = "*c^2",
            explanation = "Energy equivalent of mass m."
        ),
        FormulaItem(
            title = "Compound Interest",
            category = "Finance",
            formulaText = "A = P × (1 + r/n)^(n×t)",
            expressionTemplate = "*(1+",
            explanation = "Final amount with principal P, rate r, frequency n, years t."
        )
    )

    fun solveQuadratic(a: Double, b: Double, c: Double): String {
        if (a == 0.0) return "Not a quadratic equation (a = 0)"
        val discriminant = b * b - 4 * a * c
        return when {
            discriminant > 0 -> {
                val x1 = (-b + kotlin.math.sqrt(discriminant)) / (2 * a)
                val x2 = (-b - kotlin.math.sqrt(discriminant)) / (2 * a)
                "x₁ = ${formatVal(x1)}\nx₂ = ${formatVal(x2)}"
            }
            discriminant == 0.0 -> {
                val x = -b / (2 * a)
                "x = ${formatVal(x)} (Double Root)"
            }
            else -> {
                val real = -b / (2 * a)
                val imag = kotlin.math.sqrt(-discriminant) / (2 * a)
                "x₁ = ${formatVal(real)} + ${formatVal(imag)}i\nx₂ = ${formatVal(real)} - ${formatVal(imag)}i"
            }
        }
    }

    private fun formatVal(num: Double): String {
        if (num == kotlin.math.floor(num)) return String.format("%.0f", num)
        return String.format("%.4f", num).trimEnd('0').trimEnd('.')
    }
}
