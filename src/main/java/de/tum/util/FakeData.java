package de.tum.util;

import io.codearte.jfairy.Fairy;
import io.codearte.jfairy.producer.person.Person;

import java.util.Locale;

public class FakeData {

    private static final Fairy fairy = Fairy.create(Locale.GERMAN);

    public static Person getPerson() {
        return fairy.person();
    }

}
