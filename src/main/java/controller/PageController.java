package controller;

import dao.CardDao;
import entity.User;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class PageController implements Initializable {
    private PageController(){}
    private static User currentUser;
    private static final String HOME_GUI_FXML_PATH = "/initHomeGui.fxml";

    public void initialize(URL location, ResourceBundle resources) {
        // Initialization logic if needed
    }
    public static void setUser(User user) {
        currentUser = user;
    }

    public static void switchToCreateCards(MouseEvent actionEvent) throws IOException {
        Locale localeCC = LanguageManager.getLocale();
        FXMLLoader fxmlLoader = new FXMLLoader(PageController.class.getResource("/createCard.fxml"), LanguageManager.getBundle());
        Parent root = fxmlLoader.load();
        CcController cardCtrl = fxmlLoader.getController();
        cardCtrl.setUser(currentUser);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void switchToLibrary(MouseEvent actionEvent) throws IOException {
        Locale localeL = LanguageManager.getLocale();
        FXMLLoader fxmlLoader = new FXMLLoader(PageController.class.getResource("/libraryUI.fxml"), LanguageManager.getBundle());
        Parent root = fxmlLoader.load();
        LibraryController lc = fxmlLoader.getController();
        lc.setUser(currentUser);
        lc.getAllCards();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void switchToCreateQuiz(MouseEvent actionEvent) throws IOException {
        Locale localeCQ = LanguageManager.getLocale();
        FXMLLoader fxmlLoader = new FXMLLoader(PageController.class.getResource("/createQuizUI.fxml"), LanguageManager.getBundle());
        Parent root = fxmlLoader.load();
        QuizController qc = fxmlLoader.getController();
        List<String> categories = CardDao.getCategories(currentUser.getUserId());
        qc.currentUser = currentUser;
        qc.populateCategorySelection(categories);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    public static void switchToProfile(MouseEvent actionEvent) throws IOException {
        Locale localeP = LanguageManager.getLocale();
        FXMLLoader fxmlLoader = new FXMLLoader(PageController.class.getResource(HOME_GUI_FXML_PATH), LanguageManager.getBundle());
        Parent root = fxmlLoader.load();

        SignInController controller = fxmlLoader.getController();
        controller.setCurrentUser(currentUser);
        controller.setUserGreeting(currentUser.getUserName());

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
