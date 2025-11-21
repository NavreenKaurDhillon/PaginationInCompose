package com.example.demopaginationapp.view.screens

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.demopaginationapp.model.dataclasses.ResponseDataItem
import com.google.gson.Gson

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailScreen(data: String?, navController: NavController) {
    val context = LocalContext.current
    val responseData : ResponseDataItem = remember(data) {
        (if (data!=null){
            try {
                Gson().fromJson(data, ResponseDataItem::class.java)
            } catch (e: Exception){
                Toast.makeText((context), "Exception caused is ${e.message}", Toast.LENGTH_LONG).show()
                Log.d("hhhh", "DetailScreen: exeption caused is ${e.message} ")
            }
        } else
            null) as ResponseDataItem
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp),
    ) {
        val uriHandler = LocalUriHandler.current
        val url = responseData.owner.organizations_url
        GlideImage(
            model = responseData.owner.avatar_url,
            contentDescription = "Logo description of the repo",
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 20.dp)
                .width(120.dp)
                .align(Alignment.CenterHorizontally)
                .height(120.dp),
            )
        Text(
            color = Color.Black,
            text = buildAnnotatedString {
                withStyle(BOLD_STYLE.toSpanStyle()){
                    append("ID :")
                }
                withStyle(NORMAL_STYLE.toSpanStyle()){
                    append(responseData.id.toString())
                }
            }
        )
        Text(
                color = Color.Black,
                text = buildAnnotatedString {
                    withStyle(BOLD_STYLE.toSpanStyle()){
                        append("Name :")
                    }
                    withStyle(NORMAL_STYLE.toSpanStyle()){
                        append(responseData.name)
                    }
                }
            )

        val annotatedString = buildAnnotatedString {
            withStyle(BOLD_STYLE.toSpanStyle().copy(color = Color.Black)) {
                append("Organisational URL : ")
            }
            pushStringAnnotation(
                tag = "Org_tag",
                annotation = url
            )
            withStyle(
                style = NORMAL_STYLE.toSpanStyle().copy(
                    color = Color.Blue,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append(url)
            }
            pop()
        }


        ClickableText(
            text = annotatedString,
            onClick = { offset ->
                annotatedString.getStringAnnotations(
                    tag = "Org_tag",
                    start = offset,
                    end = offset
                ).firstOrNull()?.let { annotation ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                    context.startActivity(intent)
                }
            },
            modifier = Modifier
        )


        Row(modifier = Modifier.weight(0.1f), verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = {
                navController.navigate("product_screen")
            },modifier = Modifier.weight(0.5f)) {
                Text(text = "Goto Viewers",  textAlign = TextAlign.Center)

            }
            Spacer(Modifier.width(30.dp))
            Button(onClick = {

                navController.navigate("list_screen")
            },modifier = Modifier.weight(0.5f)) {
                Text(text = "Goto Home",  textAlign = TextAlign.Center)

            }
        }

    }
}