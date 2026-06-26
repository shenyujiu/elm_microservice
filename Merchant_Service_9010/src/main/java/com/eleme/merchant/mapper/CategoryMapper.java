package com.eleme.merchant.mapper;

import com.eleme.merchant.model.CategoryDO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper {
    List<CategoryDO> selectAll();
}
