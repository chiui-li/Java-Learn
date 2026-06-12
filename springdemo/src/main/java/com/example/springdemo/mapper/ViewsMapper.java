package com.example.springdemo.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

// import com.example.springdemo.utils.IpUtil;

@Mapper
public interface ViewsMapper {

  @Insert("insert into views(path, ip) values(#{path}, #{ip})")
  public void addLog(@Param("path") String path, @Param("ip") String ip);

}
