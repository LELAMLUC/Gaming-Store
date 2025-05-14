package com.gamingstore.store_api.controller;

import com.gamingstore.store_api.entity.Address;
import com.gamingstore.store_api.service.AccountService;
import com.gamingstore.store_api.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private AccountService accountService;
    @GetMapping("/{accountId}")
    public Address getAddressByAccountId(@PathVariable Long accountId) {
        // Giả sử mỗi account chỉ có một địa chỉ, trả về địa chỉ đầu tiên
        return addressService.getAddressByAccountId(accountId);
    }
    @PostMapping("/create-and-get/{accountId}/{name}/{phone}/{address}")
    public boolean createOrUpdateAddress(@PathVariable Long accountId,
                                         @PathVariable String name,
                                         @PathVariable String phone,
                                         @PathVariable String address) {
        boolean isSuccess = addressService.createOrUpdateAddress(accountId, name, phone, address);
        return isSuccess;
    }
}
