package org.straycats.birmancat.api.common

import org.springframework.data.jpa.domain.Specification

interface SearchCondition<T> {

    fun isValid(): Boolean
    fun toSpecs(): Specification<T>
}
