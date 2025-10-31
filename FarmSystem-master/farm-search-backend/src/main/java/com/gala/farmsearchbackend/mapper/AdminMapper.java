package com.gala.farmsearchbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gala.farmsearchbackend.model.domain.Admin;
import org.apache.ibatis.annotations.Mapper;


/**
* @author Gala
* @description 针对表【admin(系统管理员表)】的数据库操作Mapper
* @createDate 2025-09-20 20:07:08
* @Entity com.gala.farmsearchbackend.domain.Admin
*/
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {

}




