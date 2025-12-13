# Learning Guide: Kafka Serializers & Deserializers with Jackson

## Overview

Kafka only transmits **bytes**. Serializers convert your Java objects → bytes, and deserializers convert bytes → Java objects. For complex nested classes, Jackson's `ObjectMapper` handles the JSON conversion.

---

## Step 1: Understand the Interfaces

### Serializer Interface
```java
public interface Serializer<T> {
    void configure(Map<String, ?> configs, boolean isKey);  // Optional setup
    byte[] serialize(String topic, T data);                  // Convert object to bytes
    void close();                                            // Cleanup resources
}
```

### Deserializer Interface
```java
public interface Deserializer<T> {
    void configure(Map<String, ?> configs, boolean isKey);  // Optional setup
    T deserialize(String topic, byte[] data);                // Convert bytes to object
    void close();                                            // Cleanup resources
}
```

---

## Step 2: Create a Generic Serializer

The serializer is simple because Jackson can serialize any object without knowing its type at compile time.

```java
public class JacksonSerializer<T> implements Serializer<T> {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public byte[] serialize(String topic, T data) {
        if (data == null) return null;
        try {
            return mapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error serializing to JSON", e);
        }
    }
}
```

**Key points:**
- Return `null` if input is `null` (Kafka convention)
- Wrap Jackson exceptions in `SerializationException`
- One generic serializer works for ALL your classes

---

## Step 3: Create a Generic Deserializer (The Tricky Part)

Deserialization requires knowing the **target type** at runtime. Due to Java's type erasure, `JacksonDeserializer<PlayerEvent>` loses the `PlayerEvent` type info.

### Solution: Pass the class via constructor or config

```java
public class JacksonDeserializer<T> implements Deserializer<T> {
    private final ObjectMapper mapper = new ObjectMapper();
    private Class<T> targetType;

    // Used when subclassing
    protected JacksonDeserializer(Class<T> targetType) {
        this.targetType = targetType;
    }

    // Used when configured via properties
    public JacksonDeserializer() {}

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        if (targetType == null) {
            String className = (String) configs.get("value.deserializer.type");
            try {
                targetType = (Class<T>) Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new SerializationException("Target class not found: " + className, e);
            }
        }
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        if (data == null) return null;
        try {
            return mapper.readValue(data, targetType);
        } catch (IOException e) {
            throw new SerializationException("Error deserializing JSON", e);
        }
    }
}
```

---

## Step 4: Create Type-Specific Deserializers

For cleaner configuration, create concrete subclasses:

```java
public class PlayerEventDeserializer extends JacksonDeserializer<PlayerEvent> {
    public PlayerEventDeserializer() {
        super(PlayerEvent.class);
    }
}

public class TurnActionEventDeserializer extends JacksonDeserializer<TurnActionEvent> {
    public TurnActionEventDeserializer() {
        super(TurnActionEvent.class);
    }
}
```

**Why subclass?**
- No need for extra config properties
- Clear intent in `application.properties`
- Each deserializer is a one-liner

---

## Step 5: Handle Nested Classes

Jackson automatically handles nested objects if they have:
1. **No-args constructor** (or `@NoArgsConstructor` from Lombok)
2. **Getters/Setters** (or `@Data` from Lombok)

Your classes already have these! ✅

### Example: How Jackson handles `TurnActionEvent`
```
TurnActionEvent
├── timestamp (long) ✅ primitive
├── eventTypes (List<TurnActionEvtType>) ✅ enum list
├── affectedPlayer (Player)
│   ├── username (String) ✅
│   ├── teamNum (int) ✅
│   ├── pokemonTeam (Pokemon[])
│   │   ├── name, id, etc. ✅
│   │   ├── types (List<Type>) ✅ enum list
│   │   ├── moves (List<Move>) ✅ nested object
│   │   └── currentStats (EnumMap<PokemonStat, Integer>) ✅ works by default
│   └── activePokemonIndex (int) ✅
└── result (ProcessResult) ✅
```

### EnumMap - Works Out of the Box
Jackson handles `EnumMap` automatically:
```json
{
  "currentStats": {
    "HP": 100,
    "ATTACK": 55,
    "DEFENSE": 40
  }
}
```
No extra config needed as long as:
- Enum names match exactly (case-sensitive)
- Field has getter/setter (`@Data` provides this)

---

## Step 6: Handle Polymorphic Types (Interfaces)

If you need to serialize/deserialize a field of type `PlayerAction` (interface), add annotations to tell Jackson how to distinguish between implementations:

```java
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "@type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = AttackAction.class, name = "attack"),
    @JsonSubTypes.Type(value = HealAction.class, name = "heal"),
    @JsonSubTypes.Type(value = SwitchAction.class, name = "switch"),
    @JsonSubTypes.Type(value = WaitAction.class, name = "wait")
})
public interface PlayerAction { ... }
```

This adds a `"@type": "attack"` field to the JSON, allowing Jackson to know which concrete class to instantiate.

---

## Step 7: Configure in application.properties

```properties
# Outgoing (serialize objects to JSON)
mp.messaging.outgoing.player.value.serializer=com.clapped.main.messaging.serdes.JacksonSerializer

# Incoming (deserialize JSON to objects)
mp.messaging.incoming.player-in.value.deserializer=com.clapped.main.messaging.serdes.PlayerEventDeserializer
```

---

## Step 8: Test Your Serializers

```java
@Test
void testRoundTrip() throws Exception {
    JacksonSerializer<PlayerEvent> serializer = new JacksonSerializer<>();
    PlayerEventDeserializer deserializer = new PlayerEventDeserializer();
    
    PlayerEvent original = new PlayerEvent(/* ... */);
    
    byte[] bytes = serializer.serialize("test-topic", original);
    PlayerEvent result = deserializer.deserialize("test-topic", bytes);
    
    assertEquals(original, result);
}
```

---

## Quick Reference: Files to Create

| File | Purpose |
|------|---------|
| `JacksonSerializer.java` | Generic serializer (one for all types) |
| `JacksonDeserializer.java` | Generic deserializer base class |
| `PlayerEventDeserializer.java` | Concrete deserializer for PlayerEvent |
| `GameEventDeserializer.java` | Concrete deserializer for GameEvent |
| `TurnActionEventDeserializer.java` | Concrete deserializer for TurnActionEvent |
| `TurnInfoEventDeserializer.java` | Concrete deserializer for TurnInfoEvent |

---

## Common Errors & Fixes

| Error | Cause | Fix |
|-------|-------|-----|
| `InvalidDefinitionException: No serializer found` | Missing getters | Add `@Data` or getters |
| `MismatchedInputException: No default constructor` | Missing no-args constructor | Add `@NoArgsConstructor` |
| `UnrecognizedPropertyException` | JSON has extra field | Add `@JsonIgnoreProperties(ignoreUnknown = true)` |
| `InvalidFormatException` for enum | Enum case mismatch | Use `@JsonFormat` or `@JsonValue` |

