package com.alishoumar.diaryapp.model

import androidx.compose.ui.graphics.Color
import com.alishoumar.diaryapp.R
import com.alishoumar.diaryapp.ui.theme.AngryColor
import com.alishoumar.diaryapp.ui.theme.AwfulColor
import com.alishoumar.diaryapp.ui.theme.BoredColor
import com.alishoumar.diaryapp.ui.theme.CalmColor
import com.alishoumar.diaryapp.ui.theme.DepressedColor
import com.alishoumar.diaryapp.ui.theme.DisappointedColor
import com.alishoumar.diaryapp.ui.theme.HappyColor
import com.alishoumar.diaryapp.ui.theme.HumorousColor
import com.alishoumar.diaryapp.ui.theme.LonelyColor
import com.alishoumar.diaryapp.ui.theme.MysteriousColor
import com.alishoumar.diaryapp.ui.theme.NeutralColor
import com.alishoumar.diaryapp.ui.theme.RomanticColor
import com.alishoumar.diaryapp.ui.theme.ShamefulColor
import com.alishoumar.diaryapp.ui.theme.SurprisedColor
import com.alishoumar.diaryapp.ui.theme.SuspiciousColor
import com.alishoumar.diaryapp.ui.theme.TenseColor

enum class Mood(
    val icon: Int,
    val contentColor:Color,
    val containerColor: Color,

    ){

    Neutral(
        icon = R.drawable.neutral,
        contentColor = Color.Black,
        containerColor = NeutralColor
    ),
    Happy(
        icon = R.drawable.happy,
        contentColor = Color.Black,
        containerColor = HappyColor
    ),
    Angry(
        icon = R.drawable.angry,
        contentColor = Color.White,
        containerColor = AngryColor
    ),
    Bored(
        icon = R.drawable.bored,
        contentColor = Color.Black,
        containerColor = BoredColor
    ),
    Calm(
        icon = R.drawable.calm,
        contentColor = Color.Black,
        containerColor = CalmColor
    ),
    Depressed(
        icon = R.drawable.depressed,
        contentColor = Color.Black,
        containerColor = DepressedColor
    ),
    Disappointed(
        icon = R.drawable.disappointed,
        contentColor = Color.White,
        containerColor = DisappointedColor
    ),
    Humorous(
        icon = R.drawable.humorous,
        contentColor = Color.Black,
        containerColor = HumorousColor
    ),
    Lonely(
        icon = R.drawable.lonely,
        contentColor = Color.White,
        containerColor = LonelyColor
    ),
    Mysterious(
        icon = R.drawable.mysterious,
        contentColor = Color.Black,
        containerColor = MysteriousColor
    ),
    Romantic(
        icon = R.drawable.romantic,
        contentColor = Color.White,
        containerColor = RomanticColor
    ),
    Shameful(
        icon = R.drawable.shameful,
        contentColor = Color.White,
        containerColor = ShamefulColor
    ),
    Awful(
        icon = R.drawable.awful,
        contentColor = Color.Black,
        containerColor = AwfulColor
    ),
    Surprised(
        icon = R.drawable.surprised,
        contentColor = Color.Black,
        containerColor = SurprisedColor
    ),
    Suspicious(
        icon = R.drawable.suspicious,
        contentColor = Color.Black,
        containerColor = SuspiciousColor
    ),
    Tense(
        icon = R.drawable.tense,
        contentColor = Color.Black,
        containerColor = TenseColor
    )


}