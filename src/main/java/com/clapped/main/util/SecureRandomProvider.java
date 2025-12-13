package com.clapped.main.util;

import jakarta.enterprise.context.ApplicationScoped;

import java.security.SecureRandom;

@ApplicationScoped
public class SecureRandomProvider implements RandomProvider {
    private static final ThreadLocal<SecureRandom> secureRandom = ThreadLocal.withInitial(SecureRandom::new);

    @Override
    public boolean getChanceOutcome(int successChance) {
        try {
            return secureRandom.get().nextInt(0, 100) < successChance;
        } finally {
            secureRandom.remove();
        }
    }

    @Override
    public int getRandInt(int bound) {
        try {
            return secureRandom.get().nextInt(bound);
        } finally {
            secureRandom.remove();
        }
    }

    @Override
    public int getRandInt(final int lower, final int upper) {
        try {
            return secureRandom.get().nextInt(lower, upper);
        } finally {
            secureRandom.remove();
        }
    }

    @Override
    public double getRandDouble(final double lower, double upper) {
        try {
            return secureRandom.get().nextDouble(lower, upper);
        } finally {
            secureRandom.remove();
        }
    }
}
