package com.example.engine

import kotlin.math.*

enum class AngleMode {
    DEG, RAD
}

class ExpressionEvaluator {

    companion object {
        const val PI_VAL = Math.PI
        const val E_VAL = Math.E
        const val PHI_VAL = 1.618033988749895 // Golden ratio
        const val C_VAL = 299792458.0 // Speed of light in m/s
    }

    /**
     * Evaluates a mathematical expression string.
     * Returns a formatted result String or throws an Exception if invalid.
     */
    fun evaluate(expression: String, angleMode: AngleMode = AngleMode.DEG, memoryValue: Double = 0.0): String {
        val sanitized = prepareExpression(expression, memoryValue)
        if (sanitized.isBlank()) return ""

        val tokens = tokenize(sanitized)
        val parser = Parser(tokens, angleMode)
        val result = parser.parseExpression()

        if (result.isNaN()) return "Undefined"
        if (result.isInfinite()) return if (result > 0) "Infinity" else "-Infinity"

        return formatResult(result)
    }

    private fun prepareExpression(expr: String, memoryValue: Double): String {
        var s = expr
            .replace("×", "*")
            .replace("÷", "/")
            .replace("π", "pi")
            .replace("φ", "phi")
            .replace("√", "sqrt")
            .replace("³√", "cbrt")
            .replace("M", memoryValue.toString())

        // Insert implicit multiplication:
        // 1) digit or ')' or '!' or '%' followed by '(' or 'pi' or 'e' or 'phi' or 'c' or function name
        val implicitPattern1 = Regex("""(\d|\)|!|%)\s*(\(|pi|e|phi|c|sin|cos|tan|asin|acos|atan|sinh|cosh|tanh|ln|log|sqrt|cbrt|abs)""")
        s = implicitPattern1.replace(s) { matchResult ->
            "${matchResult.groupValues[1]}*${matchResult.groupValues[2]}"
        }

        // 2) 'pi', 'e', 'phi', 'c' followed by a digit or '(' or another constant or function
        val implicitPattern2 = Regex("""(pi|e|phi|c)\s*(\d|\(|sin|cos|tan|asin|acos|atan|sinh|cosh|tanh|ln|log|sqrt|cbrt|abs)""")
        s = implicitPattern2.replace(s) { matchResult ->
            "${matchResult.groupValues[1]}*${matchResult.groupValues[2]}"
        }

        return s
    }

    private fun tokenize(expr: String): List<String> {
        val tokens = mutableListOf<String>()
        var i = 0
        while (i < expr.length) {
            val c = expr[i]

            if (c.isWhitespace()) {
                i++
                continue
            }

            if (c in "+-*/^()%!") {
                tokens.add(c.toString())
                i++
                continue
            }

            if (c.isDigit() || c == '.') {
                val sb = StringBuilder()
                while (i < expr.length && (expr[i].isDigit() || expr[i] == '.' || expr[i] == 'E' || expr[i] == 'e')) {
                    if ((expr[i] == 'E' || expr[i] == 'e') && i + 1 < expr.length && (expr[i + 1] == '+' || expr[i + 1] == '-')) {
                        sb.append(expr[i])
                        i++
                        sb.append(expr[i])
                        i++
                    } else {
                        sb.append(expr[i])
                        i++
                    }
                }
                tokens.add(sb.toString())
                continue
            }

            if (c.isLetter()) {
                val sb = StringBuilder()
                while (i < expr.length && expr[i].isLetter()) {
                    sb.append(expr[i])
                    i++
                }
                tokens.add(sb.toString())
                continue
            }

            i++
        }
        return tokens
    }

    private class Parser(private val tokens: List<String>, private val angleMode: AngleMode) {
        private var pos = 0

        private fun peek(): String? = if (pos < tokens.size) tokens[pos] else null
        private fun consume(): String = tokens[pos++]

        fun parseExpression(): Double {
            var result = parseTerm()
            while (true) {
                val token = peek()
                if (token == "+" || token == "-") {
                    val op = consume()
                    val right = parseTerm()
                    if (op == "+") result += right else result -= right
                } else {
                    break
                }
            }
            return result
        }

        private fun parseTerm(): Double {
            var result = parseFactor()
            while (true) {
                val token = peek()
                if (token == "*" || token == "/" || token == "%") {
                    val op = consume()
                    val right = parseFactor()
                    when (op) {
                        "*" -> result *= right
                        "/" -> {
                            if (right == 0.0) throw ArithmeticException("Division by zero")
                            result /= right
                        }
                        "%" -> {
                            if (right == 0.0) throw ArithmeticException("Modulo by zero")
                            result %= right
                        }
                    }
                } else {
                    break
                }
            }
            return result
        }

        private fun parseFactor(): Double {
            var result = parsePower()
            while (peek() == "^") {
                consume()
                val right = parseFactor() // Right associative
                result = result.pow(right)
            }
            return result
        }

        private fun parsePower(): Double {
            val token = peek() ?: throw IllegalArgumentException("Unexpected end of expression")

            // Handle unary plus/minus
            if (token == "+") {
                consume()
                return parsePower()
            }
            if (token == "-") {
                consume()
                return -parsePower()
            }

            var value: Double

            if (token == "(") {
                consume() // consume '('
                value = parseExpression()
                if (peek() == ")") {
                    consume() // consume ')'
                }
            } else if (isNumber(token)) {
                consume()
                value = token.toDouble()
            } else if (isConstant(token)) {
                consume()
                value = when (token.lowercase()) {
                    "pi" -> PI_VAL
                    "e" -> E_VAL
                    "phi" -> PHI_VAL
                    "c" -> C_VAL
                    else -> 0.0
                }
            } else if (isFunction(token)) {
                val func = consume()
                var arg: Double
                if (peek() == "(") {
                    consume()
                    arg = parseExpression()
                    if (peek() == ")") consume()
                } else {
                    arg = parsePower()
                }
                value = evaluateFunction(func, arg)
            } else {
                throw IllegalArgumentException("Unexpected token: $token")
            }

            // Postfix operators: ! (factorial) and % (percentage)
            while (peek() == "!" || peek() == "%") {
                val op = consume()
                if (op == "!") {
                    value = factorial(value)
                } else if (op == "%") {
                    value /= 100.0
                }
            }

            return value
        }

        private fun isNumber(s: String): Boolean = s.toDoubleOrNull() != null

        private fun isConstant(s: String): Boolean =
            s.equals("pi", ignoreCase = true) ||
            s.equals("e", ignoreCase = true) ||
            s.equals("phi", ignoreCase = true) ||
            s.equals("c", ignoreCase = true)

        private fun isFunction(s: String): Boolean =
            s in listOf("sin", "cos", "tan", "asin", "acos", "atan", "sinh", "cosh", "tanh", "ln", "log", "sqrt", "cbrt", "abs")

        private fun evaluateFunction(func: String, arg: Double): Double {
            val radArg = if (angleMode == AngleMode.DEG) Math.toRadians(arg) else arg
            return when (func) {
                "sin" -> sin(radArg)
                "cos" -> cos(radArg)
                "tan" -> tan(radArg)
                "asin" -> {
                    val r = asin(arg)
                    if (angleMode == AngleMode.DEG) Math.toDegrees(r) else r
                }
                "acos" -> {
                    val r = acos(arg)
                    if (angleMode == AngleMode.DEG) Math.toDegrees(r) else r
                }
                "atan" -> {
                    val r = atan(arg)
                    if (angleMode == AngleMode.DEG) Math.toDegrees(r) else r
                }
                "sinh" -> sinh(arg)
                "cosh" -> cosh(arg)
                "tanh" -> tanh(arg)
                "ln" -> {
                    if (arg <= 0) throw ArithmeticException("Domain error: ln(x) for x <= 0")
                    ln(arg)
                }
                "log" -> {
                    if (arg <= 0) throw ArithmeticException("Domain error: log(x) for x <= 0")
                    log10(arg)
                }
                "sqrt" -> {
                    if (arg < 0) throw ArithmeticException("Domain error: sqrt(x) for x < 0")
                    sqrt(arg)
                }
                "cbrt" -> cbrt(arg)
                "abs" -> abs(arg)
                else -> arg
            }
        }

        private fun factorial(n: Double): Double {
            if (n < 0 || n != floor(n)) throw ArithmeticException("Factorial undefined for negative or non-integer")
            if (n > 170) return Double.POSITIVE_INFINITY // Overflow threshold for double
            var res = 1.0
            for (i in 2..n.toInt()) {
                res *= i
            }
            return res
        }
    }

    private fun formatResult(valNum: Double): String {
        // Round extremely small values close to zero (e.g. sin(180 deg) floating point precision)
        val num = if (abs(valNum) < 1e-15) 0.0 else valNum

        // If integer or whole number double
        if (num == floor(num) && !num.isInfinite() && abs(num) < 1e12) {
            return String.format("%.0f", num)
        }

        // Scientific notation for very large or very small non-zero numbers
        if ((abs(num) >= 1e10 || (abs(num) < 1e-6 && num != 0.0))) {
            return String.format("%.6e", num)
        }

        // Standard decimal formatting
        val formatted = String.format("%.8f", num).trimEnd('0').trimEnd('.')
        return formatted
    }
}
