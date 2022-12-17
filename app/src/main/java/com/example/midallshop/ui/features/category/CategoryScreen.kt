package com.example.midallshop.ui.features.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.midallshop.model.data.Product
import com.example.midallshop.ui.theme.Blue
import com.example.midallshop.ui.theme.Shapes
import com.example.midallshop.utils.RoutesName
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel

@Composable
fun CategoryScreen(category: String) {

    val uiController = rememberSystemUiController()
    SideEffect { uiController.setStatusBarColor(Color.White) }

    val navigation = getNavController()
    val viewModel = getNavViewModel<CategoryViewModel>()
    viewModel.getProductByCategory(category)

    Column(modifier = Modifier
        .fillMaxWidth()
    ) {

        ToolbarCategory(category, navigation)

        val products = viewModel.productByCategory.value
        CategoryList(products) {
            navigation.navigate(RoutesName.ProductScreen.route + "/" + it)
        }

    }


}

@Composable
fun ToolbarCategory(category: String, navigation: NavHostController) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = Color.White,

        navigationIcon = {
            IconButton(onClick = {
                navigation.popBackStack()
            }) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)

            }
        },

        title = {
            Text(
                modifier = Modifier.fillMaxWidth().padding(end = 70.dp),
                text = category,
                textAlign = TextAlign.Center
            )
        }

    )

}

@Composable
fun CategoryList(products: List<Product>, onCategoryItemClicked: (String) -> Unit) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
        items(products.size) {
            CategoryView(products[it], onCategoryItemClicked)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }


}

@Composable
fun CategoryView(products: Product, onCategoryItemClicked: (String) -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCategoryItemClicked.invoke(products.productId) },
        shape = RoundedCornerShape(14.dp),
        elevation = 4.dp
    ) {

        Column(modifier = Modifier.fillMaxWidth()) {

            AsyncImage(model = products.imgUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp))

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween) {

                Column(modifier = Modifier.padding(start = 16.dp)) {
                    Text(text = products.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))

                    Text(text = products.price + " Tomans",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 16.dp))
                }

                Surface(color = Blue,
                    modifier = Modifier
                        .clip(Shapes.large)
                        .padding(8.dp)
                        .align(Alignment.Bottom)
                ) {

                    Text(text = products.soldItem + " Sold",
                        fontSize = 14.sp,
                        modifier = Modifier.padding(8.dp),
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )

                }

            }

        }

    }
}
