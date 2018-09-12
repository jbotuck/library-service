package com.jacob.libraryservice.member.service.repository

import com.jacob.libraryservice.domain.Member

interface MemberRepository : ReadOnlyRepository<Member, String>