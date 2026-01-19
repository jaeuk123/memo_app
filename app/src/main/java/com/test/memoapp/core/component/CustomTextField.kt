package com.test.memoapp.core.component

import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

//텍스트 필드 배경 및 하단 라인 컬러 설정
@Composable
fun customTextFieldColors(
    backgroundColor: Color = Color.Transparent ,
    underLineColor : Color = Color.Transparent
): TextFieldColors {
    return TextFieldDefaults.colors(
        // 배경색 설정
        focusedContainerColor = backgroundColor,
        unfocusedContainerColor = backgroundColor,
        disabledContainerColor = backgroundColor,

        // 하단 라인바 제거
        focusedIndicatorColor = underLineColor,
        unfocusedIndicatorColor = underLineColor,
        disabledIndicatorColor = underLineColor,
    )
}