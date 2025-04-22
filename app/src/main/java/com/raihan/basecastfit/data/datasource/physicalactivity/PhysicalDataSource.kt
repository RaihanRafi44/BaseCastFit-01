package com.raihan.basecastfit.data.datasource.physicalactivity

import com.raihan.basecastfit.data.model.PhysicalActivity


interface PhysicalDataSource {
    fun getPhysicalActivitiesData(): List<PhysicalActivity>
}

class PhysicalDataSourceImpl : PhysicalDataSource {
    override fun getPhysicalActivitiesData(): List<PhysicalActivity> {
        return mutableListOf(
            PhysicalActivity(
                name = "Jogging",
                type = "Outdoor"
            ),
            PhysicalActivity(
                name = "Walking",
                type = "Outdoor"
            ),
            PhysicalActivity(
                name = "Cycling",
                type = "Outdoor"
            ),
            PhysicalActivity(
                name = "Jumping Jack",
                type = "Indoor"
            ),
            PhysicalActivity(
                name = "Push Up",
                type = "Indoor"
            ),
            PhysicalActivity(
                name = "Sit up",
                type = "Indoor"
            ),
            PhysicalActivity(
                name = "Crunch",
                type = "Indoor"
            ),
            PhysicalActivity(
                name = "Rusian Twist",
                type = "Indoor"
            ),
            PhysicalActivity(
                name = "Back Up",
                type = "Indoor"
            ),
            PhysicalActivity(
                name = "Bicep Curl",
                type = "Indoor"
            ),
            PhysicalActivity(
                name = "Barbel Curl",
                type = "Indoor"
            ),
            PhysicalActivity(
                name = "Squat",
                type = "Indoor"
            ),
            PhysicalActivity(
                name = "Step Up Down",
                type = "Indoor"
            ),
            PhysicalActivity(
                name = "Front Lunges",
                type = "Indoor"
            ),
            PhysicalActivity(
                name = "Calf Raise",
                type = "Indoor"
            ),
            PhysicalActivity(
                name = "Riverse Raise",
                type = "Indoor"
            ),
            PhysicalActivity(
                name = "Wall Seat",
                type = "Indoor"
            ),
            PhysicalActivity(
                name = "Zigzag Lunges",
                type = "Indoor"
            ),
            PhysicalActivity(
                name = "Tricep Exetention",
                type = "Indoor"
            ),
            PhysicalActivity(
                name = "Tricep Deep",
                type = "Indoor"
            ),
            PhysicalActivity(
                name = "Tricep Kick Back",
                type = "Indoor"
            ),
            PhysicalActivity(
                name = "Barbel Srug",
                type = "Indoor"
            ),
            PhysicalActivity(
                name = "Plank",
                type = "Indoor"
            ),
        )
    }
}