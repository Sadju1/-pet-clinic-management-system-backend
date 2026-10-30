package com.petclinic.repository;

import com.petclinic.model.ExamRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ExamRoomRepository extends JpaRepository<ExamRoom, UUID> {
}
