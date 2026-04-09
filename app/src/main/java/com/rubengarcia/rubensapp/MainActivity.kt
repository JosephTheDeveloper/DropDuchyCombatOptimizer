package com.rubengarcia.rubensapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.rubengarcia.rubensapp.gamemanager.Side
import com.rubengarcia.rubensapp.gamemanager.UnitGroup
import com.rubengarcia.rubensapp.gamemanager.UnitType
import com.rubengarcia.rubensapp.ui.composables.AddUnitGroupField
import com.rubengarcia.rubensapp.ui.composables.PrimaryColorButton
import com.rubengarcia.rubensapp.ui.composables.SingleUnitGroupDisplay
import com.rubengarcia.rubensapp.ui.composables.UnitCombatSummaryDisplay
import com.rubengarcia.rubensapp.ui.composables.UnitGroupHeader
import com.rubengarcia.rubensapp.ui.theme.DropDuchyNeutral40
import com.rubengarcia.rubensapp.ui.theme.DropDuchyNeutral60
import com.rubengarcia.rubensapp.ui.theme.DropDuchyPrimary100
import com.rubengarcia.rubensapp.ui.theme.DropDuchyPrimary40
import com.rubengarcia.rubensapp.ui.theme.DropDuchyPrimary60
import com.rubengarcia.rubensapp.ui.theme.DropDuchyPrimary80
import com.rubengarcia.rubensapp.ui.theme.RubensAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RubensAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = MainViewModel()
                    )
                }
            }
        }
    }
}
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {
    Column (
        modifier = modifier
            .fillMaxSize(),
    ) {
        DropDuchyCalculator(
            mainViewModel = viewModel
        )
    }
}
@Composable
fun DropDuchyCalculator(
    mainViewModel: MainViewModel
) {
    Column(modifier = Modifier
        .fillMaxSize(),
    ) {
        DropDuchyCalculatorTitle(
            onClearClick = { mainViewModel.buttonReset() }
        )
        HorizontalDivider(thickness = 2.dp, color = DropDuchyNeutral40)

        Row(
            modifier = Modifier.fillMaxSize(),
        ){
            PrepareUnitsContent(
                modifier = Modifier.weight(1f),
                mainViewModel = mainViewModel
            )
            VerticalDivider(thickness = 2.dp, color = DropDuchyNeutral40)
            ResultSummaryContent(
                modifier = Modifier.weight(1f),
                mainViewModel = mainViewModel
            )
        }
    }
}
@Composable
fun DropDuchyCalculatorTitle(
    modifier: Modifier = Modifier,
    onClearClick: () -> Unit = {},
){
    Row (
        modifier = modifier
            .background(color = DropDuchyNeutral60)
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            modifier = Modifier.size(50.dp),
            painter = painterResource(id = R.drawable.ic_castle),
            tint = DropDuchyPrimary60,
            contentDescription = null,
        )
        Spacer(Modifier.padding(horizontal = 50.dp))
        Text(
            text = "Drop Duchy - Combat Order Optimizer",
            style = typography.headlineLarge,
            color = DropDuchyPrimary60,
        )
        Spacer(Modifier.padding(horizontal = 50.dp))
        Icon(
            modifier = Modifier
                .size(40.dp)
                .clickable(onClick = onClearClick)
                .border(
                    width = 1.dp,
                    color = DropDuchyPrimary40,
                    shape = RoundedCornerShape(percent = 10)
                )
                .padding(6.dp),
            imageVector = Icons.Default.Refresh,
            tint = DropDuchyPrimary60,
            contentDescription = "Clear Button",
        )
    }
}

@Composable
fun PrepareUnitsContent(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel
) {
    val groups = mainViewModel.unitGroups

    Column(
       modifier = modifier,
       horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PrimaryHeader(
            modifier = Modifier,
            text = "Prepare Units"
        )
        UnitGroupsDisplay(
           modifier = Modifier,
           groups = groups,
        )
        Spacer(Modifier.weight(1f))
        Row (
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            AddUnitGroupField(
                modifier = Modifier,
                mainViewModel = mainViewModel,
            )
            PrimaryColorButton(
                modifier = Modifier,
                text = "Clear Units",
                onClick = {mainViewModel.buttonClearUnitGroups()}
            )
        }
    }
}

@Composable
fun UnitGroupsDisplay(
    modifier: Modifier = Modifier,
    groups: List<UnitGroup>,
) {
    val allyUnitGroups = groups.filter { it.side == Side.ALLY }
    val enemyUnitGroups = groups.filter { it.side == Side.ENEMY }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .background(color = DropDuchyNeutral60, shape = RoundedCornerShape(5.dp))
            .padding(8.dp),
    ) {
        TeamUnitGroup(
            modifier = Modifier.weight(1f),
            teamUnitGroups = allyUnitGroups,
            side = Side.ALLY
        )
        //VerticalDivider(modifier = Modifier, thickness = 2.dp, color = DropDuchyPrimary100)
        TeamUnitGroup(
            modifier = Modifier.weight(1f),
            teamUnitGroups = enemyUnitGroups,
            side = Side.ENEMY
        )
    }
}

@Composable
fun TeamUnitGroup(
    modifier: Modifier = Modifier,
    teamUnitGroups: List<UnitGroup>,
    side: Side
) {
    val totalUnits = teamUnitGroups.sumOf { it.count }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            UnitGroupHeader(side = side, amount = totalUnits)
            HorizontalDivider(modifier = Modifier.padding(vertical = 2.dp), thickness = 2.dp, color = DropDuchyPrimary100)
        }
        items(teamUnitGroups) { group ->
            SingleUnitGroupDisplay(group = group)
        }
    }
}

@Composable
fun ResultSummaryContent(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel
) {
    val calculationState = mainViewModel.calculationState
    val combatSummary = mainViewModel.combatSummary
    val finalResult : UnitGroup =
        combatSummary.lastOrNull()?.result ?: UnitGroup(side = Side.ALLY, type = UnitType.NEUTRAL, count = 0)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PrimaryHeader(
            modifier = Modifier,
            text = "Combat Results"
        )

        when(calculationState){
            CalculationState.IDLE -> {
                Text("Not Enough Groups")
            }
            CalculationState.READY -> {
                Text("Ready to calculate")
            }
            CalculationState.CALCULATING -> {
                Text("Calculating...")
            }
            CalculationState.ERROR -> {
                Text("Error calculating")
            }
            CalculationState.FINISHED -> {
                Text("Optimal Combat Order:\n")
                LazyColumn(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    items(combatSummary) { summary ->
                        UnitCombatSummaryDisplay(
                            summary = summary
                        )
                    }
                }

                Text("Final Result:\n")
                SingleUnitGroupDisplay(group = finalResult)

            }
        }
        Spacer(Modifier.weight(1f))
        PrimaryColorButton(
            modifier = Modifier,
            onClick = { mainViewModel.buttonStartCalculation() },
            enabled = calculationState == CalculationState.READY,
            text = "Calculate Best Order"
        )
    }
}

@Composable
fun StartCalculateButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    calculationState: CalculationState
) {
    Button(
        modifier = modifier.height(60.dp),
        shape = RoundedCornerShape(percent = 10),
        enabled = calculationState == CalculationState.READY,
        onClick = {
            onClick()
        }
    ) {
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = null,
        )
        Spacer(modifier = Modifier.padding(horizontal = 10.dp))
        Text(
            text = "Calculate Best Order",
            style = typography.headlineSmall
        )
    }
}

@Composable
fun PrimaryHeader(
    modifier: Modifier,
    text: String,
) {
    Row(
        modifier = modifier
            .height(50.dp)
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        VerticalDivider(modifier = Modifier.padding(horizontal = 12.dp),thickness = 2.dp, color = DropDuchyPrimary40)
        Text(
            modifier = Modifier,
            text = text,
            style = typography.headlineSmall,
            color = Color.White,
        )
    }
}
