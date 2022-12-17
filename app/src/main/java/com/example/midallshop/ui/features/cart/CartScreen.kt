package com.example.midallshop.ui.features.cart

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.midallshop.R
import com.example.midallshop.model.data.Product
import com.example.midallshop.ui.features.profile.AddUserLocationDataDialog
import com.example.midallshop.ui.theme.Blue
import com.example.midallshop.ui.theme.PriceBackground
import com.example.midallshop.utils.NetworkChecker
import com.example.midallshop.utils.PAYMENT_PENDING
import com.example.midallshop.utils.RoutesName
import com.example.midallshop.utils.priceConvertor
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel

@Composable
fun CartScreen() {

    val navigation = getNavController()
    val context = LocalContext.current
    val viewModel = getNavViewModel<CartViewModel>()
    viewModel.getUserCartInfo()

    Box(contentAlignment = Alignment.BottomCenter) {

        Column(modifier = Modifier.fillMaxWidth()) {

            val productCartList = viewModel.userProducts.value

            CartToolbar("My Cart", onBackPressed = {
                navigation.popBackStack()
            },
                onProfilePressed = {

                    navigation.navigate(RoutesName.ProfileScreen.route)

                })

            if (productCartList.isNotEmpty()) {
                LazyColumn(modifier = Modifier.padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(bottom = 64.dp)) {

                    items(count = productCartList.size) { it ->
                        ProductCartItem(productCartList[it],
                            isChangingNumber = viewModel.isChangingNumber.value,
                            OnAddItemClicked = {
                                viewModel.addItem(it)
                            },
                            OnRemoveItemClicked = {
                                viewModel.removeItem(it)
                            },
                            OnItemClicked = { productId ->
                                navigation.navigate(RoutesName.ProductScreen.route + "/" + productId)
                            })
                    }

                }
            } else
                NoDataAnimation()
        }

        PurchaseAll(viewModel.totalPrice.value) {

            if (viewModel.userProducts.value.isNotEmpty()) {

                val locationData = viewModel.getLocationInfo()

                if (locationData.first == "Click here to add" || locationData.second == "Click here to add") {
                    viewModel.showInfoDialog.value = true
                } else {
                    viewModel.purchaseAll(locationData.first!!,
                        locationData.second!!) { link, success ->

                        if (success) {

                            Toast.makeText(context, "Pay using ZarinPal...", Toast.LENGTH_SHORT)
                                .show()

                            viewModel.setPaymentStatus(PAYMENT_PENDING)

                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                            context.startActivity(intent)
                        } else {
                            Toast.makeText(context, "problem in payment...", Toast.LENGTH_SHORT)
                                .show()
                        }

                    }
                }

            } else
                Toast.makeText(context,
                    "Product basket is empty",
                    Toast.LENGTH_SHORT).show()
        }

        if (viewModel.showInfoDialog.value) {
            AddUserLocationDataDialog(
                true,
                onDismiss = {
                    viewModel.showInfoDialog.value = false
                },
                onSubmitClicked = { address, postalCode, checked ->

                    if (NetworkChecker(context).isInternetConnected) {
                        if (checked) {
                            viewModel.setUserLocation(address, postalCode)
                        }
                        viewModel.purchaseAll(address, postalCode) { link, success ->

                            if (success) {
                                Toast.makeText(context, "Pay using ZarinPal...", Toast.LENGTH_SHORT)
                                    .show()

                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                                context.startActivity(intent)
                            } else {
                                Toast.makeText(context, "problem in payment...", Toast.LENGTH_SHORT)
                                    .show()
                            }

                        }

                    } else {
                        Toast.makeText(context,
                            "Please connect to internet ...",
                            Toast.LENGTH_SHORT)
                            .show()
                    }

                }
            )
        }


    }


}


@Composable
fun PurchaseAll(
    totalPrice: String,
    OnPurchaseClicked: () -> Unit,
) {

    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val fraction =
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 0.15f else 0.07f

    Surface(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(fraction),
        color =  Color.White) {

        Row(horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {

            Button(onClick = {
                if (NetworkChecker(context).isInternetConnected) {
                    OnPurchaseClicked.invoke()
                } else
                    Toast.makeText(context, "Please connect to internet ...", Toast.LENGTH_SHORT)
                        .show()
            },
                modifier = Modifier
                    .size(182.dp, 40.dp)
                    .padding(start = 16.dp)) {

                Text(text = "Let's Purchase !",
                    modifier = Modifier.padding(2.dp),
                    color = Color.White,
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium))

            }

            Surface(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clip(RoundedCornerShape(14.dp)),
                color = PriceBackground
            ) {

                Text(
                    modifier = Modifier.padding(
                        top = 6.dp,
                        bottom = 6.dp,
                        start = 8.dp,
                        end = 8.dp
                    ),
                    text = "total : " + priceConvertor(totalPrice),
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium)
                )
            }

        }
    }
}

@Composable
fun ProductCartItem(
    product: Product,
    isChangingNumber: Pair<String, Boolean>,
    OnAddItemClicked: (String) -> Unit,
    OnRemoveItemClicked: (String) -> Unit,
    OnItemClicked: (String) -> Unit,
) {
    Card(backgroundColor = Color.White,
        shape = RoundedCornerShape(14.dp),
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { OnItemClicked.invoke(product.productId) }) {

        Column(modifier = Modifier.fillMaxWidth()) {

            AsyncImage(model = product.imgUrl,
                contentDescription = null,
                modifier = Modifier.height(200.dp),
                contentScale = ContentScale.Crop)

            Row(verticalAlignment = Alignment.CenterVertically) {

                Column(modifier = Modifier.padding(12.dp)) {

                    Text(text = product.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium)

                    Text(text = "From ${product.category} group",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Light,
                        modifier = Modifier.padding(top = 3.dp))

                    Text(text = "Product authenticity guarantee",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Light,
                        modifier = Modifier.padding(top = 14.dp))

                    Text(text = "Available in stock to ship",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light,
                        modifier = Modifier.padding(top = 3.dp))

                    Surface(
                        modifier = Modifier
                            .padding(top = 18.dp, bottom = 6.dp)
                            .clip(RoundedCornerShape(14.dp)),
                        color = PriceBackground
                    ) {

                        Text(
                            modifier = Modifier.padding(
                                top = 6.dp,
                                bottom = 6.dp,
                                start = 8.dp,
                                end = 8.dp
                            ),
                            text = priceConvertor(
                                (product.price.toInt() * (product.quantity
                                    ?: "1").toInt()).toString()
                            ),
                            style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        )
                    }
                }

                Surface(
                    modifier = Modifier
                        .padding(bottom = 14.dp, end = 8.dp)
                        .align(Alignment.Bottom)
                ) {


                    Card(
                        border = BorderStroke(2.dp, Blue), shape = RoundedCornerShape(14.dp)
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {


                            if (product.quantity?.toInt() == 1) {

                                IconButton(onClick = { OnRemoveItemClicked.invoke(product.productId) }) {
                                    Icon(
                                        modifier = Modifier.padding(end = 4.dp, start = 4.dp),
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null
                                    )
                                }

                            } else {

                                IconButton(onClick = { OnRemoveItemClicked.invoke(product.productId) }) {
                                    Icon(
                                        modifier = Modifier.padding(end = 4.dp, start = 4.dp),
                                        painter = painterResource(R.drawable.ic_minus),
                                        contentDescription = null
                                    )
                                }

                            }


                            // size of product
                            if (isChangingNumber.first == product.productId && isChangingNumber.second) {

                                Text(
                                    text = "...",
                                    style = TextStyle(fontSize = 18.sp),
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )

                            } else {

                                Text(
                                    text = product.quantity ?: "1",
                                    style = TextStyle(fontSize = 18.sp),
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )

                            }


                            // add button +
                            IconButton(onClick = { OnAddItemClicked.invoke(product.productId) }) {
                                Icon(
                                    modifier = Modifier.padding(end = 4.dp, start = 4.dp),
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }


            }


        }

    }
}


@Composable
fun CartToolbar(
    title: String,
    onBackPressed: () -> Unit,
    onProfilePressed: () -> Unit,
) {

    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color.White,
        title = {
            Text(text = title, textAlign = TextAlign.Center)
        },
        actions = {

            IconButton(onClick = { onProfilePressed.invoke() }) {
                Icon(Icons.Default.Person, contentDescription = null)
            }
        },
        navigationIcon = {
            IconButton(onClick = { onBackPressed.invoke() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
            }
        }
    )

}

@Composable
fun NoDataAnimation() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.no_data))
    LottieAnimation(composition = composition, iterations = LottieConstants.IterateForever)
}
