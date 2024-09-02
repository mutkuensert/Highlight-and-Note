package com.mutkuensert.highlightandnote.feature.note.presentation.detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mutkuensert.highlightandnote.feature.note.core.RepeatOnLifecycleEffect
import com.mutkuensert.highlightandnote.feature.note.core.asActivity

@Composable
fun DetailScreen(viewModel: DetailViewModel = hiltViewModel()) {
    val uiModel by viewModel.uiModel.collectAsStateWithLifecycle()

    Detail(
        uiModel = uiModel,
        onClickUndo = viewModel::handleUndoClick,
        onClickDelete = viewModel::handleDeleteClick,
        onTextChange = viewModel::handleTextChange
    )

    RepeatOnLifecycleEffect {
        viewModel.initScreen()
    }

    val activity = LocalContext.current.asActivity()

    BackHandler {
        viewModel.handleBackClick(onFinishApp = { activity?.finish() })
    }
}

@Composable
private fun Detail(
    uiModel: DetailUiModel,
    onClickUndo: () -> Unit,
    onClickDelete: () -> Unit,
    onTextChange: (String) -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Header(
            onClickUndo = onClickUndo,
            onClickDelete = onClickDelete
        )

        TextField(
            value = uiModel.text,
            onValueChange = onTextChange,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun Header(
    onClickUndo: () -> Unit,
    onClickDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.weight(1f))

        UndoButton(onClickUndo)

        DeleteButton(onClickDelete)
    }
}

@Composable
private fun UndoButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(imageVector = Icons.AutoMirrored.Filled.Undo, contentDescription = null)
    }
}

@Composable
private fun DeleteButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(imageVector = Icons.Filled.Delete, contentDescription = null)
    }
}

@Preview
@Composable
private fun DetailScreenPreview() {
    val uiModel = DetailUiModel("Some note")

    Detail(
        uiModel = uiModel,
        onClickUndo = {},
        onClickDelete = {},
        onTextChange = {}
    )
}