package live.luna.service

import live.luna.entity.Material

interface MaterialService {
    fun insert(material: Material)
    fun update(material: Material)
    fun delete(material: Material)
    fun getById(id: Long): Material?
}