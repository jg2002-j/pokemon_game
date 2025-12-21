package com.clapped.pokemon.service;

import com.clapped.boundary.rest.dto.SimplePkmnDto;
import com.clapped.main.service.GameState;
import com.clapped.main.util.RandomProvider;
import com.clapped.main.util.SecureRandomProvider;
import com.clapped.pokemon.client.PokeClient;
import com.clapped.pokemon.model.*;
import com.clapped.pokemon.model.dto.NamedResource;
import com.clapped.pokemon.model.dto.generation.GenerationDto;
import com.clapped.pokemon.model.dto.move.EffectChangeDto;
import com.clapped.pokemon.model.dto.move.MoveDto;
import com.clapped.pokemon.model.dto.move.MoveStatChangeDto;
import com.clapped.pokemon.model.dto.move.PastValueDto;
import com.clapped.pokemon.model.dto.pokemon.PokemonDto;
import com.clapped.pokemon.model.dto.pokemon.PokemonMove;
import com.clapped.pokemon.model.dto.pokemon.PokemonStatDto;
import com.clapped.pokemon.model.dto.pokemon.TypeSlotDto;
import com.clapped.pokemon.model.dto.type.TypeDto;
import com.clapped.pokemon.model.move.MoveDamageClass;
import com.clapped.pokemon.model.move.MoveFlag;
import com.clapped.pokemon.model.move.MoveStatChange;
import com.clapped.pokemon.model.move.MoveTarget;
import com.clapped.pokemon.model.pokemon.PokemonNature;
import com.clapped.pokemon.model.pokemon.PokemonStat;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.*;

@Slf4j
@ApplicationScoped
public class PokemonService {

    @Inject
    @RestClient
    PokeClient client;

    private final GameState state;
    private final RandomProvider random;
    private final PokemonStatCalculator statCalculator;

    @Inject
    public PokemonService(final GameState state, final SecureRandomProvider random, final PokemonStatCalculator statCalculator) {
        this.state = state;
        this.random = random;
        this.statCalculator = statCalculator;
    }

    public Pokemon getPokemonByIdOrName(final String identifier) {
        final Generation pokemonGen = Arrays.stream(Generation.values())
            .filter(gen -> gen.getSlug().equals(client.getSpecies(identifier).getGeneration().getName()))
            .findFirst().orElse(null);

        if (pokemonGen == null || pokemonGen.getNumber() > state.getPokemonGen().getNumber()) {
            log.error("Requested pokemon '{}' is unavailable in the selected generation.", identifier);
            throw new BadRequestException("This Pokemon isn't available in the selected generation.");
        }

        final PokemonDto raw = client.getPokemon(identifier);
        log.info("Successfully retrieved '{}' from PokeAPI.", raw.getName());

        final long id = raw.getId();
        final String spriteLink = raw.getSprites().getFrontDefault();
        final String cries = raw.getCries().getLatest();
        final String name = raw.getName();
        final PokemonNature nature = getRandomNature();
        final List<Type> types = getTypes(raw.getTypeSlotDtos());
        final List<Move> moves = getMoves(raw.getMoves());
        final EnumMap<PokemonStat, Integer> stats = getBaseStats(raw.getStats(), nature);

        return new Pokemon(id, spriteLink, cries, name, nature, types, moves, stats);
    }

    private PokemonNature getRandomNature() {
        PokemonNature[] natures = PokemonNature.values();
        return natures[random.getRandInt(natures.length)];
    }

    private List<Type> getTypes(final List<TypeSlotDto> rawTypes) {
        return rawTypes.stream()
            .map(typeSlotDto -> Type.valueOf(typeSlotDto.getType().getName().toUpperCase()))
            .toList();
    }

    private List<Move> getMoves(final List<PokemonMove> moves) {
        final List<PokemonMove> movesInGen = moves.stream()
            .filter(move -> move.getVersionGroupDetails().stream()
                .anyMatch(versionGroup -> state.getPokemonGen().getVersionGroups()
                    .contains(versionGroup.getVersionGroup().getName())))
            .toList();

        final List<PokemonMove> movesAtLevel = new ArrayList<>(movesInGen.stream()
            .filter(move -> move.getVersionGroupDetails().getFirst().getLevelLearnedAt() <= state.getPokemonLevel())
            .toList());

        Collections.shuffle(movesAtLevel);
        final List<PokemonMove> randomFour = movesAtLevel.stream().limit(4).toList();

        return randomFour.stream()
            .map(pokemonMove -> client.getMove(pokemonMove.getMove().getName()))
            .map(this::mapMove)
            .toList();
    }

    private Move mapMove(final MoveDto raw) {
        final Move move = new Move()
            .setId(raw.getId())
            .setName(raw.getPrettyName())

            .setAccuracy(raw.getAccuracy())
            .setCurrentPp(raw.getPp())
            .setBasePp(raw.getPp())
            .setPriority(raw.getPriority())
            .setPower(raw.getPower())
            .setType(Type.fromName(raw.getType().getName()))
            .setDamageClass(MoveDamageClass.fromName(raw.getDamageClass().getName()))
            .setTextDesc(raw.getEffectEntries().getFirst().getShortEffect())

            .setTarget(MoveTarget.fromName(raw.getTarget().getName()))

            .setDrain(raw.getMeta().getDrain())
            .setHealing(raw.getMeta().getHealing())
            .setFlinchChance(raw.getMeta().getFlinchChance())

            .setMaxHits(raw.getMeta().getMaxHits())
            .setMinHits(raw.getMeta().getMinHits())

            .setMaxTurns(raw.getMeta().getMaxTurns())
            .setMinTurns(raw.getMeta().getMinTurns())

            .setInflictsAilment(Ailment.fromName(raw.getMeta().getAilment().getName()))
            .setAilmentChance(raw.getMeta().getAilmentChance())
            .setChangesStats(mapStatChanges(raw))

            .setMoveFlags(mapFlags(raw));

        // TODO: overwrite values with gen-specific ones where possible
        final Optional<PastValueDto> selectedGenValues = raw.getPastValueDtos().stream()
            .filter(pastValueDto -> state.getPokemonGen().getVersionGroups()
                .contains(pastValueDto.getVersionGroup().getName()))
            .findFirst();
        final Optional<EffectChangeDto> selectedGenEffects = raw.getEffectChangeDtos().stream()
            .filter(effectChangeDto -> state.getPokemonGen().getVersionGroups()
                .contains(effectChangeDto.getVersionGroup().getName()))
            .findFirst();

        return move;
    }

    private List<MoveStatChange> mapStatChanges(final MoveDto raw) {
        final List<MoveStatChange> statChanges = new ArrayList<>();
        for (MoveStatChangeDto statChange : raw.getStatChanges()) {
            statChanges.add(
                new MoveStatChange(
                    PokemonStat.fromName(statChange.getStat().getName()),
                    statChange.getChange(),
                    raw.getMeta().getStatChance()
                )
            );
        }
        return statChanges;
    }

    private List<MoveFlag> mapFlags(final MoveDto rawMove) {
        final String effectString = rawMove.getEffectEntries().getFirst().getEffect();
        // TODO: Parse effectString and map it into effects
        return List.of();
    }

    private EnumMap<PokemonStat, Integer> getBaseStats(final List<PokemonStatDto> rawStats, final PokemonNature nature) {
        EnumMap<PokemonStat, Integer> rawStatMap = new EnumMap<>(PokemonStat.class);
        for (final PokemonStatDto rawStat : rawStats) {
            final PokemonStat pokemonStat = PokemonStat.fromName(rawStat.getStat().getName());
            rawStatMap.put(pokemonStat, rawStat.getBaseStat());
        }
        return statCalculator.calculateStats(rawStatMap, nature);
    }

    public void populateAllTypes() {
        for (Type type : Type.values()) {
            final TypeDto dto = client.getType(type.getName());
            type.setImgLink(getTypeIcon(dto));
            TypeDto.DamageRelations dr = dto.getDamageRelations();
            if (dr != null) {
                type.setImmuneAgainst(mapTypes(dr.getNoDamageTo()));
                type.setIneffectiveAgainst(mapTypes(dr.getHalfDamageTo()));
                type.setSuperEffectiveAgainst(mapTypes(dr.getDoubleDamageTo()));
            }
        }
    }

    private String getTypeIcon(final TypeDto dto) {
        return switch (state.getPokemonGen()) {
            case IV -> sprite(dto, "generation-iv", "platinum");
            case V -> sprite(dto, "generation-v", "black-2-white-2");
            case VI -> sprite(dto, "generation-vi", "x-y");
            case VII -> sprite(dto, "generation-vii", "ultra-sun-ultra-moon");
            case VIII -> sprite(dto, "generation-viii", "sword-shield");
            default -> sprite(dto, "generation-iii", "firered-leafgreen");
        };
    }

    private String sprite(TypeDto dto, String generation, String game) {
        if (dto.getSprites() == null) {
            return null;
        }
        return Optional.ofNullable(dto.getSprites().get(generation))
            .map(gen -> gen.get(game))
            .map(TypeDto.Sprite::getImgLink)
            .orElse(null);
    }

    private List<Type> mapTypes(List<NamedResource> resources) {
        return Optional.ofNullable(resources)
            .orElse(List.of())
            .stream()
            .map(NamedResource::getName)
            .map(Type::fromName)
            .toList();
    }

    public List<SimplePkmnDto> getAllValidPkmnForGen(final int genNum) {
        final List<SimplePkmnDto> allPkmn = new LinkedList<>();
        for (int i = 1; i <= genNum; i++) {
            final GenerationDto generationDto = client.getGeneration(i);
            for (final NamedResource pkmnNamedResource : generationDto.getPokemonSpecies()) {
                final String name = pkmnNamedResource.getName();
                final String url = pkmnNamedResource.getUrl();
                String[] parts = url.split("/");  // splits by "/"
                String idStr = parts[parts.length - 1].isEmpty() ? parts[parts.length - 2] : parts[parts.length - 1];
                int id = Integer.parseInt(idStr);
                allPkmn.add(new SimplePkmnDto(name, id));
            }
        }
        return allPkmn;
    }
}
