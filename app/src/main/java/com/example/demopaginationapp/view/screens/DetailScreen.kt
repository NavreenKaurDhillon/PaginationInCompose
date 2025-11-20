package com.example.demopaginationapp.view.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.demopaginationapp.model.dataclasses.ResponseDataItem
import com.google.gson.Gson

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DetailScreen(data: String?) {
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
        GlideImage(
            model = responseData.owner.avatar_url,
            contentDescription = "Logo description of the repo",
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 20.dp)
                .width(120.dp)
                .align(Alignment.CenterHorizontally)
                .height(120.dp),
            )
        Row(modifier = Modifier.padding(vertical = 10.dp)) {
            Text(
                text = "ID : ",
                style = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            )

            Text(
                text = responseData.id.toString(),
                style = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp)
            )
        }
        Row(modifier = Modifier.padding(vertical = 10.dp)) {
            Text(
                text = "Name : ",
                style = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            )

            Text(
                text = responseData.name,
                style = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp)
            )
        }
        Row(modifier = Modifier.padding(vertical = 10.dp)) {
            Text(
                text = "Organisational URL : ",
                style = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            )

            Text(
                text = responseData.owner.organizations_url,
                style = TextStyle(fontWeight = FontWeight.Normal, fontSize = 16.sp)
            )
        }

    }
}