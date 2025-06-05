package org.barrikeit.chess.service;

import lombok.extern.log4j.Log4j2;
import org.barrikeit.chess.core.service.GenericService;
import org.barrikeit.chess.core.service.dto.RoleDto;
import org.barrikeit.chess.core.service.mapper.RoleMapper;
import org.barrikeit.chess.domain.entities.Role;
import org.barrikeit.chess.domain.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class RoleService extends GenericService<Role, Integer, RoleDto> {
  private final RoleRepository repository;
  private final RoleMapper mapper;

  public RoleService(RoleRepository repository, RoleMapper mapper) {
    super(repository, mapper);
    this.repository = repository;
    this.mapper = mapper;
  }
}
