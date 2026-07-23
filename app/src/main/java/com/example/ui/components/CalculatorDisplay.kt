package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.AngleMode
import com.example.ui.theme.CyberActionEquals
import com.example.ui.theme.DarkCyberDisplayBackground

@Composable
fun CalculatorDisplay(
    expression: String,
    previewResult: String,
    memoryValue: Double,
    angleMode: AngleMode,
    errorMessage: String?,
    onToggleAngleMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(24.dp))
            .testTag("calculator_display"),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        tonalElevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.End
        ) {
            // Status Badges Row (Angle Mode, Memory Indicator)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Angle Mode Badge (Tappable)
                    Surface(
                        onClick = onToggleAngleMode,
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.testTag("angle_mode_badge")
                    ) {
                        Text(
                            text = angleMode.name,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Memory Indicator
                    if (memoryValue != 0.0) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            modifier = Modifier.testTag("memory_value_badge")
                        ) {
                            Text(
                                text = "M = ${if (memoryValue % 1 == 0.0) memoryValue.toLong() else memoryValue}",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            )
                        }
                    }
                }

                // Calculator Brand Badge
                Text(
                    text = "calciDon",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                        letterSpacing = 1.5.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Expression Input Display
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState, reverseScrolling = true),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = if (expression.isEmpty()) "0" else expression,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = if (expression.length > 15) 28.sp else 38.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    modifier = Modifier.testTag("expression_text")
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Real-time Preview Result or Error Banner
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                if (errorMessage != null) {
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        ),
                        textAlign = TextAlign.End,
                        modifier = Modifier.testTag("error_message_text")
                    )
                } else if (previewResult.isNotEmpty()) {
                    Text(
                        text = "= $previewResult",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        textAlign = TextAlign.End,
                        modifier = Modifier.testTag("preview_result_text")
                    )
                } else {
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }
}
