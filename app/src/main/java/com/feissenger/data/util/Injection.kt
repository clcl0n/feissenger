/*
 * Copyright (C) 2019 Maros Cavojsky, mpage.sk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.opinyour.android.app.data.utils

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.feissenger.data.DataRepository
import com.feissenger.data.api.WebApi
import com.feissenger.data.db.AppRoomDatabase
import com.feissenger.data.db.LocalCache

/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object Injection {

    private fun provideCache(context: Context): LocalCache {
        val database = AppRoomDatabase.getInstance(context)
        return LocalCache(database.appDao())
    }

    fun provideDataRepository(context: Context): DataRepository {
        return DataRepository.getInstance(WebApi.create(context),provideCache(context))
    }

    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(
            provideDataRepository(
                context
            )
        )
    }
}
