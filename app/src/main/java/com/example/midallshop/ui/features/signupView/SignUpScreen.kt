package com.example.midallshop.ui.features.signupView

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.midallshop.R
import com.example.midallshop.ui.theme.Blue
import com.example.midallshop.ui.theme.MidallShopTheme
import com.example.midallshop.utils.NetworkChecker
import com.example.midallshop.utils.RoutesName
import com.example.midallshop.utils.VALUE_SUCCESS
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MidallShopTheme {
        SignUpScreen()
    }
}

@Composable
fun SignUpScreen() {

    val uiController = rememberSystemUiController()
    SideEffect { uiController.setStatusBarColor(Blue) }

    val viewModel = getNavViewModel<SignUpViewModel>()
    val navigation = getNavController()
    val context = LocalContext.current

    Box() {

        BackBlueSurface()

        Column(modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top) {

            AppIcon()

            Spacer(modifier = Modifier.height(8.dp))

            MainCardView(viewModel, navigation) {
                viewModel.registerUser() {

                    if (it == VALUE_SUCCESS) {
                        navigation.navigate(RoutesName.MainScreen.route) {
                            popUpTo(RoutesName.IntroScreen.route) {
                                inclusive = true
                            }
                        }
                    } else {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

@Composable
fun BackBlueSurface() {
    Surface(color = MaterialTheme.colors.primary,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f)) {

    }
}

@Composable
fun MainCardView(
    viewModel: SignUpViewModel,
    navigation: NavController,
    onRegisterClick: () -> Unit,
) {

    val name = viewModel.name.observeAsState("")
    val email = viewModel.email.observeAsState("")
    val password = viewModel.password.observeAsState("")
    val repeatPassword = viewModel.repeatPassword.observeAsState("")
    val context = LocalContext.current


    Card(modifier = Modifier.fillMaxWidth(0.9f),
        shape = RoundedCornerShape(14.dp),
        elevation = 10.dp, backgroundColor = Color.White) {

        Column(modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(start = 18.dp, end = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {

            Text(text = "Sign Up",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Blue,
                modifier = Modifier.padding(top = 16.dp))

            Spacer(modifier = Modifier.height(12.dp))

            TextFieldSignUp(value = name.value,
                hint = "Name",
                icon = R.drawable.ic_person) {
                viewModel.name.value = it
            }

            TextFieldSignUp(value = email.value,
                hint = "Email",
                icon = R.drawable.ic_email) {
                viewModel.email.value = it
            }

            PasswordTextFieldSignUp(value = password.value,
                hint = "Password",
                icon = R.drawable.ic_password) {
                viewModel.password.value = it
            }

            PasswordTextFieldSignUp(value = repeatPassword.value,
                hint = "Repeat Password",
                icon = R.drawable.ic_password) {
                viewModel.repeatPassword.value = it
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {

                if (name.value.isNotEmpty() &&
                    email.value.isNotEmpty() &&
                    password.value.isNotEmpty() &&
                    repeatPassword.value.isNotEmpty()
                ) {
                    if (password.value.length >= 8) {
                        if (password.value == repeatPassword.value) {
                            if (Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {
                                if (NetworkChecker(context).isInternetConnected) {
                                    onRegisterClick.invoke()
                                } else
                                    Toast.makeText(context,
                                        "Please check your internet connection",
                                        Toast.LENGTH_SHORT).show()
                            } else
                                Toast.makeText(context,
                                    "email format is not true",
                                    Toast.LENGTH_SHORT).show()
                        } else
                            Toast.makeText(context, "passwords not match", Toast.LENGTH_SHORT)
                                .show()
                    } else
                        Toast.makeText(context,
                            "password characters should be more than 8!",
                            Toast.LENGTH_SHORT).show()
                } else
                    Toast.makeText(context, "Please inter all data", Toast.LENGTH_SHORT).show()

            }, modifier = Modifier.padding(8.dp)) {

                Text(text = "Register Account", modifier = Modifier.padding(8.dp))

            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 18.dp)) {

                Text(text = "Already have an account?")
                TextButton(onClick = {
                    navigation.navigate(RoutesName.SignInScreen.route) {
                        popUpTo(RoutesName.SignUpScreen.route) {
                            inclusive = true
                        }
                    }
                }) {
                    Text(text = "Log in")
                }
            }
        }
    }
}

@Composable
fun AppIcon() {
    Surface(
        shape = CircleShape,
        modifier = Modifier
            .padding(25.dp)
            .size(90.dp),
        color = Color.White,
    ) {

        Image(
            painter = painterResource(id = R.drawable.ic_icon_app),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.padding(25.dp)
        )

    }
}

@Composable
fun TextFieldSignUp(value: String, hint: String, icon: Int, onTextChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = { onTextChange(it) },
        placeholder = { Text(text = hint) },
        label = { Text(text = hint) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        leadingIcon = { Image(painter = painterResource(id = icon), contentDescription = null) },
        maxLines = 1,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)

    )
}

@Composable
fun PasswordTextFieldSignUp(
    value: String,
    hint: String,
    icon: Int,
    onTextChange: (String) -> Unit,
) {

    val passwordVisibility = remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = { onTextChange(it) },
        placeholder = { Text(text = hint) },
        label = { Text(text = hint) },
        leadingIcon = { Image(painter = painterResource(id = icon), contentDescription = null) },
        maxLines = 1,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        trailingIcon = {
            Image(
                painter = if (passwordVisibility.value) painterResource(id = R.drawable.ic_visible)
                else painterResource(
                    id = R.drawable.ic_invisible),
                contentDescription = null,
                modifier = Modifier.clickable {
                    passwordVisibility.value = !passwordVisibility.value
                })
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation()

    )
}

