package xyz.trainr.trainr.database;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.function.Consumer;

/**
 * Simplifies the usage of the MongoDB reactive streams driver by providing
 * simple callback functions
 *
 * @param <T> The response type to use for this subscriber
 * @author Lukas Schulte Pelkum
 * @version 1.0.0
 * @since 1.0.0
 */
public class CallbackSubscriber<T> implements Subscriber<T> {

    // Define the consumers to execute after an event
    private Consumer<Subscription> subscribeConsumer;
    private Consumer<T> nextConsumer;
    private Consumer<Throwable> errorConsumer;
    private Runnable completeRunnable;

    /**
     * @param consumer Gets called after the subscriber got subscribed
     */
    public void doOnSubscribe(Consumer<Subscription> consumer) {
        subscribeConsumer = consumer;
    }

    @Override
    public void onSubscribe(final Subscription s) {
        s.request(Long.MAX_VALUE);
        if (subscribeConsumer == null) return;
        subscribeConsumer.accept(s);
    }

    /**
     * @param consumer Gets called after the subscriber received a new value
     */
    public void doOnNext(Consumer<T> consumer) {
        nextConsumer = consumer;
    }

    @Override
    public void onNext(final T t) {
        if (nextConsumer == null) return;
        nextConsumer.accept(t);
    }

    /**
     * @param consumer Gets called after the subscriber received an error
     */
    public void doOnError(Consumer<Throwable> consumer) {
        errorConsumer = consumer;
    }

    @Override
    public void onError(final Throwable t) {
        if (errorConsumer == null) return;
        errorConsumer.accept(t);
    }

    /**
     * @param runnable Gets called after the subscriber completed
     */
    public void doOnComplete(Runnable runnable) {
        completeRunnable = runnable;
    }

    @Override
    public void onComplete() {
        if (completeRunnable == null) return;
        completeRunnable.run();
    }

}
