package org.straycats.birmancat.api.permission

object RoleHeader {

    sealed interface Role

    data object XUser : Role {
        const val NAME = "user ID"
        const val KEY = "x-user-id"
        const val DATA_TYPE = "long"
        const val PARAM_TYPE = "header"
    }
}
