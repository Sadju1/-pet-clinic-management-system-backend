package com.petclinic.service;

import com.petclinic.model.ExamRoom;
import com.petclinic.repository.ExamRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ExamRoomService {

    @Autowired
    private ExamRoomRepository examRoomRepository;

    public ExamRoom save(ExamRoom examRoom) {
        return examRoomRepository.save(examRoom);
    }

    public List<ExamRoom> findAll() {
        return examRoomRepository.findAll();
    }

    public Optional<ExamRoom> findById(UUID id) {
        return examRoomRepository.findById(id);
    }

    public ExamRoom update(UUID id, ExamRoom examRoom) {
        examRoom.setId(id);
        return examRoomRepository.save(examRoom);
    }

    public boolean existsById(UUID id) {
        return examRoomRepository.existsById(id);
    }

    public void deleteById(UUID id) {
        examRoomRepository.deleteById(id);
    }

    public List<ExamRoom> findFreeRooms() {
        return examRoomRepository.findFreeRooms();
    }

    public Optional<ExamRoom> findByRoomNumber(int roomNumber) {
        return examRoomRepository.findByRoomNumber(roomNumber);
    }
}
