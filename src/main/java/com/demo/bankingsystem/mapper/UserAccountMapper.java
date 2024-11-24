package com.demo.bankingsystem.mapper;

import com.demo.bankingsystem.dto.UserAccountDto;
import com.demo.bankingsystem.repository.UserAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserAccountMapper {

	UserAccount toUserAccount (UserAccountDto userAccountDto);

	UserAccountDto toUserAccountDto (UserAccount userAccount);
}
