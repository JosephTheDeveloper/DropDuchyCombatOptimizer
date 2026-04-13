package com.rubengarcia.rubensapp.ui.composables

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rubengarcia.rubensapp.MainViewModel
import com.rubengarcia.rubensapp.R
import com.rubengarcia.rubensapp.gamemanager.CombatType
import com.rubengarcia.rubensapp.gamemanager.Side
import com.rubengarcia.rubensapp.gamemanager.UnitGroup
import com.rubengarcia.rubensapp.gamemanager.UnitGroupResultSummary
import com.rubengarcia.rubensapp.gamemanager.UnitType
import com.rubengarcia.rubensapp.ui.theme.DropDuchyAlly100
import com.rubengarcia.rubensapp.ui.theme.DropDuchyAlly40
import com.rubengarcia.rubensapp.ui.theme.DropDuchyAlly60
import com.rubengarcia.rubensapp.ui.theme.DropDuchyEnemy100
import com.rubengarcia.rubensapp.ui.theme.DropDuchyEnemy40
import com.rubengarcia.rubensapp.ui.theme.DropDuchyEnemy60
import com.rubengarcia.rubensapp.ui.theme.DropDuchyEnemy80
import com.rubengarcia.rubensapp.ui.theme.DropDuchyNeutral100
import com.rubengarcia.rubensapp.ui.theme.DropDuchyNeutral40
import com.rubengarcia.rubensapp.ui.theme.DropDuchyNeutral60
import com.rubengarcia.rubensapp.ui.theme.DropDuchyPrimary40
import com.rubengarcia.rubensapp.ui.theme.DropDuchyPrimary60


@Composable
fun UnitCombatSummaryDisplay(
    modifier: Modifier = Modifier,
    summary: UnitGroupResultSummary
) {
    Row(
        modifier = modifier
            .background(color = DropDuchyNeutral60, shape = RoundedCornerShape(percent = 10))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        SingleUnitGroupDisplay(
            group = summary.groupA,
            compact = true

        )
        CombatTypeIcon(
            modifier = modifier.padding(horizontal = 6.dp),
            combatType = summary.combatType
        )
        SingleUnitGroupDisplay(
            group = summary.groupB,
            compact = true
        )
        Icon(
            imageVector = Icons.Filled.ArrowUpward,
            contentDescription = "result",
            modifier = modifier
                .padding(horizontal = 6.dp)
                .rotate(90f),
            tint = DropDuchyPrimary40
        )
        SingleUnitGroupDisplay(
            group = summary.result,
            compact = true
        )
    }
}

@Composable
fun SingleUnitGroupDisplay(
    modifier: Modifier = Modifier,
    group: UnitGroup,
    compact: Boolean = false
) {
    if(compact){
        return SingleUnitGroupDisplayCompact(
            modifier = modifier,
            group = group
        )
    }
    val troops = group.count
    var supportText = ""
    var color : Color
    var unitType = group.type

    when (group.side){
        Side.ALLY -> {
            supportText += "Ally |"
            color = DropDuchyAlly60
        }
        Side.ENEMY -> {
            supportText += "Enemy |"
            color = DropDuchyEnemy60
        }
    }

    supportText += when(group.type) {
        UnitType.SWORD -> " Sword"
        UnitType.BOW -> " Bow"
        UnitType.AXE -> " Axe"
        UnitType.NEUTRAL -> " Neutral"
    }

    if (troops == 0) {
        color = Color.Gray
        supportText = "No One Left Standing..."
        unitType = UnitType.NEUTRAL
    }

    Row(
        modifier = modifier
            .height(60.dp)
            .border(
                width = 1.dp,
                color = color,
                shape = RoundedCornerShape(percent = 10)
            )
            .padding(10.dp, 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = troops.toString(),
                style = typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = supportText,
                style = typography.labelSmall,
                color = color
            )
        }
        Spacer(Modifier.width(8.dp))
        UnitIcon(
            modifier = Modifier.size(40.dp),
            type = unitType,
            color = color
        )

    }
}
@Composable
fun SingleUnitGroupDisplayCompact(
    modifier: Modifier = Modifier,
    group: UnitGroup,
) {
    var unitType = group.type
    var color : Color = Color.Gray
    var bgColor : Color = Color.DarkGray

    when{
        group.count == 0 -> {
            color = Color.Gray
            bgColor = Color.DarkGray
            unitType = UnitType.NEUTRAL
        }
        group.side == Side.ALLY -> {
            color = DropDuchyAlly60
            bgColor = DropDuchyAlly100
        }
        group.side == Side.ENEMY -> {
            color = DropDuchyEnemy60
            bgColor = DropDuchyEnemy100
        }
    }

    Row(
        modifier = modifier
            .border(width = Dp.Hairline, shape = RoundedCornerShape(percent = 10), color = color)
            .background(color = bgColor, shape = RoundedCornerShape(percent = 10))
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = group.count.toString(),
            style = typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = color,
        )
        UnitIcon(
            modifier = Modifier.size(35.dp),
            type = unitType,
            color = color
        )

    }
}

@Composable
fun UnitIcon(
    type: UnitType,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified
) {
    val drawableId = when(type) {
        UnitType.SWORD -> R.drawable.ic_sword
        UnitType.BOW -> R.drawable.ic_bow
        UnitType.AXE -> R.drawable.ic_axe
        UnitType.NEUTRAL -> R.drawable.ic_skull
    }

    Icon(
        painter = painterResource(id = drawableId),
        contentDescription = type.name,
        modifier = modifier,
        tint = color
    )
}

@Composable
fun CombatTypeIcon(
    combatType: CombatType,
    modifier: Modifier = Modifier,
) {
    val drawableId = when(combatType) {
        CombatType.MERGE -> R.drawable.ic_merge
        CombatType.CLASH -> R.drawable.ic_clash
    }

    Icon(
        painter = painterResource(id = drawableId),
        contentDescription = combatType.name,
        modifier = modifier
            .size(50.dp)
            .border(
                width = 1.dp,
                color = DropDuchyPrimary40,
                shape = RoundedCornerShape(percent = 10)
            )
            .padding(6.dp),
        tint = DropDuchyPrimary40
    )
}

@Composable
fun UnitGroupHeader(
    modifier: Modifier = Modifier,
    amount: Int,
    side: Side
) {
    var supportText : String
    var teamColor : Color
    val backGroundColor = if (side == Side.ALLY) DropDuchyAlly100 else DropDuchyEnemy100

    when (side){
        Side.ALLY -> {
            supportText = "Ally"
            teamColor = DropDuchyAlly40
        }
        Side.ENEMY -> {
            supportText = "Enemy"
            teamColor = DropDuchyEnemy40
        }
    }

    Row(
        modifier = modifier
            .fillMaxSize()
            .background(backGroundColor, shape = RoundedCornerShape(percent = 10)),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = supportText,
            style = typography.headlineSmall,
            color = teamColor
        )

        Text(
            text = amount.toString(),
            style = typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = teamColor
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_flag),
            contentDescription = "team colored flag",
            modifier = modifier
                .size(50.dp)
                .padding(6.dp),
            tint = teamColor
        )
    }

}
@Composable
fun AddUnitGroupBox(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
) {
    val unitGroupToAdd by mainViewModel.unitGroupToAdd.collectAsState()
    val toggleAddUnitGroup = mainViewModel.toggleAddUnitGroup


    Column(
        modifier = modifier
            .background(color = DropDuchyNeutral60, shape = RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        UnitGroupSideSelector(
            modifier = Modifier.fillMaxWidth(),
            selectedSide = unitGroupToAdd?.side ?: Side.ALLY,
            setUnitGroupSide = {mainViewModel.buttonSetUnitGroupSide(it)}
        )
        Text(
            text = "UNIT SPECIFICATION",
            style = typography.labelMedium,
            color = DropDuchyNeutral40,
            letterSpacing = 1.sp
        )
        UnitGroupTypeSelector(
            modifier = Modifier.fillMaxWidth(),
            selectedType = unitGroupToAdd?.type ?: UnitType.SWORD,
            setUnitGroupType = {mainViewModel.buttonSetUnitGroupType(it)}
        )
        Text(
            text = "BATTALION SIZE",
            style = typography.labelMedium,
            color = DropDuchyNeutral40,
            letterSpacing = 1.sp
        )
        UnitGroupCountSelector(
            modifier = Modifier.fillMaxWidth(),
            selectedCount = unitGroupToAdd?.count ?: 1,
            setUnitGroupCount = {mainViewModel.buttonSetUnitGroupCount(it)}
        )
        PrimaryColorButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Add Unit Group",
            onClick = {mainViewModel.buttonAddUnitGroup()},
        )
    }
}
@Composable
fun PrimaryColorButton(
    modifier: Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = DropDuchyPrimary60,
            contentColor = DropDuchyNeutral100
        ),
        shape = RoundedCornerShape(6.dp)
    ) {
        Text(
            text = text,
            style = typography.titleLarge,
            fontWeight = FontWeight.Bold,
            letterSpacing = .5f.sp
        )
    }
}
@Composable
fun UnitGroupCountSelector(
    modifier: Modifier,
    selectedCount: Int,
    setUnitGroupCount: (Int) -> Unit
) {
    Row(
        modifier = modifier
            .background(Color.Black, shape = RoundedCornerShape(4.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "—",
            color = DropDuchyPrimary40,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { if (selectedCount > 1) setUnitGroupCount(selectedCount - 1) }
        )
        Text(
            text = selectedCount.toString(),
            color = Color.White,
            style = typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "+",
            color = DropDuchyPrimary40,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.clickable { setUnitGroupCount(selectedCount + 1) }
        )
    }
}

@Composable
fun UnitGroupTypeSelector(
    modifier: Modifier,
    selectedType: UnitType,
    setUnitGroupType: (UnitType) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        UnitTypeItem(
            modifier = Modifier.weight(1f),
            type = UnitType.SWORD,
            isSelected = selectedType == UnitType.SWORD,
            onClick = { setUnitGroupType(UnitType.SWORD) }
        )
        UnitTypeItem(
            modifier = Modifier.weight(1f),
            type = UnitType.BOW,
            isSelected = selectedType == UnitType.BOW,
            onClick = { setUnitGroupType(UnitType.BOW) }
        )
        UnitTypeItem(
            modifier = Modifier.weight(1f),
            type = UnitType.AXE,
            isSelected = selectedType == UnitType.AXE,
            onClick = { setUnitGroupType(UnitType.AXE) }
        )
        UnitTypeItem(
            modifier = Modifier.weight(1f),
            type = UnitType.NEUTRAL,
            isSelected = selectedType == UnitType.NEUTRAL,
            onClick = { setUnitGroupType(UnitType.NEUTRAL) }
        )
    }
}

@Composable
fun UnitTypeItem(
    modifier: Modifier = Modifier,
    type: UnitType,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) DropDuchyPrimary40 else Color.Transparent
    val tint = if (isSelected) DropDuchyPrimary40 else DropDuchyNeutral40

    Column(
        modifier = modifier
            .border(1.dp, borderColor, RoundedCornerShape(4.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        UnitIcon(type = type, modifier = Modifier.size(32.dp), color = tint)
        Text(
            text = type.name,
            style = typography.labelSmall,
            color = tint,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun UnitGroupSideSelector(
    modifier: Modifier,
    selectedSide: Side,
    setUnitGroupSide: (Side) -> Unit
) {
    Row(
        modifier = modifier
            .background(Color.Black, RoundedCornerShape(4.dp))
            .padding(4.dp)
    ) {
        SideItem(
            modifier = Modifier.weight(1f),
            side = Side.ALLY,
            isSelected = selectedSide == Side.ALLY,
            onClick = { setUnitGroupSide(Side.ALLY) }
        )
        SideItem(
            modifier = Modifier.weight(1f),
            side = Side.ENEMY,
            isSelected = selectedSide == Side.ENEMY,
            onClick = { setUnitGroupSide(Side.ENEMY) }
        )
    }
}

@Composable
fun SideItem(
    modifier: Modifier,
    side: Side,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        if (side == Side.ALLY) DropDuchyAlly100 else DropDuchyEnemy100
    } else Color.Transparent
    
    val contentColor = if (isSelected) {
        if (side == Side.ALLY) DropDuchyAlly60 else DropDuchyEnemy60
    } else DropDuchyNeutral40

    val iconId = if (side == Side.ALLY) R.drawable.ic_sword else R.drawable.ic_skull

    Row(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(4.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
//        Icon(
//            painter = painterResource(iconId),
//            contentDescription = null,
//            modifier = Modifier.size(18.dp),
//            tint = contentColor
//        )
//        Spacer(Modifier.width(8.dp))
        Text(
            text = side.name,
            style = typography.labelLarge,
            color = contentColor,
            fontWeight = FontWeight.Bold
        )
    }
}
