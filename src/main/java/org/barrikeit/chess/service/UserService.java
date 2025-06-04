package org.barrikeit.chess.service;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.barrikeit.chess.core.domain.Role;
import org.barrikeit.chess.core.domain.User;
import org.barrikeit.chess.core.repository.RoleRepository;
import org.barrikeit.chess.core.repository.UserRepository;
import org.barrikeit.chess.core.service.GenericService;
import org.barrikeit.chess.core.service.dto.UserDto;
import org.barrikeit.chess.core.service.mapper.UserMapper;
import org.barrikeit.chess.core.util.TimeUtil;
import org.barrikeit.chess.core.util.constants.ExceptionConstants;
import org.barrikeit.chess.core.util.exceptions.BadRequestException;
import org.barrikeit.chess.core.util.exceptions.NotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
public class UserService extends GenericService<User, Long, UserDto> {
  private final UserRepository repository;
  private final UserMapper mapper;

  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder =
      PasswordEncoderFactories.createDelegatingPasswordEncoder();

  public UserService(UserRepository repository, UserMapper mapper, RoleRepository roleRepository) {
    super(repository, mapper);
    this.repository = repository;
    this.mapper = mapper;
    this.roleRepository = roleRepository;
  }

  private User findByUsername(final String username) {
    return repository
        .findByUsernameEqualsIgnoreCase(username)
        .orElseThrow(() -> new NotFoundException(ExceptionConstants.NOT_FOUND, username));
  }

  public UserDto findDtoByUsername(final String username) {
    User user = findByUsername(username);
    return mapper.toDto(user);
  }

  public User updateLoginDateAndResetAttempts(final User user) {
    return repository.save(resetAttempts(updateLoginDate(user)));
  }

  private User resetAttempts(final User user) {
    user.setBanned(false);
    user.setBanDate(null);
    user.setLoginAttempts(0);
    return user;
  }

  private User updateLoginDate(final User user) {
    user.setLoginDate(TimeUtil.localDateTimeNow());
    return user;
  }

  public void checkAttempts(final User user) {
    int loginAttempts = user.getLoginAttempts();
    if (loginAttempts < 10) {
      loginAttempts++;
      user.setLoginAttempts(loginAttempts);
    } else {
      user.setBanned(true);
      user.setBanDate(TimeUtil.localDateTimeNow());
    }
    repository.save(user);
  }

  @Override
  @Transactional
  public UserDto save(UserDto dto) {
    User user = validateUserToCreateUpdate(dto, true);
    generateUserForCreateUpdate(dto, user);
    user.setUsername(dto.getUsername());
    user.setPassword(passwordEncoder.encode(dto.getPassword()));
    user.setLoginDate(TimeUtil.localDateTimeNow());
    mapper.updateEntity(dto, user);

    repository.save(user);
    sendEmail(user, "CREATE_USER");
    return mapper.toDto(user);
  }

  @Override
  @Transactional
  public UserDto update(Long id, UserDto dto) {
    validateToggleActivationUser(dto, true);
    User user = validateUserToCreateUpdate(dto, false);
    generateUserForCreateUpdate(dto, user);
    mapper.updateEntity(dto, user);

    repository.save(user);
    sendEmail(user, "UPDATE_USER");
    return mapper.toDto(user);
  }

  @Transactional
  public UserDto toggleActivationUser(UserDto dto) {
    validateToggleActivationUser(dto, false);
    User user = validateUserToCreateUpdate(dto, false);
    user.setEnabled(!dto.isEnabled());
    // user.setDisabledReason(dto.getDisabledReason());

    repository.save(user);
    sendEmail(user, Boolean.FALSE.equals(dto.isEnabled()) ? "ENABLE_USER" : "DISABLE_USER");
    return mapper.toDto(user);
  }

  public User validateUserToCreateUpdate(UserDto dto, boolean isCreate) {
    User user = validateUserName(dto, isCreate);
    validateMail(dto);
    return user;
  }

  /**
   * Un user no puede desactivarse a si mismo, se lanza BadRequestException en tal caso
   *
   * @param dto: user que se pretende activar/desactivar
   * @param isUpdate: si vale true se esta actualizando el user, si vale false se esta haciendo
   *     toggle de la propiedad habilitado
   */
  private void validateToggleActivationUser(UserDto dto, boolean isUpdate) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String authenticatedUser = authentication.getName();

    if (dto.getUsername().equals(authenticatedUser)
        && ((isUpdate && Boolean.FALSE.equals(dto.isEnabled()))
            || (!isUpdate && Boolean.TRUE.equals(dto.isEnabled())))) {
      throw new BadRequestException(
          ExceptionConstants.ERROR_USER_DEACTIVATE_HIMSELF, dto.getUsername());
    }
  }

  private User validateUserName(UserDto dto, boolean isCreate) {
    User user = repository.findByUsernameEqualsIgnoreCase(dto.getUsername()).orElse(new User());
    if (isCreate && !user.isNew()) {
      throw new BadRequestException(
          ExceptionConstants.ERROR_USER_NAME_ALREADY_EXISTS, dto.getUsername());
    } else if (!isCreate && user.isNew()) {
      throw new BadRequestException(ExceptionConstants.NOT_FOUND, dto.getUsername());
    }
    return user;
  }

  private void validateMail(UserDto dto) {
    if (repository
        .findByUsernameEqualsIgnoreCaseAndEmailEqualsIgnoreCase(dto.getUsername(), dto.getEmail())
        .isEmpty()) {
      throw new BadRequestException(
          ExceptionConstants.ERROR_USER_EMAIL_ALREADY_EXISTS, dto.getEmail());
    }
  }

  public void generateUserForCreateUpdate(UserDto dto, User user) {
    validateRol(dto, user);
    // add any other validations if needed
  }

  private void validateRol(UserDto dto, User user) {
    if (!dto.getRoles().isEmpty()) {
      Set<Role> roles =
          dto.getRoles().stream()
              .map(
                  role ->
                      roleRepository
                          .findByCode(role.getCode())
                          .orElseThrow(
                              () -> new NotFoundException(ExceptionConstants.NOT_FOUND, role)))
              .collect(Collectors.toSet());
      user.getRoles().clear();
      user.getRoles().addAll(roles);
    }
  }

  private void sendEmail(final User user, final String mailTipo) {}
}
