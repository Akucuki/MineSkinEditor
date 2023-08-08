package com.example.mineskineditorlibgdx.features.addonsEditor.categoryChooser

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.mineskineditorlibgdx.utils.NavigationDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryChooserViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val navDispatcher: NavigationDispatcher
) : ViewModel() {


}