package view;

import javafx.application.Platform;
import javafx.stage.Stage;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class SignInTest {

    @BeforeAll
    static void startJavaFxRuntime() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        try {
            Platform.startup(latch::countDown);
        } catch (IllegalStateException alreadyStarted) {
            // JavaFX toolkit was already started.
            latch.countDown();
        }

        if (!latch.await(5, TimeUnit.SECONDS)) {
            throw new IllegalStateException(
                    "JavaFX runtime did not start in time."
            );
        }
    }

    @Test
    void initDoesNotThrowException() {
        SignIn signIn = new SignIn();

        assertDoesNotThrow(signIn::init);
    }

    @Test
    void startLoadsLoginSceneAndSetsStageTitle() throws Exception {

        runOnFxThread(() -> {
            SignIn signIn = new SignIn();
            Stage stage = new Stage();

            try {
                signIn.start(stage);

                assertEquals("Flashers", stage.getTitle());
                assertNotNull(stage.getScene());
                assertNotNull(stage.getScene().getRoot());
                assertNotNull(signIn.controller);

            } finally {
                stage.close();
            }
        });
    }

    @FunctionalInterface
    private interface ThrowingRunnable {
        void run() throws Exception;
    }

    private static void runOnFxThread(ThrowingRunnable action)
            throws Exception {

        if (Platform.isFxApplicationThread()) {
            action.run();
            return;
        }

        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> error = new AtomicReference<>();

        Platform.runLater(() -> {
            try {
                action.run();
            } catch (Throwable e) {
                error.set(e);
            } finally {
                latch.countDown();
            }
        });

        if (!latch.await(10, TimeUnit.SECONDS)) {
            throw new IllegalStateException(
                    "JavaFX action did not finish in time."
            );
        }

        if (error.get() != null) {
            throw new RuntimeException(
                    "JavaFX action failed.",
                    error.get()
            );
        }
    }
}