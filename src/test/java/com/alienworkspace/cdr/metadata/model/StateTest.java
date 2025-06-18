package com.alienworkspace.cdr.metadata.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StateTest {
    @Test
    void testBuilderAndGetters() {
        State state = State.builder()
                .stateId(1)
                .stateName("Test State")
                .locale("en_US")
                .localePreferred(true)
                .stateCode("STA123")
                .stateGeoCode("GEO123")
                .statePhoneCode(234)
                .build();
        assertEquals(1, state.getStateId());
        assertEquals("Test State", state.getStateName());
        assertEquals("en_US", state.getLocale());
        assertTrue(state.isLocalePreferred());
        assertEquals("STA123", state.getStateCode());
        assertEquals("GEO123", state.getStateGeoCode());
        assertEquals(234, state.getStatePhoneCode());
    }

    @Test
    void testSetters() {
        State state = new State();
        state.setStateId(2);
        state.setStateName("Another State");
        state.setLocale("fr_FR");
        state.setLocalePreferred(false);
        state.setStateCode("STA456");
        state.setStateGeoCode("GEO456");
        state.setStatePhoneCode(123);
        assertEquals(2, state.getStateId());
        assertEquals("Another State", state.getStateName());
        assertEquals("fr_FR", state.getLocale());
        assertFalse(state.isLocalePreferred());
        assertEquals("STA456", state.getStateCode());
        assertEquals("GEO456", state.getStateGeoCode());
        assertEquals(123, state.getStatePhoneCode());
    }

    @Test
    void testEqualsAndHashCode() {
        State s1 = State.builder().country(Country.builder().countryId(1).countryName("A").build())
                .stateId(1).stateName("A").build();
        State s2 = State.builder().country(Country.builder().countryId(1).countryName("A").build())
                .stateId(1).stateName("A").build();
        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void testToString() {
        State state = State.builder().stateId(1).stateName("Test").build();
        assertTrue(state.toString().contains("Test"));
    }
} 