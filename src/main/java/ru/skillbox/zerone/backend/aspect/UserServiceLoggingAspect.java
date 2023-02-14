package ru.skillbox.zerone.backend.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import ru.skillbox.zerone.backend.model.dto.request.RegisterConfirmRequestDTO;
import ru.skillbox.zerone.backend.model.dto.request.RegisterRequestDTO;

@Slf4j
@Aspect
@Component
public class UserServiceLoggingAspect {

  @Pointcut("execution(* registerAccount(ru.skillbox.zerone.backend.model.dto.request.RegisterRequestDTO))")
  public void registerAccountPointcut(){}

  @AfterReturning("args(request) && registerAccountPointcut()")
  public void registerAccountAdvice(RegisterRequestDTO request){
    log.debug("IN registerAccount - user with username: {} successfully registered", request.getEmail());
  }

  @Pointcut("execution(* registrationConfirm(ru.skillbox.zerone.backend.model.dto.request.RegisterConfirmRequestDTO))")
  public void registrationConfirmPointcut(){}

  @AfterReturning("args(request) && registrationConfirmPointcut()")
  public void registrationConfirmAdvice(RegisterConfirmRequestDTO request){
    log.debug("IN registrationConfirm - user with username: {} successfully confirmed registration", request.getEmail());
  }

  @Pointcut("execution(* getCurrentUser())")
  public void getCurrentUserPointcut(){}

  @AfterReturning("getCurrentUserPointcut()")
  public void getCurrentUserAdvice(){
    log.debug("IN getCurrentUser - current user successfully loaded");
  }

  @Pointcut("execution(* loadUserByUsername(String))")
  public void loadUserByUsernamePointcut(){}

  @AfterReturning("args(username) && loadUserByUsernamePointcut()")
  public void loadUserByUsernameAdvice(String username){
    log.debug("IN loadUserByUsername - user with email: {} successfully loaded", username);
  }
}
