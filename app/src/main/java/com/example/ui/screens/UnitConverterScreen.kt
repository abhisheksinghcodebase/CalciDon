package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.engine.ConversionUnit
import com.example.engine.UnitCategory
import com.example.engine.UnitConverterEngine
import com.example.ui.CalculatorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitConverterScreen(
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier
) {
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val fromUnit by viewModel.fromUnit.collectAsState()
    val toUnit by viewModel.toUnit.collectAsState()
    val inputValue by viewModel.converterInputValue.collectAsState()
    val resultValue by viewModel.converterResultValue.collectAsState()

    var fromDropdownExpanded by remember { mutableStateOf(false) }
    var toDropdownExpanded by remember { mutableStateOf(false) }

    val categoryUnits = UnitConverterEngine.unitsMap[selectedCategory] ?: emptyList()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("unit_converter_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Unit Converter",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )

        // Category Chips Row
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(UnitCategory.values()) { category ->
                FilterChip(
                    selected = category == selectedCategory,
                    onClick = { viewModel.setUnitCategory(category) },
                    label = { Text(category.displayName) },
                    modifier = Modifier.testTag("chip_category_${category.name}")
                )
            }
        }

        // Input Value Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "From",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        value = inputValue,
                        onValueChange = { viewModel.setConverterInput(it) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("converter_input_field"),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    // From Unit Dropdown
                    Box {
                        Button(
                            onClick = { fromDropdownExpanded = true },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("from_unit_dropdown_btn")
                        ) {
                            Text("${fromUnit.name} (${fromUnit.symbol})")
                        }
                        DropdownMenu(
                            expanded = fromDropdownExpanded,
                            onDismissRequest = { fromDropdownExpanded = false }
                        ) {
                            categoryUnits.forEach { u ->
                                DropdownMenuItem(
                                    text = { Text("${u.name} (${u.symbol})") },
                                    onClick = {
                                        viewModel.setFromUnit(u)
                                        fromDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Swap Button
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = { viewModel.swapUnits() },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(50))
                    .testTag("swap_units_button")
            ) {
                Icon(
                    imageVector = Icons.Default.SwapVert,
                    contentDescription = "Swap Units",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // Output Result Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "To",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (resultValue.isEmpty()) "0" else resultValue,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("converter_result_text")
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    // To Unit Dropdown
                    Box {
                        Button(
                            onClick = { toDropdownExpanded = true },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier.testTag("to_unit_dropdown_btn")
                        ) {
                            Text("${toUnit.name} (${toUnit.symbol})")
                        }
                        DropdownMenu(
                            expanded = toDropdownExpanded,
                            onDismissRequest = { toDropdownExpanded = false }
                        ) {
                            categoryUnits.forEach { u ->
                                DropdownMenuItem(
                                    text = { Text("${u.name} (${u.symbol})") },
                                    onClick = {
                                        viewModel.setToUnit(u)
                                        toDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
