package xyz.teikou.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import xyz.teikou.entity.Attendance;

import java.util.List;
import java.util.Map;


@Mapper
public interface AttendanceMapper extends BaseMapper<Attendance> {
    
    /**
     * 计学生考勤情况
     * @param schNumber 学号
     * @return 统计结果
     */
    @Select("SELECT status as status, COUNT(*) as count FROM attendance WHERE sch_number = #{schNumber} GROUP BY status")
    List<Map<String, Object>> countAttendanceByStatus(@Param("schNumber") String schNumber);
    
    /**
     * 统计班级考勤情况
     * @param className 班级名称
     * @return 统计结果
     */
    @Select("SELECT a.status as status, COUNT(*) as count FROM attendance a " +
            "INNER JOIN stu_info s ON a.sch_number = s.sch_number " +
            "WHERE s.class_name = #{className} GROUP BY a.status")
    List<Map<String, Object>> countClassAttendanceByStatus(@Param("className") String className);
} 