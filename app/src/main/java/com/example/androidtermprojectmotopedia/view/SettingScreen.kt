package com.example.androidtermprojectmotopedia.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun SettingScreen(modifier : Modifier){

    ConstraintLayout(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        val (box1,box2,box3,box4,box5,box6) = createRefs()

        var notificationChecked by remember { mutableStateOf(true) }


        Row(modifier = Modifier
            .clickable {  }.constrainAs(box1){
            top.linkTo(parent.top, 100.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }.border(1.dp, Color.Gray, RoundedCornerShape(15.dp)).height(50.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment =  Alignment.CenterVertically){
            Text("Account Information")
        }

        Row(modifier = Modifier.clickable {  }.constrainAs(box2){
            top.linkTo(box1.bottom, 50.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }.border(1.dp, Color.Gray, RoundedCornerShape(15.dp)).height(50.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment =  Alignment.CenterVertically){
            Text("Language")
        }

        Row(modifier = Modifier.clickable {  }.constrainAs(box3){
            top.linkTo(box2.bottom, 50.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }.border(1.dp, Color.Gray, RoundedCornerShape(15.dp)).height(50.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment =  Alignment.CenterVertically){

            ConstraintLayout (modifier = Modifier.fillMaxSize()) {

                val (item1, item2) = createRefs()
                Text("Notification",modifier = Modifier.constrainAs(item1){
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                })

                Switch(checked = notificationChecked,
                    onCheckedChange = {
                        notificationChecked = it
                    }, modifier = Modifier.constrainAs(item2){
                        end.linkTo(parent.end, 16.dp)
                    })
            }
        }

        Row(modifier = Modifier.clickable {  }.constrainAs(box4){
            top.linkTo(box3.bottom, 50.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }.border(1.dp, Color.Gray, RoundedCornerShape(15.dp)).height(50.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment =  Alignment.CenterVertically){
            Text("Help & Support")
        }

        Row(modifier = Modifier.clickable {  }.constrainAs(box5){
            top.linkTo(box4.bottom, 50.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }.border(1.dp, Color.Gray, RoundedCornerShape(15.dp)).height(50.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment =  Alignment.CenterVertically){
            Text("FAQs")
        }
        
        Button(onClick = {}, colors = ButtonColors(
            contentColor = Color.Red,
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.Red
        ),border = BorderStroke(1.dp, Color.Red), modifier = Modifier.constrainAs(box6){
            top.linkTo(box5.bottom, 50.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom)
        })
        {
            Text("Log out")
        }

    }
}