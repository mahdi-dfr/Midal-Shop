package com.example.midallshop.ui.features.product

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.midallshop.R
import com.example.midallshop.model.data.Comment
import com.example.midallshop.model.data.Product
import com.example.midallshop.ui.theme.Blue
import com.example.midallshop.ui.theme.PriceBackground
import com.example.midallshop.utils.DotsTyping
import com.example.midallshop.utils.NetworkChecker
import com.example.midallshop.utils.RoutesName
import com.example.midallshop.utils.priceConvertor
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel

@Composable
fun ProductScreen(productId: String) {

    val navigation = getNavController()
    val context = LocalContext.current
    val viewModel = getNavViewModel<ProductViewModel>()
    viewModel.loadProducts(productId, NetworkChecker(context).isInternetConnected)



    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {

        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 58.dp)) {

            ProductToolbar("Details",
                viewModel.cartSize.value,
                onBackPressed = { navigation.popBackStack() },
                onCartPressed = {
                    if (NetworkChecker(context).isInternetConnected) {
                        navigation.navigate(RoutesName.CartScreen.route)
                    } else {
                        Toast.makeText(
                            context,
                            "please connect to internet first...",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })


            val comments = viewModel.productComments.value
            val product = viewModel.thisProduct.value

            ProductDetails(product, comments,
                onHashtagClicked = {
                    navigation.navigate(RoutesName.CategoryScreen.route + "/" + it)

                }, onAddDialogClicked = {
                    viewModel.addComment(comment = it, productId = productId) { message ->

                        Toast.makeText(
                            context,
                            message,
                            Toast.LENGTH_SHORT
                        ).show()


                    }

                })

        }

        AddToCart(viewModel.loadingAnim.value, viewModel.thisProduct.value.price) {
            viewModel.addToCart(productId) { message ->
                Toast.makeText(
                    context,
                    message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}


// Toolbar --------------------------------------------------------------------------------------------------

@Composable
fun ProductToolbar(
    title: String,
    badgeNumber: Int,
    onBackPressed: () -> Unit,
    onCartPressed: () -> Unit,
) {

    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color.White,
        title = {
            Text(text = title, textAlign = TextAlign.Center)
        },
        actions = {

            IconButton(onClick = { onCartPressed.invoke() }) {
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
        },
        navigationIcon = {
            IconButton(onClick = { onBackPressed.invoke() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
            }
        }
    )

}


// Product Design--------------------------------------------------------------------------------------------

@Composable
fun ProductDetails(
    product: Product,
    comment: List<Comment>,
    onHashtagClicked: (String) -> Unit,
    onAddDialogClicked: (String) -> Unit,
) {
    Column(modifier = Modifier
        .padding(16.dp)
    ) {

        ProductDesign(product, onHashtagClicked)

        Divider(modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
            .height(1.dp),
            color = LightGray)

        ProductInfo(product, comment)

        Divider(modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
            .height(1.dp),
            color = LightGray)

        ProductComment(comment, onConfirmDialog = {
            onAddDialogClicked(it)
        })


    }
}

@Composable
fun ProductDesign(product: Product, onHashtagClicked: (String) -> Unit) {
    AsyncImage(
        model = product.imgUrl,
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .size(200.dp)
            .clip(shape = RoundedCornerShape(14.dp)),
        contentScale = ContentScale.Crop
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = product.name,
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium
    )

    Spacer(modifier = Modifier.height(12.dp))

    Text(
        text = product.detailText,
        fontSize = 16.sp,
        fontWeight = FontWeight.Light,
        textAlign = TextAlign.Justify
    )

    Spacer(modifier = Modifier.height(16.dp))

    TextButton(onClick = { onHashtagClicked.invoke(product.category) }) {

        Text(
            text = "#" + product.category,
            fontSize = 13.sp,
        )
    }
}


@Composable
fun ProductInfo(product: Product, comment: List<Comment>) {

    val context = LocalContext.current

    Row(modifier = Modifier
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom) {

        Column() {

            ProductInfoColumnView(R.drawable.ic_details_comment,
                if (NetworkChecker(context).isInternetConnected) comment.size.toString() + " comments"
                else "No Internet Connection")
            ProductInfoColumnView(R.drawable.ic_details_material, product.material)
            ProductInfoColumnView(R.drawable.ic_details_price, product.soldItem + " Sold")

        }

        Surface(color = Blue, shape = RoundedCornerShape(14.dp)) {
            Text(text = product.tags, modifier = Modifier.padding(8.dp))
        }

    }
}

@Composable
fun ProductComment(
    comments: List<Comment>,
    onConfirmDialog: (String) -> Unit,
) {

    val context = LocalContext.current
    val dialogState = remember { mutableStateOf(false) }

    if (comments.isNotEmpty()) {

        Row(modifier = Modifier
            .padding(bottom = 16.dp)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {


            Text(
                text = "Comments",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            TextButton(onClick = {
                if (NetworkChecker(context).isInternetConnected)
                    dialogState.value = true
                else {
                    Toast.makeText(
                        context,
                        "please connect to internet first...",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }) {
                Text(text = "Add New Comment", fontSize = 16.sp)
            }
        }

        comments.forEach {
            CommentItem(it)
        }

    } else {
        TextButton(onClick = {
            if (NetworkChecker(context).isInternetConnected)
                dialogState.value = true
            else {
                Toast.makeText(
                    context,
                    "please connect to internet first...",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }) {
            Text(text = "Add New Comment", fontSize = 16.sp)
        }
    }


    if (dialogState.value) {
        DialogAddComment(onConfirmDialog = {
            onConfirmDialog(it)
        }, onDismissDialog = {
            dialogState.value = false
        })
    }

}

@Composable
fun DialogAddComment(
    onDismissDialog: () -> Unit,
    onConfirmDialog: (String) -> Unit,
) {
    Dialog(onDismissRequest = { onDismissDialog.invoke() }) {

        var commentValueState = remember { mutableStateOf("") }
        val context = LocalContext.current

        Card(
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxHeight(0.53f),
            elevation = 8.dp
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()) {

                Text(
                    text = "Write Your Comment",
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                CommentTextField(value = commentValueState.value, hint = "write something...") {
                    commentValueState.value = it
                }

                Row(horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()) {

                    TextButton(onClick = {
                        onDismissDialog.invoke()
                    }) {
                        Text(text = "Cancel", fontSize = 14.sp)
                    }

                    TextButton(onClick = {
                        if (commentValueState.value.isNotEmpty() && commentValueState.value.isNotBlank()) {
                            if (NetworkChecker(context).isInternetConnected) {
                                onConfirmDialog.invoke(commentValueState.value)
                                onDismissDialog.invoke()
                            } else
                                Toast.makeText(context,
                                    "Please connect to internet",
                                    Toast.LENGTH_SHORT).show()
                        }

                    }) {
                        Text(text = "Ok", fontSize = 14.sp)
                    }

                }


            }

        }

    }
}


@Composable
fun CommentItem(comment: Comment) {
    Card(
        border = BorderStroke(width = 1.dp, color = Color.LightGray),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)) {

        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = comment.userEmail,
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = comment.text,
                fontSize = 14.sp,
                color = Color.Black,
                fontWeight = FontWeight.Medium)
        }

    }
}


@Composable
fun ProductInfoColumnView(icon: Int, text: String) {
    Row(modifier = Modifier.padding(bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically) {

        Image(painter = painterResource(id = icon), contentDescription = null)

        Text(text = text, modifier = Modifier.padding(start = 8.dp))

    }
}


// Add To Cart Design----------------------------------------------------------------------------------------

@Composable
fun AddToCart(animation: Boolean, price: String, onButtonClicked: () -> Unit) {

    val configurationChange = LocalConfiguration.current
    val fraction =
        if (configurationChange.orientation == Configuration.ORIENTATION_LANDSCAPE) 0.15f else 0.08f

    Surface(color = Color.White, modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(fraction)) {

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {

            Button(onClick = { onButtonClicked.invoke() },
                modifier = Modifier.size(150.dp, 40.dp)) {

                if (animation)
                    DotsTyping()
                else {
                    Text(
                        text = "Add To Cart",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(2.dp),
                        color = Color.White,
                    )
                }


            }

            Surface(color = PriceBackground,
                modifier = Modifier.padding(8.dp),
                shape = RoundedCornerShape(14.dp)) {

                Text(
                    text = priceConvertor(price),
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(8.dp),
                )

            }

        }


    }
}


// Extras----------------------------------------------------------------------------------------------------

@Composable
fun CommentTextField(value: String, hint: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(value = value,
        onValueChange = { onValueChange(it) },
        singleLine = false,
        maxLines = 2,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth(0.9f),
        label = {
            Text(
                text = hint)
        },
        placeholder = {
            Text(
                text = hint)
        })
}
