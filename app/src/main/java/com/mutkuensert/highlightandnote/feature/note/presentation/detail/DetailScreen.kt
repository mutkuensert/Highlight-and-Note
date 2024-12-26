package com.mutkuensert.highlightandnote.feature.note.presentation.detail

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mutkuensert.highlightandnote.R
import com.mutkuensert.highlightandnote.core.compose.RepeatOnLifecycleEffect

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

    BackHandler {
        viewModel.handleBackClick()
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
            .background(MaterialTheme.colorScheme.surface)
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
private fun DeleteButton(onClickDelete: () -> Unit, modifier: Modifier = Modifier) {
    var isDeleteDialogVisible by remember { mutableStateOf(false) }

    IconButton(
        onClick = {
            isDeleteDialogVisible = true
        },
        modifier = modifier
    ) {
        Icon(Icons.Filled.Delete, contentDescription = null)
    }

    AnimatedVisibility(isDeleteDialogVisible) {
        DeleteDialog(onClickDelete, onClickCancel = { isDeleteDialogVisible = false })
    }
}

@Composable
private fun DeleteDialog(
    onClickDelete: () -> Unit,
    onClickCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        title = {
            Text(text = stringResource(R.string.delete))
        },
        text = {
            Text(text = stringResource(R.string.do_you_want_to_delete_your_note))
        },
        onDismissRequest = onClickCancel,
        confirmButton = {
            TextButton(
                onClick = onClickDelete
            ) {
                Text(stringResource(R.string.delete))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onClickCancel
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
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