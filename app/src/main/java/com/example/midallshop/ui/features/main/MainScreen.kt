package com.example.midallshop.ui.features.main

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.midallshop.R
import com.example.midallshop.model.data.Ads
import com.example.midallshop.model.data.Checkout
import com.example.midallshop.model.data.Product
import com.example.midallshop.ui.MidallShop
import com.example.midallshop.ui.theme.Blue
import com.example.midallshop.ui.theme.CardViewBackground
import com.example.midallshop.ui.theme.MidallShopTheme
import com.example.midallshop.utils.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel
import org.koin.core.parameter.parametersOf

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MidallShopTheme {
        MidallShop()
    }
}

@Composable
fun MainScreen() {

    val uiController = rememberSystemUiController()
    SideEffect { uiController.setStatusBarColor(Color.White) }

    val context = LocalContext.current

    val navigation = getNavController()

    val viewModel = getNavViewModel<MainViewModel>(
        parameters = {
            parametersOf(
                NetworkChecker(context).isInternetConnected
            )
        }
    )

    if (NetworkChecker(context).isInternetConnected) {
        viewModel.loadBadgeNumber()
    }

    if (viewModel.getPaymentStatus() == PAYMENT_PENDING) {
        if (NetworkChecker(context).isInternetConnected) {
            viewModel.getCheckoutData()
        }
    }


    Box() {

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(bottom = 16.dp)
        ) {

            if (viewModel.showProgressBar.value)
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = Blue
                )

            ToolbarView(navigation, viewModel.badgeNumber.value)

            CategoryBar(CATEGORY) {
                navigation.navigate(RoutesName.CategoryScreen.route + "/" + it)

            }

            Spacer(modifier = Modifier.height(16.dp))

            val productList = viewModel.dataProducts.value
            val addList = viewModel.dataAds.value
            Log.i("TAG", "MainScreen: "+productList.size)

            ProductSubject(TAGS, productList.shuffled(), addList) {
                navigation.navigate(RoutesName.ProductScreen.route + "/" + it)
            }

        }

        if (viewModel.showPaymentResultDialog.value) {

            PaymentResultDialog(
                checkoutResult = viewModel.checkoutData.value,
                onDismiss = {
                    viewModel.showPaymentResultDialog.value = false
                    viewModel.setPaymentStatus(NO_PAYMENT)
                }
            )

        }

    }

}


@Composable
fun ToolbarView(navigation: NavHostController, badgeNumber: Int) {
    val context = LocalContext.current

    TopAppBar(
        backgroundColor = Color.White,
        title = { Text(text = "Midall Shop") },
        actions = {
            IconButton(onClick = {
                if (NetworkChecker(context).isInternetConnected)
                    navigation.navigate(RoutesName.CartScreen.route)
                else{
                    Toast.makeText(context,
                        "Please connect to internet",
                        Toast.LENGTH_SHORT).show()
                }
            }) {
                if (badgeNumber != 0) {
                    BadgedBox(badge = {
                        Badge {
                            Text(text = badgeNumber.toString())
                        }
                    }) {

                        Icon(Icons.Default.ShoppingCart, contentDescription = null)
                    }
                } else
                    Icon(Icons.Default.ShoppingCart, contentDescription = null)
            }

            IconButton(onClick = { navigation.navigate(RoutesName.ProfileScreen.route) }) {
                Icon(Icons.Default.Person, contentDescription = null)
            }

        }

    )
}

//-----------------------------------------------------------------------------

@Composable
private fun PaymentResultDialog(
    checkoutResult: Checkout,
    onDismiss: () -> Unit
) {

    Dialog(onDismissRequest = onDismiss) {

        Card(
            elevation = 8.dp,
            shape = RoundedCornerShape(14.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Payment Result",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Main Data
                if (checkoutResult.order?.status?.toInt() == PAYMENT_SUCCESS) {

                    AsyncImage(
                        model = R.drawable.success_anim,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(110.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(text = "Payment was successful!", style = TextStyle(fontSize = 16.sp))
                    Text(
                        text = "Purchase Amount: " + priceConvertor(
                            (checkoutResult.order.amount).substring(
                                0,
                                (checkoutResult.order.amount).length - 1
                            )
                        )
                    )

                } else {

                    AsyncImage(
                        model = R.drawable.fail_anim,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(110.dp)
                            .padding(top = 6.dp, bottom = 6.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(text = "Payment was not successful!", style = TextStyle(fontSize = 16.sp))
                    Text(
                        text = "Purchase Amount: " + priceConvertor(
                            (checkoutResult.order!!.amount).substring(
                                0,
                                (checkoutResult.order!!.amount).length - 1
                            )
                        )
                    )

                }

                // Ok Button
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    TextButton(onClick = onDismiss) {
                        Text(text = "ok")
                    }
                    Spacer(modifier = Modifier.height(4.dp))

                }
            }
        }
    }
}


//-----------------------------------------------------------------------------

@Composable
fun CategoryBar(categoryList: List<Pair<String, Int>>, categoryClicked: (String) -> Unit) {

    LazyRow(
        modifier = Modifier.padding(top = 16.dp),
        contentPadding = PaddingValues(end = 16.dp)) {

        items(categoryList.size) {

            CategoryItem(categoryList[it]) { data ->
                categoryClicked.invoke(data)
            }

        }
    }

}

@Composable
fun CategoryItem(categoryList: Pair<String, Int>, onCategoryClicked: (String) -> Unit) {
    Column(
        modifier = Modifier
            .padding(start = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            color = CardViewBackground,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.clickable { onCategoryClicked.invoke(categoryList.first) }
        ) {
            Image(
                painter = painterResource(id = categoryList.second),
                modifier = Modifier.padding(16.dp),
                contentDescription = null)
        }

        Text(text = categoryList.first,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp))

    }

}

//-----------------------------------------------------------------------------

@Composable
fun AdvertisementView(product: Ads) {

    AsyncImage(
        contentScale = ContentScale.Crop,
        contentDescription = null,
        model = product.imageURL,
        modifier = Modifier
            .padding(16.dp)
            .height(260.dp)
            .fillMaxWidth()
            .clickable { }
            .clip(shape = RoundedCornerShape(10))

    )

}

//-----------------------------------------------------------------------------

@Composable
fun ProductSubject(
    tags: List<String>,
    productList: List<Product>,
    addList: List<Ads>,
    onProductClicked: (String) -> Unit,
) {

    if (productList.isNotEmpty()) {

        Column {

            tags.forEachIndexed { index, _ ->
                val dataList = productList.filter { product ->
                    product.tags == tags[index]
                }
                ProductBar(dataList, tags[index], onProductClicked)

                if (addList.size >= 2)
                    if (index == 1 || index == 2)
                        AdvertisementView(addList[index - 1])
            }

        }

    }


}


@Composable
fun ProductBar(dataList: List<Product>, tag: String, onProductClicked: (String) -> Unit) {
    Column(
        modifier = Modifier.padding(top = 32.dp)
    ) {

        Text(
            text = tag,
            modifier = Modifier.padding(start = 16.dp),
            style = MaterialTheme.typography.h6
        )

        ProductList(dataList, onProductClicked)

    }
}

@Composable
fun ProductList(data: List<Product>, onProductClicked: (String) -> Unit) {
    LazyRow(
        modifier = Modifier.padding(top = 16.dp),
        contentPadding = PaddingValues(end = 16.dp)
    ) {
        items(data.size) {

            ProductsItem(data[it], onProductClicked)

        }
    }

}

@Composable
fun ProductsItem(product: Product, onProductClicked: (String) -> Unit) {

    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .clickable { onProductClicked.invoke(product.productId) }
            .padding(start = 16.dp),
        elevation = 4.dp
    ) {

        Column() {


            AsyncImage(
                modifier = Modifier.size(250.dp),
                model = product.imgUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop)


            Column(modifier = Modifier.padding(10.dp)) {


                Text(
                    text = product.name,
                    fontSize = 17.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium,
                )

                Text(
                    text = product.price + " Tomans",
                    fontSize = 16.sp,
                    color = Color.Black,
                )

                Text(
                    text = product.soldItem + " Sold",
                    fontSize = 15.sp,
                    color = Color.Gray,
                )

            }

        }
    }


}

//-----------------------------------------------------------------------------
