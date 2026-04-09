package com.rubengarcia.rubensapp.gamemanager

import android.util.Log
import kotlin.math.ceil
enum class UnitType {
    BOW, SWORD, AXE, NEUTRAL
}
enum class Side {
    ALLY, ENEMY
}
data class UnitGroup(
    val side: Side,
    val type: UnitType,
    val count: Int
)
enum class CombatType {
    MERGE, CLASH
}
data class UnitGroupResultSummary(
    val groupA: UnitGroup,
    val groupB: UnitGroup,
    val result: UnitGroup,
    val combatType: CombatType
)

fun unitEffectiveness(a: UnitType, b: UnitType): Double {
    return when {
        a == UnitType.SWORD && b == UnitType.BOW -> 1.5
        a == UnitType.BOW && b == UnitType.AXE -> 1.5
        a == UnitType.AXE && b == UnitType.SWORD -> 1.5
        b == UnitType.SWORD && a == UnitType.BOW -> 1 / 1.5
        b == UnitType.BOW && a == UnitType.AXE -> 1 / 1.5
        b == UnitType.AXE && a == UnitType.SWORD -> 1 / 1.5
        else -> 1.0
    }
}

fun unitGroupMerge(a: UnitGroup, b: UnitGroup): UnitGroup {
    val total = a.count + b.count

    val resultingType = when {
        a.type == b.type -> a.type
        a.count > b.count -> a.type
        b.count > a.count -> b.type
        else -> b.type
    }

    return UnitGroup(
        side = a.side,
        type = resultingType,
        count = total
    )
}

fun unitGroupClash(a: UnitGroup, b: UnitGroup): UnitGroup {
    val troops = a.count
    val aEffectiveness = unitEffectiveness(a.type, b.type)
    val effectiveTroops = ceil(troops * aEffectiveness).toInt()
    val bEffectiveness = unitEffectiveness(b.type, a.type)


    if(effectiveTroops > b.count){
        val costOfTroops = ceil(b.count * bEffectiveness).toInt()
        val remaining = a.count - costOfTroops

        return a.copy(count = remaining)
    }else{
        val remaining = b.count - effectiveTroops

        return b.copy(count = remaining)
    }
}

fun resolveCombat(
    groupsOfUnits: List<UnitGroup>
): List<UnitGroupResultSummary>{

    if (groupsOfUnits.size < 2) {
        Log.e("CombatLogic", "Not enough groups of units to resolve combat, minimum of 2 required")
        return emptyList()
    }

    val results = mutableListOf<UnitGroupResultSummary>()
    var remainingResult = groupsOfUnits[0]
    var oldResult : UnitGroup

    for (i in 1..< groupsOfUnits.size) {
        val combatType: CombatType
        val a = remainingResult
        val b = groupsOfUnits[i]
        oldResult = remainingResult

         if (a.side == b.side) {
             remainingResult = unitGroupMerge(a, b)
             combatType = CombatType.MERGE
        }else{
            remainingResult = unitGroupClash(a, b)
            combatType = CombatType.CLASH
        }

        results.add(
            UnitGroupResultSummary(
                groupA = oldResult,
                groupB = groupsOfUnits[i],
                result = remainingResult,
                combatType = combatType
            )
        )
    }

    return results
}

fun findBestOrder(totalUnitGroups: List<UnitGroup>): List<UnitGroup> {
    if (totalUnitGroups.size <= 1) return totalUnitGroups

    val permutations = permutations(totalUnitGroups)
    Log.d("CombatLogic", "Permutations: $permutations ")

    return permutations.maxByOrNull {
        val finalResult = resolveCombat(it).lastOrNull()?.result
        if (finalResult?.side == Side.ALLY) finalResult.count else - (finalResult?.count ?: 0)
    } ?: totalUnitGroups
}

private fun <T> permutations(list: List<T>): List<List<T>> {
    if (list.isEmpty()) return listOf(emptyList())

    val result = mutableListOf<List<T>>()
    for (i in list.indices) {
        val element = list[i]
        val remaining = list.filterIndexed { index, _ -> index != i }
        for (p in permutations(remaining)) {
            result.add(listOf(element) + p)
        }
    }
    return result
}