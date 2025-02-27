package com.trustrace.switchEnergySystem.controller;

import com.trustrace.switchEnergySystem.dto.UserDto;
import com.trustrace.switchEnergySystem.entity.Role;
import com.trustrace.switchEnergySystem.entity.User;
import com.trustrace.switchEnergySystem.repository.RoleRepository;
import com.trustrace.switchEnergySystem.service.AuthService;
import com.trustrace.switchEnergySystem.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        User user1 = new User();
        user1.setId("user1");
        User user2 = new User();
        user2.setId("user2");

        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        List<User> result = userController.getAllUsers();

        assertEquals(2, result.size());
        verify(userService).getAllUsers();
    }

    @Test
    void testGetUserById() {
        String userId = "user1";
        User user = new User();
        user.setId(userId);

        when(userService.getUserById(userId)).thenReturn(Optional.of(user));

        Optional<User> result = userController.getUserById(userId);

        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());
        verify(userService).getUserById(userId);
    }

    @Test
    void testAddUser() {
        User user = new User();
        user.setId("user1");

        when(userService.addUser(user)).thenReturn(user);

        User result = userController.addUser(user);

        assertNotNull(result);
        assertEquals("user1", result.getId());
        verify(userService).addUser(user);
    }

    @Test
    void testDeleteUser() {
        String userId = "user1";

        userController.deleteUser(userId);

        verify(userService).deleteUserById(userId);
    }




}
