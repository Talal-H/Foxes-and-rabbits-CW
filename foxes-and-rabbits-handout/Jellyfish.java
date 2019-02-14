import java.util.List;
import java.util.Random;

/**
 * A simple model of a jellyfish.
 * Jellyfish age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29 (2)
 */
public class Jellyfish extends Animal
{
    // Characteristics shared by all jellyfish (class variables).

    // The age at which a jellyfish can start to breed.
    private static final int BREEDING_AGE = 1000;
    // The age to which a jellyfish can live.
    private static final int MAX_AGE = 10000000;
    // The likelihood of a jellyfish breeding.
    private static final double BREEDING_PROBABILITY = 0.001;
    // The maximum number of births.
    private static final int MAX_LITTER_SIZE = 1;
    // A shared random number generator to control breeding.
    private static final Random rand = Randomizer.getRandom();
    
    // Individual characteristics (instance fields).
    
    // The jellyfish's age.
    private int age;

    /**
     * Create a new jellyfish. A jellyfish may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the jellyfish will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Jellyfish(boolean randomAge, Field field, Location location)
    {
        super(field, location);
        age = 0;
        if(randomAge) {
            age = rand.nextInt(MAX_AGE);
        }
    }
    
    /**
     * This is what the jellyfish does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newJellyfish A list to return newly born jellyfish.
     */
    public void act(List<Animal> newJellyfish)
    {
        incrementAge();
        if(isAlive()) {
            giveBirth(newJellyfish);            
            // Try to move into a free location.
            Location newLocation = getField().freeAdjacentLocation(getLocation());
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }

    /**
     * Increase the age.
     * This could result in the jellyfish's death.
     */
    private void incrementAge()
    {
        age++;
        if(age > MAX_AGE) {
            setDead();
        }
    }
    
    /**
     * Check whether or not this jellyfish is to give birth at this step.
     * New births will be made into free adjacent locations.
     * @param newJellyfish A list to return newly born jellyfish.
     */
    private void giveBirth(List<Animal> newJellyfish)
    {
        // New jellyfish are born into adjacent locations.
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for(int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Jellyfish young = new Jellyfish(false, field, loc);
            newJellyfish.add(young);
        }
    }
        
    /**
     * Generate a number representing the number of births,
     * if it can breed.
     * @return The number of births (may be zero).
     */
    private int breed()
    {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= BREEDING_PROBABILITY) {
            births = rand.nextInt(MAX_LITTER_SIZE) + 1;
        }
        return births;
    }

    /**
     * A jellyfish can breed if it has reached the breedjellyfishe.
     * @return true if the jellyfish can breed, false otherwise.
     */
    private boolean canBreed()
    {
        return age >= BREEDING_AGE;
    }
}
