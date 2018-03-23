package live.luna.dao

import live.luna.entity.Address

interface AddressDao {
    fun save(address: Address): Address
    fun update(address: Address)
    fun delete(address: Address)
    fun getById(id: Long): Address?
}