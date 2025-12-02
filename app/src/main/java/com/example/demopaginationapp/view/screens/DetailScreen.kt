package com.example.demopaginationapp.view.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.demopaginationapp.model.dataclasses.ResponseDataItem
import com.example.demopaginationapp.navigation.Screens
import com.example.demopaginationapp.utils.BOLD_STYLE
import com.example.demopaginationapp.utils.Constants
import com.example.demopaginationapp.utils.NORMAL_STYLE
import com.example.demopaginationapp.utils.RounderRecGlideImage
import com.google.gson.Gson
import androidx.core.net.toUri

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
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
            }
        } else
            null) as ResponseDataItem
    }

    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {
                // go back to last screen
                navController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Go back",
                    tint = Color.Black,
                    modifier = Modifier.size(30.dp)
                )
            }
            Text(
                text = "Details",
                style = BOLD_STYLE,
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
        }
        ShowRepoDetail(responseData, context, navController)
    }

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ShowRepoDetail(
    responseData: ResponseDataItem,
    context: Context,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp).verticalScroll(rememberScrollState()),
    ) {
        val url = responseData.owner.organizations_url


        Spacer(Modifier.height(5.dp))
        RounderRecGlideImage(responseData.owner.avatar_url, 200.dp)
        Spacer(Modifier.height(15.dp))
        Column(modifier = Modifier.padding(horizontal = 10.dp)) {

            Text(
                text = buildAnnotatedString {
                    withStyle(BOLD_STYLE.toSpanStyle()) {
                        append("ID :")
                    }
                    withStyle(NORMAL_STYLE.toSpanStyle()) {
                        append(responseData.id.toString())
                    }
                }, modifier = Modifier.padding(top = 5.dp)
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(BOLD_STYLE.toSpanStyle()) {
                        append("Name :")
                    }
                    withStyle(NORMAL_STYLE.toSpanStyle()) {
                        append(responseData.name)
                    }
                }, modifier = Modifier.padding(top = 5.dp)
            )

            val annotatedString = buildAnnotatedString {
                withStyle(BOLD_STYLE.toSpanStyle()) {
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
                        val intent = Intent(Intent.ACTION_VIEW, annotation.item.toUri())
                        context.startActivity(intent)
                    }
                },
                modifier = Modifier.padding(top = 5.dp)
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(BOLD_STYLE.toSpanStyle()) {
                        append("Owner :")
                    }
                    withStyle(NORMAL_STYLE.toSpanStyle()) {
                        append(responseData.owner.id.toString())
                    }
                }, modifier = Modifier.padding(top = 5.dp)
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(BOLD_STYLE.toSpanStyle()) {
                        append("Description :")
                    }
                    withStyle(NORMAL_STYLE.toSpanStyle()) {
                        append(Constants.dummyDescription)
                    }
                }, modifier = Modifier.padding(top = 5.dp)
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(BOLD_STYLE.toSpanStyle()) {
                        append("Visibility :")
                    }
                    withStyle(NORMAL_STYLE.toSpanStyle()) {
                        append(responseData.visibility)
                    }
                }, modifier = Modifier.padding(top = 5.dp)
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(BOLD_STYLE.toSpanStyle()) {
                        append("Private:")
                    }
                    withStyle(NORMAL_STYLE.toSpanStyle()) {
                        append(responseData.private.toString())
                    }
                }, modifier = Modifier.padding(top = 5.dp)
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(BOLD_STYLE.toSpanStyle()) {
                        append("Default branch:")
                    }
                    withStyle(NORMAL_STYLE.toSpanStyle()) {
                        append(responseData.default_branch)
                    }
                }, modifier = Modifier.padding(top = 5.dp)
            )

            Spacer(Modifier.height(30.dp))
            Button(
                onClick = {
                    navController.navigate(Screens.Home)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White,
                )
            ) {
                Text(
                    text = "HOME",
                    textAlign = TextAlign.Center,
                    style = BOLD_STYLE,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                    fontSize = 18.sp
                )

            }
        }


    }
}