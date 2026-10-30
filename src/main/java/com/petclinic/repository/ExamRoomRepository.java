package com.petclinic.repository;

import com.petclinic.model.ExamRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface ExamRoomRepository extends JpaRepository<ExamRoom, UUID> {

    @Query("SELECT r FROM ExamRoom r WHERE NOT EXISTS (SELECT v FROM Veterinarian v WHERE v.examRoom = r) ORDER BY r.roomNumber ASC")
    List<ExamRoom> findFreeRooms();
}
