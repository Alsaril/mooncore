package live.luna.service

import live.luna.entity.AddressMetro

interface AddressMetroService {
    fun insert(addressMetro: AddressMetro)
    fun update(addressMetro: AddressMetro)
    fun delete(addressMetro: AddressMetro)
    fun getById(id: Long): AddressMetro?
}