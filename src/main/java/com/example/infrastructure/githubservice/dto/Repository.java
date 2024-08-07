package com.example.infrastructure.githubservice.dto;

public record Repository(String name, Owner owner, boolean fork) {
}
