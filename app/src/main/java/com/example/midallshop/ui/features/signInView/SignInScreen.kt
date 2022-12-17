package com.example.midallshop.ui.features.signInView

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset.Companion.Unspecified
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
import androidx.navigation.NavHostController
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
        SignInScreen()
    }
}

@Composable
fun SignInScreen() {

    val uiController = rememberSystemUiController()
    SideEffect { uiController.setStatusBarColor(Blue) }

    val viewModel = getNavViewModel<SignInViewModel>()
    val navigation = getNavController()
    val context = LocalContext.current

    Box {

        SignInBackBlueSurface()

        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top) {

            SignInAppIcon()

            Spacer(modifier = Modifier.height(8.dp))

            SignInMainCardView(navigation, viewModel) {
                viewModel.signIn() {

                    if (it == VALUE_SUCCESS) {
                        navigation.navigate(RoutesName.MainScreen.route) {
                            popUpTo(RoutesName.MainScreen.route) {
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
fun SignInMainCardView(
    navigation: NavHostController,
    viewModel: SignInViewModel,
    onSignInClicked: () -> Unit,
) {

    val userName = viewModel.userName.observeAsState("")
    val password = viewModel.password.observeAsState("")
    val context = LocalContext.current

    Card(shape = RoundedCornerShape(14.dp),
        elevation = 10.dp,
        modifier = Modifier.fillMaxWidth(0.9f),
        backgroundColor = Color.White) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(text = "Log In",
                color = Blue,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp))

            SignInTextField(userName.value, "User Name", R.drawable.ic_person) {
                viewModel.userName.value = it

            }

            TextFieldPasswordSignIn(password.value, "Password", R.drawable.ic_password) {

                viewModel.password.value = it
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {

                if (userName.value.isNotEmpty()
                    && password.value.isNotEmpty()
                ) {

                    if (Patterns.EMAIL_ADDRESS.matcher(userName.value).matches()) {

                        if (NetworkChecker(context).isInternetConnected) {
                            onSignInClicked.invoke()

                        } else
                            Toast.makeText(context,
                                "Please check your connection",
                                Toast.LENGTH_SHORT).show()

                    } else
                        Toast.makeText(context, "email format is not correct", Toast.LENGTH_SHORT)
                            .show()

                } else
                    Toast.makeText(context, "Please insert all data", Toast.LENGTH_SHORT).show()

            }) {
                Text(text = "Log In", modifier = Modifier.padding(12.dp))
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
            ) {

                Text(text = "Do not have an account?")

                Spacer(modifier = Modifier.width(8.dp))

                TextButton(onClick = {
                    navigation.navigate(RoutesName.SignUpScreen.route) {
                        popUpTo(RoutesName.IntroScreen.route) {
                            inclusive = true
                        }
                    }

                }) {
                    Text(text = "Sign In")

                }
            }
        }
    }
}

@Composable
fun SignInAppIcon() {
    Surface(
        shape = CircleShape,
        modifier = Modifier
            .padding(25.dp)
            .size(90.dp),
        color = Color.White
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
fun SignInBackBlueSurface() {
    Surface(color = MaterialTheme.colors.primary,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.4f)) {

    }
}

@Composable
fun SignInTextField(value: String, hint: String, icon: Int, onDataChanged: (String) -> Unit) {
    OutlinedTextField(value = value,
        onValueChange = { onDataChanged(it) },
        label = { Text(text = hint) },
        placeholder = { Text(text = hint) },
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(top = 12.dp),
        maxLines = 1,
        leadingIcon = { Image(painter = painterResource(id = icon), contentDescription = null) },
        shape = RoundedCornerShape(14.dp)
    )
}

@Composable
fun TextFieldPasswordSignIn(
    value: String,
    hint: String,
    icon: Int,
    onDataChanged: (String) -> Unit,
) {

    val passwordVisibility = remember { mutableStateOf(false) }

    OutlinedTextField(value = value,
        onValueChange = { onDataChanged(it) },
        label = { Text(text = hint) },
        placeholder = { Text(text = hint) },
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(top = 12.dp),
        maxLines = 1,
        leadingIcon = { Image(painter = painterResource(id = icon), contentDescription = null) },
        shape = RoundedCornerShape(14.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            Image(painter = if (passwordVisibility.value) painterResource(id = R.drawable.ic_visible) else painterResource(
                id = R.drawable.ic_invisible),
                contentDescription = null,
                Modifier.clickable { passwordVisibility.value = !passwordVisibility.value })
        },
        visualTransformation = if (!passwordVisibility.value) PasswordVisualTransformation() else VisualTransformation.None
    )
}


