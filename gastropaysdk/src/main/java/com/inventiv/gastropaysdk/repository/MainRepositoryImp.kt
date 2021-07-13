package com.inventiv.gastropaysdk.repository

import com.inventiv.gastropaysdk.api.GastroPayService
import com.inventiv.gastropaysdk.common.BaseRepository

internal class MainRepositoryImp(private val gastroPayService: GastroPayService) : MainRepository,
    BaseRepository()