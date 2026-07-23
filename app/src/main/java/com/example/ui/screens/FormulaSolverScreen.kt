package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.FormulaReference
import com.example.ui.CalculatorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormulaSolverScreen(
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    // Quadratic Solver State
    var quadA by remember { mutableStateOf("1") }
    var quadB by remember { mutableStateOf("-5") }
    var quadC by remember { mutableStateOf("6") }
    var quadResult by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("formula_solver_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Formulas & Constants",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )

        PrimaryTabRow(selectedTabIndex = selectedTab) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Constants") },
                modifier = Modifier.testTag("tab_constants")
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Formulas") },
                modifier = Modifier.testTag("tab_formulas")
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("Quadratic Solver") },
                modifier = Modifier.testTag("tab_quadratic")
            )
        }

        when (selectedTab) {
            0 -> {
                // Constants List
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(FormulaReference.constantsList) { constant ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${constant.name} (${constant.symbol})",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = "${constant.value} ${constant.unit}",
                                        style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary)
                                    )
                                    Text(
                                        text = constant.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Button(
                                    onClick = { viewModel.insertIntoExpression(constant.symbol) },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.testTag("insert_constant_${constant.symbol}")
                                ) {
                                    Text("Use")
                                }
                            }
                        }
                    }
                }
            }
            1 -> {
                // Formulas List
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(FormulaReference.formulasList) { formula ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = formula.title,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    AssistChip(
                                        onClick = {},
                                        label = { Text(formula.category) }
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = formula.formulaText,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = formula.explanation,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                if (formula.expressionTemplate.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = { viewModel.insertIntoExpression(formula.expressionTemplate) },
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text("Insert Formula")
                                    }
                                }
                            }
                        }
                    }
                }
            }
            2 -> {
                // Quadratic Solver Tool
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Solve ax² + bx + c = 0",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = quadA,
                                onValueChange = { quadA = it },
                                label = { Text("a") },
                                modifier = Modifier.weight(1f).testTag("quad_input_a"),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                            OutlinedTextField(
                                value = quadB,
                                onValueChange = { quadB = it },
                                label = { Text("b") },
                                modifier = Modifier.weight(1f).testTag("quad_input_b"),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                            OutlinedTextField(
                                value = quadC,
                                onValueChange = { quadC = it },
                                label = { Text("c") },
                                modifier = Modifier.weight(1f).testTag("quad_input_c"),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                        }

                        Button(
                            onClick = {
                                val a = quadA.toDoubleOrNull() ?: 1.0
                                val b = quadB.toDoubleOrNull() ?: 0.0
                                val c = quadC.toDoubleOrNull() ?: 0.0
                                quadResult = FormulaReference.solveQuadratic(a, b, c)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("solve_quadratic_btn")
                        ) {
                            Icon(imageVector = Icons.Default.Calculate, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Calculate Roots")
                        }

                        if (quadResult.isNotEmpty()) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = quadResult,
                                    modifier = Modifier.padding(16.dp),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
