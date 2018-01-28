package com.tasktracker.repository;

import com.tasktracker.model.Project;
import com.tasktracker.model.Task;
import com.tasktracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    @Query(value = "select * from users where id in (select user_id from user_roles where role='Developer') and id not in (select user_id from users_projects where project_id = ?1)", nativeQuery = true)
    List<User> findByProjectsNotIn(Long projectId);

    @Query(value = "select * from users where name like %?1 and id in (select user_id from user_roles where role='Developer') and id not in (select user_id from users_projects where project_id = ?2)", nativeQuery = true)
    List<User> findByNameAndProjectsNotIn(String name, Long projectId);

    @Query(value = "select * from users where id in (select user_id from user_roles where role='Developer') and id in (select user_id from users_projects where project_id = ?1) and id not in (select user_id from users_tasks where task_id = ?2)", nativeQuery = true)
    List<User> findByProjectsAndTasksNotIn(Long projectId, Long taskId);

    @Query(value = "select * from users where name like %?1 and id in (select user_id from user_roles where role='Developer') and id in (select user_id from users_projects where project_id = ?2) and id not in (select user_id from users_tasks where task_id = ?3)", nativeQuery = true)
    List<User> findByNameAndProjectsAndTasksNotIn(String name, Long projectId, Long taskId);

    User findByEmail(String email);

    User findByEmailAndIsEnabled(String email, boolean isEnabled);

    User findByEmailAndPasswordAndIsEnabled(String email, String password, boolean isEnabled);

    boolean existsByEmail(String email);

    boolean existsByName(String username);

    boolean existsByEmailAndPasswordAndIsEnabled(String email, String password, boolean isEnabled);
}
