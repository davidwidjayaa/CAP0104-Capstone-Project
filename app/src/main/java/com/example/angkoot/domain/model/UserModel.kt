package com.example.angkoot.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class UserModel(
    var phone: String,
    var username: String,
    var password: String
) : Parcelable