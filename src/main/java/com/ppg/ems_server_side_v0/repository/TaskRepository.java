package com.ppg.ems_server_side_v0.repository;

import com.ppg.ems_server_side_v0.domain.Employee;
import com.ppg.ems_server_side_v0.domain.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

    // Legacy methods
    @Query("SELECT t FROM Task t WHERE t.project.id = :projectId")
    List<Task> findByProjectId(@Param("projectId") String projectId);

    @Query("SELECT t FROM Task t WHERE t.status = :status")
    List<Task> findByStatusString(@Param("status") String status);

    // Enhanced methods for task management

    // Find tasks by assigned employee
    List<Task> findByAssignedToOrderByCreatedAtDesc(Employee assignedTo);

    Page<Task> findByAssignedTo(Employee assignedTo, Pageable pageable);

    // Find tasks by creator
    List<Task> findByCreatedByOrderByCreatedAtDesc(Employee createdBy);

    // Find tasks by status enum
    List<Task> findByStatusOrderByCreatedAtDesc(Task.TaskStatus status);

    Page<Task> findByStatus(Task.TaskStatus status, Pageable pageable);

    // Find tasks by priority
    List<Task> findByPriorityOrderByDueDateAsc(Task.TaskPriority priority);

    // Find tasks by assigned employee and status
    List<Task> findByAssignedToAndStatusOrderByCreatedAtDesc(Employee assignedTo, Task.TaskStatus status);

    // Find overdue tasks
    @Query("SELECT t FROM Task t WHERE t.dueDate < :currentDate AND t.status != 'COMPLETED' ORDER BY t.dueDate ASC")
    List<Task> findOverdueTasks(@Param("currentDate") LocalDate currentDate);

    // Find completed tasks
    @Query("SELECT t FROM Task t WHERE t.status = 'COMPLETED' ORDER BY t.completedAt DESC")
    List<Task> findCompletedTasks();

    Page<Task> findByStatusOrderByCompletedAtDesc(Task.TaskStatus status, Pageable pageable);

    // Search tasks by title or description
    @Query("SELECT t FROM Task t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(t.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) ORDER BY t.createdAt DESC")
    List<Task> searchTasks(@Param("searchTerm") String searchTerm);

    // Count tasks by status
    long countByStatus(Task.TaskStatus status);

    // Count tasks by assigned employee
    long countByAssignedTo(Employee assignedTo);

    // Count tasks by assigned employee and status
    long countByAssignedToAndStatus(Employee assignedTo, Task.TaskStatus status);

    // Count overdue tasks
    @Query("SELECT COUNT(t) FROM Task t WHERE t.dueDate < :currentDate AND t.status != 'COMPLETED'")
    long countOverdueTasks(@Param("currentDate") LocalDate currentDate);
}
