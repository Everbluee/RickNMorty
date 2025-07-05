package com.example.ricknmorty.views

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.ricknmorty.MainActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionBar(header: String) {
    androidx.compose.material3.TopAppBar(
        title = { Text(text = header) },
        navigationIcon = { NavigateBackIconButton(context = LocalContext.current) }
    )
}

@Composable
private fun NavigateBackIconButton(context: Context) {
    IconButton(onClick = {
        (context as? MainActivity)?.onBackPressed()
    }) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Navigate Back",
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}