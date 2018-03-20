package live.luna.service

import live.luna.entity.Address

interface AddressService {
    fun insert(address: Address)
    fun update(address: Address)
    fun delete(address: Address)
    fun getById(id: Long): Address?
}