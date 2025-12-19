package com.clapped.boundary.rest.dto;

import java.util.List;

public record PlayerDto (String username, String avatarUrl, int teamNum, List<String> pkmnTeam) {}
