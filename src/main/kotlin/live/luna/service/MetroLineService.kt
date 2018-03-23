package live.luna.service

import live.luna.entity.MetroLine

interface MetroLineService {
    fun getById(id: Long): MetroLine?
    fun getByName(name: String): MetroLine?
}