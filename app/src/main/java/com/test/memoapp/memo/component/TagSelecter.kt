package com.test.memoapp.memo.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.test.memoapp.memo.Utils.DummyItems
import com.test.memoapp.memo.data.tag.TagEntity

@Composable
fun TagSelector(
    allTags: List<TagEntity>,
    selectTags: Set<String>,
    onTagToggle: (String) -> Unit
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        allTags.forEach { (tagId, tagName) ->
            val isSelected = selectTags.contains(tagId)

            FilterChip(
                selected = isSelected,
                onClick = {
                    onTagToggle.invoke(tagId)
                },
                label = {
                    Text(text = tagName, color = Color.Black)
                },

                leadingIcon = if (isSelected) {
                    {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else null,

                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}

@Composable
@Preview
private fun previewTagSelecter() {
    TagSelector(allTags = DummyItems.tagList, selectTags = DummyItems.selected, {})
}