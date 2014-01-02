package com.svitla.dzvin.app;

/**
 * Created by slelyuk on 12/30/13.
 */
public enum ERank {
    HIGH(3),
    MEDIUM(2),
    LOW(1);

    private Number rank;

    private ERank(Number rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return String.valueOf(rank);
    }
}
