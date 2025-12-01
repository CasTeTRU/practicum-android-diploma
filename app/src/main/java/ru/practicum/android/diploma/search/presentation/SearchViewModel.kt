package ru.practicum.android.diploma.search.presentation

import android.util.Log
import androidx.lifecycle.ViewModel

class SearchViewModel : ViewModel() {
    fun searchDebounce(changedText: String) {
        Log.i(TAG, changedText)
    }

    companion object {
        private const val TAG = "SearchViewModel"
    }
}
