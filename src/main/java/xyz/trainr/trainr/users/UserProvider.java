package xyz.trainr.trainr.users;

import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.reactivestreams.client.MongoCollection;
import xyz.trainr.trainr.database.CallbackSubscriber;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Acts as a central user provider
 *
 * @author Lukas Schulte Pelkum
 * @version 1.0.0
 * @since 1.0.0
 */
public class UserProvider {

    // Define local variables
    private final Set<User> cachedUsers;
    private final MongoCollection<User> collection;

    /**
     * Creates a new user provider
     *
     * @param collection The MongoDB collection to use
     */
    public UserProvider(MongoCollection<User> collection) {
        this.cachedUsers = new HashSet<>();
        this.collection = collection;
    }

    /**
     * Loads all users into the cache
     */
    public void loadAllUsers() {
        // Define the subscriber to use for this database operation
        AtomicBoolean finished = new AtomicBoolean(false);
        CallbackSubscriber<User> subscriber = new CallbackSubscriber<>();
        subscriber.doOnNext(cachedUsers::add);
        subscriber.doOnError(Throwable::printStackTrace);
        subscriber.doOnComplete(() -> finished.set(true));

        // Perform the database operation
        collection.find().subscribe(subscriber);

        // Wait until the subscriber finished
        while (!finished.get());
    }

    /**
     * Retrieves a cached user
     * @param uuid The UUID of the user
     * @return The optional cached user
     */
    public Optional<User> getCachedUser(UUID uuid) {
        return cachedUsers.stream().filter(user -> user.getUuid().equals(uuid)).findFirst();
    }

    public CompletableFuture<Void> initializeUser(UUID uuid) {
        // Define the completable future to use
        CompletableFuture<Void> future = new CompletableFuture<>();

        // Check if there is already a cached user
        if (getCachedUser(uuid).isPresent()) {
            future.complete(null);
            return future;
        }

        // Define the user to insert
        User user = new User(uuid);

        // Define the subscriber to use for this database operation
        CallbackSubscriber<InsertOneResult> subscriber = new CallbackSubscriber<>();
        subscriber.doOnNext(result -> {
            user.setId(result.getInsertedId().asObjectId().getValue());
            future.complete(null);
            cachedUsers.add(user);
        });
        subscriber.doOnError(future::completeExceptionally);

        // Perform the database operation
        collection.insertOne(user).subscribe(subscriber);

        // Return the completable future
        return future;
    }

    /**
     * Updates the given user object
     *
     * @param user The user to update
     * @return The completable future
     */
    public CompletableFuture<Void> updateUser(User user) {
        // Define the completable future to use
        CompletableFuture<Void> future = new CompletableFuture<>();

        // Define the subscriber to use for this database operation
        CallbackSubscriber<UpdateResult> subscriber = new CallbackSubscriber<>();
        subscriber.doOnNext(result -> future.complete(null));
        subscriber.doOnError(future::completeExceptionally);

        // Perform the database operation
        collection.replaceOne(Filters.eq("uuid", user.getUuid()), user).subscribe(subscriber);

        // Return the completable future
        return future;
    }

}
