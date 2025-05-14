package com.gamingstore.store_api.repository;

import com.gamingstore.store_api.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Address findByAccountId(Long accountId);
}
