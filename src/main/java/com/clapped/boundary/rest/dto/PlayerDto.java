package com.clapped.boundary.rest.dto;

import java.util.List;

public record PlayerDto (String username, int teamNum, List<String> pkmnTeam) {}
