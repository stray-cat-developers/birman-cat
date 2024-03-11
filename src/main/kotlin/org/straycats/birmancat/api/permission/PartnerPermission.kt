package org.straycats.birmancat.api.permission

class PartnerPermission(
    private val id: Long,
) : Permission {
    private var valid: Boolean = true
    override fun isValid(): Boolean = valid
}
