package com.jakan.auth_service.controller;

import com.jakan.auth_service.dto.RoleDto;
import com.jakan.auth_service.service.facade.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping("/")
    public ResponseEntity<List<RoleDto>> findAll(){
        return ResponseEntity.ok(roleService.findAll());
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<RoleDto> findById(@PathVariable Long id){
        return ResponseEntity.ok(roleService.findById(id));
    }
    @PostMapping("/")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleDto> save(@RequestBody RoleDto dto){
        return new ResponseEntity<>(roleService.save(dto), HttpStatus.CREATED);
    }
    @PutMapping("/id/{id}")
    public ResponseEntity<RoleDto> update(@RequestBody RoleDto dto, @PathVariable Long id){
        return ResponseEntity.ok(roleService.update(dto,id));
    }
    @DeleteMapping("/id/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable Long id){
        roleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
