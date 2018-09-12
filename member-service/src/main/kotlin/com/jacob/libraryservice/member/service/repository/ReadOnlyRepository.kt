package com.jacob.libraryservice.member.service.repository

import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.Repository
import reactor.core.publisher.Mono

@NoRepositoryBean
interface ReadOnlyRepository<T, ID> : Repository<T, ID> {
    fun findById(id: ID): Mono<T>
}