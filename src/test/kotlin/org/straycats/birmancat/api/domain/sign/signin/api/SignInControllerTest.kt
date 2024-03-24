package org.straycats.birmancat.api.domain.sign.signin.api

import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.straycats.birmancat.api.config.FlowTestSupport
import org.straycats.birmancat.api.domain.sign.signup.api.SignUpControllerFlow

class SignInControllerTest : FlowTestSupport() {

    @Test
    fun signIn() {
        // Given
        val signUpControllerFlow = SignUpControllerFlow(mockMvc)
        val email = "sign-in@github.com"
        val password = "Tomcat1234!"
        signUpControllerFlow.signUp(email, password)

        val signInControllerFlow = SignInControllerFlow(mockMvc)

        // When
        signInControllerFlow.signIn(email, password)
    }

    @Test
    fun signInWrongPassword() {
        // Given
        val signUpControllerFlow = SignUpControllerFlow(mockMvc)
        val email = "sign-in-wrong@github.com"
        val password = "Tomcat1234!"
        signUpControllerFlow.signUp(email, password)

        val signInControllerFlow = SignInControllerFlow(mockMvc)

        // Fail Test
        run {
            signInControllerFlow.signIn(email, password)

            assertThrows(AssertionError::class.java) {
                signInControllerFlow.signIn(email, "234254")
            }
        }

        // Limit Count Test
        run {
            signInControllerFlow.signIn(email, password)

            assertThrows(AssertionError::class.java) {
                signInControllerFlow.signIn(email, "234234")
            }

            assertThrows(AssertionError::class.java) {
                signInControllerFlow.signIn(email, "234234")
            }

            assertThrows(AssertionError::class.java) {
                signInControllerFlow.signIn(email, password)
            }
        }
    }

    @Test
    fun changePassword() {
        // Given
        val signUpControllerFlow = SignUpControllerFlow(mockMvc)
        val email = "sign-in-changepw@github.com"
        val password = "Tomcat1234!"
        signUpControllerFlow.signUp(email, password)

        val signInControllerFlow = SignInControllerFlow(mockMvc)
        val afterPassword = "Madcat1234!"

        // success
        signInControllerFlow.changePassword(
            SignInResources.Request.ChangePassword(
                email,
                password,
                afterPassword,
            ),
        )

        // wrong pw
        assertThrows(AssertionError::class.java) {
            signInControllerFlow.changePassword(
                SignInResources.Request.ChangePassword(
                    email,
                    "madcat1324!",
                    afterPassword,
                ),
            )
        }

        // wrong pw rule
        assertThrows(AssertionError::class.java) {
            signInControllerFlow.changePassword(
                SignInResources.Request.ChangePassword(
                    email,
                    afterPassword,
                    "23425234",
                ),
            )
        }
    }
}
