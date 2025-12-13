package com.clapped.pokemon.model.dto.pokemon;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Pokemon sprites - captures all available sprite URLs from all sources.
 * Includes sprites from all game generations and other sources like Showdown, Dream World, and Home.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpritesDto {

    // Top-level static sprites (PNG format)
    @JsonProperty("back_default")
    private String backDefault;

    @JsonProperty("back_female")
    private String backFemale;

    @JsonProperty("back_shiny")
    private String backShiny;

    @JsonProperty("back_shiny_female")
    private String backShinyFemale;

    @JsonProperty("front_default")
    private String frontDefault;

    @JsonProperty("front_female")
    private String frontFemale;

    @JsonProperty("front_shiny")
    private String frontShiny;

    @JsonProperty("front_shiny_female")
    private String frontShinyFemale;

    // Other sprite sources (Dream World, Home, Official Artwork, Showdown)
    private Other other;

    // Sprites from different game versions (all generations)
    private Versions versions;

    /**
     * Other sprite sources including Dream World, Home, Official Artwork, and Showdown
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Other {
        @JsonProperty("dream_world")
        private DreamWorld dreamWorld;

        private Home home;

        @JsonProperty("official-artwork")
        private OfficialArtwork officialArtwork;

        private Showdown showdown;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class DreamWorld {
            @JsonProperty("front_default")
            private String frontDefault;

            @JsonProperty("front_female")
            private String frontFemale;
        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Home {
            @JsonProperty("front_default")
            private String frontDefault;

            @JsonProperty("front_female")
            private String frontFemale;

            @JsonProperty("front_shiny")
            private String frontShiny;

            @JsonProperty("front_shiny_female")
            private String frontShinyFemale;
        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class OfficialArtwork {
            @JsonProperty("front_default")
            private String frontDefault;

            @JsonProperty("front_shiny")
            private String frontShiny;
        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Showdown {
            @JsonProperty("back_default")
            private String backDefault;

            @JsonProperty("back_female")
            private String backFemale;

            @JsonProperty("back_shiny")
            private String backShiny;

            @JsonProperty("back_shiny_female")
            private String backShinyFemale;

            @JsonProperty("front_default")
            private String frontDefault;

            @JsonProperty("front_female")
            private String frontFemale;

            @JsonProperty("front_shiny")
            private String frontShiny;

            @JsonProperty("front_shiny_female")
            private String frontShinyFemale;
        }
    }

    /**
     * Sprites from all game generations (I through VIII)
     */
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Versions {
        @JsonProperty("generation-i")
        private GenerationI generationI;

        @JsonProperty("generation-ii")
        private GenerationII generationII;

        @JsonProperty("generation-iii")
        private GenerationIII generationIII;

        @JsonProperty("generation-iv")
        private GenerationIV generationIV;

        @JsonProperty("generation-v")
        private GenerationV generationV;

        @JsonProperty("generation-vi")
        private GenerationVI generationVI;

        @JsonProperty("generation-vii")
        private GenerationVII generationVII;

        @JsonProperty("generation-viii")
        private GenerationVIII generationVIII;

        // Generation I - Red/Blue/Yellow
        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class GenerationI {
            @JsonProperty("red-blue")
            private RedBlue redBlue;

            private Yellow yellow;

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class RedBlue {
                @JsonProperty("back_default")
                private String backDefault;

                @JsonProperty("back_gray")
                private String backGray;

                @JsonProperty("back_transparent")
                private String backTransparent;

                @JsonProperty("front_default")
                private String frontDefault;

                @JsonProperty("front_gray")
                private String frontGray;

                @JsonProperty("front_transparent")
                private String frontTransparent;
            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Yellow {
                @JsonProperty("back_default")
                private String backDefault;

                @JsonProperty("back_gray")
                private String backGray;

                @JsonProperty("back_transparent")
                private String backTransparent;

                @JsonProperty("front_default")
                private String frontDefault;

                @JsonProperty("front_gray")
                private String frontGray;

                @JsonProperty("front_transparent")
                private String frontTransparent;
            }
        }

        // Generation II - Gold/Silver/Crystal
        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class GenerationII {
            private Crystal crystal;

            private Gold gold;

            private Silver silver;

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Crystal {
                @JsonProperty("back_default")
                private String backDefault;

                @JsonProperty("back_shiny")
                private String backShiny;

                @JsonProperty("back_shiny_transparent")
                private String backShinyTransparent;

                @JsonProperty("back_transparent")
                private String backTransparent;

                @JsonProperty("front_default")
                private String frontDefault;

                @JsonProperty("front_shiny")
                private String frontShiny;

                @JsonProperty("front_shiny_transparent")
                private String frontShinyTransparent;

                @JsonProperty("front_transparent")
                private String frontTransparent;
            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Gold {
                @JsonProperty("back_default")
                private String backDefault;

                @JsonProperty("back_shiny")
                private String backShiny;

                @JsonProperty("front_default")
                private String frontDefault;

                @JsonProperty("front_shiny")
                private String frontShiny;

                @JsonProperty("front_transparent")
                private String frontTransparent;
            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Silver {
                @JsonProperty("back_default")
                private String backDefault;

                @JsonProperty("back_shiny")
                private String backShiny;

                @JsonProperty("front_default")
                private String frontDefault;

                @JsonProperty("front_shiny")
                private String frontShiny;

                @JsonProperty("front_transparent")
                private String frontTransparent;
            }
        }

        // Generation III - Ruby/Sapphire/Emerald/FireRed/LeafGreen
        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class GenerationIII {
            private Emerald emerald;

            @JsonProperty("firered-leafgreen")
            private FireRedLeafGreen fireRedLeafGreen;

            @JsonProperty("ruby-sapphire")
            private RubySapphire rubySapphire;

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Emerald {
                @JsonProperty("front_default")
                private String frontDefault;

                @JsonProperty("front_shiny")
                private String frontShiny;
            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class FireRedLeafGreen {
                @JsonProperty("back_default")
                private String backDefault;

                @JsonProperty("back_shiny")
                private String backShiny;

                @JsonProperty("front_default")
                private String frontDefault;

                @JsonProperty("front_shiny")
                private String frontShiny;
            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class RubySapphire {
                @JsonProperty("back_default")
                private String backDefault;

                @JsonProperty("back_shiny")
                private String backShiny;

                @JsonProperty("front_default")
                private String frontDefault;

                @JsonProperty("front_shiny")
                private String frontShiny;
            }
        }

        // Generation IV - Diamond/Pearl/Platinum/HeartGold/SoulSilver
        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class GenerationIV {
            @JsonProperty("diamond-pearl")
            private DiamondPearl diamondPearl;

            @JsonProperty("heartgold-soulsilver")
            private HeartGoldSoulSilver heartGoldSoulSilver;

            private Platinum platinum;

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class DiamondPearl {
                @JsonProperty("back_default")
                private String backDefault;

                @JsonProperty("back_female")
                private String backFemale;

                @JsonProperty("back_shiny")
                private String backShiny;

                @JsonProperty("back_shiny_female")
                private String backShinyFemale;

                @JsonProperty("front_default")
                private String frontDefault;

                @JsonProperty("front_female")
                private String frontFemale;

                @JsonProperty("front_shiny")
                private String frontShiny;

                @JsonProperty("front_shiny_female")
                private String frontShinyFemale;
            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class HeartGoldSoulSilver {
                @JsonProperty("back_default")
                private String backDefault;

                @JsonProperty("back_female")
                private String backFemale;

                @JsonProperty("back_shiny")
                private String backShiny;

                @JsonProperty("back_shiny_female")
                private String backShinyFemale;

                @JsonProperty("front_default")
                private String frontDefault;

                @JsonProperty("front_female")
                private String frontFemale;

                @JsonProperty("front_shiny")
                private String frontShiny;

                @JsonProperty("front_shiny_female")
                private String frontShinyFemale;
            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Platinum {
                @JsonProperty("back_default")
                private String backDefault;

                @JsonProperty("back_female")
                private String backFemale;

                @JsonProperty("back_shiny")
                private String backShiny;

                @JsonProperty("back_shiny_female")
                private String backShinyFemale;

                @JsonProperty("front_default")
                private String frontDefault;

                @JsonProperty("front_female")
                private String frontFemale;

                @JsonProperty("front_shiny")
                private String frontShiny;

                @JsonProperty("front_shiny_female")
                private String frontShinyFemale;
            }
        }

        // Generation V - Black/White with animated sprites
        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class GenerationV {
            @JsonProperty("black-white")
            private BlackWhite blackWhite;

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class BlackWhite {
                private Animated animated;

                @JsonProperty("back_default")
                private String backDefault;

                @JsonProperty("back_female")
                private String backFemale;

                @JsonProperty("back_shiny")
                private String backShiny;

                @JsonProperty("back_shiny_female")
                private String backShinyFemale;

                @JsonProperty("front_default")
                private String frontDefault;

                @JsonProperty("front_female")
                private String frontFemale;

                @JsonProperty("front_shiny")
                private String frontShiny;

                @JsonProperty("front_shiny_female")
                private String frontShinyFemale;

                @Data
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Animated {
                    @JsonProperty("back_default")
                    private String backDefault;

                    @JsonProperty("back_female")
                    private String backFemale;

                    @JsonProperty("back_shiny")
                    private String backShiny;

                    @JsonProperty("back_shiny_female")
                    private String backShinyFemale;

                    @JsonProperty("front_default")
                    private String frontDefault;

                    @JsonProperty("front_female")
                    private String frontFemale;

                    @JsonProperty("front_shiny")
                    private String frontShiny;

                    @JsonProperty("front_shiny_female")
                    private String frontShinyFemale;
                }
            }
        }

        // Generation VI - X/Y and OmegaRuby/AlphaSapphire
        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class GenerationVI {
            @JsonProperty("omegaruby-alphasapphire")
            private OmegaRubyAlphaSapphire omegaRubyAlphaSapphire;

            @JsonProperty("x-y")
            private XY xy;

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class OmegaRubyAlphaSapphire {
                @JsonProperty("front_default")
                private String frontDefault;

                @JsonProperty("front_female")
                private String frontFemale;

                @JsonProperty("front_shiny")
                private String frontShiny;

                @JsonProperty("front_shiny_female")
                private String frontShinyFemale;
            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class XY {
                @JsonProperty("front_default")
                private String frontDefault;

                @JsonProperty("front_female")
                private String frontFemale;

                @JsonProperty("front_shiny")
                private String frontShiny;

                @JsonProperty("front_shiny_female")
                private String frontShinyFemale;
            }
        }

        // Generation VII - Sun/Moon and UltraSun/UltraMoon
        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class GenerationVII {
            private Icons icons;

            @JsonProperty("ultra-sun-ultra-moon")
            private UltraSunUltraMoon ultraSunUltraMoon;

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Icons {
                @JsonProperty("front_default")
                private String frontDefault;

                @JsonProperty("front_female")
                private String frontFemale;
            }

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class UltraSunUltraMoon {
                @JsonProperty("front_default")
                private String frontDefault;

                @JsonProperty("front_female")
                private String frontFemale;

                @JsonProperty("front_shiny")
                private String frontShiny;

                @JsonProperty("front_shiny_female")
                private String frontShinyFemale;
            }
        }

        // Generation VIII - Sword/Shield
        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class GenerationVIII {
            private Icons icons;

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Icons {
                @JsonProperty("front_default")
                private String frontDefault;

                @JsonProperty("front_female")
                private String frontFemale;
            }
        }
    }
}

