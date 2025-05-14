package com.gamingstore.store_api.service;

import com.gamingstore.store_api.entity.Account;
import com.gamingstore.store_api.entity.Address;
import com.gamingstore.store_api.repository.AccountRepository;
import com.gamingstore.store_api.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AccountRepository accountRepository;
    public Address getAddressByAccountId(Long accountId) {
        // Giả sử mỗi tài khoản có một địa chỉ duy nhất, lấy địa chỉ đầu tiên
        return addressRepository.findByAccountId(accountId);
    }

    public boolean createOrUpdateAddress(Long accountId, String name, String phone, String address) {
        // Kiểm tra xem tài khoản đã có địa chỉ chưa
        Address existingAddress = addressRepository.findByAccountId(accountId);

        if (existingAddress != null) {
            // Nếu có địa chỉ, cập nhật
            return updateAddress(existingAddress, name, phone, address);
        } else {
            // Nếu chưa có, tạo mới
            return createNewAddress(accountId, name, phone, address);
        }
    }

    // Cập nhật địa chỉ đã tồn tại
    private boolean updateAddress(Address existingAddress, String name, String phone, String address) {
        existingAddress.setName(name);
        existingAddress.setPhone(phone);
        existingAddress.setAddress(address);
        addressRepository.save(existingAddress); // Lưu lại địa chỉ đã cập nhật
        return true;
    }

    // Tạo mới địa chỉ
    private boolean createNewAddress(Long accountId, String name, String phone, String address) {
        // Tìm Account theo accountId
        Account account = accountRepository.findById(accountId).orElse(null);

        if (account != null) {
            Address newAddress = new Address(name, phone, address, account); // Tạo mới địa chỉ
            addressRepository.save(newAddress); // Lưu địa chỉ mới
            return true;
        }

        return false; // Nếu không tìm thấy Account
    }
}