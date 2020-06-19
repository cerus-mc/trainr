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
     * Retrieves the given user out of the database or creates a new one if it doesn't exist
     *
     * @param uuid The UUID of the user
     * @return The completable future containing the user
     */
    public CompletableFuture<User> getUser(UUID uuid) {
        // Define the completable future to use
        CompletableFuture<User> future = new CompletableFuture<>();

        // Check if the user exists inside the cache
        Optional<User> cached = cachedUsers.stream().filter(user -> user.getUuid().equals(uuid)).findFirst();
        if (cached.isPresent()) {
            future.complete(cached.get());
            return future;
        }

        // Define the subscriber to use for this database operation
        CallbackSubscriber<User> subscriber = new CallbackSubscriber<>();
        subscriber.doOnNext(user -> {
            future.complete(user);
            cachedUsers.add(user);
        });
        subscriber.doOnError(future::completeExceptionally);
        subscriber.doOnComplete(() -> {
            // Create the user if it does not exist
            if (!future.isDone()) {
                User user = new User(uuid);
                CompletableFuture<Void> createFuture = createUser(user);
                createFuture.whenComplete((aVoid, throwable) -> {
                    if (throwable != null) {
                        future.completeExceptionally(throwable);
                        return;
                    }
                    future.complete(user);
                });
            }
        });

        // Perform the database operation
        collection.find(Filters.eq("uuid", uuid)).subscribe(subscriber);

        // Return the completable future
        return future;
    }

    /**
     * Creates a new user object
     *
     * @param user The user to create
     * @return The completable future
     */
    public CompletableFuture<Void> createUser(User user) {
        // Define the completable future to use
        CompletableFuture<Void> future = new CompletableFuture<>();

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
