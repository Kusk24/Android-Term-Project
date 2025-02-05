package com.example.androidtermprojectmotopedia.view

import android.graphics.Paint.Align
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil3.compose.AsyncImage

@Composable
fun ProfileScreen(modifier : Modifier){

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {

        val (item1,item2,item3,item4,item5) = createRefs()

        AsyncImage(model = "https://i.pinimg.com/736x/53/fe/d1/53fed15d25b9308613788977fca0d509.jpg",
            contentDescription = null,
            modifier = Modifier.size(100.dp)
                .clip(RoundedCornerShape(50.dp))
                .border(border = BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(50.dp))
                .constrainAs(item1){
                    top.linkTo(parent.top, 100.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

        Text("Profile Name", fontWeight = FontWeight.SemiBold, fontSize = 20.sp, modifier = Modifier.constrainAs(item2){
            top.linkTo(item1.bottom, 30.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })

        HorizontalDivider(thickness = 1.dp, color = Color.Black, modifier = Modifier.constrainAs(item3){
            top.linkTo(item2.bottom)
            bottom.linkTo(item3.top)
        }.padding(16.dp))

        Box(modifier = Modifier.constrainAs(item4){
            top.linkTo(item3.bottom)
        }.padding(20.dp).fillMaxWidth().height(50.dp).border(border = BorderStroke(1.dp, Color.Black),shape = RoundedCornerShape(15.dp))
            .wrapContentWidth(Alignment.CenterHorizontally).wrapContentHeight(Alignment.CenterVertically)) {
            Text("Your Distribution To Community")
        }

        LazyColumn(modifier = Modifier.constrainAs(item5){
            top.linkTo(item4.bottom)
            bottom.linkTo(parent.bottom)
        }) {

        }
    }

}