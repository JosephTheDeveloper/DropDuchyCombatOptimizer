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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.rubengarcia.rubensapp.gamemanager.Side
import com.rubengarcia.rubensapp.gamemanager.UnitGroup
import com.rubengarcia.rubensapp.gamemanager.UnitType
import com.rubengarcia.rubensapp.ui.composables.AddUnitGroupBox
import com.rubengarcia.rubensapp.ui.composables.PrimaryColorButton
import com.rubengarcia.rubensapp.ui.composables.SingleUnitGroupDisplay
import com.rubengarcia.rubensapp.ui.composables.UnitCombatSummaryDisplay
import com.rubengarcia.rubensapp.ui.composables.UnitGroupHeader
import com.rubengarcia.rubensapp.ui.theme.DropDuchyNeutral40
import com.rubengarcia.rubensapp.ui.theme.DropDuchyNeutral60
import com.rubengarcia.rubensapp.ui.theme.DropDuchyPrimary100
import com.rubengarcia.rubensapp.ui.theme.DropDuchyPrimary40
import com.rubengarcia.rubensapp.ui.theme.DropDuchyPrimary60
import com.rubengarcia.rubensapp.ui.theme.RubensAppTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)

            RubensAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = MainViewModel(),
                        windowSizeClass = windowSizeClass
                    )
                }
            }
        }
    }
}
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    windowSizeClass: WindowSizeClass
) {
    Column(modifier = modifier.fillMaxSize()) {
        DropDuchyCalculator(
            mainViewModel = viewModel,
            windowSizeClass = windowSizeClass
        )
    }
}
@Composable
fun DropDuchyCalculator(
    mainViewModel: MainViewModel,
    windowSizeClass: WindowSizeClass
) {
    val screenSize = windowSizeClass.widthSizeClass

    Column(modifier = Modifier.fillMaxSize()) {
        DropDuchyCalculatorTitle(
            modifier = Modifier,
            screenSize = screenSize,
            onClearClick = { mainViewModel.buttonReset() }
        )
        when (screenSize) {
            WindowWidthSizeClass.Compact,
            WindowWidthSizeClass.Medium ->
                DropDuchyCalculatorPortraitLayout(
                    modifier = Modifier,
                    mainViewModel = mainViewModel
                )

            WindowWidthSizeClass.Expanded ->
                DropDuchyCalculatorLandscapeLayout(
                        modifier = Modifier,
                        mainViewModel = mainViewModel
                )
        }
    }
}

@Composable
fun DropDuchyCalculatorPortraitLayout(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,

) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        PrepareUnitsContent(
            modifier = Modifier,
            mainViewModel = mainViewModel,
            compact = true
        )

        HorizontalDivider(
            thickness = 2.dp,
            color = DropDuchyNeutral40
        )

        ResultSummaryContent(
            modifier = Modifier,
            mainViewModel = mainViewModel
        )
    }
}

@Composable
fun DropDuchyCalculatorLandscapeLayout(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel
) {
    Row(modifier = Modifier.fillMaxSize()) {

        PrepareUnitsContent(
            modifier = Modifier.weight(1f),
            mainViewModel = mainViewModel,
            compact = false
        )

        VerticalDivider(
            thickness = 2.dp,
            color = DropDuchyNeutral40
        )

        ResultSummaryContent(
            modifier = Modifier.weight(1f),
            mainViewModel = mainViewModel
        )
    }
}

@Composable
fun DropDuchyCalculatorTitle(
    modifier: Modifier = Modifier,
    onClearClick: () -> Unit = {},
    screenSize: WindowWidthSizeClass
){
    var titleStyle = typography.headlineLarge
    var title = "Drop Duchy - Combat Order Optimizer"
    var iconSize = 60.dp

    when (screenSize) {
        WindowWidthSizeClass.Compact,
        WindowWidthSizeClass.Medium -> {
            titleStyle = typography.bodyLarge
            title = "Drop Duchy\nCombat Order Optimizer"
            iconSize = 40.dp
        }
    }

    Row (
        modifier = modifier
            .background(color = DropDuchyNeutral60)
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        AppLogo(
            modifier = Modifier.size(iconSize),
        )
        Text(
            text = title,
            style = titleStyle,
            color = DropDuchyPrimary60,
        )
        ClearAllButton(
            modifier = Modifier.size(iconSize),
            onClick = onClearClick
        )
    }
    HorizontalDivider(thickness = 2.dp, color = DropDuchyNeutral40)
}

@Composable
fun AppLogo(
    modifier: Modifier = Modifier,
    color: Color = DropDuchyPrimary60
) {
    Icon(
        modifier = modifier,
        painter = painterResource(id = R.drawable.ic_castle),
        tint = color,
        contentDescription = null,
    )
}

@Composable
fun ClearAllButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Icon(
        modifier = modifier
            .clickable(onClick = onClick)
            .border(
                width = 1.dp,
                color = DropDuchyPrimary40,
                shape = RoundedCornerShape(percent = 10)
            )
            .padding(6.dp),
        imageVector = Icons.Filled.Cached,
        tint = DropDuchyPrimary60,
        contentDescription = "Clear Button",
    )
}

@Composable
fun PrepareUnitsContent(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
    compact: Boolean = false
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
        if(!compact){
            Spacer(Modifier.weight(1f))
        }
        PrepareUnitsFooterButtons(
            modifier = Modifier,
            mainViewModel = mainViewModel,
            compact = compact
        )
    }
}

@Composable
fun PrepareUnitsFooterButtons(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
    compact: Boolean = false
) {
    val toggleAddUnitGroup = mainViewModel.toggleAddUnitGroup

    if (compact) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            if (!toggleAddUnitGroup) {
                PrimaryColorButton(
                    modifier = modifier.fillMaxWidth(),
                    text = "Add Unit Group",
                    onClick = {mainViewModel.buttonToggleAddUnitGroup()}
                )
            }else{
                AddUnitGroupBox(
                    modifier = modifier.fillMaxWidth(),
                    mainViewModel = mainViewModel
                )
            }
            Spacer(Modifier.height(8.dp))
            PrimaryColorButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Clear Units",
                onClick = {mainViewModel.buttonClearUnitGroups()}
            )
        }
    }else{
        Row (
            modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Bottom
        ) {
            if (!toggleAddUnitGroup) {
                PrimaryColorButton(
                    modifier = modifier.weight(.7f),
                    text = "Add Unit Group",
                    onClick = {mainViewModel.buttonToggleAddUnitGroup()}
                )
            }else{
                AddUnitGroupBox(
                    modifier = modifier.weight(.60f),
                    mainViewModel = mainViewModel
                )
            }

            Spacer(Modifier.width(8.dp))

            PrimaryColorButton(
                modifier = Modifier.weight(.4f),
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
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .background(color = DropDuchyNeutral60, shape = RoundedCornerShape(5.dp))
            .padding(8.dp),
    ) {
        TeamUnitGroup(
            modifier = Modifier.weight(1f),
            teamUnitGroups = allyUnitGroups,
            side = Side.ALLY
        )
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
fun PrimaryHeader(
    modifier: Modifier,
    text: String,
) {
    Row(
        modifier = modifier
            .height(50.dp)
            .fillMaxWidth()
            .padding(vertical = 4.dp),
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
