package com.leandrolcd.dogedexmvvm.domain

import com.leandrolcd.dogedexmvvm.data.repositoty.LoginRepository
import com.leandrolcd.dogedexmvvm.ui.authentication.utilities.UiStatus
import com.leandrolcd.dogedexmvvm.ui.model.LoginUser
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val repository: LoginRepository
) {

    suspend operator fun invoke(user: LoginUser): UiStatus<Any> {
        return repository.createUserWithEmailAndPassword(user)
    }
}