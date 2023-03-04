package com.example.authorization.controller;

public record UserRequest(String username, String password, String role) {
}
