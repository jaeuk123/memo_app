package com.test.memoapp.memo.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.test.memoapp.memo.Utils.getSectionShape

@Composable
fun TagsContents(text: String, shape: Shape, onClickEvent: (() -> Unit)) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = shape,
        color = Color.White,
    ) {
        Row(modifier = Modifier.clickable(onClick = onClickEvent), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = text,
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Icon(
                modifier = Modifier.padding(4.dp),
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = ""
            )
        }
    }
}

@Preview
@Composable
private fun previewTagContents() {
    TagsContents("태그 1", getSectionShape(0,1), { })
}