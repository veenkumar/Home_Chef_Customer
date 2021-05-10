package com.veen.homechef.Model.location.state

data class StateResponse(
    val `data`: List<StateData>,
    val msg: String,
    val status: Boolean
)