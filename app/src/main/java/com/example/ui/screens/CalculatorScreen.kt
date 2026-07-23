package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.CalculatorViewModel
import com.example.ui.MainTab
import com.example.ui.components.CalculatorDisplay
import com.example.ui.components.CalculatorKeypad
import com.example.ui.theme.AppThemeMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel
) {
    val activeTab by viewModel.activeTab.collectAsState()
    val themeMode by viewModel.themeMode.collectAsState()
    val expression by viewModel.expression.collectAsState()
    val previewResult by viewModel.previewResult.collectAsState()
    val memoryValue by viewModel.memoryValue.collectAsState()
    val angleMode by viewModel.angleMode.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isScientificExpanded by viewModel.isScientificExpanded.collectAsState()

    var themeMenuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Calculate,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "calciDon",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                },
                actions = {
                    // Theme Switcher Menu Button
                    Box {
                        IconButton(
                            onClick = { themeMenuExpanded = true },
                            modifier = Modifier.testTag("theme_switcher_btn")
                        ) {
                            Icon(imageVector = Icons.Default.Palette, contentDescription = "Change Theme")
                        }
                        DropdownMenu(
                            expanded = themeMenuExpanded,
                            onDismissRequest = { themeMenuExpanded = false }
                        ) {
                            AppThemeMode.values().forEach { mode ->
                                DropdownMenuItem(
                                    text = { Text(mode.label) },
                                    onClick = {
                                        viewModel.setThemeMode(mode)
                                        themeMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier.testTag("main_bottom_nav"),
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                NavigationBarItem(
                    selected = activeTab == MainTab.CALCULATOR,
                    onClick = { viewModel.setTab(MainTab.CALCULATOR) },
                    icon = { Icon(Icons.Default.Calculate, contentDescription = "Calculator") },
                    label = { Text("Calculator") },
                    modifier = Modifier.testTag("nav_item_calculator")
                )
                NavigationBarItem(
                    selected = activeTab == MainTab.CONVERTER,
                    onClick = { viewModel.setTab(MainTab.CONVERTER) },
                    icon = { Icon(Icons.Default.SwapHoriz, contentDescription = "Converter") },
                    label = { Text("Converter") },
                    modifier = Modifier.testTag("nav_item_converter")
                )
                NavigationBarItem(
                    selected = activeTab == MainTab.FORMULAS,
                    onClick = { viewModel.setTab(MainTab.FORMULAS) },
                    icon = { Icon(Icons.Default.Functions, contentDescription = "Formulas") },
                    label = { Text("Formulas") },
                    modifier = Modifier.testTag("nav_item_formulas")
                )
                NavigationBarItem(
                    selected = activeTab == MainTab.HISTORY,
                    onClick = { viewModel.setTab(MainTab.HISTORY) },
                    icon = { Icon(Icons.Default.History, contentDescription = "History") },
                    label = { Text("History") },
                    modifier = Modifier.testTag("nav_item_history")
                )
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (activeTab) {
                MainTab.CALCULATOR -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 8.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        CalculatorDisplay(
                            expression = expression,
                            previewResult = previewResult,
                            memoryValue = memoryValue,
                            angleMode = angleMode,
                            errorMessage = errorMessage,
                            onToggleAngleMode = { viewModel.toggleAngleMode() },
                            modifier = Modifier.weight(1f, fill = false)
                        )

                        CalculatorKeypad(
                            isScientificExpanded = isScientificExpanded,
                            onToggleScientific = { viewModel.toggleScientific() },
                            onKeyPress = { viewModel.onKeyPress(it) }
                        )
                    }
                }
                MainTab.CONVERTER -> UnitConverterScreen(viewModel)
                MainTab.FORMULAS -> FormulaSolverScreen(viewModel)
                MainTab.HISTORY -> HistoryScreen(viewModel)
            }
        }
    }
}
