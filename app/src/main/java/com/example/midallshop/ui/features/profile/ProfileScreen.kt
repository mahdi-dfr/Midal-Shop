package com.example.midallshop.ui.features.profile

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.midallshop.R
import com.example.midallshop.ui.theme.Blue
import com.example.midallshop.utils.RoutesName
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel

@Composable
fun ProfileScreen() {

    val viewModel = getNavViewModel<ProfileViewModel>()
    val navigation = getNavController()
    val context = LocalContext.current


    Box {


        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally) {

            ProfileToolbar() {
                navigation.popBackStack()
            }

            Spacer(modifier = Modifier.height(16.dp))

            MainAnimation()

            Spacer(modifier = Modifier.height(16.dp))

            SectionFields(sectionTitle = "Email Address",
                sectionText = viewModel.username.value,
                null)

            Spacer(modifier = Modifier.height(8.dp))

            SectionFields(sectionTitle = "Address", sectionText = viewModel.userLocation.value) {
                viewModel.getDialog.value = true

            }

            Spacer(modifier = Modifier.height(8.dp))

            SectionFields(sectionTitle = "Postal Code",
                sectionText = viewModel.userPostalCode.value) {
                viewModel.getDialog.value = true

            }

            Spacer(modifier = Modifier.height(8.dp))

            SectionFields(sectionTitle = "Login Time",
                sectionText = viewModel.userLoginTime.value,
                null)

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                viewModel.signOut()
                Toast.makeText(context, "Hope to see you again :)", Toast.LENGTH_SHORT).show()
                navigation.navigate(RoutesName.SignInScreen.route) {

                    popUpTo(RoutesName.MainScreen.route) {
                        inclusive = true
                    }
                }
            },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(top = 36.dp, bottom = 8.dp)) {

                Text(text = "Sign Out")

            }

        }
    }

    if (viewModel.getDialog.value) {
        AddUserLocationDataDialog(
            false,
            onDismiss = {
                viewModel.getDialog.value = false

            },
            onSubmitClicked = { address, postalCode, _ ->
                viewModel.setUserLocation(address, postalCode)
                viewModel.getUserLocationAndPostalCode()
            }
        )
    }
}

@Composable
fun AddUserLocationDataDialog(
    showSaveLocation: Boolean,
    onDismiss: () -> Unit,
    onSubmitClicked: (String, String, Boolean) -> Unit,
) {

    val context = LocalContext.current
    val checkedState = remember { mutableStateOf(true) }
    val userAddress = remember { mutableStateOf("") }
    val userPostalCode = remember { mutableStateOf("") }
    val fraction = if (showSaveLocation) 0.695f else 0.625f

    Dialog(onDismissRequest = onDismiss) {

        Card(
            modifier = Modifier.fillMaxHeight(fraction),
            elevation = 8.dp,
            shape = RoundedCornerShape(14.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {

                Text(
                    text = "Add Location Data",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))

                MainTextField(userAddress.value, "your address...") {
                    userAddress.value = it
                }

                MainTextField(userPostalCode.value, "your postal code...") {
                    userPostalCode.value = it
                }

                if (showSaveLocation) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp, start = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Checkbox(
                            checked = checkedState.value,
                            onCheckedChange = { checkedState.value = it },
                        )

                        Text(text = "Save To Profile")

                    }

                }


                // Buttons
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    TextButton(onClick = onDismiss) {
                        Text(text = "Cancel")
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    TextButton(onClick = {

                        if (
                            (userAddress.value.isNotEmpty() || userAddress.value.isNotBlank()) &&
                            (userPostalCode.value.isNotEmpty() || userPostalCode.value.isNotBlank())
                        ) {
                            onSubmitClicked(
                                userAddress.value,
                                userPostalCode.value,
                                checkedState.value
                            )
                            onDismiss.invoke()
                        } else {
                            Toast.makeText(context, "please write first...", Toast.LENGTH_SHORT)
                                .show()
                        }


                    }) {
                        Text(text = "Ok")
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileToolbar(onBackPressed: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = "My Profile",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 74.dp))
        },
        backgroundColor = Color.White,
        elevation = 4.dp,
        navigationIcon = {
            IconButton(onClick = { onBackPressed.invoke() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)

            }
        }
    )
}

@Composable
fun MainAnimation() {

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.profile_anim)
    )

    LottieAnimation(
        modifier = Modifier
            .size(270.dp)
            .padding(top = 36.dp, bottom = 16.dp),
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

}

@Composable
fun SectionFields(sectionTitle: String, sectionText: String, onTextClicked: (() -> Unit)?) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
    ) {

        Text(
            text = sectionTitle, fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 2.dp),
            color = Blue,
            fontWeight = FontWeight.Bold)

        Text(text = sectionText,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.clickable { onTextClicked?.invoke() })

        Divider(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
            color = Blue,
            thickness = 0.5.dp)

    }
}

@Composable
fun MainTextField(value: String, hint: String, onDataChanged: (String) -> Unit) {
    OutlinedTextField(value = value,
        onValueChange = { onDataChanged(it) },
        label = { Text(text = hint) },
        placeholder = { Text(text = hint) },
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(top = 12.dp),
        maxLines = 1,
        shape = RoundedCornerShape(14.dp)
    )
}