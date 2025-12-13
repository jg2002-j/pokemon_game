package com.clapped.pokemon.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum Generation {
    I("generation-i", 1, List.of("red-blue", "yellow", "red-green-japan", "blue-japan"), 151),
    II("generation-ii", 2, List.of("gold-silver", "crystal"), 251),
    III("generation-iii", 3, List.of("ruby-sapphire", "emerald", "firered-leafgreen", "colosseum", "xd"), 386),
    IV("generation-iv", 4, List.of("diamond-pearl", "platinum", "heartgold-soulsilver"), 493),
    V("generation-v", 5, List.of("black-white", "black-2-white-2"), 649),
    VI("generation-vi", 6, List.of("x-y", "omega-ruby-alpha-sapphire"), 721),
    VII("generation-vii", 7, List.of("sun-moon", "ultra-sun-ultra-moon"), 809),
    VIII("generation-viii", 8, List.of("lets-go-pikachu-lets-go-eevee", "sword-shield", "the-isle-of-armor", "the-crown-tundra", "brilliant-diamond-and-shining-pearl", "legends-arceus"), 905),
    IX("generation-ix", 9, List.of("scarlet-violet", "the-teal-mask", "the-indigo-disk"), 1025),
    X("generation-x", 10, List.of("legends-za"), 1025); // Placeholder, same as Gen IX until new data is available

    private String name;
    private int numericalVal;
    private List<String> versionGroups;
    private int numPokemonInGen;

}
