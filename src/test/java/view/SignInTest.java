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
            latch.countDown();
        }

        if (!latch.await(5, TimeUnit.SECONDS)) {
            throw new IllegalStateException("JavaFX runtime did not start in time.");
        }
    }

    @Test
    void initDoesNotThrowException() {
        SignIn signIn = new SignIn();

        assertDoesNotThrow(signIn::init);
    }

    @Test
    void startLoadsLoginSceneAndSetsStageTitle() throws Exception {
        AtomicReference<Throwable> thrown = new AtomicReference<>();

        runOnFxThread(() -> {
            try {
                SignIn signIn = new SignIn();
                Stage stage = new Stage();

                signIn.start(stage);

                assertEquals("Flashers", stage.getTitle());
                assertNotNull(stage.getScene());
                assertNotNull(stage.getScene().getRoot());
                assertNotNull(signIn.controller);

                stage.close();
            } catch (Throwable e) {
                thrown.set(e);
            }
        });

        if (thrown.get() != null) {
            fail(thrown.get());
        }
    }

    private static void runOnFxThread(Runnable action) throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                action.run();
            } finally {
                latch.countDown();
            }
        });

        if (!latch.await(5, TimeUnit.SECONDS)) {
            throw new IllegalStateException("JavaFX action did not finish in time.");
        }
    }
}