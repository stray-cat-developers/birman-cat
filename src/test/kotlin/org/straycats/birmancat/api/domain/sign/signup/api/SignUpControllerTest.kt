package org.straycats.birmancat.api.domain.sign.signup.api

import io.kotest.assertions.asClue
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.straycats.birmancat.api.config.FlowTestSupport
import org.straycats.birmancat.api.domain.account.Account
import org.straycats.birmancat.api.domain.account.AccountFinder

class SignUpControllerTest : FlowTestSupport() {

    @Autowired
    private lateinit var accountFinder: AccountFinder

    @Test
    fun signUp() {
        val email = "sign-up@github.com"
        val password = "Tomcat1980!"
        val signUpControllerFlow = SignUpControllerFlow(mockMvc)

        val id = signUpControllerFlow.signUp(email, password)
        val signUpAccount = accountFinder.findById(id)

        signUpAccount.asClue {
            it.email shouldBe email
            it.status shouldBe Account.Status.ACTIVE
        }
    }

    @Test
    fun signUpDuplicateEmail() {
        val email = "sign-up-dupli@github.com"
        val password = "Tomcat1980!"
        val signUpControllerFlow = SignUpControllerFlow(mockMvc)

        signUpControllerFlow.signUp(email, password)
        assertThrows(AssertionError::class.java) {
            signUpControllerFlow.signUp(email, password)
        }
    }

    @Test
    fun signUpWrongPasswordRule() {
        val email = "sign-up-fail@github.com"
        val password = "Tomc0!"
        val signUpControllerFlow = SignUpControllerFlow(mockMvc)

        assertThrows(AssertionError::class.java) {
            signUpControllerFlow.signUp(email, password)
        }
    }
}
