package com.example.mitalk.domain.admin.domain.repository

import com.example.mitalk.domain.admin.domain.entity.Admin
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface AdminRepository : CrudRepository<Admin, UUID> {
}