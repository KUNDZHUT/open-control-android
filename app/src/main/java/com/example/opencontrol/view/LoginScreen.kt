package com.example.opencontrol.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

class LoginScreen : Screen {

    @Composable
    override fun Content() {
        LoginForm()
    }

    @Composable
    private fun LoginForm() {
        val navigator = LocalNavigator.currentOrThrow
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(verticalArrangement = Arrangement.Center) {
                Text(text = "LoginScreen")
                Button(
                    onClick = { navigator.push(MainScreen) },
                    shape = RoundedCornerShape(40)
                ) {
                    Text(text = "Войти")
                }
            }
        }
    }
}