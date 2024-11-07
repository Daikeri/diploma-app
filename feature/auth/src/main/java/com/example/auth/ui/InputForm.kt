package com.example.auth.ui
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.auth.R
import com.example.auth.viewmodel.AuthViewModel
import kotlinx.coroutines.launch


@Composable
fun InputFormScreen(
    viewModel: AuthViewModel = hiltViewModel(),
    onTransitionButtonClick: () -> Unit
) {
    val uiState = viewModel.uiState.observeAsState()
    val snackBarState = remember { SnackbarHostState() }
    val screenScope = rememberCoroutineScope()
    Log.e("From Compose", "${uiState.value?.isSuccessful}")

    LaunchedEffect(key1 = uiState.value) {

        uiState.value?.let {
            if (it.isSuccessful) {
                onTransitionButtonClick()
            } else
                snackBarState.showSnackbar(it.errorMessage!!)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            imageVector = ImageVector.vectorResource(R.drawable.blurry_background_meta),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .blur(50.dp)
        )

        CustomSnackBar(
            snackBarState = snackBarState,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        InputFieldSurface(
            Modifier
                .align(Alignment.Center)
                .padding(bottom = 195.dp)
        ) { email: String, password: String ->
            screenScope.launch {
                viewModel.authExistUser(email, password)
            }
        }
    }
}

@Composable
fun CustomSnackBar(
    snackBarState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    SnackbarHost(
        hostState = snackBarState,
        modifier = Modifier.then(modifier)
    ) {
        Snackbar(
            actionOnNewLine = true,
            containerColor = Color(0xFF93000A),
            contentColor = Color(0xFFFFDAD6),
            modifier = Modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            snackBarState.currentSnackbarData?.visuals?.let { it1 ->
                Text(
                    modifier  = modifier.fillMaxWidth(),
                    text = it1.message,
                    style = MaterialTheme.typography.labelLarge,
                    textAlign = TextAlign.Justify
                )
            }
        }
    }
}


@Composable
fun InputFieldSurface(
    modifier: Modifier = Modifier,
    onButtonClick: (String, String) -> Unit
) {
    val emailState = rememberInputFieldState()
    val passwordState = rememberInputFieldState()

    val buttonLockState by remember {
        derivedStateOf {
            emailState.isCorrectInput && passwordState.isCorrectInput
        }
    }

    val onInputChange = { inputFieldState: InputFieldState, regex: Regex ->
        if (inputFieldState.isFirstInput)
            inputFieldState.isFirstInput = false

        inputFieldState.isCorrectInput = regex.matches(inputFieldState.textValue)
    }

    val customEasing = CubicBezierEasing(0.2f, 0f, 0f, 1f)

    Surface(
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 4.dp,
        modifier = Modifier
            .width(340.dp)
            .animateContentSize(animationSpec = tween(500, easing = customEasing))
            .then(modifier)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.login_24px),
                contentDescription = null,
                tint = Color(0xFF4F5D75), //0xFFA2D2FF
                modifier = Modifier
                    .padding(top = 10.dp)
            )

            Text(
                text = "Enter your details",
                textAlign = TextAlign.Center,
                color = Color(0xFF495057),
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = 17.sp
                ),
                modifier = Modifier
                    .padding(top = 4.dp, bottom = 10.dp)
            )

            InputField(
                inputFieldState = emailState,
                modifier = Modifier.padding(bottom = 10.dp),
                label = "Email",
                regex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,})+\$"), // email
                onValueChange = onInputChange
            )

            InputField(
                inputFieldState = passwordState,
                modifier = Modifier.padding(bottom = 40.dp),
                label = "Password",
                regex = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+\$).{8,}\$"),
                shielding = true,
                onValueChange = onInputChange
            )

            TransitionButton(isVisible = buttonLockState, onButtonClick = {
                onButtonClick(emailState.textValue, passwordState.textValue)
            })
        }
    }
}


@Composable
fun InputField(
    inputFieldState: InputFieldState,
    modifier: Modifier = Modifier,
    label: String = "",
    regex: Regex = Regex(".*"),
    shielding: Boolean = false,
    onValueChange: (InputFieldState, Regex) -> Unit = {_,_ -> }
) {
    OutlinedTextField(
        modifier = Modifier.then(modifier),
        value = inputFieldState.textValue,
        onValueChange = {
            inputFieldState.textValue = it
            onValueChange(
                inputFieldState,
                regex
            )
        },
        singleLine = true,
        label = { Text(text = label) },
        visualTransformation = if (shielding) PasswordVisualTransformation() else VisualTransformation.None,
        isError = if (inputFieldState.isFirstInput) false else !inputFieldState.isCorrectInput,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF495057),
            focusedLabelColor = Color(0xFF495057),
            unfocusedBorderColor = Color(0xFF6c757d),
            unfocusedLabelColor = Color(0xFF6c757d),
            cursorColor = Color(0xFF4F5D75)
        )
    )
}

class InputFieldState {
    var textValue by mutableStateOf("")
    var isFirstInput by mutableStateOf(true)
    var isCorrectInput by mutableStateOf(false)
}

@Composable
fun rememberInputFieldState() = remember { InputFieldState() }

@Composable
fun TransitionButton(
    isVisible: Boolean,
    onButtonClick: () -> Unit
) {
    AnimatedVisibility(visible = isVisible, modifier = Modifier.fillMaxWidth()) {
        Button(
            shape = RoundedCornerShape(
                topStart = 0.dp,
                bottomStart = 12.dp,
                topEnd = 0.dp,
                bottomEnd = 12.dp
            ),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF495057)),
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .background(Color.Red),
            onClick = { onButtonClick() },
        ) {
            Icon(imageVector = Icons.AutoMirrored.Default.ArrowForward, contentDescription = null)
        }
    }
}
