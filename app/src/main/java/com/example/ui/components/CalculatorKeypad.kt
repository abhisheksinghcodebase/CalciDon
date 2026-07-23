package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.CalculatorKey
import com.example.ui.theme.CyberActionEquals

@Composable
fun CalculatorKeypad(
    isScientificExpanded: Boolean,
    onToggleScientific: () -> Unit,
    onKeyPress: (CalculatorKey) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .testTag("calculator_keypad"),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Top Toolbar Row: Toggle Scientific Panel & Quick Parentheses
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Toggle Scientific Button
            FilterChip(
                selected = isScientificExpanded,
                onClick = onToggleScientific,
                label = { Text(if (isScientificExpanded) "Scientific (On)" else "Scientific (Off)") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Functions,
                        contentDescription = "Scientific Functions"
                    )
                },
                modifier = Modifier.testTag("toggle_scientific_chip")
            )

            // Quick Parentheses Row
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                KeyButton(
                    text = "(",
                    onClick = { onKeyPress(CalculatorKey.ParenthesisOpen) },
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .width(52.dp)
                        .height(38.dp)
                        .testTag("parenthesis_open_btn")
                )
                KeyButton(
                    text = ")",
                    onClick = { onKeyPress(CalculatorKey.ParenthesisClose) },
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .width(52.dp)
                        .height(38.dp)
                        .testTag("parenthesis_close_btn")
                )
            }
        }

        // Scientific Panel (Expandable Grid)
        AnimatedVisibility(
            visible = isScientificExpanded,
            enter = expandVertically(),
            exit = shrinkVertically()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Scientific Row 1: Trig
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    SciKeyButton("sin", { onKeyPress(CalculatorKey.Function("sin")) }, Modifier.weight(1f))
                    SciKeyButton("cos", { onKeyPress(CalculatorKey.Function("cos")) }, Modifier.weight(1f))
                    SciKeyButton("tan", { onKeyPress(CalculatorKey.Function("tan")) }, Modifier.weight(1f))
                    SciKeyButton("asin", { onKeyPress(CalculatorKey.Function("asin")) }, Modifier.weight(1f))
                    SciKeyButton("acos", { onKeyPress(CalculatorKey.Function("acos")) }, Modifier.weight(1f))
                    SciKeyButton("atan", { onKeyPress(CalculatorKey.Function("atan")) }, Modifier.weight(1f))
                }
                // Scientific Row 2: Powers & Logs
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    SciKeyButton("x²", { onKeyPress(CalculatorKey.Function("x²")) }, Modifier.weight(1f))
                    SciKeyButton("x³", { onKeyPress(CalculatorKey.Function("x³")) }, Modifier.weight(1f))
                    SciKeyButton("xʸ", { onKeyPress(CalculatorKey.Function("xʸ")) }, Modifier.weight(1f))
                    SciKeyButton("√", { onKeyPress(CalculatorKey.Function("√")) }, Modifier.weight(1f))
                    SciKeyButton("³√", { onKeyPress(CalculatorKey.Function("³√")) }, Modifier.weight(1f))
                    SciKeyButton("ln", { onKeyPress(CalculatorKey.Function("ln")) }, Modifier.weight(1f))
                }
                // Scientific Row 3: Constants & Functions
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    SciKeyButton("log", { onKeyPress(CalculatorKey.Function("log")) }, Modifier.weight(1f))
                    SciKeyButton("n!", { onKeyPress(CalculatorKey.Function("n!")) }, Modifier.weight(1f))
                    SciKeyButton("1/x", { onKeyPress(CalculatorKey.Function("1/x")) }, Modifier.weight(1f))
                    SciKeyButton("π", { onKeyPress(CalculatorKey.Constant("π")) }, Modifier.weight(1f))
                    SciKeyButton("e", { onKeyPress(CalculatorKey.Constant("e")) }, Modifier.weight(1f))
                    SciKeyButton("φ", { onKeyPress(CalculatorKey.Constant("φ")) }, Modifier.weight(1f))
                }
            }
        }

        // Memory Register Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MemoryButton("MC", { onKeyPress(CalculatorKey.MemoryClear) }, Modifier.weight(1f))
            MemoryButton("MR", { onKeyPress(CalculatorKey.MemoryRecall) }, Modifier.weight(1f))
            MemoryButton("M+", { onKeyPress(CalculatorKey.MemoryAdd) }, Modifier.weight(1f))
            MemoryButton("M-", { onKeyPress(CalculatorKey.MemorySubtract) }, Modifier.weight(1f))
        }

        // Standard Keypad Grid (5 Rows x 4 Columns)
        val numColor = MaterialTheme.colorScheme.surfaceVariant
        val opColor = MaterialTheme.colorScheme.primaryContainer
        val actionColor = MaterialTheme.colorScheme.errorContainer

        // Row 1: AC, DEL, %, ÷
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            KeyButton("AC", { onKeyPress(CalculatorKey.Clear) }, actionColor, MaterialTheme.colorScheme.onErrorContainer, Modifier.weight(1f).testTag("btn_clear"))
            KeyIconButton(Icons.AutoMirrored.Filled.Backspace, "DEL", { onKeyPress(CalculatorKey.Delete) }, actionColor, MaterialTheme.colorScheme.onErrorContainer, Modifier.weight(1f).testTag("btn_delete"))
            KeyButton("%", { onKeyPress(CalculatorKey.Operator("%")) }, opColor, MaterialTheme.colorScheme.onPrimaryContainer, Modifier.weight(1f).testTag("btn_percent"))
            KeyButton("÷", { onKeyPress(CalculatorKey.Operator("÷")) }, opColor, MaterialTheme.colorScheme.onPrimaryContainer, Modifier.weight(1f).testTag("btn_divide"))
        }

        // Row 2: 7, 8, 9, ×
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            KeyButton("7", { onKeyPress(CalculatorKey.Digit("7")) }, numColor, MaterialTheme.colorScheme.onSurface, Modifier.weight(1f).testTag("btn_7"))
            KeyButton("8", { onKeyPress(CalculatorKey.Digit("8")) }, numColor, MaterialTheme.colorScheme.onSurface, Modifier.weight(1f).testTag("btn_8"))
            KeyButton("9", { onKeyPress(CalculatorKey.Digit("9")) }, numColor, MaterialTheme.colorScheme.onSurface, Modifier.weight(1f).testTag("btn_9"))
            KeyButton("×", { onKeyPress(CalculatorKey.Operator("×")) }, opColor, MaterialTheme.colorScheme.onPrimaryContainer, Modifier.weight(1f).testTag("btn_multiply"))
        }

        // Row 3: 4, 5, 6, -
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            KeyButton("4", { onKeyPress(CalculatorKey.Digit("4")) }, numColor, MaterialTheme.colorScheme.onSurface, Modifier.weight(1f).testTag("btn_4"))
            KeyButton("5", { onKeyPress(CalculatorKey.Digit("5")) }, numColor, MaterialTheme.colorScheme.onSurface, Modifier.weight(1f).testTag("btn_5"))
            KeyButton("6", { onKeyPress(CalculatorKey.Digit("6")) }, numColor, MaterialTheme.colorScheme.onSurface, Modifier.weight(1f).testTag("btn_6"))
            KeyButton("-", { onKeyPress(CalculatorKey.Operator("-")) }, opColor, MaterialTheme.colorScheme.onPrimaryContainer, Modifier.weight(1f).testTag("btn_subtract"))
        }

        // Row 4: 1, 2, 3, +
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            KeyButton("1", { onKeyPress(CalculatorKey.Digit("1")) }, numColor, MaterialTheme.colorScheme.onSurface, Modifier.weight(1f).testTag("btn_1"))
            KeyButton("2", { onKeyPress(CalculatorKey.Digit("2")) }, numColor, MaterialTheme.colorScheme.onSurface, Modifier.weight(1f).testTag("btn_2"))
            KeyButton("3", { onKeyPress(CalculatorKey.Digit("3")) }, numColor, MaterialTheme.colorScheme.onSurface, Modifier.weight(1f).testTag("btn_3"))
            KeyButton("+", { onKeyPress(CalculatorKey.Operator("+")) }, opColor, MaterialTheme.colorScheme.onPrimaryContainer, Modifier.weight(1f).testTag("btn_add"))
        }

        // Row 5: +/-, 0, ., =
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            KeyButton("+/-", { onKeyPress(CalculatorKey.ToggleSign) }, numColor, MaterialTheme.colorScheme.onSurface, Modifier.weight(1f).testTag("btn_sign"))
            KeyButton("0", { onKeyPress(CalculatorKey.Digit("0")) }, numColor, MaterialTheme.colorScheme.onSurface, Modifier.weight(1f).testTag("btn_0"))
            KeyButton(".", { onKeyPress(CalculatorKey.Decimal) }, numColor, MaterialTheme.colorScheme.onSurface, Modifier.weight(1f).testTag("btn_decimal"))
            KeyButton("=", { onKeyPress(CalculatorKey.Equals) }, MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.onPrimary, Modifier.weight(1f).testTag("btn_equals"))
        }
    }
}

@Composable
fun KeyButton(
    text: String,
    onClick: () -> Unit,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        color = containerColor,
        contentColor = contentColor,
        modifier = modifier
            .defaultMinSize(minWidth = 48.dp, minHeight = 58.dp),
        tonalElevation = 2.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
            )
        }
    }
}

@Composable
fun KeyIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDesc: String,
    onClick: () -> Unit,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        color = containerColor,
        contentColor = contentColor,
        modifier = modifier.defaultMinSize(minWidth = 48.dp, minHeight = 58.dp),
        tonalElevation = 2.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(imageVector = icon, contentDescription = contentDesc)
        }
    }
}

@Composable
fun SciKeyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        modifier = modifier
            .defaultMinSize(minWidth = 40.dp, minHeight = 42.dp)
            .testTag("sci_btn_$text")
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            )
        }
    }
}

@Composable
fun MemoryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        contentColor = MaterialTheme.colorScheme.secondary,
        modifier = modifier
            .defaultMinSize(minWidth = 48.dp, minHeight = 36.dp)
            .testTag("mem_btn_$text")
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}
