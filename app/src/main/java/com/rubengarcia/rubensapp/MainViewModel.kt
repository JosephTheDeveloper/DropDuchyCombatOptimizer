package com.rubengarcia.rubensapp

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.rubengarcia.rubensapp.gamemanager.Side
import com.rubengarcia.rubensapp.gamemanager.UnitGroup
import com.rubengarcia.rubensapp.gamemanager.UnitGroupResultSummary
import com.rubengarcia.rubensapp.gamemanager.UnitType
import com.rubengarcia.rubensapp.gamemanager.findBestOrder
import com.rubengarcia.rubensapp.gamemanager.resolveCombat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class CalculationState {
    IDLE, READY, CALCULATING, ERROR, FINISHED
}

class MainViewModel : ViewModel() {
    private val _unitGroups = mutableStateListOf<UnitGroup>()
    val unitGroups: List<UnitGroup> get() = _unitGroups
    private val _combatSummary = mutableStateListOf<UnitGroupResultSummary>()
    val combatSummary: List<UnitGroupResultSummary> get() = _combatSummary
    private val _unitGroupToAdd = MutableStateFlow(UnitGroup(side = Side.ALLY, type = UnitType.SWORD, count = 1))
    val unitGroupToAdd: StateFlow<UnitGroup?> = _unitGroupToAdd.asStateFlow()

    private val _calculationState = mutableStateOf<CalculationState>(CalculationState.IDLE)
    val calculationState: CalculationState get() = _calculationState.value

    private val _toggleAddUnitGroup = mutableStateOf(false)
    val toggleAddUnitGroup: Boolean get() = _toggleAddUnitGroup.value
    fun buttonStartCalculation(){
        calculateCombat()
    }
    fun buttonReset(){
        clearGroups()
        _combatSummary.clear()
        toggleAddUnitGroup()
        setCalculationState(CalculationState.IDLE)
    }
    fun buttonClearUnitGroups(){
        clearGroups()
    }
    fun buttonToggleAddUnitGroup(){
        toggleAddUnitGroup()
    }
    fun buttonAddUnitGroup(){
        addUnitGroupToUnitGroups()
    }
    fun buttonRemoveUnitGroup(index: Int){
        removeUnitGroupAtIndex(index)
    }
    fun buttonSetUnitGroupSide(side: Side) {
        _unitGroupToAdd.value = _unitGroupToAdd.value.copy(side = side)
    }
    fun buttonSetUnitGroupType(type: UnitType) {
        _unitGroupToAdd.value = _unitGroupToAdd.value.copy(type = type)
    }
    fun buttonSetUnitGroupCount(count: Int) {
        _unitGroupToAdd.value = _unitGroupToAdd.value.copy(count = count)
    }
    private fun calculateCombat() {
        setCalculationState(CalculationState.CALCULATING)

        val bestOrder = findBestOrder(_unitGroups.toList())

        val bestOrderCombatSummary =
            resolveCombat(bestOrder)

        if (bestOrderCombatSummary.isNotEmpty()) {
            setCalculationState(CalculationState.FINISHED)
            clearCombatSummary()
            setCombatSummary(bestOrderCombatSummary)
        }else{
            setCalculationState(CalculationState.ERROR)
        }
    }


    // UNIT GROUP METHODS
    private fun addUnitGroup(unitGroup: UnitGroup) {
        _unitGroups.add(unitGroup)
        checkCalculationStateReady()
    }
    private fun removeUnitGroupAtIndex(index: Int) {
        _unitGroups.removeAt(index)
        checkCalculationStateReady()
    }
    private fun clearGroups() {
        _unitGroups.clear()
    }

    // CALCULATION STATE METHODS
    private fun setCalculationState(state: CalculationState) {
        _calculationState.value = state
    }
    private fun checkCalculationStateReady(){
        if(_unitGroups.size < 2){
            setCalculationState(CalculationState.IDLE)
        }else{
            setCalculationState(CalculationState.READY)
        }
    }
    private fun clearCombatSummary(){
        _combatSummary.clear()
    }
    private fun setCombatSummary(summary: List<UnitGroupResultSummary>){
        _combatSummary.addAll(summary)
    }

    // ADD UNIT GROUP & TOGGLE METHODS
    private fun toggleAddUnitGroup() {
        _toggleAddUnitGroup.value = !_toggleAddUnitGroup.value
        if (_toggleAddUnitGroup.value) {
            clearUnitGroupToAdd()
        }
    }
    private fun clearUnitGroupToAdd() {
        _unitGroupToAdd.value = UnitGroup(side = Side.ALLY, type = UnitType.SWORD, count = 1)
    }
    private fun updateUnitGroupToAdd(newUnitGroup: UnitGroup) {
        _unitGroupToAdd.value = newUnitGroup
    }
    private fun addUnitGroupToUnitGroups() {
        _unitGroupToAdd.value?.let { unitGroup ->
            addUnitGroup(unitGroup)
        }
        toggleAddUnitGroup()
        _unitGroupToAdd.value = UnitGroup(side = Side.ALLY, type = UnitType.SWORD, count = 1)
    }

}
