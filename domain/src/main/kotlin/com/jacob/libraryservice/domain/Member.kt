package com.jacob.libraryservice.domain

import org.springframework.data.elasticsearch.annotations.Document
import java.util.*

@Document(indexName = "member")
data class Member(val id: UUID?, val name: String?)