package com.petclinic.controller;

import com.petclinic.model.ExamRoom;
import com.petclinic.service.ExamRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/exam-rooms")
public class ExamRoomController {

    @Autowired
    private ExamRoomService examRoomService;

    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> save(@RequestBody ExamRoom examRoom) {
        ExamRoom saved = examRoomService.save(examRoom);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "ExamRoom saved successfully");
        response.put("data", saved);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ExamRoom>> getAll() {
        return ResponseEntity.ok(examRoomService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<ExamRoom> examRoom = examRoomService.findById(id);
        if (examRoom.isPresent()) {
            return ResponseEntity.ok(examRoom.get());
        }
        Map<String, String> response = new HashMap<>();
        response.put("message", "The exam room with that id does not exist");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable UUID id, @RequestBody ExamRoom examRoom) {
        if (!examRoomService.existsById(id)) {
            Map<String, Object> notFound = new HashMap<>();
            notFound.put("message", "The exam room with that id does not exist");
            return new ResponseEntity<>(notFound, HttpStatus.NOT_FOUND);
        }
        ExamRoom updated = examRoomService.update(id, examRoom);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "ExamRoom updated successfully");
        response.put("data", updated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable UUID id) {
        Map<String, String> response = new HashMap<>();
        if (!examRoomService.existsById(id)) {
            response.put("message", "The exam room with that id does not exist");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        examRoomService.deleteById(id);
        response.put("message", "ExamRoom deleted successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/free")
    public ResponseEntity<List<ExamRoom>> getFreeRooms() {
        return ResponseEntity.ok(examRoomService.findFreeRooms());
    }
}
