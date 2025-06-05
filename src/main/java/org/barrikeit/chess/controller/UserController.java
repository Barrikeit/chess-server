package org.barrikeit.chess.controller;

import lombok.extern.log4j.Log4j2;
import org.barrikeit.chess.core.controller.GenericController;
import org.barrikeit.chess.core.service.dto.UserDto;
import org.barrikeit.chess.domain.entities.User;
import org.barrikeit.chess.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/users")
public class UserController extends GenericController<User, Long, UserDto> {
  private final UserService service;

  public UserController(UserService service) {
    super(service);
    this.service = service;
  }

  @GetMapping("/{username}")
  public ResponseEntity<UserDto> findById(@PathVariable("username") String username) {
    return ResponseEntity.ok(service.findDtoByUsername(username));
  }
}
