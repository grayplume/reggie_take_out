package com.plume.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.plume.reggie.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
