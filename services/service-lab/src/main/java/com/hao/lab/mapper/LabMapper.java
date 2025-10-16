package com.hao.lab.mapper;

import com.github.pagehelper.Page;
import com.hao.common.annotation.AutoFill;
import com.hao.common.enumeration.OperationType;
import com.hao.dto.LabPageQueryDTO;
import com.hao.entity.Lab;
import com.hao.vo.LabVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LabMapper {

    @Delete("delete from lab where id = #{id}")
    void deleteById(Long id);

    @AutoFill(OperationType.INSERT)
    @Insert("insert into lab_lab_db.lab(name, location, capacity, status, create_time, update_time, related_major, description) " +
            "values (#{name}, #{location}, #{capacity}, #{status}, #{createTime}, #{updateTime}, #{relatedMajor}, #{description})")
    void add(Lab lab);

    Page<LabVO> pageQuery(LabPageQueryDTO labPageQueryDTO);

    void update(Lab lab);
}
