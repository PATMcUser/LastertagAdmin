package de.psc.Lasertag;

import java.util.Random;

public class Naming {
    private static String forenames[]={
            "Bob", "Alice", "Mouse",
            "Jean-Claude", "Jhon", "Jason", "Sylvester", "Vin", "Bruce", "Arnold", "Denzel", "Robert", "Gerard",
            "Simon", "Dustin", "Johnny", "Clint", "Jack", "Charlie", "Will", "Harrison", "Morgan", "Robin", "Sean",
            "Ben", "Patrick", "Mel",
    };

    private static String surnames[]={
            " Hoffman", " Eastwood", " Nicholson", " Nicholson", " Chaplin", " Smith", " Ford", " Freeman", " Williams",
            " Stewart", " Gibson", " Connery", " Kingsley", " Van Damme", " Rambo", " Schwarzenne", " Statham", " Depp",
            " Stallone", " Diesel", " Willis", " Schwarzenegger", " Washington", " Butler", " Pegg", " De Niro"
    };

    private static Random randomGenerator = new Random();

    public static String RandomeName(){
        return forenames[randomGenerator.nextInt(forenames.length)] + surnames[randomGenerator.nextInt(surnames.length)];
    }

}
