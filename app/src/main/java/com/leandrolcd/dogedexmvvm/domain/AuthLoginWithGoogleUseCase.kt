package com.leandrolcd.dogedexmvvm.domain

import com.leandrolcd.dogedexmvvm.data.repositoty.LoginRepository
import com.leandrolcd.dogedexmvvm.ui.authentication.utilities.UiStatus
import javax.inject.Inject

class AuthLoginWithGoogleUseCase @Inject constructor(
    private val repository: LoginRepository
) {
    suspend operator fun invoke(idToken: String): UiStatus<Any> = repository.authLoginWithGoogle(idToken)
}