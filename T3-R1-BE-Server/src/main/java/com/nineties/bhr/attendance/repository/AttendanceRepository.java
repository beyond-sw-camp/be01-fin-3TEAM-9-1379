package com.nineties.bhr.attendance.repository;

import com.nineties.bhr.attendance.domain.Attendance;
import com.nineties.bhr.attendance.domain.AttendanceStatus;
import com.nineties.bhr.emp.domain.Employees;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, String>, JpaSpecificationExecutor<Attendance> {
    
    @Query(value = "SELECT emp_id " +
            "FROM (SELECT a.emp_id, " +
            "             COUNT(*) AS overtime_count " +
            "      FROM attendance a " +
            "      WHERE ( " +
            "               (a.start_date = a.end_date AND TIME(a.time_out) > '19:00:00') " +
            "               OR " +
            "               (a.start_date < a.end_date AND TIME(a.time_out) < '06:00:00') " +
            "            ) " +
            "        AND a.start_date BETWEEN :startOfWeek AND :endOfWeek " +
            "      GROUP BY a.emp_id) AS subquery " +
            "WHERE overtime_count >= 3", nativeQuery = true)
    List<String> findEmployeesWithOvertimeLastWeek(@Param("startOfWeek") Date startOfWeek, @Param("endOfWeek") Date endOfWeek);

    Optional<Attendance> findFirstByEmployeesIdAndStartDate(String employeeId, LocalDate today);

    List<Attendance> findByStartDateAndStatus(Date date, AttendanceStatus status);

    List<Attendance> findByStartDate(Date date);

    int countByStatusAndStartDate(AttendanceStatus attendanceStatus, Date today);

    Optional<Attendance> findFirstByEmployeesAndStartDateAndTimeInIsNotNull(Employees employee, Date todayStart);

    Attendance findByEmployeesAndStartDate(Employees employee, Date today);

    @Query("select a from Attendance a where a.employees.id = :employeeId and FUNCTION('MONTH', a.startDate) = :month")
    List<Attendance> findByEmployeeIdAndMonth(@Param("employeeId") String employeeId, @Param("month") int month);
}
